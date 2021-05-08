package hu.zpb.quoridor;

import hu.zpb.quoridor.data.*;
import hu.zpb.quoridor.network.GameTRX;
import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GUI gameGUI = new GUI();
        gameGUI.drawMenu();

        GameTRX g = GameTRX.getInstance();
        g.setNetworkEvent(new GameTRX.NetworkEvent() {
            @Override
            public void networkEventCallback(String data) {
                g.sendGameEvent(data.toUpperCase());
            }
        });
//         g.createClient();
//         g.sendGameEvent("game event");

        g.createServer();
    }
}
