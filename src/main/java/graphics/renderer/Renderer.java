package graphics.renderer;

import graphics.Point;
import graphics.Rectangle;

import java.util.List;

public interface Renderer {
    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);
    void fillOval(Point point, int width, int height);
    void drawRect(Rectangle rectangle);
    void drawPoints(List<Point> points);
}
