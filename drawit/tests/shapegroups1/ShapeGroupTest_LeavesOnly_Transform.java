package drawit.tests.shapegroups1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.RoundedPolygon;
import drawit.shapegroups1.Extent;
import drawit.shapegroups1.LeafShapeGroup;
import drawit.shapegroups1.ShapeGroup;

class ShapeGroupTest_LeavesOnly_Transform {
	
	static IntPoint p(int x, int y) { return new IntPoint(x, y); }
	
	static IntPoint[] scale(int sx, int sy, IntPoint[] ps) {
		return Arrays.stream(ps).map(p -> new IntPoint(p.getX() * sx, p.getY() * sy)).toArray(n -> new IntPoint[n]);
	}
	
	static IntPoint[] scale(IntPoint[] points, IntPoint origin, double xFactor, double yFactor) {
		return Arrays.stream(points).map(p -> origin.plus(p.minus(origin).scale(xFactor, yFactor))).toArray(n -> new IntPoint[n]);
	}

	static IntPoint[] translate(int dx, int dy, IntPoint[] ps) {
		IntVector v = new IntVector(dx, dy);
		return Arrays.stream(ps).map(p -> p.plus(v)).toArray(n -> new IntPoint[n]);
	}
	
	static boolean samePoints(IntPoint[] expected, IntPoint[] actual) {
		return actual != null && actual.length == expected.length &&
			IntStream.range(0, expected.length).allMatch(i -> expected[i].equals(actual[i]));
	}

	public static IntPoint[] copy(IntPoint[] points) {
		IntPoint[] result = new IntPoint[points.length];
		for (int i = 0; i < points.length; i++)
			result[i] = points[i];
		return result;
	}

	IntPoint[] triangleRight = {p(-1, -1), p(-1, 1), p(1, 0)};
	IntPoint[] diamond = {p(-1, 0), p(0, -1), p(1, 0), p(0, 1)};
	IntPoint[] pentagon = {p(-1, -1), p(-2, 0), p(0, 1), p(2, 0), p(1, -1)};
	
	IntPoint[] vertices1 = translate(400, 250, scale(10, 10, triangleRight));
	RoundedPolygon poly1 = new RoundedPolygon();
	{
		poly1.setVertices(copy(vertices1));
	}
	LeafShapeGroup leaf1 = new LeafShapeGroup(poly1);
	{
		//assertEquals(390, 240, 20, 20, leaf1.getOriginalExtent());
		leaf1.scale(new IntPoint(390, 240), 3.0/2, 1.0/2);
		leaf1.translate(new IntVector(10, 15));
		// scale by 3/2, 1/2
		// translate by 10, 15
	}
	
	IntPoint[] vertices2 = translate(400, 250, scale(5, 20, diamond));
	RoundedPolygon poly2 = new RoundedPolygon();
	{
		poly2.setVertices(vertices2);
	}
	LeafShapeGroup leaf2 = new LeafShapeGroup(poly2);
	{
		//assertEquals(395, 230, 10, 40, leaf2.getOriginalExtent());
		leaf2.scale(new IntPoint(395, 230), 2, 1);
		leaf2.translate(new IntVector(-5, 5));
		// scale by 2, 1
		// translate by -5, 5
	}
	
	IntPoint[] vertices3 = translate(400, 250, scale(10, 5, pentagon));
	RoundedPolygon poly3 = new RoundedPolygon();
	{
		poly3.setVertices(vertices3);
	}
	LeafShapeGroup leaf3 = new LeafShapeGroup(poly3);
	{
		//assertEquals(380, 245, 40, 10, leaf3.getOriginalExtent());
		leaf3.scale(new IntPoint(380, 245), 2.0/5, 2);
		// scale by 2/5, 2
		// translate by 0, 0
	}
	
	static void assertEquals(int left, int top, int width, int height, Extent actual) {
		assert actual.getLeft() == left;
		assert actual.getTop() == top;
		assert actual.getWidth() == width;
		assert actual.getHeight() == height;
	}
	
	static void assertEquals(int x, int y, IntPoint p) {
		assert p.getX() == x;
		assert p.getY() == y;
	}
	
	static void assertEquals(int x, int y, IntVector v) {
		assert v.getX() == x;
		assert v.getY() == y;
	}
	
	@Test
	void testGetShape() {
		assert leaf1.getShape() == poly1;
		assert leaf2.getShape() == poly2;
		assert leaf3.getShape() == poly3;
	}
	
	@Test
	void testGetVertices() {
		IntPoint[] newVertices1 = poly1.getVertices();
		IntPoint[] expected = translate(10, 15, scale(vertices1, new IntPoint(390, 240), 3.0/2, 1.0/2));
		assert samePoints(expected, newVertices1);
	}
	
	@Test
	void testGetAllShapes() {
		assert List.of(poly1).equals(leaf1.getAllShapes());
	}
	
	@Test
	void testGetAllVertices() {
		Map<RoundedPolygon, IntPoint[]> allVertices = leaf1.getAllVertices();
		assert Set.of(poly1).equals(allVertices.keySet());
		assert samePoints(poly1.getVertices(), allVertices.get(poly1));
	}
	
	@Test
	void testGetBoundingBox() {
		assertEquals(400, 255, 30, 10, leaf1.getBoundingBox());
		assertEquals(390, 235, 20, 40, leaf2.getBoundingBox());
		assertEquals(380, 245, 16, 20, leaf3.getBoundingBox());
	}
	
	@Test
	void testGetParentGroup() {
		assert leaf1.getParentGroup() == null;
		assert leaf2.getParentGroup() == null;
		assert leaf3.getParentGroup() == null;
	}
}
