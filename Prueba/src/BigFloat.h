#ifndef BIGFLOAT_H_INCLUDED
#define BIGFLOAT_H_INCLUDED

#include "BigInt.h"
#include "FloatType.h"
#include <iostream>


class BigFloat
{
  friend std::ostream& operator<<(std::ostream&,const BigFloat&);

  public:
    BigFloat(const BigFloat&);

    BigFloat(const int value,const unsigned int sizeExp,const unsigned int sizeMantissa, const FloatType type);

    ~BigFloat();

    BigFloat& operator*=(const BigFloat&);
    const BigFloat operator*(const BigFloat&) const;

    BigFloat& operator*=(const int&);
    const BigFloat operator*(const int&) const;

    BigFloat& operator/=(const int&);
    const BigFloat operator/(const int&) const;

    BigFloat& operator+=(const BigFloat&);
    const BigFloat operator+(const BigFloat&) const;

    BigFloat& operator-=(const BigFloat&);
    const BigFloat operator-(const BigFloat&) const;

    bool operator<(const BigFloat&) const;
    bool operator>(const BigFloat&) const;
    bool operator==(const BigFloat&) const;

    bool limit() const;

    unsigned int signo() const;

    const BigFloat Abs() const;

  private:
    BigFloat(const BigInt& exp,const BigInt& mant, const FloatType type);

    BigInt _exponente;
    BigInt _mantissa;
    unsigned int _expsize;
    unsigned int _mantsize;
    FloatType _float_type;
};

std::ostream& operator<<(std::ostream&,const BigFloat&);

#endif // BIGFLOAT_H_INCLUDED
