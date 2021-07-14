package drawit.shapes1;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.shapegroups1.ShapeGroup;

abstract class ShapeImpl implements Shape {
	
	public abstract ShapeGroup getParent();
	
	public abstract boolean contains(IntPoint p);
	
	public abstract String getDrawingCommands();
	
	public abstract ControlPoint[] createControlPoints();
	
}
