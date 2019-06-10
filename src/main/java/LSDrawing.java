import graphics.Point;
import graphics.drawing.DocumentModel;
import graphics.graphicalObjects.LineSegment;
import graphics.graphicalObjects.Oval;
import gui.LSDrawingBoard;

import javax.swing.*;

public class LSDrawing {
    public static void main(String[] args) {
        DocumentModel documentModel = new DocumentModel();
        documentModel.addGraphicalObject(new Oval(new Point(100, 150), new Point(150, 100)));
        documentModel.addGraphicalObject(new LineSegment(new Point(200, 50), new Point(300, 300)));

        LSDrawingBoard gui = new LSDrawingBoard(documentModel);
        gui.setTitle("LS Drawing Board");
        SwingUtilities.invokeLater(() -> {
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.setBounds(300, 300, 600, 600);
            gui.setFocusable(true);
            gui.setVisible(true);
        });
    }
}
