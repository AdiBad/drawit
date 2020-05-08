package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.PointArrays;

class PointArraysTest {

	PointArrays pa = new PointArrays();
	@Test
	void test() {
		//check insert
		IntPoint[] points = {new IntPoint(0,0)};
		IntPoint[] newpoint = PointArrays.insert(points, 1, new IntPoint(1,1));
		
		//verified passing array handle from one variable to another
		points = PointArrays.insert(newpoint, 2, new IntPoint(2,2));
				
		Assert.assertEquals(0, newpoint[0].getX());
		Assert.assertEquals(1, points[1].getX());
		
		//check if these 3 points are a proper polygon
		String check = "This is not a polygon because it has less than 3 sides";
		Assert.assertEquals(2, newpoint.length);
		Assert.assertEquals(check,pa.checkDefinesProperPolygon(newpoint));
		
		//For null point array
		Assert.assertEquals(check,pa.checkDefinesProperPolygon(null));

		//For removing element
		Assert.assertEquals(2, pa.remove(points, 1).length);
	
		//For updating the second element
		Assert.assertEquals(45, pa.update(points, 1, new IntPoint(45,4))[1].getX());
		Assert.assertEquals(4, pa.update(points, 1, new IntPoint(4,4))[1].getX());
		Assert.assertEquals(5, pa.update(points, 1, new IntPoint(5,4))[1].getX());
		}

}
