import java.awt.Color;

//Asteroid that move like a normal one but randomly siwtches directions.
//Direction switch is still a cardinal direction.
//Waits a few seconds before switching again.

public class RandomMoveAsteroid extends Asteroid{
	
	//Tracks how long until the next change.
	int nRandomMoveDuration;
	
	//Constructor.
	public RandomMoveAsteroid(double pdbX, double pdbY, double pdbWidth, double pdbHeight,
			Color pColorIn, Vector2 pv2MovementIn, int pnSizeIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn, pv2MovementIn, pnSizeIn);
		//Set the initial time until the switch to 1 to 7 seconds.
		nRandomMoveDuration = DeepSpaceSchooner.rand.nextInt(300) + 50;
	}
	
	public void Update()
	{
		
		nRandomMoveDuration--;
		
		//When the timer runs out we switch direction.
		if(nRandomMoveDuration <= 0)
		{
			//Faster switching, .5 to 4.5 seconds.
			nRandomMoveDuration = DeepSpaceSchooner.rand.nextInt(200) + 25;
			//Randomly choose one of four directions.
			int nRandomDirection = DeepSpaceSchooner.rand.nextInt(4);
			
			switch(nRandomDirection)
			{
				case 0:
				{
					v2Movement.SetX(DeepSpaceSchooner.ASTEROID_MOVE);
					v2Movement.SetY(0);
					break;
				}
				
				case 1:
				{
					v2Movement.SetX(-DeepSpaceSchooner.ASTEROID_MOVE);
					v2Movement.SetY(0);
					break;
				}
				
				case 2:
				{
					v2Movement.SetX(0);
					v2Movement.SetY(DeepSpaceSchooner.ASTEROID_MOVE);
					break;
				}
				
				case 3:
				{
					v2Movement.SetX(0);
					v2Movement.SetY(-DeepSpaceSchooner.ASTEROID_MOVE);
					break;
				}
			}
			
		}
		
		super.Update();
		
	}

}
