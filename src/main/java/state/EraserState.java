package state;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.Renderer;

public class EraserState implements State {
    private DocumentModel documentModel;

    public EraserState(DocumentModel documentModel) {
        this.documentModel = documentModel;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

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
