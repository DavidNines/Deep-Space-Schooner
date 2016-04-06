import java.awt.Color;

//For creating a particle system. Draws funny little squares.
//They have brief lifespans are are just pretty things for the game.

public class Particle extends Sprite{
	
	//Ticks the particle will live.
	int nLifespan;
	boolean bIsAlive;
	
	//Vector movement. Particles also have a second vector that changes
	//how the particle moves. Gives randomness.
	Vector2 v2Movement;
	Vector2 v2MovementInfluence;
	double dbInertialSlow;
	
	//Constructor.
	public Particle(double pdbX, double pdbY, double pdbWidth, double pdbHeight,
			Color pColorIn, int pnLifespanIn, Vector2 pv2MovementIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		nLifespan = pnLifespanIn;
		bIsAlive = true;
		v2Movement = pv2MovementIn;
		
		dbInertialSlow = 0.85;
		
		while (v2Movement.GetX() == 0 && v2Movement.GetY() == 0)
		{
			v2Movement.SetX(DeepSpaceSchooner.rand.nextInt(9) - 4);
			v2Movement.SetY(DeepSpaceSchooner.rand.nextInt(9) - 4);
		}
		
		//Generate the random particle movement.
		double dbMoveInfX = DeepSpaceSchooner.rand.nextDouble() *
				DeepSpaceSchooner.rand.nextInt(11) - 3;
		double dbMoveInfY = DeepSpaceSchooner.rand.nextDouble() *
				DeepSpaceSchooner.rand.nextInt(11) - 3;
		
		v2MovementInfluence = new Vector2(dbMoveInfX, dbMoveInfY);
		v2MovementInfluence.MultiplyVector(0.1);
		
	}
	
	//Decrements the lifespan and sets the particle to dead if it
	//has been alive long enough. Then it moves the particle based on
	//vector movement. They also get darker.
	public void Update()
	{
		nLifespan--;
		if (nLifespan <= 0)
		{
			bIsAlive = false;
		}
		
		dbXLoc += v2Movement.GetX();
		dbYLoc += v2Movement.GetY();
		
		v2Movement.AddVector(v2MovementInfluence);
		
		v2Movement.MultiplyVector(dbInertialSlow);
		
		//Makes the particle darker. This is somewhat random to
		//add some variance to a cloud of particles.
		int nRandomDarken = DeepSpaceSchooner.rand.nextInt(100);
		
		if (nLifespan % 5 == 0 && nRandomDarken >= 50)
		{
			color = color.darker();
		}
		
	}
	
	//Get and set methods.
	public boolean GetIsAlive()
	{
		return bIsAlive;
	}
	
	public void SetIsAlive(boolean pbIsAliveIn)
	{
		bIsAlive = pbIsAliveIn;
	}

}
