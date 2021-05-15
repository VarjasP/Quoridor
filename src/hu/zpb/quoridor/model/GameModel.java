package hu.zpb.quoridor.model;

import hu.zpb.quoridor.data.GameModelData;
import hu.zpb.quoridor.network.GameTRX;
import hu.zpb.quoridor.view.GUI;
import hu.zpb.quoridor.data.Player;
import hu.zpb.quoridor.data.Wall;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameModel {

    protected GameModelData gmd;
    protected GUI gui;
    protected int myPlayerID;

    public GameModel() {
        gmd = new GameModelData();
        System.out.println("Szevasz Pista");

        GameTRX.getInstance().setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModelData data) {
                updateGame(data);
            }

            @Override
            public void playerJoined(Player player) {

            }
        });
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GameModelData getGameModelData() {
        return gmd;
    }

    public int getMyPlayerID() {
        return myPlayerID;
    }

    public void setMyPlayerID(int myPlayerID) {
        this.myPlayerID = myPlayerID;
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

        GameTRX.getInstance().sendGameEvent(gmd);
    }
    public void updateGame(GameModelData newData) {
        gmd = newData;
        gui.refreshGame();
    }

    public void addPlayers(Player serverPlayer, Player clientPlayer) {
        gmd.getPlayerList()[0] = serverPlayer;
        gmd.getPlayerList()[1] = clientPlayer;

        int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
        gmd.setCurPlayer(gmd.getPlayerList()[randomNum]);
    }

    public boolean movePlayer(Point newPos) {
        Point curPos = gmd.getCurPlayer().getActualPosition();
        // TODO: check curPlayer is the actual player

        if(!isPointWithin(newPos, 8, 8)){
            return false;
        }

        // ellenfélre nem lépünk rá
        if(isPointSame(getOtherPlayer().getActualPosition(), newPos)){
            return false;
        }

        // szomszédos mező yeah, és nincs arrafele fal
        if(isPointAdjacent(curPos, newPos) && !isWallBetween(curPos,newPos)){
            getCurrentPlayer().setActualPosition(newPos);
            gmd.setCurPlayer(getOtherPlayer());
            return true;
        }

        // ellenfél átugrása, és nincs útban fal
        if(isPointAdjacent(curPos, getOtherPlayer().getActualPosition()) &&
           isPointAdjacent(getOtherPlayer().getActualPosition(), newPos) &&
           !isWallBetween(curPos, getOtherPlayer().getActualPosition()) &&
           !isWallBetween(getOtherPlayer().getActualPosition(), newPos)){
            getCurrentPlayer().setActualPosition(newPos);
            gmd.setCurPlayer(getOtherPlayer());
            return true;
        }

        return false;
    }

    public boolean placeWall(Point pos) {
        return true;
    }

    private boolean isPointAdjacent(Point p1, Point p2){
        int dx = (int)p1.getX() -  (int)p2.getX();
        int dy = (int)p1.getY() - (int)p2.getY();
        return (dx*dx+dy*dy == 1);
    }

    private boolean isPointWithin(Point p, int limitX, int limitY){
        int x = (int)p.getX();
        int y = (int)p.getY();
        return (x >= 0 && y >= 0 && x <= limitX & y <= limitY);
    }

    private boolean isPointSame(Point p1, Point p2){
        return ((int)p1.getX() == (int)p2.getX() && (int)p1.getY() == (int)p2.getY());
    }


    // pFrom and pTo must be adjacent points
    private boolean isWallBetween(Point pointFrom, Point pointTo){
        int p1x = (int)pointFrom.getX();
        int p1y = (int)pointFrom.getY();
        int p2x = (int)pointTo.getX();
        int p2y = (int)pointTo.getY();

        // jobbra
        if(p2x-p1x == 1){
            return (getWallBy(new Point(p1x+1, p1y), 'v') != null  ||
               getWallBy(new Point(p1x+1, p1y+1), 'v') != null);
        }

        // balra
        if(p2x-p1x == -1){
            return (getWallBy(new Point(p1x, p1y), 'v') != null  ||
                    getWallBy(new Point(p1x, p1y+1), 'v') != null);
        }

        // le
        if(p2y-p1y == 1){
            return (getWallBy(new Point(p1x, p1y+1), 'h') != null  ||
                    getWallBy(new Point(p1x+1, p1y+1), 'h') != null);
        }

        // fel
        if(p2y-p1y == -1){
            return (getWallBy(new Point(p1x, p1y), 'h') != null  ||
                    getWallBy(new Point(p1x+1, p1y), 'h') != null);
        }

        return false;
    }


    // TODO: ezek game model databa?
    private  Player getOtherPlayer() {
        for (Player p : gmd.getPlayerList()) {
            if (p.getID() != gmd.getCurPlayer().getID()) {
                return p;
            }
        }

        return null; // :O
    }
    private Player getCurrentPlayer() {
        for(Player p: gmd.getPlayerList()){
            if(p.getID() == gmd.getCurPlayer().getID()){
                return p;
            }
        }

        return null; // :O
    }

    private Wall getWallBy(Point position, Character orientation){
        for(Wall w : gmd.getWallList()){
            if(w != null){
                if(isPointSame(w.getActualPosition(),position) && w.getOrientation() == orientation){
                    return w;
                }
            }
        }
        return null;
    }

}
