package drawit.tests;

import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.IntVector;
import junit.framework.Assert;

class IntPointTest {
	IntPoint point = new IntPoint(-1,0);
	IntPoint point3 = new IntPoint(4,5);
	IntPoint point2 = new IntPoint(3,3);
	
	@SuppressWarnings("deprecation")
	@Test
	void test_getxy() {
		//test basic getter functionality
		Assert.assertEquals(-1, point.getX());
		Assert.assertNotSame(1, point.getY());
	
		
		//setting border value (-2,147,483,648)and incrementing it
		IntVector vector1 = new IntVector(-2147483648,2147483647);
				
		//verify plus
		IntPoint p = point.plus(vector1);

		//also checked whether border case gave OVERFLOW value
		Assert.assertEquals(2147483647, p.getX());
		
		//check equal
		Assert.assertFalse(p.equals(point));

		//verify minus
		Assert.assertEquals(4, point2.minus(point).getX());
		
		//verify isonlinesegment
		Assert.assertFalse(point2.isOnLineSegment(point, p));
		
		//verify linesegmentintersect
		Assert.assertFalse(point.lineSegmentsIntersect(point,point2, p, point3));
	
	}
	

}
