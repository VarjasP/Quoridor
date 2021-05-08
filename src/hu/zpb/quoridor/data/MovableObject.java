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
}
