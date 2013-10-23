class Crosshair
{
  //Variables
  PVector position;
  PImage crosshair;

  //Constructor
  Crosshair()
  {
    crosshair = loadImage("data/crosshair.png");
    position = new PVector(0, 0);
  }

  //Display
  void display(float x, float y)
  {
    position.x = x;
    position.y = y;
    image(crosshair, x, y);
  }
}

