package drawit.shapegroups2;

import drawit.IntPoint;

/**
 * Each instance of this class represents a rectangular area in a 2D coordinate
 * system, whose edges are parallel to the coordinate axes.
 * 
 * Note: the "top" and "bottom" terminology used by this class assumes
 * that the Y axis points down, as is common in computer graphics. 
 * 
 * This class must deal with illegal arguments defensively.
 * 
 * @immutable
 * 
 * @invar | getLeft() <= getRight()
 * @invar | getTop() <= getBottom()
 * @invar | getWidthAsLong() == (long)getRight() - getLeft()
 * @invar | getHeightAsLong() == (long)getBottom() - getTop()
 */
public class Extent {
	
	/**
	 * @invar | 0 <= width
	 * @invar | 0 <= height
	 * @invar | left + width <= Integer.MAX_VALUE
	 * @invar | top + height <= Integer.MAX_VALUE
	 */
	private final int left;
	private final int top;
	private final long width;
	private final long height;

	/**
	 * Returns the X coordinate of the edge parallel to the Y axis
	 * with the smallest X coordinate.
	 */
	public int getLeft() { return left; }
	/**
	 * Returns the Y coordinate of the edge parallel to the X axis
	 * with the smallest Y coordinate.
	 */
	public int getTop() { return top; }
	/**
	 * Returns the X coordinate of the edge parallel to the Y axis
	 * with the largest X coordinate.
	 */
	public int getRight() { return (int)(left + width); }
	/**
	 * Returns the Y coordinate of the edge parallel to the X axis
	 * with the largest Y coordinate.
	 */
	public int getBottom() { return (int)(top + height); }
	/**
	 * Returns the distance between the edges that are parallel to the Y axis.
	 */
	public long getWidthAsLong() { return width; }
	/**
	 * Returns the distance between the edges that are parallel to the Y axis.
	 * 
	 * @throws UnsupportedOperationException if the width cannot be represented as an {@code int}
	 *    | getWidthAsLong() > Integer.MAX_VALUE
	 * @post | result == getWidthAsLong()
	 */
	public int getWidth() {
		if (width > Integer.MAX_VALUE)
			throw new UnsupportedOperationException("width too big");
		return (int)width;
	}
	/**
	 * Returns the distance between the edges that are parallel to the X axis.
	 */
	public long getHeightAsLong() { return height; }
	/**
	 * Returns the distance between the edges that are parallel to the X axis.
	 * 
	 * @throws UnsupportedOperationException if the height cannot be represented as an {@code int}
	 *    | getHeightAsLong() > Integer.MAX_VALUE
	 * @post | result == getHeightAsLong()
	 */
	public int getHeight() {
		if (height > Integer.MAX_VALUE)
			throw new UnsupportedOperationException("height too big");
		return (int)height;
	}
	
	/**
	 * Returns the top-left corner of this extent.
	 * 
	 * @post | result != null
	 * @post | result.equals(new IntPoint(getLeft(), getTop()))
	 */
	public IntPoint getTopLeft() { return new IntPoint(left, top); }
	
	/**
	 * Returns the bottom-right corner of this extent.
	 * 
	 * @post | result != null
	 * @post | result.equals(new IntPoint(getRight(), getBottom()))
	 */
	public IntPoint getBottomRight() { return new IntPoint(getRight(), getBottom()); }
	
	/**
	 * Returns whether this extent, considered as a closed set of points (i.e.
	 * including its edges and its vertices), contains the given point.
	 * 
	 * @throws IllegalArgumentException if {@code point} is null
	 *    | point == null
	 * @post
	 *    | result == (
	 *    |     getLeft() <= point.getX() && point.getX() <= getRight() &&
	 *    |     getTop() <= point.getY() && point.getY() <= getBottom()
	 *    | ) 
	 */
	public boolean contains(IntPoint point) {
		return
				getLeft() <= point.getX() && point.getX() <= getRight() &&
				getTop() <= point.getY() && point.getY() <= getBottom();
	}

	/**
	 * Returns whether this extent equals the given extent.
	 * 
	 * @post | result == (
	 *       |     other != null &&
	 *       |     getTopLeft().equals(other.getTopLeft()) &&
	 *       |     getBottomRight().equals(other.getBottomRight())
	 *       | )
	 */
	public boolean equals(Extent other) {
		return other != null && left == other.left && top == other.top && width == other.width && height == other.height;
	}
	
	/**
	 * Returns whether this extent equals the given object.
	 * 
	 * @post | result == (other instanceof Extent && this.equals((Extent)other))
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof Extent && equals((Extent)other);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getBottom();
		result = prime * result + left;
		result = prime * result + getRight();
		result = prime * result + top;
		return result;
	}
	
	@Override
	public String toString() {
		return "drawit.shapegroups1.Extent[left="+left+", top="+top+", right="+getRight()+", bottom="+getBottom()+"]";
	}
	
	private Extent(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.width = (long)right - left;
		this.height = (long)bottom - top;
	}
	
	/**
	 * Returns an object representing the extent defined by the given left, top, width, and height.
	 * 
	 * @throws IllegalArgumentException if the given width is negative
	 *    | width < 0
	 * @throws IllegalArgumentException if the given height is negative
	 *    | height < 0
	 * @throws IllegalArgumentException if the sum of {@code left} and {@code width} is greater than {@code Integer.MAX_VALUE}
	 *    | Integer.MAX_VALUE - width < left 
	 * @throws IllegalArgumentException if the sum of {@code top} and {@code height} is greater than {@code Integer.MAX_VALUE}
	 *    | Integer.MAX_VALUE - height < top
	 * @post | result != null
	 * @post | result.getLeft() == left
	 * @post | result.getTop() == top
	 * @post | result.getWidth() == width
	 * @post | result.getHeight() == height
	 */
	public static Extent ofLeftTopWidthHeight(int left, int top, int width, int height) {
		if (width < 0)
			throw new IllegalArgumentException("width is negative");
		if (height < 0)
			throw new IllegalArgumentException("height is negative");
		if (Integer.MAX_VALUE - width < left)
			throw new IllegalArgumentException("left + width too large");
		if (Integer.MAX_VALUE - height < top)
			throw new IllegalArgumentException("top + height too large");
		return new Extent(left, top, left + width, top + height);
	}
	
	/**
	 * Returns an object representing the extent defined by the given left, top, right, and bottom.
	 * 
	 * @throws IllegalArgumentException if the given right less than the given left
	 *    | right < left
	 * @throws IllegalArgumentException if the given bottom is less than the given top
	 *    | bottom < top
	 * @post | result != null
	 * @post | result.getLeft() == left
	 * @post | result.getTop() == top
	 * @post | result.getRight() == right
	 * @post | result.getBottom() == bottom
	 */
	public static Extent ofLeftTopRightBottom(int left, int top, int right, int bottom) {
		if (right < left)
			throw new IllegalArgumentException("right less than left");
		if (bottom < top)
			throw new IllegalArgumentException("bottom less than top");
		return new Extent(left, top, right, bottom);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same
	 * right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given left coordinate is greater than this extent's right coordinate
	 *    | getRight() < newLeft
	 * @post | result != null
	 * @post | result.getLeft() == newLeft
	 * @post | result.getTop() == getTop()
	 * @post | result.getRight() == getRight()
	 * @post | result.getBottom() == getBottom()
	 */
	public Extent withLeft(int newLeft) {
		if (getRight() < newLeft)
			throw new IllegalArgumentException("newLeft greater than getRight()");
		return new Extent(newLeft, top, getRight(), getBottom());
	}

	/**
	 * Returns an object that has the given top coordinate and the same
	 * left, right, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given left coordinate is greater than this extent's right coordinate
	 *    | getBottom() < newTop
	 * @post | result != null
	 * @post | result.getLeft() == getLeft()
	 * @post | result.getTop() == newTop
	 * @post | result.getRight() == getRight()
	 * @post | result.getBottom() == getBottom()
	 */
	public Extent withTop(int newTop) {
		if (getBottom() < newTop)
			throw new IllegalArgumentException("newLeft greater than getRight()");
		return new Extent(left, newTop, getRight(), getBottom());
	}

	/**
	 * Returns an object that has the given right coordinate and the same
	 * left, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given left coordinate is greater than this extent's right coordinate
	 *    | newRight < getLeft()
	 * @post | result != null
	 * @post | result.getLeft() == getLeft()
	 * @post | result.getTop() == getTop()
	 * @post | result.getRight() == newRight
	 * @post | result.getBottom() == getBottom()
	 */
	public Extent withRight(int newRight) {
		if (newRight < getLeft())
			throw new IllegalArgumentException("newLeft greater than getRight()");
		return new Extent(left, top, newRight, getBottom());
	}
	
	/**
	 * Returns an object that has the given bottom coordinate and the same
	 * left, top, and right coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given left coordinate is greater than this extent's right coordinate
	 *    | newBottom < getTop()
	 * @post | result != null
	 * @post | result.getLeft() == getLeft()
	 * @post | result.getTop() == getTop()
	 * @post | result.getRight() == getRight()
	 * @post | result.getBottom() == newBottom
	 */
	public Extent withBottom(int newBottom) {
		if (newBottom < getTop())
			throw new IllegalArgumentException("newLeft greater than getRight()");
		return new Extent(left, top, getRight(), newBottom);
	}
	
	/**
	 * Returns an object that has the given width and the same left, top,
	 * and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given width is negative
	 *    | newWidth < 0
	 * @throws IllegalArgumentException if the new right coordinate would be greater than {@code Integer.MAX_VALUE}
	 *    | Integer.MAX_VALUE - newWidth < getLeft()
	 * @post | result != null
	 * @post | result.getLeft() == getLeft()
	 * @post | result.getTop() == getTop()
	 * @post | result.getWidth() == newWidth
	 * @post | result.getHeight() == getHeight()
	 */
	public Extent withWidth(int newWidth) {
		if (newWidth < 0)
			throw new IllegalArgumentException("newWidth negative");
		if (Integer.MAX_VALUE - newWidth < getLeft())
			throw new IllegalArgumentException("new right coordinate would be greater than Integer.MAX_VALUE");
		return new Extent(left, top, left + newWidth, getBottom());
	}
	
	/**
	 * Returns an object that has the given height and the same left, top,
	 * and right coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if the given height is negative
	 *    | newHeight < 0
	 * @throws IllegalArgumentException if the new bottom coordinate would be greater than {@code Integer.MAX_VALUE}
	 *    | Integer.MAX_VALUE - newHeight < getTop()
	 * @post | result != null
	 * @post | result.getLeft() == getLeft()
	 * @post | result.getTop() == getTop()
	 * @post | result.getWidth() == getWidth()
	 * @post | result.getHeight() == newHeight
	 */
	public Extent withHeight(int newHeight) {
		if (newHeight < 0)
			throw new IllegalArgumentException("newHeight negative");
		if (Integer.MAX_VALUE - newHeight < getTop())
			throw new IllegalArgumentException("new bottom coordinate would be greater than Integer.MAX_VALUE");
		return new Extent(left, top, getRight(), top + newHeight);
	}
}
