package hu.zpb.quoridor;

import hu.zpb.quoridor.data.*;
import hu.zpb.quoridor.network.GameTRX;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GUI gameGUI = new GUI();
        gameGUI.drawMenu();

        GameModel gm = new GameModel();

        GameTRX g = GameTRX.getInstance();
        g.createServer();
        g.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModel data) {
                g.sendGameEvent(data);
            }
        });

        g.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(GameModel data) {
                System.out.println("GameModel érkezett");
            }
        });
        g.createClient();
        g.sendGameEvent(gm);

        g.createClient();
        g.createClient();
        g.sendGameEvent(gm);
    }
}
