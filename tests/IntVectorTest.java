package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import drawit.DoublePoint;
import drawit.DoubleVector;
import drawit.IntPoint;
import drawit.IntVector;
import junit.framework.Assert;

class IntVectorTest {

	IntVector point = new IntVector(-1,0);
	IntVector vec = new IntVector(9,8);
	IntVector vec2 = new IntVector(7,3);
	IntVector vec3 = new IntVector(0,0);
	
	@SuppressWarnings("deprecation")
	@Test
	void test_getxy() {
		//test basic getter functionality
		Assert.assertEquals(-1, point.getX());
		Assert.assertNotSame(1, point.getY());
	
		IntVector point1 = new IntVector(0,0);	
		//try crossproduct
		long v = point.crossProduct(point1);
		Assert.assertEquals(0, v);	
		
		//try iscollinearwith
		Assert.assertTrue(vec.isCollinearWith​(vec));
		Assert.assertFalse(vec.isCollinearWith​(vec2));
		
		//try dotproduct
		Assert.assertEquals(0, vec.dotProduct(vec3));
		Assert.assertEquals(0, vec2.dotProduct(vec3));
		Assert.assertEquals(0, point.dotProduct(vec3));
	}

}