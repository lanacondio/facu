#include <iostream>
#include <vector>
#include <cassert>


#include <complex>

#include <SFML/System.hpp>
#include <SFML/Window.hpp>
#include <SFML/Graphics.hpp>


#include "Mandelbrot.h"

#include "BigFloat.h"
#include "BigInt.h"
#include "FloatType.h"

using namespace std;


unsigned int redondear(unsigned int prec)
{
  unsigned int solDivM = (prec / 32);
  unsigned int restoM = (prec % 32);
  if(restoM!=0)
  {
    solDivM++;
    return pwr2int(5+(solDivM-1));
  }
  else
    return prec;
}

int main()
{
  unsigned int width = 256;
  unsigned int height = 256;
  unsigned int iteraciones = 255;
  unsigned int precMantissa = 32;
  unsigned int precExponente = 32;
  unsigned int myType = 0;
  FloatType floatType;
  cout<<"Ingresar cantidad de iteraciones: ";
  cin>>iteraciones;
  cout<<endl;

  cout<<"Ingresar precision de la mantissa: ";
  cin>>precMantissa;
  cout<<endl;

  cout<<"Ingresar precision del exponente: ";
  cin>>precExponente;
  cout<<endl;

  cout<<"Ingresar tipo de flotante: ";
  cout<<endl;
  cout<<"float c++: 0";
  cout<<endl;
  cout<<"float asm 32bits: 1";
  cout<<endl;
  cout<<"float asm 64bits: 2";
  cout<<endl;
  cout<<"mantisa fija 128 bits: 3";
  cout<<endl;
  cin>>myType;
  cout<<endl;

  cout<<myType;

  switch(myType){
  case 0:
	  floatType = cmath_library;
	  break;
  case 1:
	  floatType = asm_32;
	  break;
  case 2:
	  floatType = asm_64;
	  break;
  case 3:
	  floatType = asm_128;
	  break;
  }

  precMantissa = redondear(precMantissa);
  precExponente = redondear(precExponente);

  Mandelbrot m(width,height,iteraciones,precExponente,precMantissa, floatType);

  sf::Image img;

  img.LoadFromPixels(width,height,m.GetPixels());

  float zoomstep = 0;
  float zoomcenterstep_x = 0;
  float zoomcenterstep_y = 0;

  int zoomcount = 0;
  int spritezoom = 0;

  sf::Sprite spr(img);

  sf::RenderWindow App(sf::VideoMode(width,height,32), "fractal");

  App.SetFramerateLimit(60);

  while (App.IsOpened())
  {

    sf::Event Event;
    while (App.GetEvent(Event))
    {
      if (Event.Type == sf::Event::Closed)
          App.Close();
      if ((Event.Type == sf::Event::KeyPressed) && (Event.Key.Code == sf::Key::Escape))
          App.Close();

      if((Event.Type == sf::Event::KeyPressed) && (Event.Key.Code == sf::Key::P))
      {
        std::stringstream nombreArch;
        nombreArch<< zoomcount << "-" << zoomcenterstep_x << "." << zoomcenterstep_y <<".jpg";

        img.SaveToFile(nombreArch.str());
        cout<<"Imagen guardada"<<endl;
      }

      if (Event.Type == sf::Event::Resized)
          glViewport(0, 0, Event.Size.Width, Event.Size.Height);

      if ( (Event.Type == sf::Event::MouseButtonPressed) && (Event.MouseButton.Button == sf::Mouse::Left ))
      {
        if (zoomcount==0)
        {
          int mx = App.GetInput().GetMouseX();
          int my = App.GetInput().GetMouseY();

          m.MovePixels(width/2-mx,height/2-my);
          m.ZoomIn();

          sf::Vector2f s = spr.GetScale();
          zoomcount = 50;
          spritezoom++;
          zoomstep = ( pow(2.,spritezoom)  -s.x) / (float)zoomcount;
          sf::Vector2f size = spr.GetSize();
          zoomcenterstep_x = ( (-size.x/2) + 2*(width/2-mx) ) / (float)zoomcount;
          zoomcenterstep_y = ( (-size.y/2) + 2*(height/2-my)) / (float)zoomcount;
        }

      }
      if ( (Event.Type == sf::Event::MouseButtonPressed) && (Event.MouseButton.Button == sf::Mouse::Right ))
      {
        if ((zoomcount==0) && (m.GetZoom()>0))
        {
          m.ZoomOut();

          sf::Vector2f s = spr.GetScale();
          zoomcount = 50;
          spritezoom--;
          zoomstep = ( pow(2.,spritezoom)-s.x) / (float)zoomcount;
          sf::Vector2f size = spr.GetSize();
          zoomcenterstep_x = (size.x/4) / (float)zoomcount;
          zoomcenterstep_y = (size.y/4) / (float)zoomcount;
        }

      }
      if (App.GetInput().IsKeyDown(sf::Key::Left))
      {
        m.MovePixels(1,0);
        spr.Move(1,0);

      }
      if (App.GetInput().IsKeyDown(sf::Key::Right))
      {
        m.MovePixels(-1,0);
        spr.Move(-1,0);

      }
      if (App.GetInput().IsKeyDown(sf::Key::Up))
      {
        m.MovePixels(0,1);
        spr.Move(0,1);
      }
      if (App.GetInput().IsKeyDown(sf::Key::Down))
      {
        m.MovePixels(0,-1);
        spr.Move(0,-1);
      }

    }

    if ( (zoomcount==0) &&  ( m.CopyToImage(img) == true))
    {
      spr.SetX(0);
      spr.SetY(0);
      spr.SetScale(1,1);
      spritezoom=0;
    }

    if (zoomcount>0)
    {
      spr.SetScale(spr.GetScale().x+ zoomstep ,spr.GetScale().y+zoomstep);
      spr.Move(zoomcenterstep_x,zoomcenterstep_y);
      zoomcount-=1;
    }


    App.Clear();

    App.Draw(spr);

    App.Display();
  }

  return 0;
}
