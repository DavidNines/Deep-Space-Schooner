/*
 * Vector2 by L. David Aites.
 * Used for tracking movement within the game by using two dimensional vectors.
 * Two doubles for location, one for length. Automatically recals when
 * a point changes. Each point represents a point away from 0,0, where
 * the vector originates. This does NOT work for vectors with other
 * origins inherently.
 */

public class Vector2 {
	
	private double dbX;
	private double dbY;
	private double dbLength;
	
	//Constructor.
	public Vector2(double pdbX, double pdbY)
	{
		dbX = pdbX;
		dbY = pdbY;
		
		dbLength = Math.sqrt((dbX * dbX) + (dbY * dbY));
	}
	
	//Copy constructor.
	public Vector2(Vector2 pv2Vector2In)
	{
		dbX = pv2Vector2In.GetX();
		dbY = pv2Vector2In.GetY();
		dbLength = pv2Vector2In.GetLength();
	}
	
	//Get and set.
	public double GetLength()
	{
		return dbLength;
	}
	
	public double GetX()
	{
		return dbX;
	}
	
	public void SetX(double pdbXIn)
	{
		dbX = pdbXIn;
		dbLength = Math.sqrt((dbX * dbX) + (dbY * dbY));
	}
	
	public double GetY()
	{
		return dbY;
	}
	
	public void SetY(double pdbYIn)
	{
		dbY = pdbYIn;
		dbLength = Math.sqrt((dbX * dbX) + (dbY * dbY));
	}
	
	//Turns this into a unit vector. This gives back a vector
	//with a length of exactly one. Useful for getting directions.
	public Vector2 Normalize()
	{ 
		double dbXNew = dbX / dbLength;
		double dbYNew = dbY / dbLength;
		Vector2 v2New = new Vector2(dbXNew, dbYNew);
		return v2New;
	}
	
	//Multiplies the vector by the number put in. Moves the points
	//and recalculates the length as appropriate.
	public void MultiplyVector(double pdbMultIn)
	{
		dbX *= pdbMultIn;
		dbY *= pdbMultIn;
		dbLength = Math.sqrt((dbX * dbX) + (dbY * dbY));
	}
	
	//Adds one vector to the other. This is basically by adding each
	//point together to get where the new vector is. Recalcs length.
	public void AddVector(Vector2 pv2In)
	{
		dbX += pv2In.GetX();
		dbY += pv2In.GetY();
		dbLength = Math.sqrt((dbX * dbX) + (dbY * dbY));
	}
	
	//Two vectors are extremely unlikely to be the same but just in case...
	public boolean IsEqual(Vector2 pbv2One, Vector2 pbv2Two)
	{
		if (pbv2One.GetX() == pbv2Two.GetX() &&
				pbv2One.GetY() == pbv2Two.GetY())
			return true;
		
		else
			return false;
	}
	
}
