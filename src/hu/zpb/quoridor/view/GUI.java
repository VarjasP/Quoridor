package hu.zpb.quoridor.view;

import hu.zpb.quoridor.data.GameModelData;
import hu.zpb.quoridor.data.Player;
import hu.zpb.quoridor.data.Wall;
import hu.zpb.quoridor.model.*;
import hu.zpb.quoridor.network.GameTRX;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.Math.round;

public class GUI extends JComponent implements ActionListener, MouseListener {

    private JFrame menuFrame;
    private JPanel clientPanel;
    private JPanel serverPanel;
    private JPanel serverWaitingPanel;
    private JButton bSelectColor;
    private JButton bPlayServer;
    private JButton bPlayClient;
    private JTextField tfYourName;
    private JTextField tfYourIP;
    private JTextField tfSetPort;
    private JTextField tfServerIP;
    private JTextField tfServerPort;

    private String playerName;
    private Color playerColor;
    private String ipAddress;
    private int portNumber;

    private JFrame gameFrame;
    private DrawCanvas gameCanvas;
    private DrawCanvas statusCanvas;

    private JButton bGiveUp;

    private GameModel gm;
    GameTRX gameTRX;

    private Color wallColor;
    private int gridSize = 60;
    private int wallSize = 5;
    private int rectSize = gridSize-2*wallSize;
    private int playerSize = 30;

    public GUI() {

        wallColor = Color.decode("#d78564");
    }

    public void setGm(GameModel gm) {
        this.gm = gm;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public void drawGame() {
        gameFrame = new JFrame(); //creating instance of JFrame
        gameFrame.setSize(900, 600);
        gameCanvas = new DrawCanvas();
        gameCanvas.setBounds(0,0,600,600);
        gameCanvas.setBackground(Color.decode("#7f3327"));
        gameCanvas.addMouseListener(this);
        gameFrame.getContentPane().add(gameCanvas, BorderLayout.CENTER);
        gameFrame.repaint();

        JPanel statusBar = new JPanel();
        statusBar.setBounds(600,0,300,600);
        JLabel text = new JLabel("Mi van itt és hol?");
        text.setBounds(100, 80, 100, 30);
        statusBar.add(text);

        bGiveUp = new JButton("Give up");
        bGiveUp.setBounds(110, 160, 80, 30);
        bGiveUp.addActionListener(this);
        statusBar.add(bGiveUp);

        statusBar.setLayout(null);
        gameFrame.add(statusBar);
        gameFrame.setLayout(null);

        gameFrame.setVisible(true);//making the frame visible
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
    }

    public void refreshGame(){
        gameFrame.repaint();
    }

    public void drawMenuWaitingForClient() {
        //menuFrame.add(clientPanel);
        serverWaitingPanel = new JPanel();
        serverWaitingPanel.setBounds(450, 250, 450, 450);
        serverWaitingPanel.setBackground(Color.LIGHT_GRAY);

        JLabel lWaiting = new JLabel("Waiting for other player to connect...", SwingConstants.CENTER);
        lWaiting.setBounds(100, 30, 250, 50);

        serverWaitingPanel.add(lWaiting);
        serverWaitingPanel.setLayout(null);
        menuFrame.remove(clientPanel);
        menuFrame.add(serverWaitingPanel);
        menuFrame.repaint();
    }

    public void drawMenu() {
        // for connection
        gameTRX = GameTRX.getInstance();

        menuFrame = new JFrame();

        // Panel for player data

        JPanel playerPanel = new JPanel();
        playerPanel.setBounds(0, 0, 900, 250);
        playerPanel.setBackground(Color.LIGHT_GRAY);

        JLabel lQuoridor = new JLabel("Quoridor", SwingConstants.CENTER);
        lQuoridor.setFont(new Font("Algerian", Font.PLAIN, 48));
        lQuoridor.setBounds(300, 30, 300, 50);

        JLabel lYourName = new JLabel("Your name:");
        lYourName.setBounds(350, 120, 100, 30);
        tfYourName = new JTextField("Name");
        tfYourName.setBounds(450, 120, 100, 30);

        JLabel lSelectColor = new JLabel("Select a color:");
        lSelectColor.setBounds(350, 160, 100, 30);
        bSelectColor = new JButton("Choose");
        bSelectColor.setBounds(450, 160, 80, 30);
        bSelectColor.addActionListener(this);

        playerPanel.add(lQuoridor);
        playerPanel.add(lYourName);
        playerPanel.add(tfYourName);
        playerPanel.add(lSelectColor);
        playerPanel.add(bSelectColor);
        playerPanel.setLayout(null);

        // Panel for server options

        serverPanel = new JPanel();
        serverPanel.setBounds(0, 250, 450, 450);

        JLabel lRunServer = new JLabel("Run game as a server", SwingConstants.CENTER);
        lRunServer.setBounds(125, 30, 200, 50);
        JLabel lYourIP = new JLabel("Your IP:");
        lYourIP.setBounds(100, 80, 100, 30);
        tfYourIP = new JTextField();
        tfYourIP.setBounds(200, 80, 150, 30);
        tfYourIP.setText(GameTRX.getInstance().getMyIP());
        tfYourIP.setEditable(false);
        JLabel lSetPort = new JLabel("Set port:");
        lSetPort.setBounds(100, 120, 100, 30);
        tfSetPort = new JTextField("51247");
        tfSetPort.setBounds(200, 120, 150, 30);

        bPlayServer = new JButton("Play (server)");
        bPlayServer.setBounds(125, 180, 200, 50);
        bPlayServer.addActionListener(this);

        serverPanel.add(lRunServer);
        serverPanel.add(lYourIP);
        serverPanel.add(tfYourIP);
        serverPanel.add(lSetPort);
        serverPanel.add(tfSetPort);
        serverPanel.add(bPlayServer);
        serverPanel.setLayout(null);

        // Panel for client options

        clientPanel = new JPanel();
        clientPanel.setBounds(450, 250, 450, 450);

        JLabel lRunClient = new JLabel("Run game as a client", SwingConstants.CENTER);
        lRunClient.setBounds(125, 30, 200, 50);
        JLabel lServerIP = new JLabel("Server IP:");
        lServerIP.setBounds(100, 80, 100, 30);
        tfServerIP = new JTextField("127.0.0.1");
        tfServerIP.setBounds(200, 80, 150, 30);
        JLabel lServerPort = new JLabel("Server port:");
        lServerPort.setBounds(100, 120, 100, 30);
        tfServerPort = new JTextField("51247");
        tfServerPort.setBounds(200, 120, 150, 30);

        bPlayClient = new JButton("Play (client)");
        bPlayClient.setBounds(125, 180, 200, 50);
        bPlayClient.addActionListener(this);

        clientPanel.add(lRunClient);
        clientPanel.add(lServerIP);
        clientPanel.add(tfServerIP);
        clientPanel.add(lServerPort);
        clientPanel.add(tfServerPort);
        clientPanel.add(bPlayClient);
        clientPanel.setLayout(null);

        // Frame settings
        menuFrame.setTitle("Quoridor");
        menuFrame.setSize(900, 600);

        menuFrame.add(playerPanel);
        menuFrame.add(serverPanel);
        menuFrame.add(clientPanel);
        menuFrame.setLayout(null);//using no layout managers

        menuFrame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        menuFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bSelectColor) {
            playerColor = JColorChooser.showDialog(this, "Select a color", Color.BLACK);
            bSelectColor.setBackground(playerColor);
        }
        // Szerver indítása

        if(e.getSource() == bPlayServer) {
            playerName = tfYourName.getText();
            ipAddress = tfYourIP.getText();
            portNumber = Integer.parseInt(tfSetPort.getText());
            if(playerColor == null) { playerColor = Color.BLACK; }
            if(playerName == "Name") { playerName = "Server"; }

            gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
                @Override
                public void networkEventCallback(GameModelData data) {
                    gm.updateGame(data);
                }

                @Override
                public void playerJoined(Player clientPlayer) {
                    gm.addPlayers(new Player(new Point(4,0), playerColor, 0, playerName, 10), clientPlayer);
                    gameTRX.sendGameEvent(gm.getGameModelData());

                    // Frame váltás
                    menuFrame.setVisible(false);
                    drawGame();
                }
            });
            gameTRX.createServer(portNumber);

            // Várakozó frame
            drawMenuWaitingForClient();
        }
        // Kliens indítása

        if(e.getSource() == bPlayClient) {
            playerName = tfYourName.getText();
            ipAddress = tfServerIP.getText();
            portNumber = Integer.parseInt(tfServerPort.getText());
            if(playerColor == null) { playerColor = Color.WHITE; }
            if(playerName == "Name") { playerName = "Client"; }

            menuFrame.setVisible(false);

            gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
                @Override
                public void networkEventCallback(GameModelData data) {
                    gm.updateGame(data);
                }

                @Override
                public void playerJoined(Player player) {
                }
            });
            gameTRX.createClient(ipAddress, portNumber);
            gameTRX.joinPlayer(new Player(new Point(4,8), playerColor, 1, playerName, 10));

            drawGame();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        int xCoord = round(x / gridSize);
        int yCoord = round(y/ gridSize);
        int currWallNum = getUsedLength(gm.getGameModelData().getWallList());
        if (currWallNum < 20) {
            // Bal fal
            if (x % gridSize < wallSize && y % gridSize > wallSize && y % gridSize < wallSize+rectSize) {
                    gm.getGameModelData().getWallList()[currWallNum] = new Wall(new Point(xCoord, yCoord+1), wallColor, 'v');
                }
            // Jobb fal
            if (x % gridSize > wallSize+rectSize && y % gridSize > wallSize && y % gridSize < wallSize+rectSize) {
                gm.getGameModelData().getWallList()[currWallNum] = new Wall(new Point(xCoord+1, yCoord+1), wallColor, 'v');
            }
            // Fenti fal
            if (y % gridSize < wallSize && x % gridSize > wallSize && x % gridSize < wallSize+rectSize) {
                gm.getGameModelData().getWallList()[currWallNum] = new Wall(new Point(xCoord, yCoord), wallColor, 'h');
            }
            // Lenti fal
            if (y % gridSize > wallSize+rectSize && x % gridSize > wallSize && x % gridSize < wallSize+rectSize) {
                gm.getGameModelData().getWallList()[currWallNum] = new Wall(new Point(xCoord, yCoord+1), wallColor, 'h');
            }
        }

        // Mező
        if (x % gridSize > wallSize && x % gridSize < wallSize + rectSize &&
                y % gridSize > wallSize && y % gridSize < wallSize + rectSize) {
            gm.getGameModelData().getPlayerList()[0].setActualPosition(new Point(xCoord, yCoord));
        }
        refreshGame();
        GameTRX.getInstance().sendGameEvent(gm.getGameModelData());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw board
            for (int i=0; i<=8; i++) {
                for (int j=0; j<=8; j++) {
                    g.setColor(Color.decode("#b74d23"));
                    g.fillRect(i*gridSize+wallSize, j*gridSize+wallSize, rectSize, rectSize);
                }
            }

            // Draw players
            for (int i=0; i<2; i++) {
                if (gm.getGameModelData().getPlayerList()[i] != null) {
                    g.setColor(gm.getGameModelData().getPlayerList()[i].getColor());
                    g.fillOval(gm.getGameModelData().getPlayerList()[i].getActualPosition().x*gridSize+wallSize+rectSize/5,
                            gm.getGameModelData().getPlayerList()[i].getActualPosition().y*gridSize+wallSize+rectSize/5, playerSize, playerSize);
                }
            }
            // Draw walls
            g.setColor(wallColor);
            for (int i=0; i < getUsedLength(gm.getGameModelData().getWallList()); i++) {
                Wall w = gm.getGameModelData().getWallList()[i];
                Point gridP = new Point(w.getActualPosition().x, w.getActualPosition().y);
                if (w.getOrientation() == 'h') {
                    g.fillRect((gridP.x-1)*gridSize+wallSize, gridP.y*gridSize-wallSize, 2*gridSize-2*wallSize, 2*wallSize);
                }
                else {
                    g.fillRect((gridP.x)*gridSize-wallSize, (gridP.y-1)*gridSize+wallSize, 2*wallSize, 2*gridSize-2*wallSize);
                }
            }
        }
    }
    private static int getUsedLength(Wall[] arr)
    {
        int count = 0;
        for (Wall w : arr)
        {
            if (w != null)
            {
                count++;
            }
        }
        return count;
    }
}
