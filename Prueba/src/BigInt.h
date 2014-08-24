#ifndef BIGINT_H_INCLUDED
#define BIGINT_H_INCLUDED

#include "FloatType.h"
#include <iostream>
#include <vector>

using namespace std;

class BigInt
{
  friend ostream& operator<<(ostream&,const BigInt&);

  public:
    BigInt(const BigInt&);
    BigInt(const int v,const unsigned int size, FloatType type);
    BigInt(const BigInt&,const unsigned int size, FloatType type);

    ~BigInt();

    BigInt& operator*=(const BigInt&);
    const BigInt operator*(const BigInt&) const;

    BigInt& operator+=(const BigInt&);
    const BigInt operator+(const BigInt&) const;

    BigInt& operator+=(const int);
    const BigInt operator+(const int) const;


    BigInt& operator-=(const BigInt&);
    const BigInt operator-(const BigInt&) const;

    BigInt& operator-=(const int);
    const BigInt operator-(const int) const;


    BigInt& operator<<=(const unsigned int&);
    const BigInt operator<<(const unsigned int&) const;

    BigInt& operator>>=(const unsigned int&);
    const BigInt operator>>(const unsigned int&) const;

    BigInt& operator>>=(const BigInt&);


    bool operator<(const BigInt&) const;
    bool operator>(const BigInt&) const;
    bool operator>=(const BigInt&) const;
    bool operator==(const BigInt&) const;


    bool operator==(const unsigned long long int) const;


    bool isZero() const;
    void setZero();

    unsigned int bitsize() const;

    unsigned int triml();
    void truncr(unsigned int newsize);
    unsigned int to_uint() const;

    unsigned int signo() const;

    const BigInt abs() const;

    void resize(unsigned int size);

  private:

    void dosbb(BigInt& dst, const BigInt& src);

    bool gt_abs(const BigInt& v) const;

    vector<unsigned int> _value;
    unsigned int _signo;
    unsigned int _bitsize;
    FloatType _float_type;

    friend class BigFloat;
};

ostream& operator<<(ostream&,const BigInt&);

unsigned int log2int(unsigned int v);

unsigned int pwr2int(unsigned int v);

unsigned int _abs(const int& v);

extern "C"
{
  unsigned int mult(unsigned int*,const unsigned int*,const unsigned int*,const unsigned int);
  unsigned int sum(unsigned int*,const unsigned int*,const unsigned int);
  unsigned int mult128(unsigned int*,const unsigned int*,const unsigned int*,const unsigned int);
  unsigned int sum128(unsigned int*,const unsigned int*,const unsigned int);
}


#endif // BIGINT_H_INCLUDED
