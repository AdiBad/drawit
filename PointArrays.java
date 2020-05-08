package drawit;

import drawit.IntPoint;

public class PointArrays extends Object {
	/**
	 * <pre> points array not null
	 * @post Returns null if the given array of points defines a proper polygon; otherwise, returns a string describing why it does not.
	 * @param 
	 * @return
	 */
	public static String checkDefinesProperPolygon(IntPoint[] points) {
		String checkDefinesProperPolygon = null;
		if (points == null || points.length < 3) {
			checkDefinesProperPolygon = "This is not a polygon because it has less than 3 sides";
		} else {
			for(int i = 0; i < points.length; i++) {
				for(int j = i+1; j < points.length; j++) {
					if (points[i].getX() == (points[j]).getX() && points[i].getY() == points[j].getY()) {
						checkDefinesProperPolygon = "There are two seperate points in the same position. Their indices are : " + i + j;
					}
				}
			}
		}
		if (checkDefinesProperPolygon == null) {
			for(int i = 0; i < points.length; i++) {
				for(int j = 0; j < points.length ; j++) {
					if(points[j].isOnLineSegment(points[i], points[(i+1) % points.length])) { //modulo makes sure the last vertex and first vertex are counted
						checkDefinesProperPolygon = "The point at index " + String.valueOf(j) + "is on line segment of indices" + String.valueOf(i) + String.valueOf((i+1) % points.length);
						return checkDefinesProperPolygon;
					} else if (IntPoint.lineSegmentsIntersect(points[i], points[(i+1) % points.length], points[j], points[(j+1) % points.length])) {
						checkDefinesProperPolygon = "The edge at indexes " + String.valueOf(i) + " and " + String.valueOf((i+1) % points.length) + " intersects with the edge at indexes " + String.valueOf(j) + " and " + String.valueOf((j+1) % points.length);
						return checkDefinesProperPolygon;
					}
				}
			}
		}
		return checkDefinesProperPolygon;
	}
	/**
	 * <pre> points array not null
	 * @post Returns a new array with the same contents as the given array.
	 *	|	result = points
	 * @param 
	 * @return
	 */
	public static IntPoint[] copy(IntPoint[] points) {
		IntPoint[] otherpoints = points;
		return otherpoints;
		
	}
	/**
	 * <pre> Returns a new array whose elements are the elements of the given array with the given point inserted at the given index.
	 *	|	0 <= index && index <= points.length
	 * @param points, index, point
	 * @return
	 */
	public static IntPoint[] insert(IntPoint[] points, int index, IntPoint point) throws IllegalArgumentException{
		int len = points.length;
		IntPoint newpoints[] = new IntPoint[len+1];
		if(point==null) {
			throw new IllegalArgumentException();
		}
		if(0 <= index && index <= len)
		{
			for(int i=0;i<len+1;i++) 
			{
				if(i<index) {
					newpoints[i] = points[i];
				}
				else if(index==0 || i==index) {
					newpoints[i] = point;
				}
				else {
					newpoints[i] = points[i - 1];
				}
			}
			return newpoints;
		}
		else {
			throw new IllegalArgumentException();
		}
		
	}
	/**
	 * <pre> points array not null
	 *	|	0 <= index && index < length of array
	 * @post Returns a new array whose elements are the elements of the given array with the element at the given index removed.
	 * @param 
	 * @return
	 */
		public static IntPoint[] remove(IntPoint[] points, int index) throws IllegalArgumentException{
			if(points == null)
				throw new IllegalArgumentException();
			
			int len = points.length;
		IntPoint newpoints[] = new IntPoint[len-1];

		if(0 <= index && index < len)
		{
			int j=0;
			for(int i=0;i<len;i++) {
				if(i==index) {
					continue;
				}
				else {
					newpoints[j++]=points[i];
				}
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		return newpoints;
		
	}
	/**
	 * <pre> points array not null
	 *	|	0 <= index && index < length of array
	 * @post Returns a new array whose elements are the elements of the given array with the element at the given index replaced by the given point.
	 * @param 
	 * @return
	 */
	public static IntPoint[] update(IntPoint[] points, int index, IntPoint value) {
		
		int len = points.length;
		if(value==null)
			throw new IllegalArgumentException();
		if(0 <= index && index < len)
		{
			points[index]=value;		
		}
		else {
			throw new IllegalArgumentException();
		}
		return points;
		
	}
	
}
