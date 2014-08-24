#include "BigInt.h"
#include "FloatType.h"
#include <cmath>
#include <bitset>
#include <cassert>
using namespace std;


BigInt::BigInt(const BigInt& v):
  _value(v._value),
  _signo(v._signo),
  _bitsize(v._bitsize),
  _float_type(v._float_type)
{

}

unsigned int _abs(const int &v)
{
  return  ( (v>=0) ? v : -1*v );
}

BigInt::BigInt(const int v,const unsigned int size, FloatType type):
  _value( (size-1)/32 + 1 ),
  _signo(0),
  _bitsize(size),
  _float_type(type)
{
  setZero();
  _signo = (v>=0) ? 0 : 1;

  _value.back() = _abs(v);
  (*this) <<= (size % 32);
}


BigInt::BigInt(const BigInt& v,const unsigned int size, FloatType type):
  _value((size-1)/32 + 1),
  _signo(0),
  _bitsize(size),
  _float_type(type)
{
  setZero();

  _signo=v._signo;

  for (unsigned int i=0;i<v._value.size();i++)
  {
    _value[i]=v._value[i];
  }


  (*this) >>= (_bitsize-v._bitsize);
}

void BigInt::resize(unsigned int size)
{
  unsigned int newsize = (size-1)/32 + 1;
  if (newsize > _value.size()) _value.resize(newsize);
  (*this) >>= (size-_bitsize);
  _bitsize=size;
}

BigInt::~BigInt()
{

}

BigInt& BigInt::operator*=(const BigInt& v)
{
  BigInt r(*this);
  setZero();

  switch(v._float_type)
  {
  	  case cmath_library:
		{
			unsigned int* dest = &_value[0];
			unsigned int* src1 = &r._value[0];
			const unsigned int* src2 = &v._value[0];
			unsigned int size = _value.size() / 2;   // por que size ya tiene el doble de largo
			unsigned int cont = size - 1;

			unsigned int acum [_value.size()];

			while(cont > 0)
			{
				const int iter = _value.size() - 1;
				unsigned int temp [_value.size()];
				for(int i = iter; i> 0; i--)
				{
					temp[iter * 4] = src1[cont * 4] * src2[iter * 4];
				}

				int j = iter;
				for(int i= _value.size() - 1; i>(cont-size-1); i--)
				{
					acum[i * 4] +=  temp[j * 4];
					j--;
				}
				cont--;
			}

			for(int i = size - 1 ; i> 0; i--)
			{
				dest[i * 8] = acum[i * 8];
			}

		}

		break;

  	  case asm_32:
  		mult(&_value[0],&r._value[0],&v._value[0],_value.size() / 2 );
  		  break;

  	  case asm_64:
  		mult(&_value[0],&r._value[0],&v._value[0],_value.size() / 2 );
  		  break;
  	  case asm_128:
  		mult128(&_value[0],&r._value[0],&v._value[0],_value.size() / 2 );
  	  		  break;
  }

  _signo = (r._signo ^ v._signo );

  if (isZero()) _signo=0;

  return *this;
}

const BigInt BigInt::operator*(const BigInt& v) const
{

	BigInt r(0,bitsize(), this->_float_type);

	switch(v._float_type)
	  {
	  	  case cmath_library:
	  	  {
	  		unsigned int* dest = &r._value[0];
	  		const unsigned int* src1 = &_value[0];
	  		const unsigned int* src2 = &v._value[0];
	  		unsigned int size = _value.size() / 2;   // por que size ya tiene el doble de largo
	  		unsigned int cont = size - 1;

	  		unsigned int acum [_value.size()];
	  		while(cont > 0)
	  		{
	  			const int iter = _value.size() - 1;
	  			unsigned int temp [_value.size()];
	  			for(int i = iter; i>= 0; i--)
	  			{
	  				temp[iter * 4] = src1[cont * 4] * src2[iter * 4];
	  			}

	  			int j = iter;
	  			for(int i= _value.size() - 1; i>(cont-size-1); i--)
	  			{
	  				acum[i * 4] +=  temp[j * 4];
	  				j--;
	  			}
	  			cont--;
	  		}

	  		for(int i = size - 1 ; i> 0; i--)
			{
				dest[i * 4] = acum[i * 4];
			}

	  	  }

	  	  break;

	  	  case asm_32:
	  		  mult(&r._value[0],&_value[0],&v._value[0],_value.size() / 2);
	  		  break;

	  	  case asm_64:
	  		  mult(&r._value[0],&_value[0],&v._value[0],_value.size() / 2);
	  		  break;
	  	  case asm_128:
	  		  mult128(&r._value[0],&_value[0],&v._value[0],_value.size() / 2);
	  	  		  break;
	  }

  r._signo = _signo ^ v._signo;

  if (isZero()) r._signo=0;

  return r;
}


BigInt& BigInt::operator+=(const BigInt& v)
{
  if (_signo==v._signo)
  {

    switch(v._float_type)
    {
    case cmath_library:
    	for(unsigned int i = 0; i< v._value.size(); i++){
    			this->_value[i] = this->_value[i] + v._value[i];
    		}
    	break;
    case asm_32:
    	sum(&(this->_value[0]),&v._value[0], v._value.size());
    	break;
    case asm_64:
    	sum(&(this->_value[0]),&v._value[0], v._value.size());
    	break;
    case asm_128:
    	sum128(&(this->_value[0]),&v._value[0], v._value.size());
    	break;
    }

  }
  else
  {

    if (  this->gt_abs(v) )
    {
      dosbb(*this,v);
    }
    else
    {
      BigInt tmp(v);
      dosbb(tmp,*this);
      *this = tmp;
    }
  }

  if (isZero()) _signo=0;

  return *this;
}

const BigInt BigInt::operator+(const BigInt& v) const
{
  return BigInt(*this)+=v;
}

BigInt& BigInt::operator+=(const int v)
{
  (*this)+=BigInt(v,_bitsize, this-> _float_type);
  return (*this);
}

const BigInt BigInt::operator+(const int v) const
{
  return BigInt(*this)+=v;
}



void BigInt::dosbb(BigInt& dst, const BigInt& src)
{
  asm volatile ("\
.intel_syntax noprefix; \
\
      jecxz subend ;\
      dec %[count]; \
      clc ; \
subloop:\
      mov eax, dword ptr [%[src]+%[count]*4] ;\
      sbb dword ptr [%[dest]+%[count]*4], eax ;\
\
      jecxz subend ;\
      dec %[count];\
      jmp subloop;\
\
subend:\
\
.att_syntax;"
    :
    : [dest] "D" (&dst._value[0]), [src] "S" (&src._value[0]), [count] "c" (src._value.size())
    : "eax"
  );
}

BigInt& BigInt::operator-=(const BigInt& v)
{
  if (_signo==v._signo)
  {

    if (  this->gt_abs(v) )
    {
      dosbb(*this,v);
    }
    else
    {
      BigInt tmp(v);
      dosbb(tmp,*this);
      tmp._signo ^= 1;
      *this = tmp;
    }
  }
  else
  {

    switch(v._float_type)
    {
    	case cmath_library:
    		for(unsigned int i = 0; i< v._value.size(); i++){
    		    	this->_value[i] = _value[i] + v._value[i];
    		    }
      	break;
      case asm_32:
    	  sum(&(this->_value[0]),&v._value[0], v._value.size());
      	break;
      case asm_64:
    	  sum(&(this->_value[0]),&v._value[0], v._value.size());
      	break;
      case asm_128:
    	  sum128(&(this->_value[0]),&v._value[0], v._value.size());
      	break;
      }

  }

  if (isZero()) _signo=0;

  return *this;

}

const BigInt BigInt::operator-(const BigInt& v) const
{
  return BigInt(*this)-=v;
}


BigInt& BigInt::operator-=(const int v)
{

  (*this)-=BigInt(v,_bitsize, this->_float_type);
  return (*this);
}

const BigInt BigInt::operator-(const int v) const
{
  return BigInt(*this)-=v;
}

BigInt& BigInt::operator<<=(const unsigned int& s)
{
  unsigned int bigshift = s / 32;

  if (bigshift>0)
  {
    for (unsigned int i=0;i<_value.size()-bigshift;i++)
    {
      _value[i]=_value[i+bigshift];
    }
    for (unsigned int i=_value.size()-bigshift;i<_value.size();i++)
    {
      _value[i]=0;
    }
  }

  unsigned int smallshift = s % 32;

  if (smallshift>0)
  {
    for (unsigned int i=0;i<_value.size()-bigshift-1;i++)
    {
      _value[i] <<= smallshift;
      _value[i] |= (_value[i+1] >> (32-smallshift));
    }

    _value[_value.size()-bigshift-1] <<= smallshift;
  }

  if (isZero()) _signo=0;

  return (*this);
}

const BigInt BigInt::operator<<(const unsigned int& v) const
{
  return BigInt(*this)<<=v;
}

BigInt& BigInt::operator>>=(const unsigned int& s)
{
  unsigned int bigshift = s / 32;

  if(bigshift>=_value.size())
  {
    setZero();
  }
  else{

    if (bigshift>0)
    {
      for (unsigned int i=_value.size()-1;i>=bigshift;i--)
      {
        _value[i]=_value[i-bigshift];
      }
      for (unsigned int i=bigshift;i>0;i--)
      {
        _value[i-1]=0;
      }
    }

    unsigned int smallshift = s % 32;

    if (smallshift>0)
    {
      for (unsigned int i=_value.size()-1;i>bigshift;i--)
      {
        _value[i] >>= smallshift;
        _value[i] |= (_value[i-1] << (32-smallshift));
      }

      _value[bigshift] >>= smallshift;
    }
    if (isZero()) _signo=0;
  }
  return (*this);
}

const BigInt BigInt::operator>>(const unsigned int& v) const
{
  return BigInt(*this)>>=v;
}

unsigned int BigInt::triml()
{
  unsigned int l=0,i=0;

  while ( (i<_value.size() ) && (_value[i]==0) ) i++;

  if (i<_value.size())
  {
    l = log2int(_value[i]);
    l = (i*32) + (32-l-1);
  }

  (*this)<<=l;

  if (isZero()) _signo=0;

  return l;
}

void BigInt::truncr(unsigned int newsize)
{
  _value.resize( (newsize-1)/32 + 1);

  unsigned int bitsResto = newsize % 32;

  if (bitsResto!=0)
  {
    _value.back() &= ~( pwr2int(bitsResto)-1 );
  }
  _bitsize = newsize;

  if (isZero()) _signo=0;

}

unsigned int BigInt::to_uint() const
{
  return _value.back();
}

bool BigInt::operator<(const BigInt& v) const
{
  if (_signo == v._signo)
  {
    unsigned int i=0;

    while ((i<_value.size()) && (_value[i]==v._value[i])) i++;

    return ( (i<_value.size()) && (
       ( (_signo==0) && ( _value[i]<v._value[i] ) ) ||
       ( (_signo==1) && ( _value[i]>v._value[i] ) ) ) );
  }
  else
  {
    return (_signo > v._signo);
  }

}


bool BigInt::operator>=(const BigInt& v) const
{
  if (_signo == v._signo)
  {
    unsigned int i=0;
    while ((i<_value.size()) && (_value[i]==v._value[i])) i++;

    return ( (i==_value.size()) || ( (_signo==0) && ( _value[i]>v._value[i] ) ) ||
       ( (_signo==1) && ( _value[i]<v._value[i] ) ) );

  }
  else
  {
    return (_signo < v._signo);
  }
}

bool BigInt::operator>(const BigInt& v) const
{
  if (_signo == v._signo)
  {
    unsigned int i=0;
    while ((i<_value.size()) && (_value[i]==v._value[i])) i++;

    return ( (i<_value.size()) && (
       ( (_signo==0) && ( _value[i]>v._value[i] ) ) ||
       ( (_signo==1) && ( _value[i]<v._value[i] ) ) ) );
  }
  else
  {
    return (_signo < v._signo);
  }

}

bool BigInt::gt_abs(const BigInt& v) const
{
  unsigned int i=0;
  while ((i<_value.size()) && (_value[i]==v._value[i])) i++;

  return ( (i<_value.size()) && ( _value[i]>v._value[i] ) );
}

bool BigInt::operator==(const BigInt& v) const
{
  if (_signo != v._signo) return false;

  unsigned int i=0;
  while ((i<_value.size()) && (_value[i]==v._value[i])) i++;

  return (i==_value.size());

}

unsigned int BigInt::signo() const
{
  return _signo;
}

bool BigInt::isZero() const
{
  unsigned int i=0;
  while ((i<_value.size()) && (_value[i]==0)) i++;
  return (i==_value.size());
}

void BigInt::setZero()
{
  for ( unsigned int i=0;i<_value.size();i++) _value[i]=0;
  _signo=0;
}


unsigned int BigInt::bitsize() const
{
  return _bitsize;
}

const BigInt BigInt::abs() const
{
  BigInt r(*this);
  r._signo = 0;
  return r;
}


bool BigInt::operator==(const unsigned long long int v) const
{
  return  ( (_value[0]) == ((unsigned int*)&v)[1] ) && ( (_value[1]) == ((unsigned int*)&v)[0] );
}

std::ostream& operator<<(std::ostream& o,const BigInt& v)
{
  o << ( (v._signo==0) ? "+" : "-" );
  for(unsigned int i = 0; i < v._value.size(); i++)
  {
    o<< hex << v._value[i] << "." << dec;
  }

  return o;
}


unsigned int log2int(unsigned int v)
{
  if (v==0) return 0;

  bitset<32> bits(v);
  unsigned int i = bits.size()-1;

  while ( (i>0) && (!bits[i]) ) i--;
  return i;
}

unsigned int pwr2int(unsigned int v)
{
  return (1 << v);
}
