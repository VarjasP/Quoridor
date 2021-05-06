package hu.zpb.quoridor.data;

import java.awt.Point;

public class Player extends MovableObject{
    protected int ID;
    protected String name;
    protected Point[] possiblePositions;
    protected int availableWalls;
}
