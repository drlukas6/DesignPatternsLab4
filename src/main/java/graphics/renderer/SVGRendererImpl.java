package graphics.renderer;

import graphics.Point;
import graphics.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {
    private List<String> lines = new ArrayList<>();
    private String filename;

    public SVGRendererImpl(String filename, int width, int height) {
        this.filename = filename;
        String start = String.format("<svg height=\"%d\" width=\"%d\">", width, height);
        lines.add(start);
    }

    public void close() {
        lines.add("</svg>");

        System.out.println("SVG DESCRTIPTION:");
        for(String line: lines) {
            System.out.println(line);
        }
    }

    @Override
    public void drawLine(Point s, Point e) {
        String description = String.format(
                "\t<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:blue;stroke-width:2\" />",
                s.getX(), s.getY(), e.getX(), e.getY());
        lines.add(description);
    }

    @Override
    public void fillPolygon(Point[] points) {

    }

    @Override
    public void fillOval(Point point, int width, int height) {
        int coefA = width / 2;
        int coefB = height / 2;
        Point center = new Point(point.getX() + coefA, point.getY() + coefB);

        String description = String.format(
                "\t<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" style=\"fill:blue;stroke:red;stroke-width:2\" />",
                center.getX(), center.getY(), coefA * 2, coefB * 2);
        lines.add(description);
    }

    @Override
    public void drawRect(Rectangle rectangle) {

    }

    @Override
    public void drawPoints(List<Point> points) {

    }
}
