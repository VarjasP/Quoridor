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
    }
}
