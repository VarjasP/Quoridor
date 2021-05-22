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
    
    public void setGameModelData(GameModelData data) {
    	gmd = data;
    }

    protected Boolean isGameFinished() {
        int endRow;
        if (gmd.getCurPlayer().getID() == 0) {
            endRow = 8;
        }
        else {
            endRow = 0;
        }
        if ((int)gmd.getCurPlayer().getActualPosition().getY()==endRow) {
            if (gmd.getCurPlayer().getID() == 0)
                gmd.setWinnerID(0);
            else
                gmd.setWinnerID(1);
            return true;
        }
        return false;
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

        if(!isPointWithin(newPos, 8, 8)){
            return false;
        }

        // ellenfélre nem lépünk rá
        if(isPointSame(getOtherPlayer().getActualPosition(), newPos)){
            return false;
        }

        // szomszédos mező yeah, és nincs arrafele fal
        if(isPointAdjacent(curPos, newPos) && !isWallBetween(curPos,newPos)){
            gmd.getCurPlayer().setActualPosition(newPos);
            gmd.getPlayerList()[gmd.getCurPlayer().getID()] = gmd.getCurPlayer();
            if (isGameFinished()) {
                gmd.setGameFinished(true);
            }
            gmd.setCurPlayer(getOtherPlayer());
            return true;
        }

        // ellenfél átugrása yeah, és nincs útban fal
        if(isPointAdjacent(curPos, getOtherPlayer().getActualPosition()) &&
           isPointAdjacent(getOtherPlayer().getActualPosition(), newPos) &&
           !isWallBetween(curPos, getOtherPlayer().getActualPosition()) &&
           !isWallBetween(getOtherPlayer().getActualPosition(), newPos)){
            // átugrandó játékos mögötti cella
            Point behindCell = new Point((int)(2*getOtherPlayer().getActualPosition().getX() - curPos.getX()),
                    (int)(2*getOtherPlayer().getActualPosition().getY() - curPos.getY()));
            System.out.print("Behind cell: ");
            System.out.println(behindCell);
            // ellenfelet csak akkor ugorhatjuk átlósan, ha mögötte fal ven
            if(isPointSame(newPos, behindCell) || isWallBetween(getOtherPlayer().getActualPosition(), behindCell)) {
                System.out.println();
                gmd.getCurPlayer().setActualPosition(newPos);
                gmd.getPlayerList()[gmd.getCurPlayer().getID()] = gmd.getCurPlayer();
                if (isGameFinished()) {
                    gmd.setGameFinished(true);
                }
                gmd.setCurPlayer(getOtherPlayer());
                return true;
            }
        }

        return false;
    }

    public boolean placeWall(Wall wall) {
        Point pos = wall.getActualPosition();
        Character orientation = wall.getOrientation();
        int px = (int)pos.getX();
        int py = (int)pos.getY();

        // játékosnak van-e elég fala?
        if (gmd.getCurPlayer().getAvailableWalls() <= 0) {
            return false;
        }

        // pálya széle
        if (!isPointWithin(pos,1,8,1,8)) {
            return false;
        }

        // másik fallal ütközés
        if (getWallBy(pos,'h') != null || getWallBy(pos,'v') != null) { // ugyan azon a ponton már van fal
            return false;
        }
        if (orientation == 'h'){ // horizontális fal ellenőrzése balra-jobbra
            if (getWallBy(new Point(px-1, py),orientation) != null ||
               getWallBy(new Point(px+1, py),orientation) != null) {
                return false;
            }
        }
        if (orientation == 'v'){ // horizontális fal ellenőrzése balra-jobbra
            if (getWallBy(new Point(px, py-1),orientation) != null ||
               getWallBy(new Point(px, py+1),orientation) != null) {
                return false;
            }
        }

        // játékosokat nem zárjuk el
        if (isQuarantined(gmd.getPlayerList()[0], wall) || isQuarantined(gmd.getPlayerList()[1], wall)) {
            return false;
        }

        gmd.addWall(wall);
        gmd.getCurPlayer().minusAvailableWalls();
        gmd.getPlayerList()[gmd.getCurPlayer().getID()] = gmd.getCurPlayer();
        gmd.setCurPlayer(getOtherPlayer());
        return true;
    }

    private boolean isPointAdjacent(Point p1, Point p2){
        int dx = (int)p1.getX() -  (int)p2.getX();
        int dy = (int)p1.getY() - (int)p2.getY();
        return (dx*dx+dy*dy == 1);
    }

    private boolean isPointWithin(Point p, int limitX, int limitY){
        return isPointWithin(p, 0, limitX, 0, limitY);
    }

    private boolean isPointWithin(Point p, int lowX, int highX, int lowY, int highY){
        int x = (int)p.getX();
        int y = (int)p.getY();
        return (x >= lowX && x <= highX && y >= lowY && y <= highY);
    }

    private boolean isPointSame(Point p1, Point p2){
        return ((int)p1.getX() == (int)p2.getX() && (int)p1.getY() == (int)p2.getY());
    }


    // pFrom and pTo must be adjacent points
    private boolean isWallBetween(Point pointFrom, Point pointTo){
        return isWallBetween(pointFrom, pointTo, null);
    }

    private boolean isWallBetween(Point pointFrom, Point pointTo, Wall extraWall) {
        int p1x = (int)pointFrom.getX();
        int p1y = (int)pointFrom.getY();
        int p2x = (int)pointTo.getX();
        int p2y = (int)pointTo.getY();

        // pályán kívüli pont esetében "fal" van a két játékos között
        if(!isPointWithin(pointFrom, 8, 8) || !isPointWithin(pointTo, 8, 8)) {
            return true;
        }

        // jobbra
        if(p2x-p1x == 1){
            return (getWallBy(new Point(p1x+1, p1y), 'v', extraWall) != null  ||
                    getWallBy(new Point(p1x+1, p1y+1), 'v', extraWall) != null);
        }

        // balra
        if(p2x-p1x == -1){
            return (getWallBy(new Point(p1x, p1y), 'v', extraWall) != null  ||
                    getWallBy(new Point(p1x, p1y+1), 'v', extraWall) != null);
        }

        // le
        if(p2y-p1y == 1){
            return (getWallBy(new Point(p1x, p1y+1), 'h', extraWall) != null  ||
                    getWallBy(new Point(p1x+1, p1y+1), 'h', extraWall) != null);
        }

        // fel
        if(p2y-p1y == -1){
            return (getWallBy(new Point(p1x, p1y), 'h', extraWall) != null  ||
                    getWallBy(new Point(p1x+1, p1y), 'h', extraWall) != null);
        }

        return false;
    }

    private  Player getOtherPlayer() {
        for (Player p : gmd.getPlayerList()) {
            if (p.getID() != gmd.getCurPlayer().getID()) {
                return p;
            }
        }

        return null;
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

    private Wall getWallBy(Point position, Character orientation, Wall extraWall){
        if(extraWall != null) {
            if (isPointSame(extraWall.getActualPosition(), position) && extraWall.getOrientation() == orientation) {
                return extraWall;
            }
        }

        for(Wall w : gmd.getWallList()){
            if(w != null){
                if(isPointSame(w.getActualPosition(),position) && w.getOrientation() == orientation){
                    return w;
                }
            }
        }
        return null;
    }

    private boolean isQuarantined(Player player, Wall newWall) {
        int[][] flowArr = new int[9][9];
        flowArr[(int)player.getActualPosition().getX()][(int)player.getActualPosition().getY()] = 1;
        int watchedNum = 1;
        boolean modified = true;
        while(modified) {
            modified = false;
            for (int x=0; x<9; x++) {
                for (int y=0; y<9; y++) {
                    if (flowArr[x][y] == watchedNum) {
                        Point currP = new Point(x,y);
                        Point[] neighborList = new Point[4];
                        neighborList[0] = new Point(x+1,y);
                        neighborList[1] = new Point(x-1,y);
                        neighborList[2] = new Point(x,y+1);
                        neighborList[3] = new Point(x,y-1);
                        for (Point currN : neighborList) {
                            if (isPointWithin(currN, 8,8) && !isWallBetween(currP, currN, newWall)) {
                                if (flowArr[(int)currN.getX()][(int)currN.getY()] == 0) {
                                    flowArr[(int)currN.getX()][(int)currN.getY()] = watchedNum + 1;
                                    modified = true;
                                }
                            }
                        }
                    }
                }
            }
            watchedNum++;
        }

        int endRow;
        if (player.getID() == 0) {
            endRow = 8;
        }
        else {
            endRow = 0;
        }
        boolean obstructed = true;
        for (int x=0; x<9; x++) {
            if (flowArr[x][endRow] != 0) {
                obstructed = false;
            }
        }

        return obstructed;
    }

}
