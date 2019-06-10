package graphics.graphicalObjects;

import graphics.Point;
import graphics.Rectangle;
import graphics.renderer.Renderer;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;

import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject {
    private static int ovalCount = 1;

    public Oval(Point startPoint, Point endPoint) {
        super(new Point[] {startPoint, endPoint});
    }

    public Oval() {
        super(new Point[] {new Point(10, 0 ), new Point(0, 10)});
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

        Point diff = startPoint.difference(endPoint);

        Point recStartPoint = new Point(startPoint.getX() - diff.getX(), startPoint.getY() - 2*diff.getY());

        return new Rectangle(
                recStartPoint.getX(),
                recStartPoint.getY(),
                diff.getX() * 2,
                diff.getY() * 2);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Rectangle rec = getBoundingBox();
        Point recCenterPoint = new Point(rec.getX() + rec.getWidth() / 2, rec.getY() + rec.getHeight() / 2);
        Point difference = recCenterPoint.difference(mousePoint);

        return Math.sqrt(Math.pow(difference.getX(), 2) + Math.pow(difference.getY(), 2));
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0).copy(), getHotPoint(1).copy());
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public String getShapeID() {
        return "Oval " + ovalCount++;
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {

    }

    @Override
    public void save(List<String> rows) {

    }

    @Override
    public void render(Renderer r) {
        Rectangle rec = getBoundingBox();
        r.fillOval(new Point(rec.getX(), rec.getY()), rec.getWidth(), rec.getHeight());
        r.drawRect(rec);
    }
}
