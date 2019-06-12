package gui;

import factories.GraphicalObjectFactory;
import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.LineSegment;
import graphics.graphicalObjects.Oval;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.listeners.DocumentModelListener;
import graphics.renderer.G2DRendererImpl;
import graphics.renderer.Renderer;
import graphics.renderer.SVGRendererImpl;
import jdk.nashorn.internal.runtime.ECMAException;
import state.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

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
        loadButton.addActionListener(l -> {
            showLoadDialog();
            this.requestFocus();
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(l -> {
            showSaveDialog();
            this.requestFocus();
        });

        JButton svgExportButton = new JButton("Export SVG");
        svgExportButton.addActionListener(l -> {
            showSVGExportDialog();
            this.requestFocus();
        });

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
        eraserButton.addActionListener(l -> {
            state.onLeaving();
            this.state = new EraserState(documentModel, new G2DRendererImpl((Graphics2D) getGraphics()));
            this.requestFocus();
        });

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

    private void showSVGExportDialog() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to export SVG to");

        int userSelection = fileChooser.showSaveDialog(this);
        String location = "";
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            location = fileToSave.getAbsolutePath();
        } else {
            return;
        }

        SVGRendererImpl svgRenderer = new SVGRendererImpl(location, getWidth(), getHeight());

        for(GraphicalObject object: documentModel.list()) {
            object.render(svgRenderer);
        }

        svgRenderer.close();
    }

    private void showSaveDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save to");

        int userSelection = fileChooser.showSaveDialog(this);
        String location = "";
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            location = fileToSave.getAbsolutePath();
        } else {
            return;
        }

        writeLinesToFile(location);
    }

    private void showLoadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to load drawing from");

        int userSelection = fileChooser.showSaveDialog(this);

        String filename;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File chosenFile = fileChooser.getSelectedFile();
            filename = chosenFile.getAbsolutePath();
        } else {
            return;
        }

        loadLinesFromFile(filename);
    }

    private void loadLinesFromFile(String filename) {
        Stack<GraphicalObject> objects = new Stack<>();

        try {
            Stream<String> lines = Files.lines(Paths.get(filename), Charset.defaultCharset());
            lines.forEach(l -> {
                GraphicalObjectFactory.instance.graphicalObjectWithDescription(objects, l);
            });

            System.out.println("Loading done!");

            documentModel.clear();

            while (!objects.empty()) {
                documentModel.addGraphicalObject(objects.pop());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeLinesToFile(String filename) {
        Path file = Paths.get(filename);

        if (!filename.endsWith(".lsd")) {
            filename += ".lsd";
        }

        List<String> objectDescriptions = new LinkedList<>();
        for (GraphicalObject object: documentModel.list()) {
            object.save(objectDescriptions);
        }
        try {
            Files.write(file, objectDescriptions, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
