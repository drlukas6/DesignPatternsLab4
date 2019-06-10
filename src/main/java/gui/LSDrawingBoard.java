package gui;

import com.sun.tools.internal.xjc.ModelLoader;
import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.LineSegment;
import graphics.graphicalObjects.Oval;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.listeners.DocumentModelListener;
import graphics.renderer.G2DRendererImpl;
import graphics.renderer.Renderer;
import state.AddShapeState;
import state.IdleState;
import state.SelectShapeState;
import state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LSDrawingBoard extends JFrame implements DocumentModelListener {
    private DocumentModel documentModel;

    private State state = new IdleState();

    public LSDrawingBoard(DocumentModel documentModel) throws HeadlessException {
        this.documentModel = documentModel;

        setAutoRequestFocus(true);

        setLayout(new BorderLayout());
        setupMenubar();

        documentModel.addDocumentModelListener(this);
        this.addMouseListener(new LSDrawingBoardMouseListener());
        this.addMouseMotionListener(new LSDrawingBoardMouseMotionListener());
        this.addKeyListener(new LSDrawingBoardKeyListener());

        this.setFocusable(true);


    }

    private void setupMenubar() {
        JToolBar toolBar = new JToolBar("Tool bar");

        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JButton svgExportButton = new JButton("Export SVG");
        JButton lineButton = new JButton("Line");
        lineButton.addActionListener(l -> {
            state.onLeaving();
            this.state = new AddShapeState(new LineSegment(), documentModel);
            this.requestFocus();
        });

        JButton ovalButton = new JButton("Oval");
        ovalButton.addActionListener(l -> {
            state.onLeaving();
            this.state = new AddShapeState(new Oval(), documentModel);
            this.requestFocus();
        });

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(l -> {
            state.onLeaving();
            this.state = new SelectShapeState(documentModel);
            this.requestFocus();
        });

        JButton eraserButton = new JButton("Eraser");

        toolBar.add(loadButton);
        toolBar.add(saveButton);
        toolBar.add(svgExportButton);
        toolBar.add(lineButton);
        toolBar.add(ovalButton);
        toolBar.add(selectButton);
        toolBar.add(eraserButton);

        add(toolBar, BorderLayout.NORTH);
    }

    @Override
    public void documentChange() {
        repaint();
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
