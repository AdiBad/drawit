package drawit.shapegroups1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import drawit.IntPoint;
import drawit.RoundedPolygon;
import logicalcollections.LogicalList;

/**
 * Each instance of this class represents a non-leaf shape group in a shape group graph.
 * 
 * @invar | getSubgroups() != null
 * @invar | LogicalList.distinct(getSubgroups())
 * @invar | getSubgroups().stream().allMatch(g -> g != null && g.getParentGroup() == this)
 */
public class NonleafShapeGroup extends ShapeGroup {

	/**
	 * @invar | LogicalList.distinct(subgroups)
	 * @invar | subgroups.stream().allMatch(g -> g != null && g.parent == this)
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	ArrayList<ShapeGroup> subgroups;

	/**
	 * Returns the list of subgroups of this shape group.
	 * 
	 * @basic
	 * @peerObjects
	 */
	public List<ShapeGroup> getSubgroups() { return List.copyOf(subgroups); }
	
	/**
	 * Returns the number of subgroups of this non-leaf shape group.
	 * 
	 * @post | result == getSubgroups().size()
	 */
	public int getSubgroupCount() {
		return subgroups.size();
	}

	/**
	 * Returns the subgroup at the given (zero-based) index in this non-leaf shape group's list of subgroups.
	 * 
	 * @throws IllegalArgumentException if the given index is out of bounds
	 *    | index < 0 || getSubgroups().size() <= index
	 * @post | result == getSubgroups().get(index)
	 */
	public ShapeGroup getSubgroup(int index) {
		if (index < 0 || getSubgroups().size() <= index)
			throw new IllegalArgumentException("index out of bounds");
		return subgroups.get(index);
	}
	
	@Override
	public String getDrawingCommands() {
		StringBuilder builder = new StringBuilder();
		for (int i = subgroups.size() - 1; 0 <= i; i--)
			builder.append(subgroups.get(i).getDrawingCommands());
		return builder.toString();
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
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (ShapeGroup subgroup : subgroups) {
			Extent boundingBox = subgroup.getBoundingBox();
			minX = Math.min(minX, boundingBox.getLeft());
			maxX = Math.max(maxX, boundingBox.getRight());
			minY = Math.min(minY, boundingBox.getTop());
			maxY = Math.max(maxY, boundingBox.getBottom());
		}
		return Extent.ofLeftTopRightBottom(minX, minY, maxX, maxY);
	}

	/**
	 * Return the first subgroup in this non-leaf shape group's list of subgroups whose
	 * bounding box contains the given point.
	 * 
	 * @throws IllegalArgumentException if {@code point} is null
	 *    | point == null
	 * @post
	 *    | Objects.equals(result,
	 *    |     getSubgroups().stream().filter(g -> g.getBoundingBox().contains(point))
	 *    |         .findFirst().orElse(null))
	 */
	public ShapeGroup getSubgroupAt(IntPoint point) {
		if (point == null)
			throw new IllegalArgumentException("point is null");
		
		for (ShapeGroup group : subgroups)
			if (group.getBoundingBox().contains(point))
				return group;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @post | Objects.equals(result, getSubgroups().stream().flatMap(g -> g.getAllShapes().stream()).collect(Collectors.toList()))
	 */
	public List<RoundedPolygon> getAllShapes() {
		return subgroups.stream().flatMap(subgroup -> subgroup.getAllShapes().stream()).collect(Collectors.toList());
	}
	
	/**
	 * Initializes this object to represent a non-leaf shape group that directly contains the given
	 * subgroups, in the given order.
	 * 
	 * @mutates | this
	 * @mutates_properties | (...subgroups).getParentGroup()
	 * @inspects | subgroups
	 * 
	 * @throws IllegalArgumentException if {@code subgroups} is null
	 *    | subgroups == null
	 * @throws IllegalArgumentException if {@code subgroups} has less than two elements
	 *    | subgroups.length < 2
	 * @throws IllegalArgumentException if any element of {@code subgroups} is null
	 *    | Arrays.stream(subgroups).anyMatch(g -> g == null)
	 * @throws IllegalArgumentException if the given subgroups are not distinct
	 *    | !LogicalList.distinct(List.of(subgroups))
	 * @throws IllegalArgumentException if any of the given subgroups already has a parent
	 *    | Arrays.stream(subgroups).anyMatch(g -> g.getParentGroup() != null)
	 * 
	 * @post | Objects.equals(getSubgroups(), List.of(subgroups))
	 * @post | Arrays.stream(subgroups).allMatch(g -> g.getParentGroup() == this)
	 * @post | getParentGroup() == null
	 */
	public NonleafShapeGroup(ShapeGroup[] subgroups) {
		if (subgroups == null)
			throw new IllegalArgumentException("subgroups is null");
		if (subgroups.length < 2)
			throw new IllegalArgumentException("subgroups has less than two elements");
		if (Arrays.stream(subgroups).anyMatch(g -> g == null))
			throw new IllegalArgumentException("subgroups has null elements");
		if (!LogicalList.distinct(List.of(subgroups)))
			throw new IllegalArgumentException("subgroups has duplicate elements");
		if (Arrays.stream(subgroups).anyMatch(g -> g.getParentGroup() != null))
			throw new IllegalArgumentException("some of the given groups already have a parent");
		
		this.subgroups = new ArrayList<>(Arrays.asList(subgroups));
		for (ShapeGroup group : subgroups) {
			assert group.getParentGroup() == null;
			group.parent = this;
		}
	}
	
}
