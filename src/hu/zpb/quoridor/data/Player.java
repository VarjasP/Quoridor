package hu.zpb.quoridor.data;

import java.awt.*;

public class Player extends MovableObject{
    protected int ID;
    protected String name;
//    protected Point[] possiblePositions;
    protected int availableWalls;



    public Player() {
    }

    public Player(Point startP, Color startC, int ID, String name, int availableWalls) {
        this.actualPosition = startP;
        this.color = startC;
        this.ID = ID;
        this.name = name;
        this.availableWalls = availableWalls;
    }



}
