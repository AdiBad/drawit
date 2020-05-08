package drawit;

public final class DoubleVector extends Object{
	private final double x;
	private final double y;
	
	public DoubleVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * @post Returns this DoubleVector x coordinate
	 *	|	result = this.x
	 */
	public double getX() {
		return this.x;
		
	}
	/**
	 * @post Returns this DoubleVector y coordinate
	 *	|	result = this.y
	 */
	public double getY() {
		return this.y;
		
	}
	/**
	 * @creates | new DoubleVector()
	 * @pre Argument {@code d} is not {@code null}
	 * @post Returns a DoubleVector object whose coordinates are obtained by multiplying this vector's coordinates by the given scale factor
	 * | result.getX() == this.getX()*d
	 * | result.getY() == this.getY()*d
	 * @param d
	 */
	public DoubleVector scale(double d){
		return new DoubleVector(this.x*d, this.y*d);
		
	}
	/**
	 * @creates	|	new DoubleVector()
	 * @pre Argument {@code other} is not {@code null}
	 * 	|	other != null
	 * @post Returns an DoubleVector object representing the vector obtained by displacing this point by the given vector.
	 *	|	result.getX() == this.getX() + vector.getX()
	 *	|	result.getY() == this.getY() + vector.getY()
	 * @param other
	 */
	public DoubleVector plus(DoubleVector other) {
		return new DoubleVector(other.getX()+this.x, other.getY()+this.y);
		
	}
	/**
	 * @creates	double
	 * @post returns Euclidean distance from origin
	 *	|	result == Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2))
	 * @return d
	 */
	public double getSize() {
		//finding Euclidean distance from origin
		double dist = Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2));
		return dist;
		
	}
	/**
	 * @creates	|	new DoubleVector()
	 * @pre Argument {@code other} is not {@code null}
	 * 	|	other != null
	 * @post returns dot product of this vector and given vector
	 *	|	result == this.getX() * other.getX() + this.getY() * other.getY()
	 * @param other
	 */
		public double dotProduct(DoubleVector other) {
			double dotProd = this.getX() * other.getX() + this.getY() * other.getY();
		return dotProd;
		
	}
	/**
	 * @creates	|	new DoubleVector()
	 * @pre Argument {@code other} is not {@code null}
	 * 	|	other != null
	 * @post returns cross product of this vector and given vector
	 *	|	result == this.getX() * other.getY() - this.getY() * other.getX()
	 * @param other
	 * @return double
	 */
	public double crossProduct(DoubleVector other) {
		double crossProd = this.getX() * other.getY() - this.getY() * other.getX();
		return crossProd;
		
	}
	/**
	 * @post Returns the angle from positive X to this vector, in radians.
	 *	|	result == Math.atan2(this.y, this.x)
	 */
		public double asAngle() {
			return Math.atan2(this.y, this.x);
		
	}
}
