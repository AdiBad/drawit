package drawit.shapegroups1.exporter;

import java.awt.Color;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import drawit.IntPoint;
import drawit.RoundedPolygon;
import drawit.shapegroups1.LeafShapeGroup;
import drawit.shapegroups1.NonleafShapeGroup;
import drawit.shapegroups1.ShapeGroup;

public class ShapeGroupExporter {
	
	public static Object toPlainData(IntPoint point) {
		return Map.of("x", point.getX(), "y", point.getY());
	}
	
	public static Object toPlainData(Color color) {
		return Map.of("red", color.getRed(), "green", color.getGreen(), "blue", color.getBlue());
	}
	
	public static Object toPlainData(RoundedPolygon polygon) {
		return Map.of(
				"vertices", Arrays.stream(polygon.getVertices()).map(p -> toPlainData(p)).collect(Collectors.toList()),
				"radius", polygon.getRadius(),
				"color", toPlainData(polygon.getColor()));
	}
	
	public static Object toPlainData(ShapeGroup shapeGroup) {
		if (shapeGroup instanceof LeafShapeGroup)
			return Map.of("shape", toPlainData(((LeafShapeGroup)shapeGroup).getShape()));
		else
			return Map.of("subgroups", ((NonleafShapeGroup)shapeGroup).getSubgroups().stream().map(g -> toPlainData(g)).collect(Collectors.toList()));
	}

}
