package drawit;

import java.awt.Color;
import java.util.Arrays;

/**
 * An instance of this class is a mutable abstraction storing a rounded polygon defined by a set of 2D points with integer coordinates
 * and a nonnegative corner radius.
 * 
 * @invar | getVertices() != null
 * @invar | Arrays.stream(getVertices()).allMatch(v -> v != null)
 * @invar | PointArrays.checkDefinesProperPolygon(getVertices()) == null
 * @invar | 0 <= getRadius()
 * @invar | getColor() != null
 */
public class RoundedPolygon {
	
	/**
	 * @representationObject
	 * @invar | vertices != null
	 * @invar | Arrays.stream(vertices).allMatch(v -> v != null)
	 * @invar | PointArrays.checkDefinesProperPolygon(vertices) == null
	 * @invar | 0 <= radius
	 */
	private IntPoint[] vertices = new IntPoint[0];
	private int radius;
	private Color color = Color.yellow;
	
	/**
	 * Returns a new array whose elements are the vertices of this rounded polygon.
	 * 
	 * @creates | result
	 */
	public IntPoint[] getVertices() {
		return PointArrays.copy(vertices);
	}
	
	/**
	 * Returns the radius of the corners of this rounded polygon.
	 */
	public int getRadius() { return radius; }
	
	public Color getColor() { return color; }

	/**
	 * @mutates | this
	 * @post | getVertices().length == 0
	 * @post | getRadius() == 0
	 * @post | getColor().equals(Color.yellow)
	 */
	public RoundedPolygon() {}
	
	/**
	 * Sets the vertices of this rounded polygon to be equal to the elements of the given array.
	 * 
	 * @inspects | newVertices
	 * @mutates | this
	 * @throws IllegalArgumentException | newVertices == null
	 * @throws IllegalArgumentException | Arrays.stream(newVertices).anyMatch(v -> v == null)
	 * @throws IllegalArgumentException if the given vertices do not define a proper polygon.
	 *     | PointArrays.checkDefinesProperPolygon(newVertices) != null
	 * @post | Arrays.equals(getVertices(), newVertices)
	 * @post | getRadius() == old(getRadius())
	 * @post | getColor().equals(old(getColor()))
	 */
	public void setVertices(IntPoint[] newVertices) {
		if (newVertices == null)
			throw new IllegalArgumentException("newVertices is null");
		if (Arrays.stream(newVertices).anyMatch(v -> v == null))
			throw new IllegalArgumentException("An element of newVertices is null");
		IntPoint[] copy = PointArrays.copy(newVertices);
		String msg = PointArrays.checkDefinesProperPolygon(copy);
		if (msg != null)
			throw new IllegalArgumentException(msg);
		vertices = copy;
	}
	
	/**
	 * Sets this rounded polygon's corner radius to the given value. 
	 * 
	 * @throws IllegalArgumentException if the given radius is negative.
	 *    | radius < 0
	 * @mutates | this
	 * @post | Arrays.equals(getVertices(), old(getVertices()))
	 * @post | getRadius() == radius
	 * @post | getColor().equals(old(getColor()))
	 */
	public void setRadius(int radius) {
		if (radius < 0)
			throw new IllegalArgumentException("The given radius is negative");
		this.radius = radius;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @throws IllegalArgumentException | !(0 <= index && index <= getVertices().length)
	 * @throws IllegalArgumentException | point == null
	 * @throws IllegalArgumentException | PointArrays.checkDefinesProperPolygon(PointArrays.insert(getVertices(), index, point)) != null
	 * @mutates | this
	 * @post | Arrays.equals(getVertices(), PointArrays.insert(old(getVertices()), index, point))
	 * @post | getRadius() == old(getRadius())
	 * @post | getColor().equals(old(getColor()))
	 */
	public void insert(int index, IntPoint point) {
		if (!(0 <= index && index <= getVertices().length))
			throw new IllegalArgumentException("index out of range");
		if (point == null)
			throw new IllegalArgumentException("point is null");
		setVertices(PointArrays.insert(vertices, index, point));
	}
	
	/**
	 * @throws IllegalArgumentException | !(0 <= index && index < getVertices().length)
	 * @throws IllegalArgumentException | PointArrays.checkDefinesProperPolygon(PointArrays.remove(getVertices(), index)) != null
	 * @mutates | this
	 * @post | Arrays.equals(getVertices(), PointArrays.remove(old(getVertices()), index))
	 * @post | getRadius() == old(getRadius())
	 * @post | getColor().equals(old(getColor()))
	 */
	public void remove(int index) {
		if (!(0 <= index && index < getVertices().length))
			throw new IllegalArgumentException("index out of range");
		setVertices(PointArrays.remove(vertices, index));
	}
	
	/**
	 * @throws IllegalArgumentException | !(0 <= index && index < getVertices().length)
	 * @throws IllegalArgumentExeption | point == null
	 * @throws IllegalArgumentException | PointArrays.checkDefinesProperPolygon(PointArrays.update(getVertices(), index, point)) != null
	 * @mutates | this
	 * @post | Arrays.equals(getVertices(), PointArrays.update(old(getVertices()), index, point))
	 * @post | getRadius() == old(getRadius())
	 * @post | getColor().equals(old(getColor()))
	 */
	public void update(int index, IntPoint point) {
		if (!(0 <= index && index < getVertices().length))
			throw new IllegalArgumentException("index out of range");
		if (point == null)
			throw new IllegalArgumentException("point is null");
		setVertices(PointArrays.update(vertices, index, point));
	}
	
	/**
	 * Returns {@code true} iff the given point is contained by the (non-rounded) polygon defined by this rounded polygon's vertices.
	 * This method does not take into account this rounded polygon's corner radius; it assumes a corner radius of zero.
	 * 
	 * <p>A point is contained by a polygon if it coincides with one of its vertices, or if it is on one of its edges, or if it is in the polygon's interior.
	 * 
	 * <p><b>Implementation hints:</b> A point is in the interior of a polygon if
	 * a line from that point to infinity in any direction (an "exit path" for the point) intersects with the polygon's perimeter an odd number of times.
	 * 
	 * <p>Use the line from the given point in the positive X direction as the exit path.
	 * 
	 * <p>First, find the first vertex that is not on the exit path.
	 * If you don't find such a vertex, return {@code false}.
	 * 
	 * <p>Now, starting with this vertex, V, find the next vertex, V', that is not on the exit path.
	 * If there are no vertices on the exit path between V and V', then check if the edge VV' intersects with the exit path:
	 * check that VV' straddles the carrier of the exit path and that (the signum of the cross product of VP and VV') times (the signum of the cross
	 * product of +X and VV') is negative.
	 * If there are vertices on the exit path between V and V', then count one intersection between the exit path and the polygon's perimeter iff
	 * VV' straddles the carrier of the exit path.
	 * 
	 * <p>Repeat this until you again reach the first vertex that is not on the exit path.
	 * 
	 * @pre | point != null
	 * @inspects | this
	 * @mutates nothing |
	 */
	public boolean contains(IntPoint point) {
		// We call the half-line extending from `point` to the right the "exit path"
		// Find first vertex that is not on the exit path
		int firstVertex;
		{
			int i = 0;
			for (;;) {
				if (i == vertices.length) // Zero or one vertices
					return false;
				if (vertices[i].equals(point))
					return true;
				if (!(vertices[i].getY() == point.getY() && vertices[i].getX() > point.getX()))
					break;
				i++;
			}
			firstVertex = i;
		}
		IntVector exitVector = new IntVector(1, 0);
		// Count how many times the exit path crosses the polygon
		int nbEdgeCrossings = 0;
		for (int index = firstVertex; ; ) {
			IntPoint a = vertices[index];
			// Find the next vertex that is not on the exit path
			boolean onExitPath = false;
			int nextIndex = index;
			IntPoint b;
			for (;;) {
				int nextNextIndex = (nextIndex + 1) % vertices.length;
				if (point.isOnLineSegment(vertices[nextIndex], vertices[nextNextIndex]))
					return true;
				nextIndex = nextNextIndex;
				b = vertices[nextIndex];
				if (b.equals(point))
					return true;
				if (b.getY() == point.getY() && b.getX() > point.getX()) {
					onExitPath = true;
					continue;
				}
				break;
			}
			if (onExitPath) {
				if ((b.getY() < point.getY()) != (a.getY() < point.getY()))
					nbEdgeCrossings++;
			} else {
				// Does `ab` straddle the exit path's carrier?
				if (Math.signum(a.getY() - point.getY()) * Math.signum(b.getY() - point.getY()) < 0) {
					// Does the exit path straddle `ab`'s carrier?
					IntVector ab = b.minus(a);
					if (Math.signum(point.minus(a).crossProduct(ab)) * Math.signum(exitVector.crossProduct(ab)) < 0)
						nbEdgeCrossings++;
				}
			}
			if (nextIndex == firstVertex)
				break;
			index = nextIndex;
		}
		return nbEdgeCrossings % 2 == 1;
	}
		
	/**
	 * Returns a textual representation of a set of drawing commands for drawing this rounded polygon.
	 * 
	 * <p>The returned text consists of a sequence of drawing operators and arguments, separated by spaces. The drawing operators are
	 * {@code line} and {@code arc}. Each argument is a decimal representation
	 * of a floating-point number.
	 * 
	 * <p>Operator {@code line} takes four arguments: X1 Y1 X2 Y2; it draws a line between (X1, Y1) and (X2, Y2).
	 * {@code arc} takes five: X Y R S E.
	 * It draws a part of a circle. The circle is defined by its center (X, Y) and its radius R. The part to draw is defined by the start angle A
	 * and angle extent E, both in radians. Positive X is angle zero; positive Y is angle {@code Math.PI / 2}; negative Y is angle {@code -Math.PI / 2}.
	 * 
	 * <p>For example, the following commands draw a rounded square with corner radius 10:
	 * <pre>
     * line 110 100 190 100 
	 * arc 190 110 10 -1.5707963267948966 1.5707963267948966 
	 * line 200 110 200 190 
	 * arc 190 190 10 0 1.5707963267948966 
	 * line 190 200 110 200 
	 * arc 110 190 10 1.5707963267948966 1.5707963267948966
	 * line 100 190 100 110
	 * arc 110 110 10 3.141592653589793 1.5707963267948966
	 * </pre>
	 * 
	 * <p>By rounding a corner, the adjacent edges are cut short by some amount.
	 * The corner radius to be used for a particular corner is the largest radius that is not greater than this rounded polygon's corner radius
	 * and that is such that no more than half of each adjacent edge is cut off by it.
	 * 
	 * <p><b>Implementation hints:</b>
	 * First, if this rounded polygon has less than three vertices, return an empty string.
	 * 
	 * <p>Then, draw each corner, including half of each adjacent edge. Let B be the vertex for which we are drawing the corner; let A be the preceding
	 * vertex and C the succeding one. Let BAC be the center of the line segment BA, and BCC be the center of the line segment BC.
	 * 
	 * <p>If BA and BC are collinear, just draw the lines BAC-B and B-BCC.
	 * 
	 * <p>Otherwise, let BAU be the unit vector from B to A, and BCU the unit vector from B to C.
	 * Then BAU + BCU points in the direction of the bisector. Let BSU be the unit vector in that direction.
	 * 
	 * <p>First, suppose the center of the corner would be at B + BSU. Compute how much is cut off from BA by projecting BSU onto BA:
	 * BAUcutoff = dot product of BAU and BSU
	 * (By symmetry, the same amount is cut off from BC.)
	 * The radius of the corner would then be unitRadius = absolute value of cross product of BSU and BAU.
	 * Now, determine the scale factor to apply: this is the minimum of the scale factor that would scale unitRadius to this.getRadius() and the scale
	 * factor that would scale BAUcutoff to half of the minimum of the size of BA and BC.
	 * From this, we can easily determine the actual center and actual radius of the corner.
	 * 
	 * <p>To determine the start angle, use the cutoff length to compute the point on BA where the arc starts,
	 * and turn the vector from the corner's center to that point into
	 * an angle. Similarly, compute the end angle. The angle extent is the difference between the two, after adding or subtracting 2PI as necessary
	 * to obtain a value between -PI and PI.
	 * 
	 * @inspects | this
	 * @mutates nothing |
	 * @post | result != null
	 */
	public String getDrawingCommands() {
		if (vertices.length < 3)
			return "";
		StringBuilder commands = new StringBuilder();
		for (int index = 0; index < vertices.length; index++) {
			IntPoint a = vertices[(index + vertices.length - 1) % vertices.length];
			IntPoint b = vertices[index];
			IntPoint c = vertices[(index + 1) % vertices.length];
			DoubleVector ba = a.minus(b).asDoubleVector();
			DoubleVector bc = c.minus(b).asDoubleVector();
			DoublePoint baCenter = b.asDoublePoint().plus(ba.scale(0.5));
			DoublePoint bcCenter = b.asDoublePoint().plus(bc.scale(0.5));
			double baSize = ba.getSize();
			double bcSize = bc.getSize();
			if (ba.crossProduct(bc) == 0) {
				commands.append("line " + bcCenter.getX() + " " + bcCenter.getY() + " " + b.getX() + " " + b.getY() + "\n");
				commands.append("line " + b.getX() + " " + b.getY() + " " + baCenter.getX() + " " + baCenter.getY() + "\n");
			} else {
				DoubleVector baUnit = ba.scale(1/baSize);
				DoubleVector bcUnit = bc.scale(1/bcSize);
				DoubleVector bisector = baUnit.plus(bcUnit);
				bisector = bisector.scale(1/bisector.getSize());
				double unitEdgeDistance = baUnit.dotProduct(bisector);
				double unitRadius = Math.abs(bisector.crossProduct(baUnit));
				double scaleFactor = Math.min(this.radius / unitRadius, Math.min(baSize, bcSize) / 2 / unitEdgeDistance);
				DoublePoint center = b.asDoublePoint().plus(bisector.scale(scaleFactor));
				double radius = unitRadius * scaleFactor;
				DoublePoint bcCornerStart = b.asDoublePoint().plus(bcUnit.scale(unitEdgeDistance * scaleFactor));
				DoublePoint baCornerStart = b.asDoublePoint().plus(baUnit.scale(unitEdgeDistance * scaleFactor));
				double baAngle = baCornerStart.minus(center).asAngle();
				double bcAngle = bcCornerStart.minus(center).asAngle();
				double angleExtent = bcAngle - baAngle;
				if (angleExtent < -Math.PI)
					angleExtent += 2 * Math.PI;
				else if (Math.PI < angleExtent)
					angleExtent -= 2 * Math.PI;
				commands.append("line " + baCenter.getX() + " " + baCenter.getY() + " " + baCornerStart.getX() + " " + baCornerStart.getY() + "\n");
				commands.append("arc " + center.getX() + " " + center.getY() + " " + radius + " " + baAngle + " " + angleExtent + "\n");
				commands.append("line " + bcCornerStart.getX() + " " + bcCornerStart.getY() + " " + bcCenter.getX() + " " + bcCenter.getY() + "\n");
			}
		}
		commands.append("fill " + color.getRed() + " " + color.getGreen() + " " + color.getBlue() + "\n");
		return commands.toString();
	}
	
}
