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

void setup()
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

void draw()
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
void keyPressed() {

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

void keyReleased() 
{
  if (key == 'w' || key == 'W') keyup = false; 
  if (key == 's' || key == 'S') keydown = false; 
  if (key == 'a' || key == 'A') keyleft = false; 
  if (key == 'd' || key == 'D') keyright = false;
}

void keyTyped()
{
  if (key == 'q' || key == 'Q')
  {
    flickerBackground();
  }
}

//Load all the walls
void setupWalls()
{
  walls.add(new Wall(0, 0, 100, 100));
}

//Flicker the background
void flickerBackground()
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

