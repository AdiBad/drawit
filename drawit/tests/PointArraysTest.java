package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.PointArrays;

class PointArraysTest {

	static IntPoint pt(int x, int y) { return new IntPoint(x, y); }
	
	@Test
	void testCheckDefinesProperPolygon_proper() {
		IntPoint[] triangle = {pt(100, 100), pt(200, 200), pt(100, 150)};
		assert PointArrays.checkDefinesProperPolygon(triangle) == null;
		
		IntPoint[] square = {pt(10, 10), pt(100, 10), pt(100, 100), pt(10, 100)};
		assert PointArrays.checkDefinesProperPolygon(square) == null;
		
		IntPoint[] squareExtraVertex = {pt(10, 10), pt(100, 10), pt(100, 50), pt(100, 100), pt(10, 100)};
		assert PointArrays.checkDefinesProperPolygon(squareExtraVertex) == null;
		
		IntPoint[] squareRotated = {pt(200, 100), pt(100, 200), pt(200, 300), pt(300, 200)};
		assert PointArrays.checkDefinesProperPolygon(squareRotated) == null;
		
		IntPoint[] pentagon = {pt(200, 100), pt(100, 200), pt(150, 300), pt(250, 300), pt(300, 200)};
		assert PointArrays.checkDefinesProperPolygon(pentagon) == null;
		
		IntPoint[] hexagon = {pt(200, 100), pt(100, 200), pt(100, 300), pt(200, 400), pt(300, 300), pt(300, 200)};
		assert PointArrays.checkDefinesProperPolygon(hexagon) == null;
	}
	
	@Test
	void testCheckDefinesProperPolygon_coincidingVertices() {
		IntPoint[] twistedSquare_extraVertices = {pt(100, 100), pt(200, 100), pt(150, 150), pt(100, 200), pt(200, 200), pt(150, 150)};
		assert PointArrays.checkDefinesProperPolygon(twistedSquare_extraVertices) != null;
	}
	
	@Test
	void testCheckDefinesProperPolygon_vertexOnEdge() {
		IntPoint[] flatTriangle = {pt(100, 100), pt(200, 100), pt(300, 100)};
		assert PointArrays.checkDefinesProperPolygon(flatTriangle) != null;
		
		IntPoint[] twistedSquare_extraVertex = {pt(100, 100), pt(200, 100), pt(150, 150), pt(100, 200), pt(200, 200)};
		assert PointArrays.checkDefinesProperPolygon(twistedSquare_extraVertex) != null;
	}
	
	@Test
	void testCheckDefinesProperPolygon_intersectingEdges() {
		IntPoint[] twistedSquare = {pt(100, 100), pt(200, 100), pt(100, 200), pt(200, 200)};
		assert PointArrays.checkDefinesProperPolygon(twistedSquare) != null;
		
		IntPoint[] twistedHexagon = {pt(100, 200), pt(200, 100), pt(100, 300), pt(200, 400), pt(300, 300), pt(300, 200)};
		assert PointArrays.checkDefinesProperPolygon(twistedHexagon) != null;
	}
	
	static boolean samePoints(IntPoint[] expected, IntPoint[] actual) {
		return actual != null && actual.length == expected.length &&
			IntStream.range(0, expected.length).allMatch(i -> expected[i].equals(actual[i]));
	}
	
	@Test
	void testCopy() {
		IntPoint[] square = {pt(10, 10), pt(100, 10), pt(100, 100), pt(10, 100)};
		IntPoint[] squareCopy = Arrays.copyOf(square, square.length);
		IntPoint[] copy = PointArrays.copy(square);
		assert Arrays.equals(squareCopy, square); // Method did not modify the given array
		assert samePoints(square, copy);
	}
	
	@Test
	void testInsert() {
		IntPoint[] hexagon = {pt(200, 100), pt(100, 200), pt(100, 300), pt(200, 400), pt(300, 300), pt(300, 200)};
		IntPoint[] result = PointArrays.insert(hexagon, 1, pt(150, 150));
		IntPoint[] result_expected = {pt(200, 100), pt(150, 150), pt(100, 200), pt(100, 300), pt(200, 400), pt(300, 300), pt(300, 200)};
		assert samePoints(result_expected, result);
	}
	
	@Test
	void testRemove() {
		IntPoint[] pentagon = {pt(200, 100), pt(100, 200), pt(150, 300), pt(250, 300), pt(300, 200)};
		IntPoint[] result = PointArrays.remove(pentagon, 2);
		IntPoint[] result_expected = {pt(200, 100), pt(100, 200), pt(250, 300), pt(300, 200)};  
		assert samePoints(result_expected, result);
	}

	@Test
	void testUpdate() {
		IntPoint[] squareRotated = {pt(200, 100), pt(100, 200), pt(200, 300), pt(300, 200)};
		IntPoint[] result = PointArrays.update(squareRotated, 3, pt(400, 200));
		IntPoint[] result_expected = {pt(200, 100), pt(100, 200), pt(200, 300), pt(400, 200)};
		assert samePoints(result_expected, result);
	}
	
	@Test
	void testTranslate() {
		IntPoint[] points = {pt(123, 456), pt(789, 12), pt(345, 678)};
		IntPoint[] result = PointArrays.translate(points, new IntVector(1000, 2000));
		IntPoint[] result_expected = {pt(1123, 2456), pt(1789, 2012), pt(1345, 2678)};
		assert samePoints(result_expected, result);
	}
	
	@Test
	void testScale() {
		IntPoint[] points = {pt(1123, 2456), pt(1789, 2012), pt(1345, 2678)};
		IntPoint[] result = PointArrays.scale(points, pt(1000, 2000), 10000, 100000);
		IntPoint[] result_expected = {pt(1231000, 45602000), pt(7891000, 1202000), pt(3451000, 67802000)};
		assert samePoints(result_expected, result);
		IntPoint[] result2 = PointArrays.scale(result, pt(1000, 2000), 1.0/10000, 1.0/100000);
		IntPoint[] result_expected2 = {pt(1123, 2456), pt(1789, 2012), pt(1345, 2678)};
		assert samePoints(result_expected2, result2);
	}
}
