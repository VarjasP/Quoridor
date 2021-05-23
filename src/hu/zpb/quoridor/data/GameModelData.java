package hu.zpb.quoridor.data;

import java.awt.*;

public class GameModelData {
    protected Player[] playerList;
    protected Wall[] wallList;
    protected Player curPlayer;
    protected boolean gameFinished;
    protected int winnerID;

    public GameModelData() {
        playerList = new Player[2];
        wallList = new Wall[20];
        gameFinished = false;
    }

    public void resetGame() {
        wallList = new Wall[20];
        gameFinished = false;
        playerList[0].setActualPosition(new Point(4,0));
        playerList[1].setActualPosition(new Point(4,8));
        playerList[0].setAvailableWalls(10);
        playerList[1].setAvailableWalls(10);
    }

    public Player[] getPlayerList() {
        return playerList;
    }

    public void setPlayerList(Player[] playerList) {
        this.playerList = playerList;
    }

    public Wall[] getWallList() {
        return wallList;
    }

    public void setWallList(Wall[] wallList) {
        this.wallList = wallList;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    public void addWall(Wall wall){
        wallList[getWallCount()] = wall;
    }

    public int getWallCount() {
        int count = 0;
        for (Wall w : wallList) {
            if (w != null) {
                count++;
            }
        }
        return count;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public boolean getGameFinished() {
        return gameFinished;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }
}


