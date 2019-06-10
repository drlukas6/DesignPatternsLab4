package gui;

import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.G2DRendererImpl;
import graphics.renderer.Renderer;
import state.IdleState;
import state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LSDrawingBoard extends JFrame {
    private DocumentModel documentModel;

    private State state = new IdleState();

    public LSDrawingBoard(DocumentModel documentModel) throws HeadlessException {
        this.documentModel = documentModel;
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Renderer renderer = new G2DRendererImpl((Graphics2D) g);

        for (GraphicalObject object : documentModel.list()) {
            object.render(renderer);
            state.afterDraw(renderer, object);
        }

        state.afterDraw(renderer);
    }


    private class LSDrawingBoardKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            state.keyPressed(e.getKeyCode());
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    state.onLeaving();
                    state = new IdleState();
                    break;
                default:
                    System.err.println("Needs to be implemented -> " + e.getKeyChar());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class LSDrawingBoardMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            state.mouseDragged(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class LSDrawingBoardMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point mousePoint = new Point(e.getPoint().x, e.getPoint().y);
            state.mouseDown(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point mousePoint = new Point(e.getPoint().x, e.getPoint().y);
            state.mouseUp(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
