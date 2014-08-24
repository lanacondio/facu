#ifndef MANDELBROT_H_INCLUDED
#define MANDELBROT_H_INCLUDED

#include <cmath>
#include <SFML/System.hpp>
#include <SFML/Graphics.hpp>

#include "BigFloat.h"
#include "FloatType.h"
#include <complex>

using namespace std;

class Mandelbrot : private sf::Thread
{
  public:
    Mandelbrot(const Mandelbrot&);
    Mandelbrot(unsigned int width,unsigned int height,unsigned int loops,unsigned int exp_size, unsigned int mant_size,const FloatType type);
    ~Mandelbrot();

    bool CopyToImage(sf::Image& img);

    void ZoomIn();
    void ZoomOut();
    unsigned int GetZoom();

    void MovePixels(int x,int y);

    unsigned char* GetPixels();

    void test();

    unsigned int iterate(complex<BigFloat>& z, const complex<BigFloat>& c,int iter,const BigFloat& limit);

  private:

    void Do();

    unsigned char* _pixels;

    unsigned int _exp_size;
    unsigned int _mantissa_size;

    unsigned int _width;
    unsigned int _height;
    BigFloat _im_start;
    BigFloat _re_start;
    BigFloat _im_step;
    BigFloat _re_step;
    unsigned int _zoom;
    unsigned int _loops;

    bool _running;
    bool _updated;

    FloatType _type;
    virtual void Run();
};

#endif // MANDELBROT_H_INCLUDED
