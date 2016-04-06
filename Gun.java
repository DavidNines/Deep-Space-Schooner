import java.awt.Color;

//This doesn't actually do a whole lot.
public class Gun extends Sprite{

	public Gun(double pdbX, double pdbY, double pdbWidth, double pdbHeight, Color pColorIn)
	{
		super(pdbX, pdbY, pdbWidth, pdbHeight, pColorIn);
	}
	
	public void SetX(double pdbXIn)
	{
		dbXLoc = pdbXIn;
	}
	
	public void SetY(double pdbYIn)
	{
		dbYLoc = pdbYIn;
	}
	
	public void SetLocation(double pdbXIn, double pdbYIn)
	{
		dbXLoc = pdbXIn;
		dbYLoc = pdbYIn;
	}
	
}
