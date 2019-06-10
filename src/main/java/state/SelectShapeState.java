package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;

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
