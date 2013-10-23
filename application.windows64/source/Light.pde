class Light
{
  //Variables
  PVector position;
  PImage light;
  PImage mask;
  boolean expanding = true;

  float origWidth;
  float origHeight;
  float lightWidth;
  float lightHeight;
  float destWidth;
  float lightIncrement;

  //Constructor
  public Light(float x, float y)
  {
    position = new PVector(x, y);
    light = loadImage("data/light.png");
    mask = loadImage("data/mask.png");
    light.mask(mask);

    origWidth = light.width;
    origHeight = light.height;
    lightWidth = light.width;
    lightHeight = light.width;
    destWidth = light.width + 15;
    lightIncrement = 1.5;
  }

  //Display the light
  void display()
  {
    imageMode(CENTER);
    image(light, position.x, position.y, lightWidth, lightHeight);
  }

  //Make the light flicker
  void flicker()
  {
    if (expanding == true && lightWidth < destWidth)
    {
      lightWidth += lightIncrement;
      lightHeight += lightIncrement;    
      if (lightWidth >= destWidth)
      {
        expanding = false;
      }
    }
    else if (expanding == false && lightWidth > origWidth) 
    {
      lightWidth -= lightIncrement;
      lightHeight -= lightIncrement;

      if (lightWidth <= origWidth)
      {
        lightIncrement = random(0.4, 1.5);
        expanding = true;
      }
    }
  }
}

