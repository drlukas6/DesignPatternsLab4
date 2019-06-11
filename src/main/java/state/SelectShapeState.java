package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.CompositeObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;
import graphics.utils.GeometryUtil;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
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

        if (!ctrlDown) {
            documentModel.clearSelectedObjects();
        }

        object.setSelected(true);
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
            case 61:
                handleStackMovement(true);
                return;
            case 47:
                handleStackMovement(false);
                return;
            case KeyEvent.VK_G:
                List<GraphicalObject> selectedObjects = new LinkedList<>(documentModel.getSelectedObjects());

                for (GraphicalObject object: selectedObjects) {
                    documentModel.removeGraphicalObject(object);
                }

                GraphicalObject composite = new CompositeObject(selectedObjects);
                composite.setSelected(true);
                documentModel.addGraphicalObject(composite);

                documentModel.notifyListeners();

                return;
            case KeyEvent.VK_U:
                if (documentModel.getSelectedObjects().size() == 0 || documentModel.getSelectedObjects().size() > 1) {
                    return;
                }

                GraphicalObject obj = documentModel.getSelectedObjects().get(0);

                if (!(obj.getClass() == CompositeObject.class)) {
                    return;
                }

                CompositeObject compObj = (CompositeObject) obj;

                List<GraphicalObject> compositedObjects = compObj.getObjects();

                documentModel.removeGraphicalObject(compObj);


                for(GraphicalObject gObj: compositedObjects) {
                    gObj.setSelected(true);
                    documentModel.addGraphicalObject(gObj);
                }

                documentModel.notifyListeners();

                return;
            default:
                break;
        }

        if (deltaX != 0 || deltaY != 0) {
            for(GraphicalObject object: documentModel.getSelectedObjects()) {
                for(int i = 0; i < object.getNumberOfHotPoints(); ++i) {
                    object.translate(new Point(deltaX, deltaY));
                }
            }
            documentModel.notifyListeners();
        }
    }

    private void handleStackMovement(boolean increase) {
        if (documentModel.getSelectedObjects().size() < 0 || documentModel.getSelectedObjects().size() > 1) {
            return;
        }

        GraphicalObject selectedObject = documentModel.getSelectedObjects().get(0);

        if (increase) {
            documentModel.increaseZ(selectedObject);
        } else {
            documentModel.decreaseZ(selectedObject);
        }

        documentModel.notifyListeners();
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
