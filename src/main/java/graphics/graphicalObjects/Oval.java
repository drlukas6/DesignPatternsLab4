package graphics.graphicalObjects;

import graphics.Point;
import graphics.Rectangle;
import graphics.renderer.Renderer;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import org.w3c.dom.css.Rect;

import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject {
    private static int ovalCount = 1;

    private int instanceId;

    public Oval(Point startPoint, Point endPoint) {
        super(new Point[] {startPoint, endPoint});
        this.instanceId = ovalCount++;
    }

    public Oval() {
        super(new Point[] {new Point(20, 0 ), new Point(0, 20)});
        this.instanceId = ovalCount++;
    }

    public Oval(int xb, int yb, int xr, int yr) {
        this(new Point(xb, yb), new Point(xr, yr));
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
        Point bottomPoint = getHotPoint(0);
        Point rightPoint = getHotPoint(1);

        Point centerPoint = new Point(bottomPoint.getX(), rightPoint.getY());
        int coefA = rightPoint.getX() - centerPoint.getX();
        int coefB = bottomPoint.getY() - centerPoint.getY();

        double fracA = Math.pow(mousePoint.getX() - centerPoint.getX(), 2) / Math.pow(coefA, 2);
        double fracB = Math.pow(mousePoint.getY() - centerPoint.getY(), 2) / Math.pow(coefB, 2);

        double result = fracA + fracB;

        return result <= 1 ? 0.0 : result;
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
        return "Oval " + instanceId;
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

        if (isSelected()) {
            r.drawRect(rec);
            for(Point hotPoint: getHotPoints()) {
                r.drawRect(new Rectangle(hotPoint.getX() - 3, hotPoint.getY() - 3, 6, 6));
            }
        }
    }
}
