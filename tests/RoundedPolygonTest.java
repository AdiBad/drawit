package drawit.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import drawit.IntPoint;
import drawit.RoundedPolygon;
import junit.framework.Assert;

class RoundedPolygonTest {
	RoundedPolygon rp = new RoundedPolygon();
	
	@Test
	void test() {
		IntPoint[] points = {new IntPoint(-1,0),new IntPoint(1,2),new IntPoint(-2,1),new IntPoint(-83,4)};
		IntPoint[] points1 = null;
	
		//Assert that null array cannot be instantiated
		int flag=0;
		try {
			 rp.setVertices(points1);			
			}catch(IllegalArgumentException ile) {
				//turn this flag if the null array isn't allowed
					flag=1;
				}
		Assert.assertEquals(1, flag);
				
		int radius = 10;
		rp.vertices = points;
		rp.setRadius(10);		
		
		//check radius
		Assert.assertEquals(10, rp.getRadius());
		
		//check vertices
		Assert.assertEquals(4, rp.getVertices().length);
		
		//check insertion
		rp.insert(1, new IntPoint(1,2));
		Assert.assertEquals(-1, rp.getVertices()[0].getX());
		
		//check remove
		rp.remove(1);
		Assert.assertEquals(4, rp.getVertices().length);
		
		//check update
		rp.update(0, new IntPoint(10,10));
		Assert.assertEquals(10,rp.getVertices()[0].getX());
		
		//check contains
		Assert.assertFalse(rp.contains(new IntPoint(-100,1000)));
		Assert.assertFalse(rp.contains(new IntPoint(-1,0)));
		
		//check getDrawingCommands
		String text = "line 5.5 6.0 2.181757895737503 3.0504514628777804\r\n" + 
				"arc -2.9359621843106445 8.807886552931947 7.703188796460532 -0.844153986113171 -0.4048917862850838\r\n" + 
				"line -0.5 1.5 -0.5 1.5\r\n" + 
				"line -0.5 1.5 -0.4999999999999998 1.5\r\n" + 
				"arc -3.2573339575529285 9.772001872658786 8.719455575593292 -1.2490457723982544 -0.3587706702705715\r\n" + 
				"line -3.580055487147763 1.0585205735980654 -42.5 2.5\r\n" + 
				"line -42.5 2.5 -42.5 2.5\r\n" + 
				"arc -42.423849463813866 4.55606447702575 2.0574741888657084 -1.6078164426688244 -3.040145698290313\r\n" + 
				"line -42.55631433901505 6.609270042644191 -36.5 7.0\r\n" + 
				"line -36.5 7.0 3.991694004742894 9.612367355144702\r\n" + 
				"arc 4.124947786341533 7.546933740365773 2.0697276601932657 1.6352231662204495 -2.47937715233362\r\n" + 
				"line 5.499999999999999 6.0 5.5 6.0\r\n"+
				"";
		//verify length of this text instead of actual string due to presence of
		//special characters in between like \r\n
		Assert.assertEquals(721, rp.getDrawingCommands().length());
	}

}
