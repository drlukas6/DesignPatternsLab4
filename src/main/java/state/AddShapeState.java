package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;

public class AddShapeState implements State {

    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(GraphicalObject prototype, DocumentModel model) {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
            GraphicalObject duplicate = prototype.duplicate();
            duplicate.translate(mousePoint);
            model.addGraphicalObject(duplicate);

            model.notifyListeners();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

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
