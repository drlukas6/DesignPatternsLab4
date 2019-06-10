package gui;

import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.renderer.G2DRendererImpl;
import graphics.renderer.Renderer;

import javax.swing.*;
import java.awt.*;

public class LSDrawingBoard extends JFrame {
    private DocumentModel documentModel;

    public LSDrawingBoard(DocumentModel documentModel) throws HeadlessException {
        this.documentModel = documentModel;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Renderer renderer = new G2DRendererImpl((Graphics2D) g);

        for (GraphicalObject object : documentModel.list()) {
            object.render(renderer);
        }
    }

}
