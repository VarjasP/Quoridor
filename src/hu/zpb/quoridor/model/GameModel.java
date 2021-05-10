package hu.zpb.quoridor.model;

import hu.zpb.quoridor.data.GameModelData;
import hu.zpb.quoridor.network.GameTRX;
import hu.zpb.quoridor.view.GUI;
import hu.zpb.quoridor.data.Player;
import hu.zpb.quoridor.data.Wall;

import java.awt.*;

public class GameModel {
    /* TODO: gamespace? */

    protected GameModelData gmd;
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GameModelData getGameModelData() {
        return gmd;
    }

    protected Boolean movePlayer(Player player, Point newPos) {
        return true;
    }
    protected Boolean placeWall(Point pos) {
        return true;
    }

    // Pista teszt konstruktor
    public GameModel() {
        gmd = new GameModelData();
        gmd.getPlayerList()[0] = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);
//        gmd.getWallList()[0] = new Wall(new Point (1,1), gui.getWallColor(), 'h');
        Color clr = Color.decode("#d78564");
        gmd.getWallList()[0] = new Wall(new Point (3,2), clr, 'h');
        gmd.getWallList()[1] = new Wall(new Point (7,5), clr, 'v');
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
        gui.drawGame();
    }

    public void addPlayer(Player newPlayer) {
        gmd.getPlayerList()[1] = newPlayer;
//        TODO: random curPlayer
    }




}
