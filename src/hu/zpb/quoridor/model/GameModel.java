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

    protected Boolean movePlayer(Player player, Point newPos) {
        return true;
    }
    protected Boolean placeWall(Point pos) {
        return true;
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
}
