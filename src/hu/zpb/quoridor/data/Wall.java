package hu.zpb.quoridor.data;

import java.awt.*;

public final class Wall extends MovableObject{
    private Character orientation;

    public Wall(Point position, Color color, Character orientation) {
        this.actualPosition = position;
        this.color = color;
        this.orientation = orientation;
    }

    public Wall(Point position, Character orientation) {
        this.actualPosition = position;
        this.color = Color.decode("#d78564");
        this.orientation = orientation;
    }

    public Character getOrientation() {
        return orientation;
    }

    public void setOrientation(Character orientation) {
        this.orientation = orientation;
    }
}
