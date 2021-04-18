package hu.zpb.quoridor.data;

import java.awt.*;

public final class Wall extends MovableObject{
    private String orientation;

    public Wall(Point position, Color color, String orientation) {
        this.actualPosition = position;
        this.color = color;
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
