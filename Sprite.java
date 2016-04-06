import java.awt.*;
import java.awt.geom.*;

/*
 * Sprite by L. David Aites.
 * This tracks location, color, the image, and handles drawing.
 * This is not abstract so background things can be drawn in.
 * Sprites just kind of exist in the game. They don't interact.
 */

public class Sprite {
	
	//In case we want to ever make it invisible or turn it off.
	protected boolean bVisible;
	protected boolean bActive;
	
	protected Color color;
	
	protected Image image;
	protected AffineTransform transform;
	
	//Location and size variables.
	protected double dbXLoc;
	protected double dbYLoc;
	protected double dbWidth;
	protected double dbHeight;
	
	protected Graphics2D g2d;
	
	//Constructor.
	public Sprite(double pdbX, double pdbY, double pdbWidth, double pdbHeight, Color pColorIn)
	{
		dbXLoc = pdbX;
		dbYLoc = pdbY;
		dbWidth = pdbWidth;
		dbHeight = pdbHeight;
		transform = new AffineTransform();
		transform.setToIdentity();
		color = pColorIn;
	}
	
	//Right now it just draws a white rectangle. Lets us see location.
	public void Draw(Graphics2D g)
	{
		g.setColor(color);
		g.setStroke(new BasicStroke(2));
		g.drawRect((int)dbXLoc, (int)dbYLoc, (int)dbWidth, (int)dbHeight);
	}
	
	//Not used. Images are not implemented.
	public void LoadImage()
	{
		
	}
	
	//Get and set.
	public void SetVisible(boolean pbVisible)
	{
		bVisible = pbVisible;
	}
	
	public boolean GetIsVisible()
	{
		return bVisible;
	}
	
	public void SetActive(boolean pbActive)
	{
		bActive = pbActive;
	}
	
	public boolean GetIsActive()
	{
		return bActive;
	}
	
	public void SetXLoc(double pdbX)
	{
		dbXLoc = pdbX;
	}
	
	public double GetXLoc()
	{
		return dbXLoc;
	}
	
	public void SetYLoc(double pdbY)
	{
		dbYLoc = pdbY;
	}
	
	public double GetYLoc()
	{
		return dbYLoc;
	}
	
	public void SetImage(Image pImageIn)
	{
		image = pImageIn;
	}
	
	public Image GetImage()
	{
		return image;
	}
	
}
