import java.awt.Color;
import java.awt.Rectangle;

//The player's guns shoot these. This class mostly just handles collissions.

public class Projectile extends Sprite{
	
	private Rectangle rctBoundingBox;
	private Vector2 v2Movement;
	private boolean bCanHurtPlayer, bCanHurtEnemy, bIsDead;
	
	public Projectile(double pdbX, double pdbY, double pdbWidth, double pdbHeight, Color pColorIn,
			Vector2 pv2MovementIn)
	{
		
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
		v2Movement = pv2MovementIn;
		rctBoundingBox = new Rectangle((int)pdbX, (int)pdbY, (int)pdbWidth, (int)pdbHeight);
		bIsDead = false;
		
	}
	
	public boolean CheckCollission(Rectangle prctCollide)
	{
		if(rctBoundingBox.intersects(prctCollide))
		{
			bIsDead = true;
			return true;
		}
		
		else 
			return false;
	}
	
	public boolean GetIsDead()
	{
		return bIsDead;
	}
	
	public void Update()
	{
		dbXLoc += v2Movement.GetX();
		dbYLoc += v2Movement.GetY();
		rctBoundingBox.x = (int)dbXLoc;
		rctBoundingBox.y = (int)dbYLoc;
	}
	
	public void VectorMove(Vector2 pv2MoveIn)
	{
		v2Movement.AddVector(pv2MoveIn);
	}
	
}
