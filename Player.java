import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

//Right now the player really just extends sprite.
//Keeps track of player movement and location.
//Movement is vector-based.
//Also tracks if the player has been hit recently and stores the score.

public class Player extends Sprite{
	
	//How far the player moves per tick.
	double dbMoveDist;
	double dbInertialSlow;
	
	//2 seconds of invulnerability after getting hit.
	static int INVULN_COOLDOWN = 100;
	
	//Tells the various methods what direction the player is moving.
	//Mostly used for drawing particles.
	boolean bLeftThrust, bRightThrust, bUpThrust, bDownThrust;
	
	//For tracking still being alive and playable as well as
	//immune to damage from being hit recently.
	boolean bInvulnerable, bAlive;
	
	Vector2 v2Movement;
	
	//Modifiers to movement speed. Better engines are faster.
	//Better gear, especially armor, makes the player heaiver and slower.
	double dbEngineMod, dbWeightMod;
	
	//Player has two guns.
	Gun gunMain, gunSecond;
	
	//Bounding rectangle.
	Rectangle rctBoundingBox;
		
	int nPlayerScore;
	int nInvulnCooldown;
	
	//Constructor.
	public Player(double pdbX, double pdbY, double pdbWidth, double pdbHeight, Color pColorIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		dbMoveDist = 5;
		nPlayerScore = 0;
		v2Movement = new Vector2(0, 0);
		dbInertialSlow = 0.95;
		rctBoundingBox = new Rectangle((int)pdbX, (int)pdbY, (int)pdbWidth, (int)pdbHeight);
		gunMain = new Gun(dbXLoc + 8, dbYLoc + 8, 8, 8, Color.PINK);
		gunSecond = new Gun(dbXLoc + 16, dbYLoc + 16, 8, 8, Color.PINK);
		dbEngineMod = 0.35;
		dbWeightMod = 1.0;
		nInvulnCooldown = 0;
		bAlive = true;
		bInvulnerable = false;
	}
	
	//Used when the game is over and the player has restarted after dying.
	//Initializes the player to the original values.
	public void ResetPlayer()
	{
		dbMoveDist = 5;
		nPlayerScore = 0;
		v2Movement = new Vector2(0, 0);
		dbInertialSlow = 0.95;
		gunMain = new Gun(dbXLoc + 8, dbYLoc + 8, 8, 8, Color.PINK);
		gunSecond = new Gun(dbXLoc + 16, dbYLoc + 16, 8, 8, Color.PINK);
		dbEngineMod = 0.35;
		dbWeightMod = 1.0;
		nInvulnCooldown = 0;
		bInvulnerable = false;
		bAlive = true;
	}
	
	//Pass in a bounding box, return if the player is colloding or not.
	public boolean CheckCollission(Rectangle prctCollide)
	{
		if(rctBoundingBox.intersects(prctCollide))
		{
			return true;
		}
		
		else return false;
	}
	
	//All the player drawing. Also draws the guns in as well as the score.
	//If the player is moving in a direction also draw a small square.
	public void Draw(Graphics2D g)
	{
		
		//Sets the player color. Invulnerable is cyan, alive is blue.
		//Daed is dark gray.
		if(bInvulnerable)
			color = Color.CYAN;
		
		else
			color = Color.BLUE;
		
		if(!bAlive)
			color = Color.DARK_GRAY;
		
		super.Draw(g);
		g.setColor(Color.WHITE);
		g.drawString("Score - " + Integer.toString(nPlayerScore), 10, 25);
		gunMain.Draw(g);
		gunSecond.Draw(g);
		
		g.setColor(Color.ORANGE);
		
		//Draws an engine square on the sides that are thrusting.
		//Only happens if the player is actually playing.
		if(bRightThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
			g.drawRect((int)dbXLoc - 6, (int)dbYLoc + 13, 6, 6);
		
		if(bDownThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
			g.drawRect((int)dbXLoc + 13, (int)dbYLoc - 6, 6, 6);
		
		if(bUpThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
			g.drawRect((int)dbXLoc + 13, (int)dbYLoc + 32, 6, 6);
			
		if(bLeftThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
			g.drawRect((int)dbXLoc + 32, (int)dbYLoc + 13, 6, 6);
		
	}
	
	//Receiving and losing points.
	public void GetPoints(int pnPointsEarned)
	{
		nPlayerScore += pnPointsEarned;
	}
	
	public void LosePoints(int pnPointsLost)
	{
		nPlayerScore -= pnPointsLost;
	}
	
	//Fires every tick. Updates player info, handles movement.
	public void Update()
	{
		
		//Decrement the invulnerability cooldown if applicable.
		if (nInvulnCooldown > 0)
		{
			nInvulnCooldown--;
		}
		
		//When it runs out return to normal.
		if (nInvulnCooldown <= 0)
		{
			bInvulnerable = false;
		}
		
		//Vector-based movement. Reads the player's vector and
		//updates x and y appropriately.
		dbXLoc += v2Movement.GetX();
		dbYLoc += v2Movement.GetY();
		
		//Recalc the bounding box every frame.
		rctBoundingBox.x = (int)dbXLoc;
		rctBoundingBox.y = (int)dbYLoc;
		
		//Slows the player down by its inertial value.
		v2Movement.MultiplyVector(dbInertialSlow);
		
		//Recalc gun positions.
		gunMain.SetLocation(dbXLoc + 8, dbYLoc + 8);
		gunSecond.SetLocation(dbXLoc + 16, dbYLoc + 16);
		
		//If the player is moving in a particular direction,
		//based on which keys are down, set the thrust booleans for drawing.
		if(DeepSpaceSchooner.arybKeys[DeepSpaceSchooner.KEY_A])
			bLeftThrust = true;
		
		else
			bLeftThrust = false;
		
		if(DeepSpaceSchooner.arybKeys[DeepSpaceSchooner.KEY_S])
			bDownThrust = true;
		
		else
			bDownThrust = false;
		
		if(DeepSpaceSchooner.arybKeys[DeepSpaceSchooner.KEY_W])
			bUpThrust = true;
		
		else bUpThrust = false;
		
		if(DeepSpaceSchooner.arybKeys[DeepSpaceSchooner.KEY_D])
			bRightThrust = true;
		
		else
			bRightThrust = false;
	}
	
	//This generates the engine particles. Only generates one per call.
	//Multiple particles will come from the main method.
	//Each if generates a particle that is basically the same,
	//except for the location.
	public void CreateParticles(ArrayList<Particle> parypParticlesIn)
	{
		
		if(bUpThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
		{
			
			//Vector-based movement. Gets the player's movement first
			//then applies the random movement from the particle class.
			Vector2 v2ParticleVector = new Vector2(v2Movement);
			
			//Determines a random color.
			int nRandomColor = DeepSpaceSchooner.rand.nextInt(3) + 1;
			Color clrRandom = Color.ORANGE;
			
			switch(nRandomColor)
			{
				case 1:
				{
					clrRandom = Color.WHITE;
					break;
				}
				
				case 2:
				{
					clrRandom = Color.ORANGE;
					break;
				}
				
				case 3:
				{
					clrRandom = Color.YELLOW;
					break;
				}
								
			}
			
			//Finally, add the particle to the array.
			parypParticlesIn.add(new Particle(dbXLoc + 14, dbYLoc + 32,
					4, 4, clrRandom, 30, v2ParticleVector));
		}
		
		if(bDownThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
		{
			Vector2 v2ParticleVector = new Vector2(v2Movement);
			
			int nRandomColor = DeepSpaceSchooner.rand.nextInt(3) + 1;
			Color clrRandom = Color.ORANGE;
			
			switch(nRandomColor)
			{
				case 1:
				{
					clrRandom = Color.WHITE;
					break;
				}
				
				case 2:
				{
					clrRandom = Color.ORANGE;
					break;
				}
				
				case 3:
				{
					clrRandom = Color.YELLOW;
					break;
				}
								
			}
			
			parypParticlesIn.add(new Particle(dbXLoc + 14, dbYLoc - 4,
					4, 4, clrRandom, 30, v2ParticleVector));
		}
		
		if(bLeftThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
		{
			Vector2 v2ParticleVector = new Vector2(v2Movement);
			
			int nRandomColor = DeepSpaceSchooner.rand.nextInt(3) + 1;
			Color clrRandom = Color.ORANGE;
			
			switch(nRandomColor)
			{
				case 1:
				{
					clrRandom = Color.WHITE;
					break;
				}
				
				case 2:
				{
					clrRandom = Color.ORANGE;
					break;
				}
				
				case 3:
				{
					clrRandom = Color.YELLOW;
					break;
				}
								
			}
			
			parypParticlesIn.add(new Particle(dbXLoc + 32, dbYLoc + 14,
					4, 4, clrRandom, 30, v2ParticleVector));
		}
		
		if(bRightThrust && DeepSpaceSchooner.gamestate == GameState.MainScreen)
		{
			Vector2 v2ParticleVector = new Vector2(v2Movement);
			
			int nRandomColor = DeepSpaceSchooner.rand.nextInt(3) + 1;
			Color clrRandom = Color.ORANGE;
			
			switch(nRandomColor)
			{
				case 1:
				{
					clrRandom = Color.WHITE;
					break;
				}
				
				case 2:
				{
					clrRandom = Color.ORANGE;
					break;
				}
				
				case 3:
				{
					clrRandom = Color.YELLOW;
					break;
				}
								
			}
			
			parypParticlesIn.add(new Particle(dbXLoc - 6, dbYLoc + 14,
					4, 4, clrRandom, 30, v2ParticleVector));
		}
	}
	
	//Takes a movement vector in and adds it to the player's vector.
	//Allows basic inertial modelling for movement.
	//The vector coming in is modified by the player's engine and weight.
	public void VectorMove(Vector2 pbv2MoveIn)
	{
		pbv2MoveIn.MultiplyVector(dbEngineMod);
		pbv2MoveIn.MultiplyVector(dbWeightMod);
		v2Movement.AddVector(pbv2MoveIn);
	}
	
	// OLD DEPRECATED MOVEMENT CODE
	/*
	public void MoveLeft()
	{
		dbXLoc -= dbMoveDist;
		rctBoundingBox.x = (int)dbXLoc;
	}
	
	public void MoveRight()
	{
		dbXLoc += dbMoveDist;
		rctBoundingBox.x = (int)dbXLoc;
	}
	
	public void MoveUp()
	{
		dbYLoc -= dbMoveDist;
		rctBoundingBox.y = (int)dbYLoc;
	}
	
	public void MoveDown()
	{
		dbYLoc += dbMoveDist;
		rctBoundingBox.y = (int)dbYLoc;
	}
	*/
	// END OF OLD DEPRECATED CODE
	
	// Get methods.
	public Rectangle GetBoundingBox()
	{
		return rctBoundingBox;
	}

	public Gun GetGun()
	{
		return gunMain;
	}
	
	public Gun GetSecondGun()
	{
		return gunSecond;
	}
	
	public void SetInvulnerable()
	{
		bInvulnerable = true;
		nInvulnCooldown = INVULN_COOLDOWN;
	}
	
	public boolean GetInvulnerable()
	{
		return bInvulnerable;
	}
	
	public void SetAlive(boolean pbIsAlive)
	{
		bAlive = pbIsAlive;
	}
	
	public boolean GetIsAlive()
	{
		return bAlive;
	}
	
	public int GetScore()
	{
		return nPlayerScore;
	}
	
}
