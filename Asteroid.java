import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

//Asteroids just drift from one end of the screen to the other.
//They're kind of slow and are mostly meant to get blown up.
//They don't react or act in any way.
//When they get blown up the chunk apart if they're big enough.
public class Asteroid extends Sprite {
	
	//Used for vector-based movement.
	Vector2 v2Movement;
	
	//For keeping track of how big and hurt they are.
	//Bigger asteroids fragment into more chunks.
	int nSizeClass, nHitPoints;
	
	//For tracking if it has been blown up or floated off the screen.
	boolean bIsAlive;
	boolean bOffscreen;
	
	Rectangle rctBoundingBox;
	
	//Constructor.
	public Asteroid(double pdbX, double pdbY, double pdbWidth, double pdbHeight,
			Color pColorIn, Vector2 pv2MovementIn, int pnSizeIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		nSizeClass = pnSizeIn;
		nHitPoints = nSizeClass * 2;
		v2Movement = pv2MovementIn;
		bIsAlive = true;
		bOffscreen = false;
		rctBoundingBox = new Rectangle((int)dbXLoc, (int)dbYLoc,
				(int)dbWidth, (int)dbHeight);
	}
	
	//If the asteroid gets blown up this will get called.
	//What it does is it checks the asteroid's size.
	//If the asteroid is big enough it will create some smaller asteroids.
	//These can then chunk apart as well.
	public void ChunkAsteroid(ArrayList<Asteroid> pnaryaAsteroids)
	{

		//Determine maximum size and number.
		int nMaxSize = nSizeClass - 1;
		int nMaxAsteroids = nSizeClass;
		
		//Randomly determine asteroid number. Always spawn at least one.
		int nAsteroidsSpawned = DeepSpaceSchooner.rand.nextInt(nMaxAsteroids) + 1;
		
		//Asteroids are generated in this loop.
		//If the asteroid was destroyed from being offscreen this does nothing.
		while (nAsteroidsSpawned > 0 && bOffscreen == false)
		{
			
			//Size of generated asteroid. Will be at least 1.
			int nSizeToSpawn = DeepSpaceSchooner.rand.nextInt(nMaxSize) + 1;
			
			//Generates a random vector.
			double dbXVect = DeepSpaceSchooner.rand.nextDouble();
			double dbYVect = DeepSpaceSchooner.rand.nextDouble();
			
			//Flips x or y 50% of the time each.
			boolean bFlipX = DeepSpaceSchooner.rand.nextBoolean();
			boolean bFlipY = DeepSpaceSchooner.rand.nextBoolean();
			
			if (bFlipX)
				dbXVect *= -1;
			
			if (bFlipY)
				dbYVect *= -1;
			
			//Actually construct the vector.
			Vector2 v2NewAstVect = new Vector2(dbXVect, dbYVect);
			
			pnaryaAsteroids.add(new Asteroid(dbXLoc, dbYLoc,
					8 + nSizeToSpawn * 6, 8 + nSizeToSpawn * 6, Color.GRAY,
					v2NewAstVect, nSizeToSpawn));
			
			nAsteroidsSpawned --;
		}
	}
	
	//Creates a cloud of particles whenever the asteroid chunks.
	public void ChunkParticles(ArrayList<Particle> parypParticlesIn)
	{
		
		//How many?
		int nParticles = DeepSpaceSchooner.rand.nextInt(50) + 25;
		while(nParticles > 0)
		{
			double dbParticleX = dbXLoc + DeepSpaceSchooner.rand.nextInt((int)dbWidth);
			double dbParticleY = dbYLoc + DeepSpaceSchooner.rand.nextInt((int)dbHeight);
			
			Vector2 v2RandomVector = new Vector2(DeepSpaceSchooner.rand.nextDouble(),
					DeepSpaceSchooner.rand.nextDouble());
			
			parypParticlesIn.add(new Particle(dbParticleX, dbParticleY,
					4, 4, Color.GRAY, 30, v2RandomVector));
			
			nParticles--;
		}
	}
	
	//Mostly just moves the asteroid and checks its health.
	public void Update()
	{
		dbXLoc += v2Movement.GetX();
		dbYLoc += v2Movement.GetY();
		
		//Has it taken enough damage to die?
		if (nHitPoints < 0)
			bIsAlive = false;
		
		//Reposition the bounding box.
		rctBoundingBox.x = (int)dbXLoc;
		rctBoundingBox.y = (int)dbYLoc;
		
		//Check if the asteroid is offscreen. If it is, it is dead.
		if (dbXLoc > 900 || dbXLoc < -100 || dbYLoc > 700 || dbYLoc < -100)
		{
			bIsAlive = false;
			bOffscreen = true;
		}
	}
	
	//If something wants to hurt the asteroid.
	public void TakeDamage(int pnDamageTaken)
	{
		nHitPoints -= pnDamageTaken;
	}
	
	//Generates a few particles for when it gets hit.
	public void OnHitParticles(ArrayList<Particle> parypParticlesIn)
	{
		
		//How many?
		int nParticles = DeepSpaceSchooner.rand.nextInt(10) + 5;
		while(nParticles > 0)
		{
			double dbParticleX = dbXLoc + DeepSpaceSchooner.rand.nextInt((int)dbWidth);
			double dbParticleY = dbYLoc + DeepSpaceSchooner.rand.nextInt((int)dbHeight);
			
			Vector2 v2RandomVector = new Vector2(DeepSpaceSchooner.rand.nextDouble(),
					DeepSpaceSchooner.rand.nextDouble());
			
			parypParticlesIn.add(new Particle(dbParticleX, dbParticleY,
					4, 4, Color.GRAY, 30, v2RandomVector));
			
			nParticles--;
		}
	}
	
	//Get and set methods.
	public Rectangle GetBoundingBox()
	{
		return rctBoundingBox;
	}
	
	public boolean GetIsAlive()
	{
		return bIsAlive;
	}
	
	public boolean GetIsOffscreen()
	{
		return bOffscreen;
	}
	
	public void SetIsAlive(boolean pbIsAliveIn)
	{
		bIsAlive = pbIsAliveIn;
	}
	
	public void SetOffscreen(boolean pbOffscreenIn)
	{
		bOffscreen = pbOffscreenIn;
	}
	
	public int GetSize()
	{
		return nSizeClass;
	}
	

}
