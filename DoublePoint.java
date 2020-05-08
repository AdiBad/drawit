package drawit;

public final class DoublePoint extends Object {
	private final double x;
	private final double y;
	
	public DoublePoint(double x, double y){
		this.x=x;
		this.y=y;
	}
	/**
	 * @post Returns this DoublePoint x coordinate
	 *	|	result = this.x
	 * @param 
	 * @return
	 */
	public double getX() {
		return this.x;
		
	}
	/**
	 * @post Returns this DoublePoint y coordinate
	 *	|	result = this.y
	 * @param 
	 * @return
	 */
	public double getY() {
		return this.y;
		
	}
	/**
	 *  <pre> b not null
	 * @post Returns an DoubleVector object representing the displacement from other to this.
	 *	|	result.getX() == this.getX() - other.getX()
	 *	|	result.getY() == this.getY() - other.getY()
	 * @param 
	 * @return
	 */
	public DoubleVector minus(DoublePoint b) {
		double temp_x = this.x - b.getX();
		double temp_y = this.y - b.getY();
		return new DoubleVector(temp_x,temp_y);
	}
	/**
	 * <pre> vector not null
	 * @post Returns an DoublePoint object representing the point obtained by displacing this point by the given vector.
	 *	|	result.getX() == this.getX() + vector.getX()
	 *	|	result.getY() == this.getY() + vector.getY()
	 * @param other
	 * @return
	 */
	public DoublePoint plus(DoubleVector other) {
		double temp_x = this.x + other.getX();
		double temp_y = this.y + other.getY();
		return new DoublePoint(temp_x,temp_y);
		
	}	
	/**
	 * <pre> vector not null
	 * @post Returns an IntPoint object whose coordinates are obtained by rounding the coordinates of this point to the nearest integer.
	 *	|	result.getX() == (int)Math.round(this.x)
	 *	|	result.getY() == (int)Math.round(this.y)
	 * @param 
	 * @return
	 */
	public IntPoint round() {
		return new IntPoint((int)Math.round(this.x),(int)Math.round(this.y));
		
	}
	
}
