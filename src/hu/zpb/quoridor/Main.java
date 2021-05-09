package hu.zpb.quoridor;

import hu.zpb.quoridor.data.*;
import hu.zpb.quoridor.network.GameTRX;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GUI gameGUI = new GUI();
        GameModel gm = new GameModel();

        gameGUI.drawMenu();



        GameTRX g = GameTRX.getInstance();
        g.createServer();
        g.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModel data) {
                g.sendGameEvent(data);
            }

            @Override
            public void playerJoined(Player player) {

            }
        });

        g.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModel data) {
                System.out.println("GameModel érkezett");
            }

            @Override
            public void playerJoined(Player player) {
                System.out.println("Player érkezett");
            }
        });

        g.createClient();
        g.joinPlayer(new Player(new Point(4,0), Color.BLACK, 1, "én", 10));
        g.sendGameEvent(gm);

        g.createClient();
        g.joinPlayer(new Player(new Point(4,0), Color.BLUE, 1, "rattyer", 10));
        g.createClient();
        g.joinPlayer(new Player(new Point(4,0), Color.RED, 1, "géza", 10));
        g.sendGameEvent(gm);
    }
}
