import java.awt.Color;
import java.awt.Rectangle;

//Old enemy code. No longer actually used.
//Really just kept track of itself and moved randomly.
//Mostly just for testing.
public class Enemy extends Sprite{
	
	int nTicksLeft;
	int nDirection;
	int nHitPoints;
	
	double dbMoveSpeed;
	Rectangle rctBoundingBox;
	Vector2 v2Movement;
	EnemyType enemytype;
	boolean bIsDead;
	
	//Constructor.
	public Enemy(double pdbX, double pdbY, double pdbWidth, double pdbHeight, Color pColorIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		nTicksLeft = 0;
		dbMoveSpeed = 1;
		rctBoundingBox = new Rectangle((int)pdbX, (int)pdbY, (int)pdbWidth, (int)pdbHeight);
		v2Movement = new Vector2(0, 0);
		enemytype = EnemyType.RandomMover;
		boolean bIsDead = false;
		nHitPoints = 5;
	}
	
	public Enemy(double pdbX, double pdbY, double pdbWidth, double pdbHeight,
			Color pColorIn, EnemyType pEnemyTypeIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		nTicksLeft = 0;
		dbMoveSpeed = 1;
		rctBoundingBox = new Rectangle((int)pdbX, (int)pdbY, (int)pdbWidth, (int)pdbHeight);
		v2Movement = new Vector2(0, 0);
		enemytype = pEnemyTypeIn;
		boolean bIsDead = false;
	}
	
	//Used when things want to hurt it.
	public void TakeDamage(int pnDamageTaken)
	{
		if (nHitPoints > 0)
		{
			nHitPoints -= pnDamageTaken;
		}
		if (nHitPoints <= 0)
		{
			nHitPoints = 0;
			bIsDead = true;
		}
	}
		
	public Rectangle GetBoudnginBox()
	{
		return rctBoundingBox;
	}
	
	public boolean GetIsDead()
	{
		return bIsDead;
	}
	
	//If the enemy is colliding with whatever called this it changes color.
	public boolean CheckCollission(Rectangle prctCollide)
	{
		if(rctBoundingBox.intersects(prctCollide))
		{
			RandomColor();
			return true;
		}
		
		else return false;
	}
	
	//Called every tick. Makes the enemy move.
	public void Update()
	{
		dbXLoc += v2Movement.GetX();
		dbYLoc += v2Movement.GetY();
		v2Movement.MultiplyVector(0.95);
	}
	
	//Generates random movement.
	public void MoveRandom()
	{
		
		//Check to see if there is any time left for random movement.
		//If there is not, reset the direction and give it a new duration.
		//This will be 25 to 50 ticks currently.
		//0 is up, 1 is right, 2 is down, 3 is left.
		//After we decide which direction to go we move.
		if(nTicksLeft <= 0)
		{
			nTicksLeft = DeepSpaceSchooner.rand.nextInt(25) + 25;
			nDirection = DeepSpaceSchooner.rand.nextInt(4);			
		}
		else
		{
			nTicksLeft--;
		}
		
		switch(nDirection)
		{
			case 0:
			{
				v2Movement.AddVector(new Vector2(-0.1, 0));
				break;
			}
			
			case 1:
			{
				v2Movement.AddVector(new Vector2(0.1, 0));
				break;
			}
			
			case 2:
			{
				v2Movement.AddVector(new Vector2(0, 0.1));
				break;
			}
			
			case 3:
			{
				v2Movement.AddVector(new Vector2(0, -0.1));;
				break;
			}
		}
		
		RecalcBoundingBox();
		
	}
	
	public void RecalcBoundingBox()
	{
		rctBoundingBox.x = (int)dbXLoc;
		rctBoundingBox.y = (int)dbYLoc;
	}
	
	public void RandomColor()
	{
		int nRed = DeepSpaceSchooner.rand.nextInt(255);
		int nGreen = DeepSpaceSchooner.rand.nextInt(255);
		int nBlue = DeepSpaceSchooner.rand.nextInt(255);
		this.color = new Color(nRed, nGreen, nBlue);
	}
}
