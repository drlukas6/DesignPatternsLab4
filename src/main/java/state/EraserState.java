package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EraserState implements State {
    private DocumentModel documentModel;
    private Renderer renderer;

    private List<Point> eraserContactPoints = new LinkedList<>();
    private Set<GraphicalObject> touchedObjects = new HashSet<>();


    public EraserState(DocumentModel documentModel, Renderer renderer) {
        this.documentModel = documentModel;
        this.renderer = renderer;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        eraserContactPoints.clear();
        touchedObjects.clear();

        eraserContactPoints.add(mousePoint);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        eraserContactPoints.clear();
        for(GraphicalObject object: touchedObjects) {
            documentModel.removeGraphicalObject(object);
        }
        touchedObjects.clear();

        documentModel.notifyListeners();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        eraserContactPoints.add(mousePoint);

        GraphicalObject touchedObject = documentModel.findSelectedGraphicalObject(mousePoint);

        if (touchedObject == null) {
            return;
        }

        touchedObjects.add(touchedObject);

        renderer.drawPoints(eraserContactPoints);
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}
