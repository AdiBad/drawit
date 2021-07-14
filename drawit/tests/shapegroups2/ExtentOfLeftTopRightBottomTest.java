package drawit.tests.shapegroups2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.shapegroups2.Extent;

class ExtentOfLeftTopRightBottomTest {
	
	Extent extent = Extent.ofLeftTopRightBottom(10, 33, 47, 127);
	
	@Test
	void testGetLeft() {
		assert extent.getLeft() == 10;
	}
	
	@Test
	void testGetTop() {
		assert extent.getTop() == 33;
	}
	
	@Test
	void testGetRight() {
		assert extent.getRight() == 47;
	}
	
	@Test
	void testGetBottom() {
		assert extent.getBottom() == 127;
	}
	
	@Test
	void testGetWidth() {
		assert extent.getWidth() == 37;
	}
	
	@Test
	void testGetHeight() {
		assert extent.getHeight() == 94;
	}
	
	@Test
	void testGetTopLeft() {
		assert extent.getTopLeft().equals(new IntPoint(10, 33));
	}
	
	@Test
	void testGetBottomRight() {
		assert extent.getBottomRight().equals(new IntPoint(47, 127));
	}
	
	@Test
	void testContains() {
		assert extent.contains(new IntPoint(20, 60));
		assert !extent.contains(new IntPoint(60, 20));
	}

	@Test
	void testWithLeft() {
		Extent e = extent.withLeft(6);
		assert e.getLeft() == 6;
		assert e.getTop() == 33;
		assert e.getRight() == 47;
		assert e.getBottom() == 127;
		assert e.getWidth() == 41;
		assert e.getHeight() == 94;
	}

	@Test
	void testWithTop() {
		Extent e = extent.withTop(6);
		assert e.getLeft() == 10;
		assert e.getTop() == 6;
		assert e.getRight() == 47;
		assert e.getBottom() == 127;
		assert e.getWidth() == 37;
		assert e.getHeight() == 121;
	}

	@Test
	void testWithRight() {
		Extent e = extent.withRight(37);
		assert e.getLeft() == 10;
		assert e.getTop() == 33;
		assert e.getRight() == 37;
		assert e.getBottom() == 127;
		assert e.getWidth() == 27;
		assert e.getHeight() == 94;
	}

	@Test
	void testWithBottom() {
		Extent e = extent.withBottom(93);
		assert e.getLeft() == 10;
		assert e.getTop() == 33;
		assert e.getRight() == 47;
		assert e.getBottom() == 93;
		assert e.getWidth() == 37;
		assert e.getHeight() == 60;
	}

	@Test
	void testWithWidth() {
		Extent e = extent.withWidth(100);
		assert e.getLeft() == 10;
		assert e.getTop() == 33;
		assert e.getRight() == 110;
		assert e.getBottom() == 127;
		assert e.getWidth() == 100;
		assert e.getHeight() == 94;
	}

	@Test
	void testWithHeight() {
		Extent e = extent.withHeight(400);
		assert e.getLeft() == 10;
		assert e.getTop() == 33;
		assert e.getRight() == 47;
		assert e.getBottom() == 433;
		assert e.getWidth() == 37;
		assert e.getHeight() == 400;
	}
	
	@Test
	void testToString() {
		assertEquals("drawit.shapegroups1.Extent[left=10, top=33, right=47, bottom=127]", extent.toString());
	}
	
	@Test
	void testEqualsObject() {
		assertEquals(Extent.ofLeftTopRightBottom(10, 33, 47, 127), extent);
		assertFalse(Extent.ofLeftTopRightBottom(10, 33, 47, 128).equals(extent));
		assertFalse(Extent.ofLeftTopRightBottom(10, 33, 46, 127).equals(extent));
		assertFalse(Extent.ofLeftTopRightBottom(10, 32, 47, 127).equals(extent));
		assertFalse(Extent.ofLeftTopRightBottom(11, 33, 47, 127).equals(extent));
		assertTrue(List.of(extent).contains(Extent.ofLeftTopRightBottom(10, 33, 47, 127)));
		
		assertEquals(extent.hashCode(), Extent.ofLeftTopRightBottom(10, 33, 47, 127).hashCode());
		assertTrue(new HashSet<>(List.of(extent)).contains(Extent.ofLeftTopRightBottom(10, 33, 47, 127)));
	}

}
