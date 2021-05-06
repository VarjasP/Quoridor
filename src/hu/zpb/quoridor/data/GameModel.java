package hu.zpb.quoridor.data;

import java.awt.Point;

public class GameModel {
    /* TODO: gamespace? */

    protected Player[] playerList;
    protected Wall[] wallList;
    protected Player[] currPlayer;

    protected Boolean movePlayer(Player player, Point newPos) {
        return true;
    }
    protected Boolean placeWall(Point pos) {
        return true;
    }

    protected void calcPossiblePlayerPos() {
    }
    protected Boolean isGameFinished() {
        if () {

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
