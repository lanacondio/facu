#include "Mandelbrot.h"
#include "FloatType.h"

#include <cassert>

//copio la imagen para hacer el show
bool Mandelbrot::CopyToImage(sf::Image& img)
{
  if (!_running && _updated)
  {
    img.LoadFromPixels(_width,_height,GetPixels());
    cout<<"show"<<endl;
    _updated = false;

    return true;
  }
  else
  {
    return false;
  }
}

//hago el run
void Mandelbrot::Run()
{
  sf::Clock t;

  complex<BigFloat> c(_re_start,_im_start);

  BigFloat limit(2,_exp_size,_mantissa_size, this->_type);

  //itero por fila
  for (unsigned int i=0;(i<_height) && _running;i++)
  {
    cout << i << endl;
    int row = i*_width*4;
    c.real()=_re_start;


    if(this->_type != asm_128)
    {
    	//itero por columna
		for(unsigned int j=0;(j<_width) && _running ;j++)
		{
		  //creo el valor
		  complex<BigFloat> z( BigFloat(0,_exp_size,_mantissa_size, this->_type) , BigFloat(0,_exp_size,_mantissa_size, this->_type));

		  //hago la iteracion
		  unsigned int p = iterate(z,c,_loops,limit);

		  //imprimo resultado y asigno valores
		  cout<< p << endl;
		  _pixels[row+j*4+0]=p;
		  _pixels[row+j*4+1]=p;
		  _pixels[row+j*4+2]=p;
		  _pixels[row+j*4+3]=255;

		  c.real()+=_re_step;
		}
    }
    else
    {
    	//itero en 4 columnas en paralelo
    	unsigned int iterSize[4];
    	for(unsigned int j=0;(j<_width) && _running ;j+=4)
    	{
    		//creo los valores
    		complex<BigFloat> z_0( BigFloat(0,_exp_size,_mantissa_size, this->_type) , BigFloat(0,_exp_size,_mantissa_size, this->_type));
    		complex<BigFloat> z_1( BigFloat(0,_exp_size,_mantissa_size, this->_type) , BigFloat(0,_exp_size,_mantissa_size, this->_type));
    		complex<BigFloat> z_2( BigFloat(0,_exp_size,_mantissa_size, this->_type) , BigFloat(0,_exp_size,_mantissa_size, this->_type));
    		complex<BigFloat> z_3( BigFloat(0,_exp_size,_mantissa_size, this->_type) , BigFloat(0,_exp_size,_mantissa_size, this->_type));

    		complex<BigFloat> complexSize[4];
    		complexSize[0] = z_0;
    		complexSize[1] = z_1;
    		complexSize[2] = z_2;
    		complexSize[3] = z_3;

    		//hago la iteracion

    		iterSize = iterateArray(complexSize, c, _loops, limit);
    		//imprimo resultado y asigno valores


    		for (int x= j; x<4; x++)
    		{    t = x+j;
    			cout<< iterSize[x] << endl;
				_pixels[row+t*4+0]=iterSize[x];
				_pixels[row+t*4+1]=iterSize[x];
				_pixels[row+t*4+2]=iterSize[x];
				_pixels[row+t*4+3]=255;
    		}
    		c.real()+=_re_step;
    	}
    }
    c.imag()+=_im_step;

  }

  if (_running)
  {
    cout<<t.GetElapsedTime()<< " seconds"<<endl;
    _updated=true;
  }

  _running=false;
}

//constructor por defecto
Mandelbrot::Mandelbrot(const Mandelbrot&):
  _pixels(0),
  _exp_size(0),
  _mantissa_size(0),
  _width(0),
  _height(0),
  _im_start(0,0,0, this->_type),
  _re_start(0,0,0, this->_type),
  _im_step(0,0,0, this->_type),
  _re_step(0,0,0, this->_type),
  _zoom(0),
  _loops(0),
  _running(false),
  _updated(false),
  _type(cmath_library)
{
  assert(true);
}

//constructor por parametro
Mandelbrot::Mandelbrot(unsigned int width,unsigned int height,unsigned int loops,unsigned int exp_size, unsigned int mant_size,const FloatType type):
  _pixels(0),
  _exp_size(exp_size),
  _mantissa_size(mant_size),
  _width(width),
  _height(height),
  _im_start(-2,_exp_size,_mantissa_size, type),
  _re_start(-2,_exp_size,_mantissa_size, type),
  _im_step(BigFloat(4,_exp_size,_mantissa_size, type) / _height),
  _re_step(BigFloat(4,_exp_size,_mantissa_size, type) / _width),
  _zoom(0),
  _loops(loops),
  _running(false),
  _updated(false),
  _type(type)
{
  _pixels = new unsigned char[_width*_height*4];

  for (unsigned int i=0;i<_width*_height;i++)
  {
    _pixels[i*4+0]=0;
    _pixels[i*4+1]=0;
    _pixels[i*4+2]=0;
    _pixels[i*4+3]=255;
  }

  Do();
}

Mandelbrot::~Mandelbrot()
{
  delete[] _pixels;
}

void Mandelbrot::Do()
{
  _running = false;
  Wait();
  _updated = false;
  _running = true;
  Launch();
}

void Mandelbrot::ZoomIn()
{
  _zoom++;

  BigFloat h(_height,_exp_size,_mantissa_size, this->_type);
  h/=4;
  h*=_im_step;
  BigFloat w(_width,_exp_size,_mantissa_size, this->_type);
  w/=4;
  w*=_re_step;

  _im_start += h;
  _re_start += w;

  _re_step /= 2;
  _im_step /= 2;
  Do();
}

void Mandelbrot::ZoomOut()
{
  if (_zoom>0)
  {
    _zoom--;

    BigFloat h = BigFloat(_height,_exp_size,_mantissa_size, this->_type) / 2;
    BigFloat w = BigFloat(_width,_exp_size,_mantissa_size, this->_type) / 2;

    _im_start -= ( h * _im_step );
    _re_start -= ( w * _re_step );

    _re_step *= 2;
    _im_step *= 2;
    Do();
  }
}

unsigned int Mandelbrot::GetZoom()
{
  return _zoom;
}

//muevo el foco
void Mandelbrot::MovePixels(int x,int y)
{
  BigFloat tmp(y,_exp_size,_mantissa_size, this->_type);

  tmp*=_im_step;

  _im_start -= ( BigFloat(y,_exp_size,_mantissa_size, this->_type) * _im_step );
  _re_start -= ( BigFloat(x,_exp_size,_mantissa_size, this->_type) * _re_step );
  Do();
}

unsigned char* Mandelbrot::GetPixels()
{
  return _pixels;
}

//itero
unsigned int Mandelbrot::iterate(complex<BigFloat>& z, const complex<BigFloat>& c,int iter,const BigFloat& limit)
{
  BigFloat r = z.real().Abs();
  BigFloat i = z.imag().Abs();

  if ( (iter>0)  && (r<limit)  && (i<limit) && (_running))
  {
	//z = z^2 + c
    z *= z;

    z+=c;

    return iterate(z,c,iter-1,limit);

  }
  else
  {
    return iter;
  }
}
