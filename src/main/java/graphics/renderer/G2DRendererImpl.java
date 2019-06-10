package graphics.renderer;

import graphics.Point;
import graphics.Rectangle;

import java.awt.*;
import java.util.Arrays;

public class G2DRendererImpl implements Renderer {
    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        g2d.setColor(Color.BLUE);
        int[] xPoints = Arrays.stream(points).mapToInt(Point::getX).toArray();
        int[] yPoints = Arrays.stream(points).mapToInt(Point::getY).toArray();
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(xPoints, yPoints, xPoints.length);
    }

    @Override
    public void fillOval(Point point, int width, int height) {
        g2d.setColor(Color.BLUE);

        g2d.fillOval(point.getX(), point.getY(), width, height);
        g2d.setColor(Color.RED);
        g2d.drawOval(point.getX(), point.getY(), width, height);
    }

    @Override
    public void drawRect(Rectangle rectangle) {
        g2d.setColor(Color.BLACK);

        g2d.drawRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }
}
