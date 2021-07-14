package drawit;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Declares a number of methods useful for working with arrays of {@code IntPoint} objects.
 */
public class PointArrays {
	
	private PointArrays() { throw new AssertionError("This class is not meant to be instantiated"); }

	/**
	 * Returns {@code null} if the given array of points defines a proper polygon; otherwise, returns a string describing why it does not.
	 *
	 * <p>We interpret an array of N points as the polygon whose vertices are the given points and
	 * whose edges are the open line segments between point I and point (I + 1) % N, for I = 0, ..., N - 1.
	 *
	 * <p>A proper polygon is one where no two vertices coincide and no vertex is on any edge and no two edges intersect.
	 * 
	 * <p>If there are exactly two points, the polygon is not proper. Notice that if there are more than two points and no two vertices
	 * coincide and no vertex is on any edge, then no two edges intersect at more than one point.
	 * 
	 * @pre | points != null
	 * @pre | Arrays.stream(points).allMatch(p -> p != null)
	 * @inspects | points
	 * @mutates nothing |
	 * @post
	 *    | (result == null) == (
	 *    |     points.length != 2 &&
	 *    |     IntStream.range(0, points.length).allMatch(i ->
	 *    |         IntStream.range(0, points.length).allMatch(j -> i == j || !points[i].equals(points[j]))) &&
	 *    |     IntStream.range(0, points.length).allMatch(i ->
	 *    |         IntStream.range(0, points.length).allMatch(j -> !points[i].isOnLineSegment(points[j], points[(j + 1) % points.length]))) &&
	 *    |     IntStream.range(0, points.length).allMatch(i ->
	 *    |         IntStream.range(0, points.length).allMatch(j -> i == j ||
	 *    |             !IntPoint.lineSegmentsIntersect(points[i], points[(i + 1) % points.length], points[j], points[(j + 1) % points.length])))
	 *    | )
	 */
	public static String checkDefinesProperPolygon(IntPoint[] points) {
		if (points.length == 2)
			return "Line segment 0 intersects with line segment 1";
		// If `points.length != 2`, then either some vertices are on some line segments or the line segments have at most one point in common.
		for (int i = 0; i < points.length - 1; i++) {
			for (int j = i + 1; j < points.length; j++) {
				if (points[i].equals(points[j]))
					return "IntPoint " + i + " coincides with point " + j;
				if (points[i].isOnLineSegment(points[j], points[(j + 1) % points.length]))
					return "IntPoint " + i + " is on line segment " + j;
				if (points[j].isOnLineSegment(points[i], points[i + 1]))
					return "IntPoint " + j + " is on line segment " + i;
				if (IntPoint.lineSegmentsIntersect(points[i], points[i + 1], points[j], points[(j + 1) % points.length]))
					return "Line segment " + i + " intersects with line segment " + j;
			}
		}
		return null;
	}
	
	/**
	 * Returns a new array with the same contents as the given array.
	 * @pre | points != null
	 * @pre | Arrays.stream(points).allMatch(p -> p != null)
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length
	 * @post | IntStream.range(0, points.length).allMatch(i -> result[i].equals(points[i]))  
	 */
	public static IntPoint[] copy(IntPoint[] points) {
		IntPoint[] result = new IntPoint[points.length];
		for (int i = 0; i < points.length; i++)
			result[i] = points[i];
		return result;
	}

	/**
	 * Returns a new array whose elements are the elements of the given array with the given point inserted at the given index.
	 * 
	 * @pre | points != null
	 * @pre | Arrays.stream(points).allMatch(p -> p != null)
	 * @pre | 0 <= index && index <= points.length
	 * @pre | point != null
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length + 1
	 * @post | IntStream.range(0, index).allMatch(i -> result[i].equals(points[i]))
	 * @post | result[index].equals(point)
	 * @post | IntStream.range(index, points.length).allMatch(i -> result[i + 1].equals(points[i]))
	 */
	// Javadoc notes:
	// - It's also acceptable to allow null elements, but then you have to use Objects.equals or == to compare elements
	// - It's also acceptable to compare elements using == in this case
	public static IntPoint[] insert(IntPoint[] points, int index, IntPoint point) {
		IntPoint[] result = new IntPoint[points.length + 1];
		for (int i = 0; i < index; i++)
			result[i] = points[i];
		result[index] = point;
		for (int i = index; i < points.length; i++)
			result[i + 1] = points[i];
		return result;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the element at the given index removed.
	 * 
	 * @pre | points != null
	 * @pre | Arrays.stream(points).allMatch(p -> p != null)
	 * @pre | 0 <= index && index < points.length
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length - 1
	 * @post | IntStream.range(0, index).allMatch(i -> result[i].equals(points[i]))
	 * @post | IntStream.range(index + 1, points.length).allMatch(i -> result[i - 1].equals(points[i]))
	 */
	public static IntPoint[] remove(IntPoint[] points, int index) {
		IntPoint[] result = new IntPoint[points.length - 1];
		for (int i = 0; i < index; i++)
			result[i] = points[i];
		for (int i = index; i < result.length; i++)
			result[i] = points[i + 1];
		return result;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the element at the given index replaced by the given point.
	 * 
	 * @pre | points != null
	 * @pre | 0 <= index && index < points.length
	 * @pre | value != null
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length
	 * @post | IntStream.range(0, points.length).allMatch(i -> result[i].equals(i == index ? value : points[i]))
	 */
	public static IntPoint[] update(IntPoint[] points, int index, IntPoint value) {
		IntPoint[] result = copy(points);
		result[index] = value;
		return result;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array, translated along the given vector.
	 * 
	 * @pre | points != null
	 * @pre | delta != null
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length
	 * @post | IntStream.range(0, points.length).allMatch(i -> result[i].equals(points[i].plus(delta)))
	 */
	public static IntPoint[] translate(IntPoint[] points, IntVector delta) {
		return Arrays.stream(points).map(p -> p.plus(delta)).toArray(n -> new IntPoint[n]);
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array, scaled by the given X and Y factors.
	 * The scaling is done with respect to the given origin.
	 * 
	 * For example: scaling point (20, 20) with respect to origin (10, 10) by xFactor == 30 and yFactor == 50 results in
	 * point (310, 510).
	 * 
	 * @pre | points != null
	 * @pre | origin != null
	 * @inspects | points
	 * @mutates nothing |
	 * @creates | result
	 * @post | result != null
	 * @post | result.length == points.length
	 * @post | IntStream.range(0, points.length).allMatch(i -> result[i].equals(origin.plus(points[i].minus(origin).scale(xFactor, yFactor))))
	 */
	public static IntPoint[] scale(IntPoint[] points, IntPoint origin, double xFactor, double yFactor) {
		return Arrays.stream(points).map(p -> origin.plus(p.minus(origin).scale(xFactor, yFactor))).toArray(n -> new IntPoint[n]);
	}

}
