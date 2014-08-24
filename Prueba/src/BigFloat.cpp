#include <bitset>

#include "BigFloat.h"
#include "FloatType.h"
#include <cassert>
#include <cmath>

BigFloat::BigFloat(const BigFloat& v):
  _exponente(v._exponente),
  _mantissa(v._mantissa),
  _expsize(v._expsize),
  _mantsize(v._mantsize),
  _float_type(v._float_type)
{

}

BigFloat::BigFloat(const BigInt& exp,const BigInt& mant, const FloatType type):
  _exponente(exp),
  _mantissa(mant),
  _expsize(exp.bitsize()),
  _mantsize(mant.bitsize()),
  _float_type(type)
{

}


BigFloat::BigFloat(const int value,const unsigned int sizeExp,const unsigned int sizeMantissa, const FloatType type):
  _exponente( log2int( _abs( value )) ,sizeExp, type),
  _mantissa( value , sizeMantissa, type),
  _expsize(sizeExp),
  _mantsize(sizeMantissa),
  _float_type(type)
{
  _mantissa.triml();
}

BigFloat::~BigFloat()
{

}

BigFloat& BigFloat::operator*=(const BigFloat& v)
{
  //duplico la mantisa porque puede que el tamaño se duplique
  _mantissa.resize(_mantsize*2);
  BigInt m2(v._mantissa,_mantsize*2, v._float_type);

  _mantissa*=m2;

  unsigned int s = _mantissa.triml();

  _exponente += 1;
  _exponente -= s;

  //trunco la mantisa hasta el _mantsize
  _mantissa.truncr(_mantsize);

  _exponente += v._exponente;

  if (_mantissa.isZero()) _exponente.setZero();

  return *this;
}

const BigFloat BigFloat::operator*(const BigFloat& v) const
{
  return BigFloat(*this)*=v;
}


BigFloat& BigFloat::operator*=(const int& v)
{
  if (v==0)
  {
    _mantissa.setZero();
    _exponente.setZero();
  }
  else
  {
    unsigned int lv = log2int( _abs(v) );

    _exponente += BigInt(lv,_exponente.bitsize(), this->_float_type);

    _mantissa._signo = (v>=0) ? 0 : 1;
  }

  return *this;
}

const BigFloat BigFloat::operator*(const int& v) const
{
  return BigFloat(*this)*=v;
}

BigFloat& BigFloat::operator/=(const int& v)
{
  unsigned int lv = log2int(_abs(v));

  _exponente-=lv;
  return (*this);
}

const BigFloat BigFloat::operator/(const  int& v) const
{
  return BigFloat(*this)/=v;
}


BigFloat& BigFloat::operator+=(const BigFloat& v)
{
    _mantissa.resize(_mantsize+1);
    BigInt m2 = BigInt(v._mantissa,_mantsize+1, this->_float_type);

    if ( _exponente < v._exponente )
    {
      BigInt d = (v._exponente-_exponente);
      _exponente = v._exponente;
      _mantissa >>= d.to_uint();
    }
    else if (_exponente > v._exponente)
    {
      BigInt d = (_exponente-v._exponente);
      m2 >>= d.to_uint();
    }

    _mantissa += m2;

    unsigned int s=_mantissa.triml();

    _exponente+= 1;
    _exponente-= s;

    _mantissa.truncr(_mantsize);

    if (_mantissa.isZero()) _exponente.setZero();

  return (*this);
}


const BigFloat BigFloat::operator+(const BigFloat& v) const
{
  return BigFloat(*this)+=v;
}

BigFloat& BigFloat::operator-=(const BigFloat& v)
{
  _mantissa.resize(_mantsize+1);
  BigInt m2 = BigInt(v._mantissa,v._mantsize+1, this->_float_type);


  if (v._exponente>_exponente)
  {
    BigInt d = (v._exponente-_exponente);
    _exponente = v._exponente;
    _mantissa >>= d.to_uint(); //esto limita el tamanio del exponente
  }
  else if (v._exponente<_exponente)
  {
    BigInt d = (_exponente-v._exponente);
    m2 >>= d.to_uint(); //esto limita el tamanio del exponente
  }

  _mantissa -= m2;

  unsigned int s=_mantissa.triml();

  _exponente+= 1;
  _exponente-= s;

  _mantissa.truncr(_mantsize);

  if (_mantissa.isZero()) _exponente.setZero();

  return (*this);
}

const BigFloat BigFloat::operator-(const BigFloat& v) const
{
  return BigFloat(*this)-=v;
}

bool BigFloat::operator<(const BigFloat& v) const
{
  if (_exponente==v._exponente)
  {
    return (_mantissa<v._mantissa);
  }
  else
  {
    return ( _exponente<v._exponente || _mantissa<v._mantissa );
  }
}

bool BigFloat::operator>(const BigFloat& v) const
{
  if (_exponente==v._exponente)
  {
    return (_mantissa>v._mantissa);
  }
  else
  {
    return (_exponente>v._exponente || _mantissa>v._mantissa);
  }
}

bool BigFloat::operator==(const BigFloat& v) const
{
  return ( (_exponente==v._exponente) && (_mantissa==v._mantissa));
}

unsigned int BigFloat::signo() const
{
  return _mantissa.signo();
}

const BigFloat BigFloat::Abs() const
{
  return BigFloat(_exponente,_mantissa.abs(), this-> _float_type);
}


std::ostream& operator<<(std::ostream& o,const BigFloat& v)
{
  o<< v._mantissa << " x 2^" << v._exponente;
  return o;
}
