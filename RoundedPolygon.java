package drawit;

public class RoundedPolygon extends Object{
	private int radius;
	public IntPoint[] vertices;
	public RoundedPolygon() {
		
	}
	/**
	 * @post Returns a new array whose elements are the vertices of this rounded polygon.
	 *  | result = this.vertices
	 * @param 
	 * @return
	 */
	public IntPoint[] getVertices() {
		
		return this.vertices;
		
	}
	/**
	 * @post Returns the radius of the corners of this rounded polygon.
	 *  | result = this.radius
	 * @param 
	 * @return
	 */
	public int getRadius() {
		return this.radius;
		
	}
	/**
	 * <pre> points array not null
	 * @post Sets the vertices of this rounded polygon to be equal to the elements of the given array.
	 * @param 
	 * @return
	 */
	public void setVertices(IntPoint[] newVertices) throws IllegalArgumentException {
		if(PointArrays.checkDefinesProperPolygon(newVertices) != null) {
			throw new IllegalArgumentException();
		}
		vertices = newVertices;
	}
	/**
	 * @post Sets this rounded polygon's corner radius to the given value.
	 * @param 
	 * @return
	 */
	public void setRadius(int radius) throws IllegalArgumentException{	
		if(radius < 0) {
			throw new IllegalArgumentException("Radius cannot be less than 0");
		}
		this.radius = radius;
	}
	/**
	 * <pre> Returns a new array whose elements are the elements of the given array with the given point inserted at the given index.
	 * | 0 <= index && index <= points.length
	 * @param points, index, point
	 * @return
	 */
	public void insert(int index, IntPoint point) throws IllegalArgumentException {
		int len = vertices.length;
		IntPoint newpoints[] = new IntPoint[len+1];
		
		if(point==null) {
			throw new IllegalArgumentException();
		}
		if(0 <= index && index <= len)
		{
			for(int i=0;i<len+1;i++)
			{
				if(i<index) {
					newpoints[i] = vertices[i];
				}
				else if(index==0 || i==index) {
					newpoints[i] = point;
				}
				else {
					newpoints[i] = vertices[i - 1];
				}
			}
			vertices = newpoints;
				
		} else
			throw new IllegalArgumentException();
	}
	/**
	 * <pre> points array not null
	 * | 0 <= index && index < length of array
	 * @post Returns a new array whose elements are the elements of the given array with the element at the given index removed.
	 * @param 
	 * @return
	 */
	public void remove (int index) throws IllegalArgumentException {
		int len = vertices.length;
		IntPoint newpoints[] = new IntPoint[len-1];
		if(0 <= index && index < len)
		{
			int j=0;
			for(int i=0;i<len;i++) {
				if(i==index) {
					continue;
				}
				else {
					newpoints[j++]=vertices[i];
				}
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		vertices = newpoints;
	}
	/**
	 * <pre> points array not null
	 * | 0 <= index && index < length of array
	 * @post Returns a new array whose elements are the elements of the given array with the element at the given index replaced by the given point.
	 * @return
	 */
	public void update(int index, IntPoint point) throws IllegalArgumentException  {
		int len = vertices.length;
		
		if(point==null) {
			throw new IllegalArgumentException();
		}
		if(0 <= index && index < len)
		{
			vertices[index]=point;
		}
		
	}
	/**
	 * <pre> point not null
	 * @post Returns true iff the given point is contained by the (non-rounded) polygon defined by this rounded polygon's vertices.
	 * @param 
	 * @return
	 */
	
	public boolean contains(IntPoint point) throws IllegalArgumentException {
		int maxX = getVertices()[0].getX();
		boolean intersect = false;
		if(point!=null) {
			for(int i = 0; i < this.getVertices().length;i++) {
				if (this.getVertices()[i].equals(point)) { // checking if point coincides with vertex
					return true;
				}
				else if (point.isOnLineSegment(getVertices()[i], getVertices()[(i+1)%(this.getVertices().length)] )) { // checking if on line segment
					return true;
				} else if(getVertices()[i].getX() > maxX) {
					maxX = getVertices()[i].getX();
				}
				else {
					continue;
				}
			}
			IntPoint X = new IntPoint((maxX+1),point.getY());
			for(int i = 0; i < this.getVertices().length; i++) {
				if(getVertices()[i].isOnLineSegment(point, X)) {
					intersect = !intersect;
				}
				else if(IntPoint.lineSegmentsIntersect(getVertices()[i], getVertices()[(i+1)%getVertices().length], point, X)) {
					intersect = !intersect;
				}
			}
			return intersect;
		} else {
	    	throw new IllegalArgumentException();
		}
	}

	/**
	 * @post Returns a textual representation of a set of drawing commands for drawing this rounded polygon.
	 * @param 
	 * @return
	 */
	public String getDrawingCommands() {
		String getDrawingCommands = "";
		if(this.getVertices().length < 3) {
			return getDrawingCommands;
		} else {
			for(int i = 0; i < getVertices().length ;i++) {
				IntPoint A = getVertices()[i];
				IntPoint B = getVertices()[(i+1) % (getVertices().length)];
				IntPoint C = getVertices()[(i+2) % (getVertices().length)];
				IntVector BA = A.minus(B);
				IntVector BC = C.minus(B);
				DoubleVector BAU = BA.asDoubleVector().scale(1/(BA.asDoubleVector().getSize())); // unit vector of BA
				DoubleVector BCU = BC.asDoubleVector().scale(1/(BC.asDoubleVector().getSize())); // unit vector of BC
				DoubleVector BSU = BAU.plus(BCU); //bisector vector
				BSU = BSU.scale(1/BSU.getSize()); //unit bisector vector
				DoublePoint BAC = B.asDoublePoint().plus(BA.asDoubleVector().scale(0.5)); //midpoint of BA
				DoublePoint BCC = B.asDoublePoint().plus(BC.asDoubleVector().scale(0.5)); //midpoint of BC
				if(BA.isCollinearWith​(BC)) { // if the three vertices in a row are collinear
					getDrawingCommands = getDrawingCommands + "line " + String.valueOf(BAC.getX()) + " " + String.valueOf(BAC.getY()) + " " + String.valueOf(BCC.getX()) + " " + String.valueOf(BCC.getY()) + "\n";
				} else {
					double BAUcutoff = BAU.dotProduct(BSU); //temporary cutoff
					double unitRadius = Math.abs(BSU.crossProduct(BAU)); //radius of unit vector circle
					double minBABC = Math.min(BA.asDoubleVector().scale(0.5).getSize(), BC.asDoubleVector().scale(0.5).getSize()); //the smallest of the two half sides
					double scaleFactor = Math.min(this.getRadius()/unitRadius, minBABC/BAUcutoff); //scales according either to the smallest of the two half sides (highest bound possible) or given radius
					double radius = unitRadius * scaleFactor; // actual radius
					DoublePoint centre = B.asDoublePoint().plus(BSU.scale(scaleFactor)); //new actual centre
					double totalCutoff = BAUcutoff * scaleFactor; // total cutoff length
					DoublePoint BAarcStartingPoint = B.asDoublePoint().plus(BAU.scale(totalCutoff)); // ??? I think this is the starting point at least
					double startAngle = BAarcStartingPoint.minus(centre).asAngle();
					DoublePoint BCarcEndingPoint = B.asDoublePoint().plus(BCU.scale(totalCutoff));
					double endAngle = BCarcEndingPoint.minus(centre).asAngle();
					double angleExtent = endAngle - startAngle;
					if (angleExtent <= -(Math.PI)) {
						angleExtent += 2 * Math.PI;
					} else if (angleExtent > Math.PI) {
						angleExtent -= 2* Math.PI;
					}
					getDrawingCommands = getDrawingCommands + "line " + String.valueOf(BAC.getX()) + " " + String.valueOf(BAC.getY()) + " " + String.valueOf(BAarcStartingPoint.getX()) + " " + String.valueOf(BAarcStartingPoint.getY()) + "\n" + "arc " + String.valueOf(centre.getX()) + " " + String.valueOf(centre.getY()) + " " + String.valueOf(radius) + " " + String.valueOf(startAngle) + " " + String.valueOf(angleExtent) + "\n" + "line " + String.valueOf(BCarcEndingPoint.getX()) + " " + String.valueOf(BCarcEndingPoint.getY()) + " " + String.valueOf(BCC.getX()) + " " + String.valueOf(BCC.getY()) + "\n";
				}
			}
		}
		return getDrawingCommands;
	}
}
