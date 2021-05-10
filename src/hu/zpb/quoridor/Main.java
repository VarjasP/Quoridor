package hu.zpb.quoridor;

import hu.zpb.quoridor.data.*;
import hu.zpb.quoridor.model.*;
import hu.zpb.quoridor.network.GameTRX;
import hu.zpb.quoridor.view.GUI;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GUI gameGUI = new GUI();
        GameModel gameGM = new GameModel();

        gameGUI.setGm(gameGM);
        gameGM.setGui(gameGUI);

        gameGUI.drawMenu();


        GameTRX gameTRX = GameTRX.getInstance();
        System.out.print(gameTRX.getMyIP());


//        // szerver callback in menu
//        gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
//            @Override
//            public void networkEventCallback(GameModelData data) {
//            }
//
//            @Override
//            public void playerJoined(Player player) {
//                gameGM.addPlayer(player);
//                gameTRX.sendGameEvent(gameGM.getGameModelData());
//                // TODO: start game gui
//            }
//        });
//        gameTRX.createServer();

//       // client callback in menu
        gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModelData data) {
                // TODO: start game gui
            }

            @Override
            public void playerJoined(Player player) {
            }
        });
        gameTRX.createClient("188.156.188.252", 51247);
        gameTRX.joinPlayer(new Player(new Point(4,0), Color.BLACK, 1, "géza", 10));
    }
}
