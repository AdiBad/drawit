package drawit.shapes1;

import drawit.IntPoint;
import drawit.shapegroups1.ShapeGroup;

/**
 * Interface that generalizes classes {@code RoundedPolygonShape} and {@code ShapeGroupShape}.
 */
public interface Shape {

	ShapeGroup getParent();

	boolean contains(IntPoint p);

	String getDrawingCommands();

	ControlPoint[] createControlPoints();

}