package hu.zpb.quoridor.data;
import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {
//    private static final long serialVersionUID = 7148504528835036003L;
    static void drawGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int gridSize = 60;
        int wallSize = 5;
        int rectSize = gridSize-2*wallSize;

        Player[] pls = new Player[2];
        Point p1 = new Point(0,5);
        Point p2 = new Point(8,5);
        pls[0] = new Player(new Point(4,0), Color.BLACK, 1, "én", 10);
        pls[1] = new Player(new Point(4,8), Color.WHITE, 1, "te", 10);
//        GameModel gm = new GameModel()

        for (int i=0; i<=8; i++) {
            for (int j=0; j<=8; j++) {
                g.setColor(Color.RED);
                g.fillRect(i*gridSize+wallSize, j*gridSize+wallSize, rectSize, rectSize);
            }
        }
        g.setColor(pls[0].color);
        g.fillOval(pls[0].actualPosition.x*gridSize+wallSize+rectSize/4, pls[0].actualPosition.y*gridSize+wallSize+rectSize/4, 25,25);
        g.setColor(pls[1].color);
        g.fillOval(pls[1].actualPosition.x*gridSize+wallSize+rectSize/4, pls[1].actualPosition.y*gridSize+wallSize+rectSize/4, 25,25);
    }
}
