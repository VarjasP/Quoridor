package hu.zpb.quoridor.data;

public class GameModelData {
    protected Player[] playerList;
    protected Wall[] wallList;
    protected Player curPlayer;

    public GameModelData() {
        playerList = new Player[2];
        wallList = new Wall[20];
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
}


