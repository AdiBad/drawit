package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import drawit.DoublePoint;
import drawit.DoubleVector;
import drawit.IntPoint;
import drawit.IntVector;
import junit.framework.Assert;

class DoubleVectorTest {

	DoubleVector point = new DoubleVector(-1,0);
	DoubleVector vec2 = new DoubleVector(0,0);
	
	@SuppressWarnings("deprecation")
	@Test
	void test_getxy() {
		//test basic getter functionality
		Assert.assertEquals(-1.0, point.getX());
		Assert.assertNotSame(1, point.getY());
	
		DoubleVector vector1 = new DoubleVector(-1,14);
		
		//check plus function
		DoubleVector p = point.plus(vector1);
		
		//verify whether border case gave OVERFLOW value
		Assert.assertEquals(-2.0, p.getX());	
		
		//check scale
		Assert.assertEquals(2*vector1.getX(), vector1.scale(2).getX());
		Assert.assertEquals(-1*vector1.getX(), vector1.scale(-1).getX());
		Assert.assertEquals(20*vector1.getX(), vector1.scale(20).getX());
		Assert.assertEquals(45*vector1.getX(), vector1.scale(45).getX());
		
		//check size
		Assert.assertEquals(1.0, point.getSize());
		Assert.assertEquals(14, (int)vector1.getSize());
		
		//check crossproduct
		Assert.assertEquals(0.0, vec2.crossProduct(vector1));

		//check asangle
		Assert.assertEquals(0.0, vec2.asAngle());
		Assert.assertNotSame(10.0, vec2.asAngle());
	}

}
