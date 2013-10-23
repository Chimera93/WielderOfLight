import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Wielder_of_Light extends PApplet {

//Player values
Player player;

//Keypress 
boolean keyup = false;
boolean keyright = false;
boolean keyleft = false;
boolean keydown = false;

//Arraylist to hold our lights
ArrayList<Light> lights;
int maxLights;

//Hold our walls
ArrayList<Wall> walls;

//Make the crosshair
Crosshair target;

//Local global variables
float numFlickersLeft = 3;
float bgColor = 0;

public void setup()
{
  size(800, 600);
  background(0);
  noCursor();

  //Setup our lights
  lights = new ArrayList<Light>();
  maxLights = 0;

  //Setup our walls
  walls = new ArrayList<Wall>();
  setupWalls();

  target = new Crosshair();

  //Setup player 
  player = new Player((int)width, (int)height, target);
}

public void draw()
{
  background(bgColor);

  //Draw the player
  player.display();

  //Draw the lights
  for (Light light : lights)
  {    
    light.display();
    light.flicker();
  }

  //Draw the walls
  for (Wall wall : walls)
  {
    wall.display();
  }

  //Draw the crosshair
  target.display(mouseX, mouseY);

  //Update the player();
  player.update(keyup, keyright, keydown, keyleft);
}

//----------------------------------------------------------------------------
//This method controls keyPresses
//Code based from http://wiki.processing.org/w/Multiple_key_presses
//----------------------------------------------------------------------------
public void keyPressed() {

  if (key == 'w' || key == 'W') keyup = true; 
  if (key == 's' || key == 'S') keydown = true; 
  if (key == 'a' || key == 'A') keyleft = true; 
  if (key == 'd' || key == 'D') keyright = true;

  //Handle light drops
  if (key == 'f' || key == 'F')
  { 
    if (maxLights < 10)
    {
      lights.add(new Light(player.position.x, player.position.y));
      maxLights++;
    }
    else
    {
      lights.remove(0);
      lights.add(new Light(player.position.x, player.position.y));
    }
  }
}

public void keyReleased() 
{
  if (key == 'w' || key == 'W') keyup = false; 
  if (key == 's' || key == 'S') keydown = false; 
  if (key == 'a' || key == 'A') keyleft = false; 
  if (key == 'd' || key == 'D') keyright = false;
}

public void keyTyped()
{
  if (key == 'q' || key == 'Q')
  {
    flickerBackground();
  }
}

//Load all the walls
public void setupWalls()
{
  walls.add(new Wall(0, 0, 100, 100));
}

//Flicker the background
public void flickerBackground()
{
  if (numFlickersLeft > 0)
  {
    numFlickersLeft--;
    println("Flickering background");
    int limit = 30;
    float increment = 1;

    bgColor = limit;
  }
}

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
  public void display(float x, float y)
  {
    position.x = x;
    position.y = y;
    image(crosshair, x, y);
  }
}

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
    lightIncrement = 1.5f;
  }

  //Display the light
  public void display()
  {
    imageMode(CENTER);
    image(light, position.x, position.y, lightWidth, lightHeight);
  }

  //Make the light flicker
  public void flicker()
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
        lightIncrement = random(0.4f, 1.5f);
        expanding = true;
      }
    }
  }
}

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
  public void display()
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
  public void update(boolean up, boolean right, boolean down, boolean left)
  {

    if (up && position.y > playerHeight/2) position.y -= speed;
    if (down && position.y < screenHeight - playerHeight/2) position.y += speed;
    if (left && position.x > playerWidth/2) position.x -= speed;
    if (right && position.x < screenWidth - playerWidth/2) position.x += speed;
  }
}

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
  public void display()
  {
    rectMode(CENTER);
    fill(0);
    rect(position.x, position.y, wallWidth, wallHeight);
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Wielder_of_Light" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
