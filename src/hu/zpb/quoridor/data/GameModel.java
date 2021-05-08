package hu.zpb.quoridor.data;

import java.awt.*;

public class GameModel {
    /* TODO: gamespace? */

    protected Player[] playerList;
    protected Wall[] wallList;
    protected Player curPlayer;

    protected Boolean movePlayer(Player player, Point newPos) {
        return true;
    }
    protected Boolean placeWall(Point pos) {
        return true;
    }

    // Pista teszt konstruktor
    public GameModel() {
        this.playerList = new Player[2];
        this.wallList = new Wall[20];
        this.curPlayer = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);
    }

    protected void calcPossiblePlayerPos() {
    }
    protected Boolean isGameFinished() {
        if (false) {

            return true;
        } else {
            return false;
        }
    }

    protected void makeMove() {
    }
    protected void updateGame() {
    }




}
