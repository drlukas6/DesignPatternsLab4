package graphics.graphicalObjects;

import graphics.Point;
import graphics.Rectangle;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CompositeObject extends AbstractGraphicalObject {
    private List<GraphicalObject> objects;

    private static int compositeCount = 1;

    public CompositeObject(List<GraphicalObject> objects) {
        super(new Point[0]);
        this.objects = objects;
    }

    @Override
    public Rectangle getBoundingBox() {
        List<Rectangle> rects = new LinkedList<>();

        for(GraphicalObject object: objects) {
            rects.add(object.getBoundingBox());
        }

        rects.sort(Comparator.comparingInt(Rectangle::getX));

        Rectangle start = rects.get(0);
        Rectangle end = rects.get(rects.size() - 1);

        Point startPoint = new Point(start.getX(), start.getY());
        int width = (end.getX() + end.getWidth()) - start.getX();

        rects.sort(Comparator.comparingInt(Rectangle::getY));

        Rectangle top = rects.get(0);
        Rectangle bottom = rects.get(rects.size() - 1);

        int height = (end.getY() + end.getHeight()) - start.getY();

        return new Rectangle(startPoint.getX(), startPoint.getY(), width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return 0;
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject object: objects) {
            object.render(r);
        }

        if (isSelected()) {
            r.drawRect(getBoundingBox());
        }
    }

    @Override
    public String getShapeName() {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> duplObjects = new LinkedList<>();
        for(GraphicalObject object: objects) {
            duplObjects.add(object.duplicate());
        }

        return new CompositeObject(duplObjects);
    }

    @Override
    public String getShapeID() {
        return "Composite " + compositeCount++;
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {

    }

    @Override
    public void save(List<String> rows) {

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        objects.forEach(o -> o.setSelected(selected));
    }
}
