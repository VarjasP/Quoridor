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

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GameModelData getGameModelData() {
        return gmd;
    }

    // Pista teszt konstruktor
    public GameModel() {
        gmd = new GameModelData();
        System.out.println("Szevasz Pista");
//        this.curPlayer = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);

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

        if(isPointAdjacent(curPos, newPos)){
            getCurrentPlayer().setActualPosition(newPos);
            gmd.setCurPlayer(getOtherPlayer());
            return true;
        }

        // ellenfél átugrása
        if(isPointAdjacent(curPos, getOtherPlayer().getActualPosition()) &&
           isPointAdjacent(getOtherPlayer().getActualPosition(), newPos)){
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

}
