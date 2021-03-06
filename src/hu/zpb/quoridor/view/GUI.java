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

    private GameModel gm;
    GameTRX gameTRX;
    private boolean gameJustStarted;

    private JFrame gameFrame;
    private JPanel gameBackgroundPanel;
    private JPanel statusBar;
    private DrawCanvas gameCanvas;

    private JFrame menuFrame;
    private JPanel clientPanel;
    private JPanel serverPanel;
    private JPanel serverWaitingPanel;
    private JDialog dError;

    private JButton bSelectColor;
    private JButton bPlayServer;
    private JButton bPlayClient;
    private JButton bGiveUp;
    private JButton bRematch;
    private JTextField tfYourName;
    private JTextField tfYourIP;
    private JTextField tfSetPort;
    private JTextField tfServerIP;
    private JTextField tfServerPort;
    private JLabel lCurrentPlayer;
    private JLabel lServerPlayerWalls;
    private JLabel lClientPlayerWalls;
    private JLabel lFireWorks;

    private String playerName;
    private Color playerColor;
    private String ipAddress;
    private int portNumber;

    private Color wallColor;
    private int gridSize = 60;
    private int wallSize = 5;
    private int rectSize = gridSize-2*wallSize;
    private int playerSize = 30;

    public GUI() {

        wallColor = Color.decode("#d78564");
        gameJustStarted = true;
        dError = new JDialog(menuFrame, "Error Message", true);
        dError.setSize(500, 120);
        bRematch = new JButton("Rematch");
        bRematch.setBounds(100, 370, 100, 40);
        bRematch.addActionListener(this);
        bRematch.setVisible(false);
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

        Icon imgIcon = new ImageIcon(getClass().getResource("fireworks.gif"));
        lFireWorks = new JLabel(imgIcon, SwingConstants.CENTER);
        lFireWorks.setBounds(100, 0, 800, 600);
        lFireWorks.setVisible(false);
        gameFrame.add(lFireWorks);

        // J??t??kt??r

        gameBackgroundPanel = new JPanel();
        gameBackgroundPanel.setBackground(Color.decode("#7f3327"));
        gameBackgroundPanel.setBounds(0,0,600,600);

        gameCanvas = new DrawCanvas();
        gameCanvas.setBounds(30,10,540,540);
        gameCanvas.setBackground(Color.decode("#7f3327"));
        gameCanvas.addMouseListener(this);

        gameBackgroundPanel.add(gameCanvas);
        gameBackgroundPanel.setLayout(null);
        gameFrame.getContentPane().add(gameBackgroundPanel, BorderLayout.CENTER);
        gameFrame.repaint();

        JPanel statusBar = new JPanel();
        statusBar.setBounds(600,0,300,600);

        // Szerver j??t??kos adatai

        JLabel lServerPlayerName = new JLabel(gm.getGameModelData().getPlayerList()[0].getName(), SwingConstants.CENTER);
        lServerPlayerName.setBounds(0, 50, 300, 30);
        lServerPlayerName.setFont(new Font("Arial", Font.PLAIN, 24));
        statusBar.add(lServerPlayerName);

        lServerPlayerWalls = new JLabel("Available walls: " +
                Integer.toString(gm.getGameModelData().getPlayerList()[0].getAvailableWalls()), SwingConstants.CENTER);
        lServerPlayerWalls.setBounds(75, 100, 150, 30);
        lServerPlayerWalls.setFont(new Font("Arial", Font.PLAIN, 16));
        statusBar.add(lServerPlayerWalls);

        // Felad??s gomb

        bGiveUp = new JButton("Give up");
        bGiveUp.setBounds(100, 280, 100, 40);
        bGiveUp.addActionListener(this);
        statusBar.add(bGiveUp);
        statusBar.add(bRematch);

        // Aktu??lis j??t??kos
        lCurrentPlayer = new JLabel();
        // Ha szerver j??t??kos j??n
        if (gm.getGameModelData().getCurPlayer().getID() == 0) {
            lCurrentPlayer.setText("Your turn: " + gm.getGameModelData().getPlayerList()[0].getName());
            lCurrentPlayer.setBounds(0, 200, 300, 30);
        // Ha kliens j??t??kos j??n
        } else if (gm.getGameModelData().getCurPlayer().getID() == 1) {
            lCurrentPlayer.setText("Your turn: " + gm.getGameModelData().getPlayerList()[1].getName());
            lCurrentPlayer.setBounds(0, 370, 300, 30);
        }
        lCurrentPlayer.setFont(new Font("Arial", Font.BOLD, 20));
        lCurrentPlayer.setForeground(Color.decode("#7f3327"));
        lCurrentPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        statusBar.add(lCurrentPlayer);

        // Kliens j??t??kos adatai

        JLabel lClientPlayerName = new JLabel(gm.getGameModelData().getPlayerList()[1].getName(), SwingConstants.CENTER);
        lClientPlayerName.setBounds(0, 450, 300, 30);
        lClientPlayerName.setFont(new Font("Arial", Font.PLAIN, 24));
        statusBar.add(lClientPlayerName);

        lClientPlayerWalls = new JLabel("Available walls: " +
                Integer.toString(gm.getGameModelData().getPlayerList()[1].getAvailableWalls()), SwingConstants.CENTER);
        lClientPlayerWalls.setBounds(75, 500, 150, 30);
        lClientPlayerWalls.setFont(new Font("Arial", Font.PLAIN, 16));
        statusBar.add(lClientPlayerWalls);

        statusBar.setLayout(null);
        gameFrame.add(statusBar);
        gameFrame.setLayout(null);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
    }

    public void refreshGame(){

        // St??tuszb??r friss??t??se

        // Ha v??ge a j??t??knak
        if (gm.getGameModelData().getGameFinished() == true) {
            int wID = gm.getGameModelData().getWinnerID();
            bRematch.setVisible(true);
            bGiveUp.setVisible(false);
            lCurrentPlayer.setText("Winner: " + gm.getGameModelData().getPlayerList()[wID].getName());
            lCurrentPlayer.setBounds(0, 200, 300, 30);
            if (wID == gm.getMyPlayerID()) {
                lFireWorks.setVisible(true);
            }
        } else {
            // Ha szerver j??t??kos j??n
            if (gm.getGameModelData().getCurPlayer().getID() == 0) {
                lCurrentPlayer.setText("Your turn: " + gm.getGameModelData().getPlayerList()[0].getName());
                lCurrentPlayer.setBounds(0, 200, 300, 30);
                // Ha kliens j??t??kos j??n
            } else if (gm.getGameModelData().getCurPlayer().getID() == 1) {
                lCurrentPlayer.setText("Your turn: " + gm.getGameModelData().getPlayerList()[1].getName());
                lCurrentPlayer.setBounds(0, 370, 300, 30);
            }
            bGiveUp.setVisible(true);
            bRematch.setVisible(false);
            lFireWorks.setVisible(false);
        }
        lServerPlayerWalls.setText("Available walls: " +
                Integer.toString(gm.getGameModelData().getPlayerList()[0].getAvailableWalls()));
        lClientPlayerWalls.setText("Available walls: " +
                Integer.toString(gm.getGameModelData().getPlayerList()[1].getAvailableWalls()));

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

        menuFrame.setLayout(null);
        menuFrame.setResizable(false);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        menuFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bSelectColor) {
            playerColor = JColorChooser.showDialog(this, "Select a color", Color.BLACK);
            bSelectColor.setBackground(playerColor);
        }
        // Szerver ind??t??sa

        if (e.getSource() == bPlayServer) {
            playerName = tfYourName.getText();
            ipAddress = tfYourIP.getText();

            try {
                portNumber = Integer.parseInt(tfSetPort.getText());
            }
            catch (NumberFormatException ex) {
                ex.printStackTrace();
                dError.setLayout(new FlowLayout());
                dError.add(new JLabel("Tisztelt Koll??ga!"));
                dError.add(new JLabel("Ez a j??t??k ??rett, felel??ss??gteljes m??rn??k??knek k??sz??lt."));
                dError.add(new JLabel("K??rem, ha egy adatbeviteli mez?? sz??mot v??r, legyen sz??ves sz??mot megadni,"));
                dError.add(new JLabel("megfelel?? form??tumban, nem pedig sz??veget."));
                dError.add(new JLabel("Egy??ttm??k??d??s??t k??sz??nj??k!"));
                dError.setLocationRelativeTo(null);
                dError.setVisible(true);
            }


            if (playerColor == null) { playerColor = Color.BLACK; }
            if (playerName.equals("Name")) { playerName = "Server"; }

            gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
                @Override
                public void networkEventCallback(GameModelData data) {
                    gm.updateGame(data);
                }

                @Override
                public void playerJoined(Player clientPlayer) {
                    gm.addPlayers(new Player(new Point(4,0), playerColor, 0, playerName, 10), clientPlayer);
                    gm.setMyPlayerID(0);
                    gameTRX.sendGameEvent(gm.getGameModelData());

                    // Frame v??lt??s
                    menuFrame.setVisible(false);
                    drawGame();
                }
            });
            gameTRX.createServer(portNumber);

            // V??rakoz?? frame
            drawMenuWaitingForClient();
        }

        // Kliens ind??t??sa

        if(e.getSource() == bPlayClient) {
            playerName = tfYourName.getText();
            ipAddress = tfServerIP.getText();
            portNumber = Integer.parseInt(tfServerPort.getText());
            if(playerColor == null) { playerColor = Color.WHITE; }
            if(playerName.equals("Name")) { playerName = "Client"; }

            gameTRX.setNetworkEvent(new GameTRX.NetworkEvent() {
                @Override
                public void networkEventCallback(GameModelData data) {
                    if (gameJustStarted == true) {
                        gameJustStarted = false;
                        menuFrame.setVisible(false);
                        gm.setGameModelData(data);
                        drawGame();
                    } else {
                        gm.updateGame(data);
                    }
                }

                @Override
                public void playerJoined(Player player) {
                }
            });
            gameTRX.createClient(ipAddress, portNumber);
            gameTRX.joinPlayer(new Player(new Point(4,8), playerColor, 1, playerName, 10));
            gm.setMyPlayerID(1);
        }

        // Felad??s

        if (e.getSource() == bGiveUp) {
            if (gm.getMyPlayerID() == 0) {
                gm.getGameModelData().setWinnerID(1);
            } else {
                gm.getGameModelData().setWinnerID(0);
            }
            gm.getGameModelData().setGameFinished(true);
            GameTRX.getInstance().sendGameEvent(gm.getGameModelData());
            refreshGame();
        }

        // Rematch

        if (e.getSource() == bRematch) {
            int wID = gm.getGameModelData().getWinnerID();
            int lID = ((wID + 1) % 2);
            gm.getGameModelData().resetGame();
            gm.getGameModelData().setCurPlayer(gm.getGameModelData().getPlayerList()[lID]);
            GameTRX.getInstance().sendGameEvent(gm.getGameModelData());
            refreshGame();
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
        if (gm.getMyPlayerID() == gm.getGameModelData().getCurPlayer().getID() && !gm.getGameModelData().getGameFinished()) {
            int x=e.getX();
            int y=e.getY();
            int xCoord = round(x / gridSize);
            int yCoord = round(y/ gridSize);

            Wall w = null;

            // Bal fal
            if (x % gridSize < wallSize && y % gridSize > wallSize && y % gridSize < wallSize+rectSize) {
                w = new Wall(new Point(xCoord, yCoord+1), 'v');
            }
            // Jobb fal
            if (x % gridSize > wallSize+rectSize && y % gridSize > wallSize && y % gridSize < wallSize+rectSize) {
                w = new Wall(new Point(xCoord+1, yCoord+1), 'v');
            }
            // Fenti fal
            if (y % gridSize < wallSize && x % gridSize > wallSize && x % gridSize < wallSize+rectSize) {
                w = new Wall(new Point(xCoord, yCoord), 'h');
            }
            // Lenti fal
            if (y % gridSize > wallSize+rectSize && x % gridSize > wallSize && x % gridSize < wallSize+rectSize) {
                w = new Wall(new Point(xCoord, yCoord+1), 'h');
            }

            if(w != null){
                if(gm.placeWall(w)){
                    refreshGame();
                    GameTRX.getInstance().sendGameEvent(gm.getGameModelData());
                }
            }

            // Mez??
            if (x % gridSize > wallSize && x % gridSize < wallSize + rectSize &&
                    y % gridSize > wallSize && y % gridSize < wallSize + rectSize) {
                if(gm.movePlayer(new Point(xCoord, yCoord)))
                {
                    refreshGame();
                    GameTRX.getInstance().sendGameEvent(gm.getGameModelData());
                }
            }
        }
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
            for (int i=0; i < gm.getGameModelData().getWallCount(); i++) {
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
}
