package graphics.graphicalObjects.abstracts;

import graphics.Point;
import graphics.listeners.GraphicalObjectListener;
import graphics.utils.GeometryUtil;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;

    private List<GraphicalObjectListener> listeners = new LinkedList<>();

    public AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        hotPointSelected = new boolean[hotPoints.length];
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
    }

    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    public void setHotPointSelected(int index, boolean selected) {
        hotPointSelected[index] = selected;
    }

    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
    }

    public void translate(Point delta) {
        Point[] translatedPoints = new Point[hotPoints.length];

        for(int i = 0; i < hotPoints.length; i++) {
            translatedPoints[i] = hotPoints[i].translate(delta);
        }

        this.hotPoints = translatedPoints;
    }


    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for(GraphicalObjectListener listener: listeners) {
            listener.graphicalObjectChanged(this);
        }
    }

    public void notifySelectionListeners() {
        for(GraphicalObjectListener listener: listeners) {
            listener.graphicalObjectSelectionChanged(this);
        }
    }

    public Point[] getHotPoints() {
        return hotPoints;
    }
}
