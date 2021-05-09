package hu.zpb.quoridor.data;

import java.awt.*;

public class GameModel {
    /* TODO: gamespace? */

    protected int dummy;

    public int getDummy() {
        return dummy;
    }

    public void setDummy(int dummy) {
        this.dummy = dummy;
    }

    protected Player[] playerList;
    protected Wall[] wallList;
    protected Player curPlayer;

    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public int getGUIdummy() {
        return gui.getDummy();
    }

    protected Boolean movePlayer(Player player, Point newPos) {
        return true;
    }
    protected Boolean placeWall(Point pos) {
        return true;
    }

    // Pista teszt konstruktor
    public GameModel() {
        this.playerList = new Player[2];
        playerList[0] = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);
        this.wallList = new Wall[20];
//        this.curPlayer = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);
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
    public void updateGame(GameModel newData) {

        gui.drawGame();
    }

    public void addPlayer(Player newPlayer) {
        playerList[1] = newPlayer;
//        TODO: random curPlayer
    }




}
