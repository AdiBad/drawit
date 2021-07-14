package drawitgui2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

import drawit.DoubleVector;
import drawit.IntPoint;
import drawit.IntVector;
import drawit.RoundedPolygon;
import drawit.shapegroups2.LeafShapeGroup;
import drawit.shapegroups2.NonleafShapeGroup;
import drawit.shapegroups2.ShapeGroup;
import drawit.shapes2.ControlPoint;
import drawit.shapes2.RoundedPolygonShape;
import drawit.shapes2.Shape;
import drawit.shapes2.ShapeGroupShape;

class DrawItCanvas extends JPanel {
	
	static int DEFAULT_SHAPE_SIZE = 40;
	static int DEFAULT_CORNER_RADIUS = 10;
	static int HIT_MARGIN = 5;
	static int HANDLE_RADIUS = 5;
	
	JTextField messageField;
	JTextArea textArea;
	ShapeStringifier shapeStringifier = new ShapeStringifier();
	ArrayList<Shape> shapes = new ArrayList<>();
	ArrayList<Shape> selectedShapes = new ArrayList<>();
	Shape shapeBeingDelayMoved;
	int controlPointBeingDelayMoved = -1;
	IntVector controlPointBeingDelayMovedDelta;
	String keyPresses = "";
	
	public String toString() {
		shapeStringifier.appendLine("shapes");
		shapeStringifier.pushIndent();
		for (Shape shape : shapes)
			shapeStringifier.append(shape);
		shapeStringifier.popIndent();
		shapeStringifier.appendLine("");
		shapeStringifier.appendLine("selectedShapes");
		shapeStringifier.pushIndent();
		for (Shape shape : selectedShapes)
			shapeStringifier.append(shape);
		shapeStringifier.popIndent();
		shapeStringifier.appendLine("");
		shapeStringifier.appendLine("keyPresses (most recent first)");
		shapeStringifier.pushIndent();
		shapeStringifier.appendLine(keyPresses);
		shapeStringifier.popIndent();
		
		return shapeStringifier.toString();
	}
	
	void stateChanged() {
		textArea.setText(toString());
		repaint();
	}
	
	private void catchErrors(Runnable body) {
		try {
			body.run();
			messageField.setText("");
		} catch (IllegalArgumentException e) {
			messageField.setText(e.getMessage());
		}
		stateChanged();
	}
	
	/**
	 * Returns the orthogonal projection of the given point {@code p} onto
	 * the carrier of the nonempty line segment {@code ab}. Returns
	 * {@code null} if either the projection is not on the (closed) line
	 * segment, or the distance to the line segment is greater than
	 * {@code maxDist}.
	 */
	private static IntPoint getProjection(IntPoint p, IntPoint a, IntPoint b, int maxDist) {
		DoubleVector ab = b.minus(a).asDoubleVector();
		double size = ab.getSize();
		DoubleVector abUnit = ab.scale(1/size);
		DoubleVector ap = p.minus(a).asDoubleVector();
		double dist = Math.abs(ap.crossProduct(abUnit));
		double dot = ap.dotProduct(abUnit);
		if (dist < maxDist && 0 <= dot && dot <= size)
			return a.asDoublePoint().plus(abUnit.scale(dot)).round();
		else
			return null;
	}

	DrawItCanvas() {
		setFocusable(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				IntPoint p = new IntPoint(e.getX(), e.getY());
				if (e.getClickCount() == 2) {
					if (selectedShapes.size() == 1) {
						Shape selectedShape = selectedShapes.get(0);
						if (selectedShape instanceof RoundedPolygonShape) {
							RoundedPolygon polygon = ((RoundedPolygonShape) selectedShape).getPolygon();
							IntPoint[] vertices = polygon.getVertices();
							if (vertices.length > 2) {
								for (int i = 0; i < vertices.length; i++) {
									IntPoint a = vertices[i];
									IntPoint b = vertices[(i + 1) % vertices.length];
									
									IntPoint proj = getProjection(p, a, b, HIT_MARGIN);
									if (proj != null) {
										polygon.insert(i + 1, proj);
										stateChanged();
										return;
									}
								}
							}
						}
					}
					
					RoundedPolygon polygon = new RoundedPolygon();
					polygon.setVertices(new IntPoint[] {
						new IntPoint(e.getX(), e.getY()),
						new IntPoint(e.getX() + DEFAULT_SHAPE_SIZE, e.getY()),
						new IntPoint(e.getX() + DEFAULT_SHAPE_SIZE, e.getY() + DEFAULT_SHAPE_SIZE),
						new IntPoint(e.getX(), e.getY() + DEFAULT_SHAPE_SIZE)
					});
					polygon.setRadius(DEFAULT_CORNER_RADIUS);
					Shape selectedShape = new RoundedPolygonShape(null, polygon);
					shapes.add(0, selectedShape);
					selectedShapes.clear();
					selectedShapes.add(selectedShape);
					stateChanged();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				IntPoint p = new IntPoint(e.getX(), e.getY());
				
				if (selectedShapes.size() == 1) {
					Shape selectedShape = selectedShapes.get(0);
					ControlPoint[] controlPoints = selectedShape.createControlPoints();
					int controlPointIndex = -1;
					IntPoint shapePoint = p;
					for (int i = 0; i < controlPoints.length; i++) {
						if (Math.abs(shapePoint.getX() - controlPoints[i].getLocation().getX()) < HIT_MARGIN &&
							Math.abs(shapePoint.getY() - controlPoints[i].getLocation().getY()) < HIT_MARGIN)
							controlPointIndex = i;
					}
					if (controlPointIndex != -1) {
						ControlPoint controlPoint = controlPoints[controlPointIndex];
						if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0) {
							catchErrors(() -> controlPoint.remove());
							return;
						}
						boolean moveLive = controlPoint.moveLive();
						if (!moveLive) {
							shapeBeingDelayMoved = selectedShape;
							controlPointBeingDelayMoved = controlPointIndex;
							controlPointBeingDelayMovedDelta = new IntVector(0, 0);
						}
						MouseAdapter ma = new MouseAdapter() {
							@Override
							public void mouseReleased(MouseEvent e) {
								removeMouseListener(this);
								removeMouseMotionListener(this);
								if (!moveLive) {
									catchErrors(() -> controlPoint.move(controlPointBeingDelayMovedDelta));
									shapeBeingDelayMoved = null;
									controlPointBeingDelayMoved = -1;
									controlPointBeingDelayMovedDelta = null;
								}
							}
							
							@Override
							public void mouseDragged(MouseEvent e1) {
								IntPoint q = new IntPoint(e1.getX(), e1.getY());
								IntVector delta = q.minus(p);
								if (moveLive)
									catchErrors(() -> controlPoint.move(delta));
								else
									controlPointBeingDelayMovedDelta = delta;
								repaint();
							}
						};
						addMouseListener(ma);
						addMouseMotionListener(ma);
						return;
					}
					
					if (selectedShape instanceof ShapeGroupShape) {
						ShapeGroup group = ((ShapeGroupShape)selectedShape).getShapeGroup();
						if (group instanceof LeafShapeGroup) {
							RoundedPolygon polygon = ((LeafShapeGroup)group).getShape();
							if (polygon.contains(p)) {
								selectedShapes.clear();
								selectedShapes.add(new RoundedPolygonShape(group, polygon));
								stateChanged();
								return;
							}
						} else {
							ShapeGroup subgroup = ((NonleafShapeGroup)group).getSubgroupAt(p);
							if (subgroup != null) {
								selectedShapes.clear();
								selectedShapes.add(new ShapeGroupShape(subgroup));
								stateChanged();
								return;
							}
						}
					}
				}

				if (!e.isShiftDown())
					selectedShapes.clear();
				for (Shape shape : shapes) {
					if (shape.contains(p)) {
						selectedShapes.add(shape);
						break;
					}
				}
				if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0 && selectedShapes.size() == 1) {
					shapes.remove(selectedShapes.get(0));
					selectedShapes.clear();
				}
				stateChanged();
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				keyPresses = e.getKeyChar() + " " + keyPresses;
				if (e.getKeyChar() == 'G') {
					if (selectedShapes.size() == 1 && selectedShapes.get(0) instanceof RoundedPolygonShape && selectedShapes.get(0).getParent() == null) {
						Shape group = new ShapeGroupShape(new LeafShapeGroup(((RoundedPolygonShape)selectedShapes.get(0)).getPolygon()));
						shapes.remove(selectedShapes.get(0));
						selectedShapes.set(0, group);
						shapes.add(0, group);
						stateChanged();
						return;
					}
					if (selectedShapes.size() >= 2) {
						ShapeGroup[] groups = new ShapeGroup[selectedShapes.size()];
						for (int i = 0; i < groups.length; i++) {
							Shape shape = selectedShapes.get(i);
							if (!(shape instanceof ShapeGroupShape))
								return;
							groups[i] = ((ShapeGroupShape)shape).getShapeGroup();
						}
						catchErrors(() -> {
							Shape group = new ShapeGroupShape(new NonleafShapeGroup(groups));
							shapes.removeAll(selectedShapes);
							selectedShapes.clear();
							selectedShapes.add(group);
							shapes.add(0, group);
						});
						stateChanged();
						return;
					}
				}
				if (e.getKeyChar() == 'F' || e.getKeyChar() == 'B') {
					if (selectedShapes.size() == 1 && selectedShapes.get(0) instanceof ShapeGroupShape && selectedShapes.get(0).getParent() != null) {
						if (e.getKeyChar() == 'F')
							((ShapeGroupShape)selectedShapes.get(0)).getShapeGroup().bringToFront();
						else
							((ShapeGroupShape)selectedShapes.get(0)).getShapeGroup().sendToBack();
						stateChanged();
					}
				}
				for (Shape selectedShape : selectedShapes) {
					if (selectedShape instanceof RoundedPolygonShape) {
						RoundedPolygon polygon = ((RoundedPolygonShape)selectedShape).getPolygon();
						switch (e.getKeyChar()) {
						case '-': catchErrors(() -> polygon.setRadius(polygon.getRadius() - 1)); break;
						case '+': catchErrors(() -> polygon.setRadius(polygon.getRadius() + 1)); break;
						case 'r': polygon.setColor(Color.red); break;
						case 'g': polygon.setColor(Color.green); break;
						case 'b': polygon.setColor(Color.blue); break;
						}
						stateChanged();
					}
				}
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 400);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape shape = shapes.get(i);
			String cmds = shape.getDrawingCommands();
			DrawingCommandsInterpreter interpreter = new DrawingCommandsInterpreter(cmds);
			
			interpreter.execute(g2d);
		}

		for (Shape selectedShape : selectedShapes) {
			g2d.setColor(Color.black);
			ControlPoint[] controlPoints = selectedShape.createControlPoints();
			for (int i = 0; i < controlPoints.length; i++) {
				ControlPoint controlPoint = controlPoints[i];
				IntPoint location = controlPoint.getLocation();
				g2d.drawRect(location.getX() - HANDLE_RADIUS, location.getY() - HANDLE_RADIUS, 2*HANDLE_RADIUS, 2*HANDLE_RADIUS);
				if (selectedShape == shapeBeingDelayMoved && i == controlPointBeingDelayMoved) {
					g2d.setColor(Color.blue);
					IntPoint newLocation = location.plus(controlPointBeingDelayMovedDelta);
					g2d.drawLine(location.getX(), location.getY(), newLocation.getX(), newLocation.getY());
					g2d.drawRect(newLocation.getX() - HANDLE_RADIUS, newLocation.getY() - HANDLE_RADIUS, 2*HANDLE_RADIUS, 2*HANDLE_RADIUS);
					g2d.setColor(Color.black);
				}
			}
		}
	}
	
}
