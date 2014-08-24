#ifndef COMPLEX_H_INCLUDED
#define COMPLEX_H_INCLUDED

#include <iostream>
#include <cmath>


using namespace std;

template<class T>
class Complex
{
  template<class S> friend std::ostream& operator<<(std::ostream&,const Complex<S>&);

  public:
    Complex<T>();
    Complex<T>(const Complex<T>&);
    Complex<T>(const T&,const T&);
    Complex<T>(T *,T *);

    Complex<T>& operator*=(const Complex<T>&);
    const Complex<T> operator*(const Complex<T>&) const;

    Complex<T>& operator+=(const Complex<T>&);
    const Complex<T> operator+(const Complex<T>&) const;

    T& real();
    T& imag();
  private:
    T* _real;
    T* _imag;
};

template<class T>
T& Complex<T>::real()
{
  return *_real;
}

template<class T>
T& Complex<T>::imag()
{
  return *_imag;
}

template <class T>
Complex<T>::Complex()
{
  _real = new T();
  _imag = new T();
}

template <class T>
Complex<T>::Complex(const Complex<T>& c)
{
  _real = new T(*c._real);
  _imag = new T(*c._imag);
}

template <class T>
Complex<T>::Complex(const T& re,const T& im)
{
  _real = new T(re);
  _imag = new T(im);
}

template <class T>
Complex<T>::Complex(T *re,T *im)
{
  _real=re;
  _imag=im;
}

template <class T>
Complex<T>& Complex<T>::operator*=(const Complex<T>& c)
{
  T tmp(*_real);

  tmp = (*_real) * (*c._real) - (*_imag) * (*c._imag);

  T tmp1(*_real);
  tmp1*= *c._imag;

  cout << "tmp1: " << tmp1 << endl;

  T tmp2(*c._real);
  tmp2*= *_imag;

  cout << "tmp2: " << tmp2 << endl;

  *_imag = (*_real) * (*c._imag) + (*c._real) * (*_imag);


  *_real = tmp;

  return *this;
}

template <class T>
const Complex<T> Complex<T>::operator*(const Complex<T>& c) const
{
  return Complex(*this)*=c;
}

template <class T>
Complex<T>& Complex<T>::operator+=(const Complex<T>& c)
{
  *_real += *c._real;
  *_imag += *c._imag;
  return *this;
}

template <class T>
const Complex<T> Complex<T>::operator+(const Complex<T>& c) const
{
  return Complex(*this)+=c;
}

template <class S>
std::ostream& operator<<(std::ostream& o,const Complex<S>& c)
{
  return o<<"("<<*c._real<<","<<*c._imag<<")";
}

#endif // COMPLEX_H_INCLUDED
