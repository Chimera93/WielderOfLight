class Wall
{
  //Variables
  PVector position;
  float wallWidth;
  float wallHeight;

  //Constructor
  public Wall(float x, float y, float w, float h)
  {
    position = new PVector(x, y);

    wallWidth = w;
    wallHeight = h;
  }

  //Display wall
  void display()
  {
    rectMode(CENTER);
    fill(0);
    rect(position.x, position.y, wallWidth, wallHeight);
  }
}

