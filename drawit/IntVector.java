package drawit;

import java.util.Objects;

/**
 * An instance of this class represents a displacement in the two-dimensional plane.
 * 
 * @immutable
 */
public class IntVector {
	
	private final int x;
	private final int y;
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	/**
	 * @pre | other != null
	 * @post | result == (getX() == other.getX() && getY() == other.getY())
	 */
	public boolean equals(IntVector other) {
		return x == other.x && y == other.y;
	}
	
	/**
	 * Returns a number that depends only on this object's coordinates.
	 * 
	 * @post | result == 31 * (31 + getX()) + getY()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	/**
	 * Returns a textual representation of this object.
	 * 
	 * @post | Objects.equals(result, "IntVector [x=" + getX() + ", y=" + getY() + "]")
	 */
	@Override
	public String toString() {
		return "IntVector [x=" + x + ", y=" + y + "]";
	}
	
	/**
	 * Returns {@code true} if the given object's class is {@code IntPoint} and
	 * this object represents the same displacement as the given object.
	 * 
	 * @post | result == (object != null && object.getClass() == this.getClass() && this.equals((IntVector)object))
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		IntVector other = (IntVector) object;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/**
	 * @mutates | this
	 * @post | getX() == x
	 * @post | getY() == y
	 */
	public IntVector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the cross product of this vector and the given vector.
	 *
	 * @pre | other != null
	 * @post | result == (long)getX() * other.getY() - (long)getY() * other.getX()
	 */
	public long crossProduct(IntVector other) {
		return (long)x * other.y - (long)y * other.x;
	}
	
	/**
	 * Returns the vector obtained by multiplying this vector's X coordinate by factor {@code xFactor}
	 * and its Y coordinate by factor {@code yFactor}.
	 * 
	 * @mutates nothing |
	 * @post | result != null
	 * @post | result.getX() == (int)Math.round(this.getX() * xFactor)
	 * @post | result.getY() == (int)Math.round(this.getY() * yFactor)
	 */
	public IntVector scale(double xFactor, double yFactor) {
		return new IntVector((int)Math.round(this.x * xFactor), (int)Math.round(this.y * yFactor));
	}
	
	/**
	 * Returns whether this vector is collinear with the given vector.
	 *
	 * @pre | other != null
	 * @post | result == (this.crossProduct(other) == 0)
	 */
	public boolean isCollinearWith(IntVector other) {
		return crossProduct(other) == 0;
	}
	
	/**
	 * Returns the dot product of this vector and the given vector.
	 *
	 * @pre | other != null
	 * @post | result == (long)getX() * other.getX() + (long)getY() * other.getY()
	 */
	public long dotProduct(IntVector other) { return (long)x * other.x + (long)y * other.y; }
	
	/**
	 * Returns a {@code DoubleVector} object that represents the same vector represented by this {@code IntVector} object.
	 * 
	 * @post | result != null
	 */
	public DoubleVector asDoubleVector() {
		return new DoubleVector(this.x, this.y);
	}
	
}
