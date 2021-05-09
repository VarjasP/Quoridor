package hu.zpb.quoridor.data;
import java.awt.Point;
import java.awt.Color;

public class MovableObject {
    protected Point actualPosition;
    protected Color color;

    public MovableObject() {
    }

    public MovableObject(Point actualPosition, Color color) {
        this.actualPosition = actualPosition;
        this.color = color;
    }

    public Point getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(Point actualPosition) {
        this.actualPosition = actualPosition;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
