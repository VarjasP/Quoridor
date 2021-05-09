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

//        gameGUI.drawMenu();



        GameTRX gameTRX = GameTRX.getInstance();
//        gameTRX.createServer();
        gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModelData data) {
                System.out.println("GameModel érkezett");
                gameGM.updateGame(data);
            }

            @Override
            public void playerJoined(Player player) {
                System.out.println("Player érkezett");
                gameGM.addPlayer(player);
                gameTRX.sendGameEvent(gameGM.getGameModelData());
            }
        });
        System.out.print(gameTRX.getMyIP());



        gameTRX.createClient("188.156.188.252", 51247);
        gameTRX.joinPlayer(new Player(new Point(4,0), Color.BLACK, 1, "én", 10));
        gameTRX.sendGameEvent(gameGM.getGameModelData());
//
//        gameTRX.createClient();
//        gameTRX.joinPlayer(new Player(new Point(4,0), Color.BLUE, 1, "rattyer", 10));
//        gameTRX.createClient();
//        gameTRX.joinPlayer(new Player(new Point(4,0), Color.RED, 1, "géza", 10));
//        gameTRX.sendGameEvent(gameGM.getGameModelData());
    }
}
