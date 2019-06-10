package graphics.graphicalObjects;

import graphics.Point;
import graphics.Rectangle;
import graphics.renderer.Renderer;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.utils.GeometryUtil;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {

    private static int lineCount = 1;


    public LineSegment(Point startPoint, Point endPoint) {
        super(new Point[] {startPoint, endPoint});
    }

    public LineSegment() {
        super(new Point[] {new Point(0, 0 ), new Point(10, 0)});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point startPoint = getHotPoint(0);
        Point endPoint = getHotPoint(1);

        if (startPoint.compareTo(endPoint) > 0) {
            Point tmp = startPoint;
            startPoint = endPoint;
            endPoint = tmp;
        }

        int smallerY = startPoint.getY() < endPoint.getY() ? startPoint.getY() : endPoint.getY();

        Point rectangleStartPoint = new Point(startPoint.getX(), smallerY);

        return new Rectangle(
                rectangleStartPoint.getX(),
                rectangleStartPoint.getX(),
                rectangleStartPoint.difference(endPoint).getX(),
                rectangleStartPoint.difference(endPoint).getY());
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0).copy(), getHotPoint(1).copy());
    }

    @Override
    public String getShapeName() {
        return "Line";
    }

    @Override
    public String getShapeID() {
        return "Line " + lineCount++;
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {

    }

    @Override
    public void save(List<String> rows) {

    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }
}