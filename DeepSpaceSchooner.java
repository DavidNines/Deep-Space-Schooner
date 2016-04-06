import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/*
 * Deep Space Schooner by L. David Aites.
 * This is inspired by asteroids. It has a bunch of squares that mostly
 * float across the screen. Some move randomly, others chase.
 * The player can shoot them and blow them up for points.
 */

public class DeepSpaceSchooner extends JApplet implements Runnable{
	
	//For setting the resolution. 800x600.
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	
	//Each level runs for one minute.
	public static int LEVEL_LENGTH = 1000;
	
	//3 seconds to respawn if the player dies.
	public static int RESPAWN_DELAY = 150;
	
	//20 ticks between spawn updates. Makes for 2 a second.
	public static int SPAWN_DELAY = 25;
	
	//In case the player clears them really, really quickly.
	public static int MINIMUM_ASTEROIDS = 3;
	
	//Tracks what state the game is in.
	public static GameState gamestate;
	
	public static Random rand = new Random();
	
	//Constants to indicate which boolean value the keyboard key
	//points to in the boolean array.
	public static final int KEY_W = 0;
	public static final int KEY_A = 1;
	public static final int KEY_S = 2;
	public static final int KEY_D = 3;
	public static final int KEY_SPACEBAR = 4;
	public static final int KEY_CRTL = 5;
	public static final int KEY_P = 6;
	public static final int KEY_O = 7;
	
	//Standard asteroid move speed.
	public static double ASTEROID_MOVE = 1.5;
	
	//For tracking where the mouse is.
	private int nMouseXLoc;
	private int nMouseYLoc;
		
	//For tracking where the window is. For keeping the mouse in the right place.
	public int nWindowX;
	public int nWindowY;
	
	//Tracking the rate of fire of the guns.
	private int nGunCooldown;
	private int nGunBaseCooldown;
	
	private int nLevel;
	private int nPlayerLives;
	
	//For tracking the chance asteroids have to spawn.
	private int nAsteroidChancePerLevel = 2;
	private int nAsteroidChance;
	
	private int nSpawnCheckDelay;
	private int nLevelTime;
	
	//Boolean array to track which keys are pressed.
	public static boolean[] arybKeys = new boolean[85];
	boolean bLeftMouseDown;
	
	//Are we playing with infinite lives mode on?
	boolean bInfiniteLives;
	
	//For double buffering.
	private Image imgOffscreenCanvas;
	private Graphics2D g2dOffscreenBrush;
	
	//Timing things.
	private Timer timer;
	private TimerListener clock;
	
	//Entity storage things.
	public static Player player;
	private ArrayList<Enemy> aryeEnemies;
	private Sprite sprtMouse;
	private ArrayList<Projectile> arypProjectiles;
	private ArrayList<Particle> arypParticles;
	private ArrayList<Asteroid> aryaAsteroids;
	
	//Initialization stuff.
	public void init()
	{
		
		//Window init. 800x600, black background.
		this.setBackground(Color.BLACK);
		
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		gamestate = GameState.IntroScreen;
		bLeftMouseDown = false;
		
		//Create the offscreen image to draw all over.
		imgOffscreenCanvas = createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
		g2dOffscreenBrush = (Graphics2D)imgOffscreenCanvas.getGraphics();
		
		nGunBaseCooldown = 10;
		nGunCooldown = 10;
		
		//Set up the listeners.
		this.addKeyListener(new TheKeyListener());
		this.addMouseListener(new TheMouseListener());
		
		//Default player loc right now is 100, 100. Size is 32x32.
		//Right now he's just a square. This will change.
		player = new Player(400, 300, 32, 32, Color.BLUE);
		sprtMouse = new Sprite(0, 0, 8, 8, Color.RED);
		
		//Initialize the timers. The timer fires every 20 ms.
		//Grants a framerate of 50 FPS.
		clock = new TimerListener();
		timer = new Timer(20, clock);
		
		bInfiniteLives = false;
		
		nAsteroidChance = 0;
		nLevel = 1;
		nPlayerLives = 5;
		
		//Old test enemy code.
		//Start with three enemies of various colors.
		/*
		aryeEnemies = new ArrayList<Enemy>();
		aryeEnemies.add(new Enemy(200, 200, 64, 64, Color.GREEN));
		aryeEnemies.add(new Enemy(400, 400, 64, 64, Color.RED));
		aryeEnemies.add(new Enemy(250, 250, 64, 64, Color.GRAY));
		*/
		
		//Initialize the arrays. Start empty.
		arypProjectiles = new ArrayList<Projectile>();
		arypParticles = new ArrayList<Particle>();
		aryaAsteroids = new ArrayList<Asteroid>();
		
		//Set the timers to default.
		nLevelTime = LEVEL_LENGTH;
		nSpawnCheckDelay = SPAWN_DELAY;
		
		//Start with a few asteroids on the screen.
		for (int i = 0; i < 5; i++)
		{
			this.SpawnAsteroid();
		}
		
		//Mouse starts at 0,0 but that won't last long.
		nMouseXLoc = 0;
		nMouseYLoc = 0;
		
		//Initialize the spawn chance to zero percent.
		nAsteroidChance = 0;
		
		//Initialize the key array to false.
		for(int i = 0; i < 85; i++)
		{
			arybKeys[i] = false;
		}
		
		//Once everything is initialized start ticking.
		timer.start();
		
	}
	
	//Just redraws the screen with the offscreen image.
	public void paint(Graphics g)
	{
		this.requestFocus();
		
		g.drawImage(imgOffscreenCanvas, 0, 0, null);
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void destroy()
	{
		
	}
	
	public void run()
	{
		
	}
	
	//Where the input is processed. This will get ugly.
	private void ProcessInput()
	{
		
		//Player movement. If a key is being held influence the movement.
		//Each movement key adds a vector to the player.
		if(arybKeys[KEY_A] && gamestate == GameState.MainScreen)
		{
			player.VectorMove(new Vector2(-0.35, 0));
		}
		
		if(arybKeys[KEY_D] && gamestate == GameState.MainScreen)
		{
			player.VectorMove(new Vector2(0.35, 0));
		}
		
		if(arybKeys[KEY_W] && gamestate == GameState.MainScreen)
		{
			player.VectorMove(new Vector2(0, -0.35));
		}
		
		if(arybKeys[KEY_S] && gamestate == GameState.MainScreen)
		{
			player.VectorMove(new Vector2(0, 0.35));
		}
		
		//Starts the game if it's in the intro screen.
		if(arybKeys[KEY_SPACEBAR] && gamestate == GameState.IntroScreen)
		{
			gamestate = GameState.MainScreen;
		}
		
		if(arybKeys[KEY_SPACEBAR] && gamestate == GameState.PlayerDead)
		{
			Restart();
		}
		
		//Cheat code. Grants infinite lives.
		if(arybKeys[KEY_P] && arybKeys[KEY_CRTL])
		{
			bInfiniteLives = true;
		}
		
		//Turns it off.
		if(arybKeys[KEY_O] && arybKeys[KEY_CRTL])
		{
			bInfiniteLives = false;
		}
		
		//If the gun is ready to fire, the game is being played, and
		//the player is holding the left button, fire some bullets.
		if(bLeftMouseDown && nGunCooldown < 1 && !player.GetInvulnerable() &&
				gamestate == GameState.MainScreen)
		{
			nGunCooldown = nGunBaseCooldown;
			
			//Construct the bullet vectors.
			//First, get the lines between the guns and the cursor.
			Vector2 v2ProjectileOneVector = new Vector2((player.GetGun().GetXLoc() - nMouseXLoc) * -1,
					(player.GetGun().GetYLoc() - nMouseYLoc) * -1);
			Vector2 v2ProjectileTwoVector = new Vector2((player.GetSecondGun().GetXLoc() - nMouseXLoc) * -1,
					(player.GetSecondGun().GetYLoc() - nMouseYLoc) * -1);
			
			//Turn them into normalize vectors so we're left with only
			//the direction. Then multipy them for their speed.
			//Very fancy.
			v2ProjectileOneVector = v2ProjectileOneVector.Normalize();
			v2ProjectileTwoVector = v2ProjectileTwoVector.Normalize();
			v2ProjectileOneVector.MultiplyVector(7);
			v2ProjectileTwoVector.MultiplyVector(7);
			
			//Add them to the array list.
			arypProjectiles.add(new Projectile(player.GetGun().GetXLoc(), player.GetGun().GetYLoc(),
					4, 4, Color.CYAN, v2ProjectileOneVector));
			arypProjectiles.add(new Projectile(player.GetSecondGun().GetXLoc(),
					player.GetSecondGun().GetYLoc(), 4, 4, Color.CYAN, v2ProjectileTwoVector));
		}
		
	}
	
	//Ticks every 20 ms. Runs update and draw methods.
	//Works out to 50 frames per second.
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			//Get where the mouse is poitning and draw the targetting sprite there.
			Point ptMouseLoc = MouseInfo.getPointerInfo().getLocation();
			
			player.Update();
			
			//Set where the mouse is drawn. Based on the window's location
			//relative to the computer's desktop. Looks weird but it's right.
			nMouseXLoc = ptMouseLoc.x - nWindowX;
			nMouseYLoc = ptMouseLoc.y - nWindowY;
			
			sprtMouse.SetXLoc((double)nMouseXLoc);
			sprtMouse.SetYLoc((double)nMouseYLoc);
			
			//Clears the screen first.
			g2dOffscreenBrush.clearRect(0,  0,  SCREEN_WIDTH, SCREEN_HEIGHT);
			
			//Player always gets drawn.
			player.Draw(g2dOffscreenBrush);
			
			//Tell the player how to play if it's the intro.
			if (gamestate == GameState.IntroScreen)
			{
				g2dOffscreenBrush.drawString("WASD to move, spacebar to start",
						300, 300);
			}
			
			//Make screen updates. Check that method.
			//Keeps it tidier.
			if(gamestate == GameState.MainScreen)
			{
				MainScreenUpdate();
				MainScreenDraw();
			}
			
			//If the player is dead say the game is over and post the score.
			if(gamestate == GameState.PlayerDead)
			{
				g2dOffscreenBrush.setColor(Color.WHITE);
				g2dOffscreenBrush.drawString("Game over. So sad.", 300, 300);
				g2dOffscreenBrush.drawString("Final score - " +
						Integer.toString(player.GetScore()), 300, 330);
				g2dOffscreenBrush.drawString("Spacebar to restart.", 300, 360);
			}
			
			//The mouse always gets drawn.
			sprtMouse.Draw(g2dOffscreenBrush);
			
			//Draw what level it is.
			g2dOffscreenBrush.setColor(Color.WHITE);
			g2dOffscreenBrush.drawString("Level - " + Integer.toString(nLevel) +
					" Lives - " + Integer.toString(nPlayerLives), 10, 575);
						
			//After drawing everything on the secondary canvas do input.
			ProcessInput();
			
			//Actually draw it.
			repaint();
			
			//If we can see the window update the window locations.
			//Without the visible check this can sometimes throw errors.
			if(isVisible())
			{
				nWindowX = getLocationOnScreen().x;
				nWindowY = getLocationOnScreen().y;
			}
		}
	}
	
	//Where most of the magic happens. Sort of.
	//This mostly iterates through the array lists and calls updates.
	//Checks collissions, cleans up dead entitles. That sort of thing.
	private void MainScreenUpdate()
	{
		//First make sure the player is still alive.
		if (nPlayerLives <= 0)
			player.SetAlive(false);
		
		//Infinite lives just sets the lives to 5,000,000 every tick.
		if (bInfiniteLives == true)
			nPlayerLives = 5000000;
		
		//If the player is dead, game is over. Clear the lists, change the state.
		if (player.GetIsAlive() == false)
		{
			gamestate = GameState.PlayerDead;
			aryaAsteroids.clear();
			arypProjectiles.clear();
			arypParticles.clear();
		}
		
		
		//Timing variables decrement each tick.
		nLevelTime--;
		nSpawnCheckDelay--;
		
		//Tracking of the level. Each level only lasts a set amount of time.
		//Not really a separate level, just kind of...timers.
		if(nLevelTime <= 0)
		{
			nLevelTime = LEVEL_LENGTH;
			nLevel++;
		}
		
		SpawnRoutine();
		
		//Update asteroids.
		AsteroidListIterator();
		
		//Old test code for enemies and collissions.
		/*
		for (Enemy enemy : aryeEnemies)
		{
			enemy.MoveRandom();
			enemy.Update();
			
			for (Projectile projectile : arypProjectiles)
			{
				if (projectile.CheckCollission(enemy.GetBoudnginBox()))
				{
					enemy.TakeDamage(1);
				}
			}
			
			if (enemy.CheckCollission(player.GetBoundingBox()))
			{
				player.GetPoints(1);
			}
			
		}
		*/
		
		//Update projectiles.
		for (Projectile projectile : arypProjectiles)
		{
			projectile.Update();
		}
		
		//Update particles.
		for (Particle particle : arypParticles)
		{
			particle.Update();
		}
		
		//Create engine particles where appropriate.
		player.CreateParticles(arypParticles);
		
		//If the particle list has particles this checks and sees
		//if any of them are off screen and should be removed.
		if(!arypProjectiles.isEmpty())
		{
			for (int i = arypProjectiles.size() - 1; i >= 0; i--)
				{
					if (arypProjectiles.get(i).GetXLoc() < -25 ||
							arypProjectiles.get(i).GetXLoc() > 825 ||
							arypProjectiles.get(i).GetYLoc() < -25 ||
							arypProjectiles.get(i).GetYLoc() > 625 ||
							arypProjectiles.get(i).GetIsDead())
						arypProjectiles.remove(i);
				}
		}
		
		//More old enemy code.
		/*
		if(!aryeEnemies.isEmpty())
		{
			for (int i = aryeEnemies.size() - 1; i >= 0; i--)
			{
				if(aryeEnemies.get(i).GetIsDead())
				{
					for (int j = rand.nextInt(25) + 50; j >= 0; j--)
					{
						arypParticles.add(new Particle(aryeEnemies.get(i).GetXLoc() + rand.nextInt(32),
								aryeEnemies.get(i).GetYLoc() + rand.nextInt(32), 3, 3, Color.ORANGE,
								rand.nextInt(75) + 25, new Vector2(rand.nextInt(7) - 3, rand.nextInt(7) - 3)));								
					}
					
					aryeEnemies.remove(i);
				}
			}
		}
		*/
		
		//Particles have a lifespan. This checks and sees if any are dead.
		//If they are they get removed and discarded.
		if(!arypParticles.isEmpty())
		{
			for (int i = arypParticles.size() - 1; i >= 0; i--)
			{
				if(!arypParticles.get(i).GetIsAlive())
				{
					arypParticles.remove(i);
				}
			}
		}
		

		
		//If the gun has been fired reduce the wait time.
		if (nGunCooldown > 0)
			nGunCooldown--;
		
	}
	
	//Iterates through the asteroid list and do things to asteroids.
	private void AsteroidListIterator()
	{
		for (Asteroid asteroid : aryaAsteroids)
		{
			asteroid.Update();
			
			//Check and see if the player shot an asteroid.
			for (Projectile projectile : arypProjectiles)
			{
				if (projectile.CheckCollission(asteroid.GetBoundingBox()))
				{
					asteroid.TakeDamage(1);
					asteroid.OnHitParticles(arypParticles);
					player.GetPoints(1);
				}
			}
			
			//Check collissions between asteroids and the player.
			//Kill the player if there is a collission.
			if(player.CheckCollission(asteroid.GetBoundingBox()) &&
					!player.GetInvulnerable())
			{
				nPlayerLives--;
				player.SetInvulnerable();
			}
		}
		
		//Check the state of asteroids. If the asteroid got blown up chunk it.
		//If it is offscreen just remove it and don't chunk it.
		if(!aryaAsteroids.isEmpty())
		{
			for (int i = aryaAsteroids.size() - 1; i >= 0; i--)
			{
				Asteroid astCurrent = aryaAsteroids.get(i);
				
				if(!astCurrent.GetIsAlive())
				{
					if (astCurrent.GetSize() > 1)
					{
						astCurrent.ChunkAsteroid(aryaAsteroids);
						astCurrent.ChunkParticles(arypParticles);
					}
					
					//If the asteroid was blown up by the player
					//the player gets some points.
					//More points for bigger rocks.
					if (!astCurrent.GetIsOffscreen())
					{
						player.GetPoints(astCurrent.GetSize() * 50);
					}
					
					aryaAsteroids.remove(i);
				}
			}
		}
	}
	
	//Restarts the game. Really just resets a lot of variables
	//and puts the game state back to MainScreen.
	private void Restart()
	{
		nPlayerLives = 5;
		nLevel = 1;
		nLevelTime = LEVEL_LENGTH;
		nAsteroidChance = 0;
		gamestate = GameState.MainScreen;
		player.ResetPlayer();
		aryaAsteroids.clear();
		arypParticles.clear();
		arypProjectiles.clear();
	}
	
	//Runs through all the spawn checks.
	private void SpawnRoutine()
	{

		//If there aren't enough asteroids spawn some more.
		if(aryaAsteroids.size() <= MINIMUM_ASTEROIDS && nLevelTime > 100)
		{
			int nAsteroids = MINIMUM_ASTEROIDS - aryaAsteroids.size();
			
			while (nAsteroids > 0)
			{
				SpawnAsteroid();
				nAsteroids--;
			}
			
		}
		
		//Check for spawning new rocks 2 times a second.
		if(nSpawnCheckDelay <= 0)
		{
			nSpawnCheckDelay = SPAWN_DELAY;
			
			//Works out to a percentage chance to spawn new rocks.
			//Becomes more likely and more populated each level.
			int nRandomSpawnCheck = rand.nextInt(100);
			
			if (nRandomSpawnCheck <= nAsteroidChance)
			{
				nAsteroidChance = nAsteroidChancePerLevel * nLevel;
				
				int nAsteroids = rand.nextInt(2) + 1 + nLevel / 5;
				
				while(nAsteroids > 0)
				{
					SpawnAsteroid();
					nAsteroids--;
				}
			}
			
			//If the spawn fails the next spawn will be more likely.
			else
			{
				nAsteroidChance += nAsteroidChancePerLevel * nLevel;
			}
		}
		
	}
	
	//Randomly spawns asteroids. There is an equal chance of it spawning
	//on any screen edge. They appear just off screen and then drift
	//toward the opposite side.
	
	//There is a chance for special asteroids. Oh no.
	private void SpawnAsteroid()
	{
		
		//This random asteroid code is bad and I would like to 
		//rewrite it but do not have the time.
		//Defaults to 0, which means a normal asteroid.
		int nAstType = 0;
		
		//Asteroids grow harder varieties at higher levels.
		//This also makes the majority of asteroids always be regular.
		if (nLevel > 8)
			nAstType = rand.nextInt(9);
		
		else if (nLevel > 5)
			nAstType = rand.nextInt(8);
		
		else if (nLevel > 2)
			nAstType = rand.nextInt(7);
		
		//Random location, one of the four sides of the screen.
		int nRandLoc = rand.nextInt(3);
		switch(nRandLoc)
		{
			//Top.
			case 0:
			{
				int nXLoc = rand.nextInt(820) - 20;
				int nYLoc = -50;
				int nSize = rand.nextInt(8) + 2;
				
				Vector2 v2AstMove = new Vector2(0, 1.5);
				
				//1 in 9 will be a chaser at appropriate levels.
				if(nAstType == 8)
				{
					aryaAsteroids.add(new HomingAsteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				//1 or 2 of 9 will be random movers.
				else if(nAstType == 6 || nAstType == 7)
				{
					aryaAsteroids.add(new RandomMoveAsteroid(
							nXLoc, nYLoc, 16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				//Rest are generic floaters.
				else
				{
					aryaAsteroids.add(new Asteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				break;
			}
			
			//Left side.
			case 1:
			{
				int nXLoc = -50;
				int nYLoc = rand.nextInt(620) - 20;
				int nSize = rand.nextInt(4) + 2;
				
				Vector2 v2AstMove = new Vector2(1.5, 0);
				
				if(nAstType == 8)
				{
					aryaAsteroids.add(new HomingAsteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else if(nAstType == 6 || nAstType == 7)
				{
					aryaAsteroids.add(new RandomMoveAsteroid(
							nXLoc, nYLoc, 16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else
				{
					aryaAsteroids.add(new Asteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				break;
			}
			
			//Bottom.
			case 2:
			{
				int nXLoc = rand.nextInt(820) - 20;
				int nYLoc = 650;
				int nSize = rand.nextInt(4) + 2;
				
				Vector2 v2AstMove = new Vector2(0, -1.5);
				
				if(nAstType == 8)
				{
					aryaAsteroids.add(new HomingAsteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else if(nAstType == 6 || nAstType == 7)
				{
					aryaAsteroids.add(new RandomMoveAsteroid(
							nXLoc, nYLoc, 16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else
				{
					aryaAsteroids.add(new Asteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				break;
			}
			
			//Right side.
			case 3:
			{
				int nXLoc = 850;
				int nYLoc = rand.nextInt(620) - 20;
				int nSize = rand.nextInt(4) + 2;
				
				Vector2 v2AstMove = new Vector2(-1.5, 0);
				
				if(nAstType == 8)
				{
					aryaAsteroids.add(new HomingAsteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else if(nAstType == 6 || nAstType == 7)
				{
					aryaAsteroids.add(new RandomMoveAsteroid(
							nXLoc, nYLoc, 16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				
				else
				{
					aryaAsteroids.add(new Asteroid(nXLoc, nYLoc,
							16 + nSize * 6, 16 + nSize * 6,
							Color.GRAY, v2AstMove, nSize));
				}
				break;
			}
		}
	}
	
	//Primary draw method. It draws things.
	private void MainScreenDraw()
	{
		for (Particle particle : arypParticles)
		{
			particle.Draw(g2dOffscreenBrush);
		}
		
		for (Asteroid asteroid : aryaAsteroids)
		{
			asteroid.Draw(g2dOffscreenBrush);
		}
		
		//Old pre-asteroids enemy code.
		/*
		for (Enemy enemy : aryeEnemies)
		{
			enemy.Draw(g2dOffscreenBrush);
		}
		*/
		
		for (Projectile projectile : arypProjectiles)
		{
			projectile.Draw(g2dOffscreenBrush);
		}
		
		//Draws lines between the cursor and the ship.
		//For gun testing purposes. Uncomment if wanted.
		/*
		g2dOffscreenBrush.setColor(Color.WHITE);
		g2dOffscreenBrush.drawLine((int)player.GetGun().GetXLoc() + 4,
				(int)player.GetGun().GetYLoc() + 4, nMouseXLoc + 4, nMouseYLoc + 4);
		g2dOffscreenBrush.drawLine((int)player.GetSecondGun().GetXLoc() + 4,
				(int)player.GetSecondGun().GetYLoc() + 4, nMouseXLoc + 4, nMouseYLoc + 4);
		*/
	}
	
	//Doesn't do a whole lot right now.
	private class TheMouseListener implements MouseListener,
	MouseMotionListener
	{
		
		public void mouseClicked(MouseEvent e)
		{
			
		}
		
		public void mousePressed(MouseEvent e)
		{
			bLeftMouseDown = true;
		}
		
		public void mouseReleased(MouseEvent e)
		{
			bLeftMouseDown = false;
		}
		
		public void mouseEntered(MouseEvent e)
		{
			
		}
		
		public void mouseExited(MouseEvent e)
		{
			
		}
		
		public void mouseMoved(MouseEvent e)
		{
		}
		
		public void mouseDragged(MouseEvent e)
		{
			
		}
	}
	
	//Decides which keys are on.
	private class TheKeyListener implements KeyListener
	{
		
		//When a key gets pressed the appropriate member of the array
		//is set to true. It stays that way until released.
		public void keyPressed(KeyEvent e)
		{
			
			if(e.getKeyCode() == KeyEvent.VK_W)
			{
				arybKeys[KEY_W] = true; 
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A)
			{
				arybKeys[KEY_A] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_S)
			{
				arybKeys[KEY_S] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_D)
			{
				arybKeys[KEY_D] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				arybKeys[KEY_SPACEBAR] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			{
				arybKeys[KEY_CRTL] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_P)
			{
				arybKeys[KEY_P] = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_O)
			{
				arybKeys[KEY_O] = true;
			}
			
		}
		
		public void keyTyped(KeyEvent e)
		{
			
		}
		
		public void keyReleased(KeyEvent e)
		{
		
			//Released keys are set to false.
			if(e.getKeyCode() == KeyEvent.VK_W)
			{
				arybKeys[KEY_W] = false; 
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A)
			{
				arybKeys[KEY_A] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_S)
			{
				arybKeys[KEY_S] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_D)
			{
				arybKeys[KEY_D] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				arybKeys[KEY_SPACEBAR] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			{
				arybKeys[KEY_CRTL] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_P)
			{
				arybKeys[KEY_P] = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_O)
			{
				arybKeys[KEY_O] = false;
			}
			
		}
		
	}
	
}