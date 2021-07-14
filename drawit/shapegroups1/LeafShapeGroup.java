package drawit.shapegroups1;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import drawit.IntPoint;
import drawit.RoundedPolygon;

/**
 * Each instance of this class represents a leaf shape group in a shape group graph.
 */
public class LeafShapeGroup extends ShapeGroup {

	/**
	 * @invar | shape != null 
	 */
	final RoundedPolygon shape;

	/**
	 * Returns the shape directly contained by this leaf shape group.
	 * 
	 * @immutable
	 * 
	 * @post | result != null
	 */
	public RoundedPolygon getShape() { return shape; }
	
	/**
	 * {@inheritDoc}
	 * 
	 * @post | Objects.equals(result, List.of(getShape()))
	 */
	public List<RoundedPolygon> getAllShapes() { return List.of(shape); }
	
	@Override
	public String getDrawingCommands() {
		return shape.getDrawingCommands();
	}

	/**
	 * Returns the smallest extent that contains all of the shapes contained directly or indirectly by this shape group.
	 * 
	 * @inspects | this, ...getAllShapes()
	 * @post | result != null
	 * @post | result.getLeft() == getAllShapes().stream().flatMap(s -> Arrays.stream(s.getVertices())).mapToInt(p -> p.getX()).min().getAsInt()
	 * @post | result.getTop() == getAllShapes().stream().flatMap(s -> Arrays.stream(s.getVertices())).mapToInt(p -> p.getY()).min().getAsInt()
	 * @post | result.getRight() == getAllShapes().stream().flatMap(s -> Arrays.stream(s.getVertices())).mapToInt(p -> p.getX()).max().getAsInt()
	 * @post | result.getBottom() == getAllShapes().stream().flatMap(s -> Arrays.stream(s.getVertices())).mapToInt(p -> p.getY()).max().getAsInt()
	 */
	@Override
	public Extent getBoundingBox() {
		IntPoint[] vertices = shape.getVertices();
		if (vertices.length == 0)
			throw new IllegalStateException("no vertices");
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (IntPoint p : vertices) {
			minX = Math.min(minX, p.getX());
			maxX = Math.max(maxX, p.getX());
			minY = Math.min(minY, p.getY());
			maxY = Math.max(maxY, p.getY());
		}
		return Extent.ofLeftTopRightBottom(minX, minY, maxX, maxY);
	}

	/**
	 * Initializes this object to represent a leaf shape group that directly contains the given shape.
	 * 
	 * @throws IllegalArgumentException if {@code shape} is null
	 *    | shape == null
	 * @throws IllegalArgumentException if {@code shape} has less than three vertices
	 *    | shape.getVertices().length < 3
	 * @mutates | this
	 * @post | getShape() == shape
	 * @post | getParentGroup() == null
	 */
	public LeafShapeGroup(RoundedPolygon shape) {
		if (shape == null)
			throw new IllegalArgumentException("shape is null");
		if (shape.getVertices().length < 3)
			throw new IllegalArgumentException("shape has less than three vertices");
		
		this.shape = shape;
	}
	
}
