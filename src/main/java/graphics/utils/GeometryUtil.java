package graphics.utils;

import graphics.Point;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        double xDifference = (point2.getX() - point1.getX());
        double yDifference = (point2.getY() - point1.getY());
        return Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        // Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
        // početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
        // tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
        // Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
        // udaljenosti od P do početne/konačne točke segmenta.

        double distance = crossProduct(s, e, p) / distanceFromPoint(s, e);

        double dot1 = dotProduct(s, e, p);
        if (dot1 > 0) {
            return distanceFromPoint(e, p);
        }

        double dot2 = dotProduct(e, s, p);
        if (dot2 > 0) {
            return distanceFromPoint(s, p);
        }

        return Math.abs(distance);
    }

    //Compute the dot product AB . BC
    private static double dotProduct(Point pointA, Point pointB, Point pointC)
    {
        Point AB = new Point(pointB.getX() - pointA.getX(), pointB.getY() - pointA.getY());
        Point AC = new Point(pointC.getX() - pointA.getX(), pointC.getY() - pointA.getY());

        return AB.getX() * AC.getX() + AB.getY() + AC.getY();
    }

    //Compute the cross product AB x AC
    private static double crossProduct(Point pointA, Point pointB, Point pointC)
    {
        Point AB = new Point(pointB.getX() - pointA.getX(), pointB.getY() - pointA.getY());
        Point AC = new Point(pointC.getX() - pointA.getX(), pointC.getY() - pointA.getY());
        return AB.getX() * AC.getY() - AB.getY() * AC.getX();
    }

}
