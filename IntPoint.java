package drawit;

import drawit.IntVector;

public final class IntPoint extends Object{
	private final int x,y;
	public IntPoint(int x, int y) {
		this.x=x; this.y=y;
	}
	/**
	 * @post Returns this intpoint x coordinate
	 *	|	result = this.x
	 */
	public int getX(){
		return this.x;
			
	}
	/**
	 * @post Returns this intpoint y coordinate
	 *  | result = this.y
	 */
	public int getY(){
		return this.y;
			
	}
	/**
	 *  <pre> other not null
	 * @post Returns true if this point has the same coordinates as the given point; returns false otherwise.
	 *	|	true = other.getX()==this.x && other.getY()==this.y
	 * @param other
	 */
	public boolean equals(IntPoint other) {
		if(other.getX()==this.x && other.getY()==this.y)
			return true;
		return false;
		
	}
	/**
	 *  <pre> other not null
	 * @post Returns an IntVector object representing the displacement from other to this.
	 *	|	result.getX() == this.getX() - other.getX()
	 *	|	result.getY() == this.getY() - other.getY()
	 * @param other
	 */
	public IntVector minus(IntPoint other) {
		return new IntVector(this.x-other.getX(),this.y-other.getY());
		
	}
	/**
	 * <pre> b, c not null
	 * @post Returns an IntVector object representing the displacement from other to this.
	 *	|	result.getX() == this.getX() - other.getX()
	 *	|	result.getY() == this.getY() - other.getY()
	 * @param other
	 */
	public boolean isOnLineSegment(IntPoint b, IntPoint c) {
		if (this.minus(b).isCollinearWith​(c.minus(b))) {
			double dotbabc = this.minus(b).dotProduct(c.minus(b));
			double dotbcbc = c.minus(b).dotProduct(c.minus(b));
			boolean isOnLineSegment = dotbabc > 0 && dotbabc < dotbcbc;
			return isOnLineSegment;
		} else {
			return false;
		}
	}
	/**
	 * @post Returns a DoublePoint object that represents the same 2D point represented by this IntPoint object
	 *	|	result.getX() == this.getX()
	 *	|	result.getY() == this.getY()
	 */
	public DoublePoint asDoublePoint() {
		
		return new DoublePoint((double)this.x, (double)this.y);
		
	}
	/**
	 * <pre> vector not null
	 * @post Returns an IntPoint object representing the point obtained by displacing this point by the given vector.
	 *	|	result.getX() == this.getX() + vector.getX()
	 *	|	result.getY() == this.getY() + vector.getY()
	 * @param other
	 */
	public IntPoint plus(IntVector vector) {
		return  new IntPoint (this.x+vector.getX(),this.y+vector.getY());
		
	}
	/**
	 * @pre The line segments have at most one point in common.
	 *	|	!(b.minus(a).isCollinearWith(d.minus(c)) && (a.isOnLineSegment(c,d) || b.isOnLineSegment(c,d) || c.isOnLineSegment(a,b)))
	 * @post if(Math.signum(ac.crossProduct​(ab))*(Math.signum(ad.crossProduct​(ab)))<0) true 
	 *	|	else false
	 * @param 
	 */
	public static boolean lineSegmentsIntersect(IntPoint a, IntPoint b, IntPoint c, IntPoint d) {

		IntVector ac = c.minus(a);
		IntVector ab = b.minus(a);
		IntVector ad = d.minus(a);

		return (Math.signum(ac.crossProduct(ab))*(Math.signum(ad.crossProduct(ab)))) < 0;
	}
	
}
