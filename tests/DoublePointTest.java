package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import drawit.DoublePoint;
import drawit.DoubleVector;
import drawit.IntPoint;
import drawit.IntVector;
import junit.framework.Assert;

class DoublePointTest {

	DoublePoint point = new DoublePoint(-1,0);
	DoublePoint point2 = new DoublePoint(-1.56,0);
	
	@SuppressWarnings("deprecation")
	@Test
	void test_getxy() {
		//test basic getter functionality
		Assert.assertEquals(-1.0, point.getX());
		Assert.assertNotSame(1, point.getY());
	
		DoubleVector vector1 = new DoubleVector(1,2);
		
		//verify plus
		DoublePoint p = point.plus(vector1);
		
		//verify getter after plus
		Assert.assertEquals(0.0, p.getX());	

		//verify minus
		Assert.assertEquals(1.0, p.minus(point).getX());
		
		//verify round
		Assert.assertEquals(0, point.round().getY());
		Assert.assertEquals(-2, point2.round().getX());
			
	}
}
