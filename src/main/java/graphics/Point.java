package graphics;

public class Point implements Comparable<Point> {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point translate(Point dp) {
        return new Point(this.x + dp.x, this.y + dp.y);
    }

    public Point difference(Point p) {
        return new Point(Math.abs(this.x - p.x), Math.abs(this.y - p.y));
    }

    @Override
    public int compareTo(Point o) {
        if (this.getX() == o.getX()) {
            return this.getY() - o.getY();
        }

        return this.getX() - o.getX();
    }

    public Point copy() {
        return new Point(x, y);
    }
}
