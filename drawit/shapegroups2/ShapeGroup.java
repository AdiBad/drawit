package drawit.shapegroups2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.PointArrays;
import drawit.RoundedPolygon;

import logicalcollections.LogicalList;
import logicalcollections.LogicalSet;

/**
 * Each instance of this class represents a shape group. A shape group is either a leaf group,
 * in which case it directly contains a single shape, or it is a non-leaf group, in which case it directly contains
 * two or more subgroups, which are themselves shape groups.
 * 
 * @invar | getParentGroup() == null ||
 *        | getParentGroup().getSubgroups() != null && getParentGroup().getSubgroups().contains(this)
 * @invar | !getAncestors().contains(this)
 */
public abstract class ShapeGroup {
	
	/**
	 * @invar | (parent == null) == (nextSibling == null)
	 * @invar A non-leaf shape group has at least two subgroups
	 *    | nextSibling != this
	 * @invar | (parent == null) == (previousSibling == null)
	 * @invar | parent == null || nextSibling.parent == parent && nextSibling.previousSibling == this
	 * @invar | parent == null || previousSibling.parent == parent && previousSibling.nextSibling == this
	 * @invar | parent == null || parent.getSubgroupsPrivate().contains(this)
	 * @invar | !getAncestorsPrivate().contains(this)
	 */
	/** @peerObject */
	NonleafShapeGroup parent;
	/** @peerObject */
	ShapeGroup previousSibling;
	/** @peerObject */
	ShapeGroup nextSibling;
	
	/**
	 * Returns the set of the ancestors of this shape group.
	 * 
	 * @post | result != null
	 * @post | result.equals(LogicalSet.<ShapeGroup>matching(ancestors ->
	 *       |     getParentGroup() == null || ancestors.contains(getParentGroup()) &&
	 *       |     ancestors.allMatch(ancestor ->
	 *       |         ancestor.getParentGroup() == null || ancestors.contains(ancestor.getParentGroup()))))
	 */
	public Set<ShapeGroup> getAncestors() {
		return getAncestorsPrivate();
	}
	
	Set<ShapeGroup> getAncestorsPrivate() {
		return LogicalSet.<ShapeGroup>matching(ancestors ->
			parent == null || ancestors.contains(parent) &&
			ancestors.allMatch(ancestor -> ancestor.parent == null || ancestors.contains(ancestor.parent))
		);
	}
	
	/**
	 * Returns the shape group that directly contains this shape group, or {@code null}
	 * if no shape group directly contains this shape group.
	 * 
	 * @basic
	 * @peerObject
	 */
	public NonleafShapeGroup getParentGroup() { return parent; }
	
	/**
	 * Returns the list of the RoundedPolygon objects contained directly or indirectly by this shape group,
	 * in depth-first order.
	 * 
	 * @post The result is not null.
	 *    | result != null
	 * @post The elements of the result are not null.
     *       (More details are given in the subclasses.)
	 *    | result.stream().allMatch(s -> s != null)
	 */
	public abstract List<RoundedPolygon> getAllShapes();
	
	/**
	 * Returns a map that maps each RoundedPolygon object contained directly or
	 * indirectly by this shape group to its current list of vertices.
	 * 
	 * @inspects | this, ...getAllShapes()
	 * @post | result != null
	 * @post | result.keySet().equals(Set.copyOf(getAllShapes()))
	 * @post | getAllShapes().stream().allMatch(s -> Arrays.equals(result.get(s), s.getVertices()))
	 */
	public Map<RoundedPolygon, IntPoint[]> getAllVertices() {
		return getAllShapes().stream().collect(Collectors.toMap(s -> s, s -> s.getVertices()));
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
	public abstract Extent getBoundingBox();
	
	/**
	 * Returns a textual representation of a sequence of drawing commands for drawing
	 * the shapes contained directly or indirectly by this shape group.
	 * 
	 * For the syntax of the drawing commands, see {@code RoundedPolygon.getDrawingCommands()}.
	 * 
	 * @inspects | this, ...getAllShapes()
	 * @post | result != null
	 */
	public abstract String getDrawingCommands();
	
	void remove() {
		nextSibling.previousSibling = previousSibling;
		previousSibling.nextSibling = nextSibling;
		if (this == parent.firstChild)
			parent.firstChild = nextSibling;
	}
	
	void insertBeforeFirstSibling() {
		nextSibling = parent.firstChild;
		previousSibling = nextSibling.previousSibling;
		nextSibling.previousSibling = this;
		previousSibling.nextSibling = this;
	}
	
	/**
	 * Moves this shape group to the front of its parent's list of subgroups.
	 * 
	 * @throws UnsupportedOperationException if this shape group has no parent
	 *    | getParentGroup() == null
	 * @mutates_properties | getParentGroup().getSubgroups()
	 * @post | getParentGroup().getSubgroups().equals(
	 *       |     LogicalList.plusAt(LogicalList.minus(old(getParentGroup().getSubgroups()), this), 0, this))
	 */
	public void bringToFront() {
		if (parent == null)
			throw new UnsupportedOperationException("no parent");
		
		remove();
		insertBeforeFirstSibling();
		parent.firstChild = this;
	}
	
	/**
	 * Moves this shape group to the back of its parent's list of subgroups.
	 * 
	 * @throws UnsupportedOperationException if this shape group has no parent
	 *    | getParentGroup() == null
	 * @mutates_properties | getParentGroup().getSubgroups()
	 * @post | getParentGroup().getSubgroups().equals(
	 *       |     LogicalList.plus(LogicalList.minus(old(getParentGroup().getSubgroups()), this), this))
	 */
	public void sendToBack() {
		if (parent == null)
			throw new UnsupportedOperationException("no parent");
		
		remove();
		insertBeforeFirstSibling();
	}
	
	/**
	 * Translate (= displace) the shapes contained directly or indirectly by this shape group along
	 * the given vector.
	 * 
	 * @throws IllegalArgumentException if {@code delta} is null
	 *    | delta == null
	 * @inspects | this
	 * @mutates_properties | (...getAllShapes()).getVertices()
	 * @post
	 *    | getAllShapes().stream().allMatch(s ->
	 *    |     Arrays.equals(s.getVertices(), PointArrays.translate(old(getAllVertices()).get(s), delta))) 
	 */
	public void translate(IntVector delta) {
		for (RoundedPolygon shape : getAllShapes())
			shape.setVertices(PointArrays.translate(shape.getVertices(), delta));
	}
	
	/**
	 * Scale the shapes contained directly or indirectly by this shape group.
	 * 
	 * @throws IllegalArgumentException if {@code origin} is null
	 *    | origin == null
	 * @inspects | this
	 * @mutates_properties | (...getAllShapes()).getVertices()
	 * @post
	 *    | getAllShapes().stream().allMatch(s ->
	 *    |     Arrays.equals(s.getVertices(), PointArrays.scale(old(getAllVertices()).get(s), origin, xFactor, yFactor)))
	 */
	public void scale(IntPoint origin, double xFactor, double yFactor) {
		for (RoundedPolygon shape : getAllShapes())
			shape.setVertices(PointArrays.scale(shape.getVertices(), origin, xFactor, yFactor));
	}
}
