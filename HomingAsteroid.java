import java.awt.Color;

//Works just like the asteroid class except that it's movement vector
//is influenced by player location. It floats like normal briefly but
//then breaks off and chases the player. It takes it a moment to
//get right on the player's trail thanks to vector movement.

public class HomingAsteroid extends Asteroid{
	
	//Used for the secondary vector toward the player.
	Vector2 v2MovementInfluence;
	
	//How long it will float for before chasing.
	int nFloatTime;
	
	//Constructor.
	public HomingAsteroid(double pdbX, double pdbY, double pdbWidth, double pdbHeight,
			Color pColorIn, Vector2 pv2MovementIn, int pnSizeIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn, pv2MovementIn, pnSizeIn);
		v2MovementInfluence = new Vector2 (0, 0);
		nFloatTime = DeepSpaceSchooner.rand.nextInt(175) + 25;
	}
	
	//Drifts onto the screen for two seconds. Then it chases.
	//So creepy.
	public void Update()
	{
		if (nFloatTime > 0)
			nFloatTime--;
		
		//Homing behavior.
		//This then creates a vector pointed toward the player and adds it.
		//The end result is a normal vector that chases the player.
		if (nFloatTime <= 0)
		{
			double dbPlayerX = DeepSpaceSchooner.player.GetXLoc();
			double dbPlayerY = DeepSpaceSchooner.player.GetYLoc();
			v2MovementInfluence.SetX(dbPlayerX - dbXLoc);
			v2MovementInfluence.SetY(dbPlayerY - dbYLoc);
			v2MovementInfluence = v2MovementInfluence.Normalize();
			
			v2Movement.AddVector(v2MovementInfluence);
			v2Movement = v2Movement.Normalize();
			v2Movement.MultiplyVector(0.75);
		}
		
		super.Update();
	}

}
