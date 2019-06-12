package graphics.graphicalObjects;

import graphics.Point;
import graphics.Rectangle;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;
import graphics.utils.GeometryUtil;

import java.util.*;

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
        Rectangle rect = getBoundingBox();

        Point A = new Point(rect.getX(), rect.getY());
        Point B = new Point(rect.getX() + rect.getWidth(), rect.getY());
        Point C = new Point(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        Point D = new Point(rect.getX(), rect.getY() + rect.getHeight());


        double ABDist = GeometryUtil.distanceFromLineSegment(A, B, mousePoint);
        double ADDist = GeometryUtil.distanceFromLineSegment(A, D, mousePoint);
        double CBDist = GeometryUtil.distanceFromLineSegment(C, B, mousePoint);
        double CDDist = GeometryUtil.distanceFromLineSegment(C, D, mousePoint);

        List<Double> dists = Arrays.asList(ABDist, ADDist, CBDist, CDDist);
        dists.sort(Double::compareTo);

        return dists.get(0) < 0.0 ? 0.0 : dists.get(0) ;
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
    public GraphicalObject load(Stack<GraphicalObject> stack, String data) {
        String[] elements = data.split(" ");

        if (elements.length != 1) {
            return null;
        }

        int numberOfElements = Integer.parseInt(elements[0]);
        List<GraphicalObject> objects = new LinkedList<>();

        for(int i = 0; i < numberOfElements; ++i) {
            objects.add(stack.pop());
        }

        GraphicalObject comp = new CompositeObject(objects);

        stack.push(comp);
        return comp;
    }

    @Override
    public void save(List<String> rows) {
        for (GraphicalObject object: objects) {
            object.save(rows);
        }
        rows.add(getShapeName() + " " + objects.size());
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        objects.forEach(o -> o.setSelected(false));
    }

    public List<GraphicalObject> getObjects() {
        return objects;
    }
}
