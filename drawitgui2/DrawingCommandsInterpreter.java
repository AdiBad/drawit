package drawitgui2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayDeque;
import java.util.Deque;

class DrawingCommandsInterpreter {
	
	String[] tokens;
	int i;
	
	DrawingCommandsInterpreter(String commands) {
		tokens = commands.split("\\s+");
	}
	
	private String nextToken() {
		if (i == tokens.length)
			throw new IllegalArgumentException("Unexpected end of drawing commands text: argument expected");
		return tokens[i++];
	}
	
	private double arg() {
		return Double.parseDouble(nextToken());
	}
	
	private int parseInt() {
		return Integer.parseInt(nextToken());
	}
	
	private Color parseColor() {
		return new Color(parseInt(), parseInt(), parseInt());
	}
	
	private static Arc2D.Double getCircularArc(Graphics2D g, double x, double y, double radius, double arcStart, double arcExtent) {
		return new Arc2D.Double(x - radius, y - radius, 2 * radius, 2 * radius, - arcStart * 180 / Math.PI, - arcExtent * 180 / Math.PI, Arc2D.OPEN);
	}
	
	void execute(Graphics2D g) {
		Deque<Graphics2D> stack = new ArrayDeque<>();
		
		Path2D.Double path = new Path2D.Double();
		while (i < tokens.length) {
			String cmd = tokens[i++];
			switch (cmd) {
			case "": break;
			case "line": path.append(new Line2D.Double(arg(), arg(), arg(), arg()), true); break;
			case "arc": path.append(getCircularArc(g, arg(), arg(), arg(), arg(), arg()), true); break;
			case "fill":
				g.setColor(parseColor());
				path.closePath();
				g.fill(path);
				path = new Path2D.Double();
				break;
			case "pushTranslate":
				stack.push(g);
				g = (Graphics2D)g.create();
				g.translate(arg(), arg());
				break;
			case "pushScale":
				stack.push(g);
				g = (Graphics2D)g.create();
				g.scale(arg(), arg());
				break;
			case "popTransform":
				g = stack.pop();
				break;
			default: throw new IllegalArgumentException("No such drawing command: '" + cmd + "'");
			}
		}
	}

}
