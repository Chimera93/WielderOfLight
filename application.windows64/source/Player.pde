class Player
{
  //Variables
  PVector position;
  float playerWidth;
  float playerHeight;
  PImage mask;
  float speed;
  float screenWidth;
  float screenHeight;
  Light light;
  Crosshair playerTarget;

  boolean isColliding = false;

  //Constructor
  public Player(int w, int h, Crosshair c)
  {
    //Set up values for player
    position = new PVector(w/2, h/2);
    playerWidth = 50;
    playerHeight = 50;
    speed = 8;
    light = new Light(position.x, position.y);    
    screenWidth = w;
    screenHeight = h;
    
    playerTarget = c;
  }

  //Draw the player
  void display()
  {
    //Draw image and mask
    light.position.x = position.x;
    light.position.y = position.y;
    light.display();
    light.flicker();

    //Draw player
    PVector direction = PVector.sub(playerTarget.position, this.position);
    
    pushMatrix();
    translate(position.x, position.y);
    rotate(direction.heading());
    rectMode(CENTER);
    fill(0);
    rect(0, 0, playerWidth, playerHeight);
    popMatrix();
  }

  //Update the player
  void update(boolean up, boolean right, boolean down, boolean left)
  {

    if (up && position.y > playerHeight/2) position.y -= speed;
    if (down && position.y < screenHeight - playerHeight/2) position.y += speed;
    if (left && position.x > playerWidth/2) position.x -= speed;
    if (right && position.x < screenWidth - playerWidth/2) position.x += speed;
  }
}

