package drawit;

public final class IntVector extends Object{
	private final int x,y;
	public IntVector(int x, int y) {
		this.x=x;
		this.y=y;
	}
	/**
	 * @post Returns this intvector x coordinate
	 *	|	result = this.x
	 * @param 
	 * @return
	 */
	public int getX() {
		return this.x;
		
	}
	/**
	 * @post Returns this intvector y coordinate
	 *	|	result = this.y
	 * @param 
	 * @return
	 */
	public int getY() {
		return this.y;
		
	}
	/**
	 * <pre> other not null
	 * @post Returns the cross product of this vector and the given vector.
	 *	|	result == (long)getX() * other.getY() - (long)getY() * other.getX()
	 *	|	result.getY() == this.getY()
	 * @param other
	 * @return
	 */
	public long crossProduct(IntVector other) {
		return (long)getX() * other.getY() - (long)getY() * other.getX();
		
	}
	/**
	 * <pre> other not null
	 * @post Returns whether this vector is collinear with the given vector.
	 *	|	result == (this.crossProduct(other) == 0)
	 * @param other
	 * @return
	 */
	public boolean isCollinearWith​(IntVector other) {
		return this.crossProduct(other)==0;
	}
	/**
	 * @post Returns the dot product of this vector and the given vector.
	 *	|	result == (long)getX() * other.getX() + (long)getY() * other.getY()
	 * @param other
	 * @return
	 */
	public long dotProduct(IntVector other) {
		return (long)getX() * other.getX() + (long)getY() * other.getY();
		
	}
	/**
	 * 
	 * @post Returns a DoubleVector object that represents the same vector represented by this IntVector object.
	 *	|	result.getX() == this.getX() 
	 *	|	result.getY() == this.getY()
	 * @param other
	 * @return
	 */
	public DoubleVector asDoubleVector() {
		return new DoubleVector((double)getX(),(double)getY());
	}
}
