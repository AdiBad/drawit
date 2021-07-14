package drawitgui2;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import drawit.IntPoint;
import drawit.RoundedPolygon;
import drawit.shapegroups2.ShapeGroup;
import drawit.shapes2.RoundedPolygonShape;
import drawit.shapes2.Shape;
import drawit.shapes2.ShapeGroupShape;
import drawit.shapegroups2.Extent;
import drawit.shapegroups2.LeafShapeGroup;
import drawit.shapegroups2.NonleafShapeGroup;

class ShapeStringifier {
	
	String indentIncrement = "  ";
	StringBuilder builder = new StringBuilder();
	Deque<String> indentStack = new ArrayDeque<String>();
	String indent = "";
	WeakHashMap<ShapeGroup, Integer> groupIds = new WeakHashMap<>();
	int nextGroupId = 1;
	WeakHashMap<RoundedPolygon, Integer> polygonIds = new WeakHashMap<>();
	int nextPolygonId = 1;
	
	int getGroupId(ShapeGroup group) {
		Integer id = groupIds.get(group);
		if (id == null) {
			id = nextGroupId++;
			groupIds.put(group, id);
		}
		return id;
	}
	
	int getPolygonId(RoundedPolygon polygon) {
		Integer id = polygonIds.get(polygon);
		if (id == null) {
			id = nextPolygonId++;
			polygonIds.put(polygon, id);
		}
		return id;
	}
	
	void pushIndent() {
		indentStack.push(indent);
		indent += indentIncrement;
	}
	
	void popIndent() {
		indent = indentStack.pop();
	}
	
	void appendLine(String text) {
		builder.append(indent);
		builder.append(text);
		builder.append("\n");
	}
	
	static String toString(Color color) {
		return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
	}
	
	static String toString(Extent extent) {
		return "[topLeft=" + toString(extent.getTopLeft()) + ", bottomRight=" + toString(extent.getBottomRight()) + "]";
	}
	
	static String toString(IntPoint point) {
		return "(" + point.getX() + ", " + point.getY() + ")";
	}
	
	static String toString(IntPoint[] points) {
		return "[" + Arrays.stream(points).map(p -> toString(p)).collect(Collectors.joining(", ")) + "]";
	}
	
	void append(RoundedPolygon polygon) {
		appendLine("RoundedPolygon(id=" + getPolygonId(polygon) + ")[vertices=" + toString(polygon.getVertices()) + ", radius=" + polygon.getRadius() + ", color=" + toString(polygon.getColor()) + "]");
	}
	
	void append(ShapeGroup group) {
		appendLine(group.getClass().getName() + "(id=" + getGroupId(group) + ")");
		pushIndent();
		if (group instanceof LeafShapeGroup) {
			RoundedPolygon shape = ((LeafShapeGroup)group).getShape();
			append(shape);
		} else {
			List<ShapeGroup> subgroups = ((NonleafShapeGroup)group).getSubgroups();
			for (ShapeGroup subgroup : subgroups)
				append(subgroup);
		}
		popIndent();
	}
	
	void append(Shape shape) {
		if (shape instanceof RoundedPolygonShape)
			append(((RoundedPolygonShape)shape).getPolygon());
		else
			append(((ShapeGroupShape)shape).getShapeGroup());
	}
	
	public String toString() {
		String result = builder.toString();
		builder = new StringBuilder();
		return result;
	}

}
