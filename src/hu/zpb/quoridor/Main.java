package hu.zpb.quoridor;

import hu.zpb.quoridor.data.*;
import hu.zpb.quoridor.network.GameTRX;
import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GUI gameGUI = new GUI();
        gameGUI.drawMenu();
        
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();//creating instance of JFrame

            f.setSize(900, 600);//400 width and 500 height
            var panel = new GUI();
            panel.setBackground(Color.WHITE);
            f.getContentPane().add(panel, BorderLayout.CENTER);

            f.setVisible(true);//making the frame visible
            f.setResizable(false);
            f.setLocationRelativeTo(null);
        });

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
