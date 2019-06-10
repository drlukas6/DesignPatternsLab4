package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class SelectShapeState implements State{
    private DocumentModel documentModel;


    public SelectShapeState(DocumentModel documentModel) {
        this.documentModel = documentModel;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

        GraphicalObject object = documentModel.findSelectedGraphicalObject(mousePoint);

        if (object == null) {
            if (!ctrlDown) {
                documentModel.clearSelectedObjects();
            }
            return;
        }

        object.setSelected(true);

        if (!ctrlDown) {
            documentModel.clearSelectedObjects();
        }

        documentModel.addGraphicalObject(object);
    }


    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {
        List<GraphicalObject> selected = documentModel.getSelectedObjects();
        if (selected.size() == 0 || selected.size() > 1) {
            return;
        }

        GraphicalObject object = selected.get(0);
        int selectedPtIndex = documentModel.findSelectedHotPoint(object, mousePoint);

        if (selectedPtIndex < 0) {
            return;
        }

        object.setHotPointSelected(selectedPtIndex, true);
        Point selectedPoint = object.getHotPoint(selectedPtIndex);

        selectedPoint.setX(mousePoint.getX());
        selectedPoint.setY(mousePoint.getY());
        documentModel.notifyListeners();
    }

    @Override
    public void keyPressed(int keyCode) {

        int deltaX = 0;
        int deltaY = 0;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                deltaX = -2;
                break;
            case KeyEvent.VK_UP:
                deltaY = -2;
                break;
            case KeyEvent.VK_RIGHT:
                deltaX = 2;
                break;
            case KeyEvent.VK_DOWN:
                deltaY = 2;
                break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_MINUS:
                handleStackMovement();
                return;
            default:
                break;
        }

        for(GraphicalObject object: documentModel.getSelectedObjects()) {
            for(int i = 0; i < object.getNumberOfHotPoints(); ++i) {
                Point pnt = object.getHotPoint(i);
                pnt.setX(pnt.getX() + deltaX);
                pnt.setY(pnt.getY() + deltaY);
            }
        }

        documentModel.notifyListeners();
    }

    private void handleStackMovement() {
        if (documentModel.getSelectedObjects().size() < 0 || documentModel.getSelectedObjects().size() > 1) {
            return;
        }

        GraphicalObject selectedObject = documentModel.getSelectedObjects().get(0);

        documentModel.increaseZ(selectedObject);
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {
        documentModel.clearSelectedObjects();
    }
}
