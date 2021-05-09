package hu.zpb.quoridor.view;

import hu.zpb.quoridor.model.*;
import hu.zpb.quoridor.network.GameTRX;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ActionListener {

    private JFrame menuFrame;
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

    private GameModel gm;



    public void setGm(GameModel gm) {
        this.gm = gm;
    }

    public void drawGame() {
        JFrame f = new JFrame();//creating instance of JFrame

        f.setSize(900, 600);//400 width and 500 height
        var panel = this;
        panel.setBackground(Color.WHITE);
        f.getContentPane().add(panel, BorderLayout.CENTER);
        f.repaint();

        f.setVisible(true);//making the frame visible
        f.setResizable(false);
        f.setLocationRelativeTo(null);

    }

    public void drawMenu() {

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

        JPanel serverPanel = new JPanel();
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

        JPanel clientPanel = new JPanel();
        clientPanel.setBounds(450, 250, 450, 450);

        JLabel lRunClient = new JLabel("Run game as a client", SwingConstants.CENTER);
        lRunClient.setBounds(125, 30, 200, 50);
        JLabel lServerIP = new JLabel("Server IP:");
        lServerIP.setBounds(100, 80, 100, 30);
        tfServerIP = new JTextField(GameTRX.getInstance().getMyIP());
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
        if(e.getSource() == bPlayServer) {
            playerName = tfYourName.getText();
            ipAddress = tfYourIP.getText();
            portNumber = Integer.parseInt(tfSetPort.getText());
            menuFrame.setVisible(false);
            drawGame();
        }
        if(e.getSource() == bPlayClient) {
            playerName = tfYourName.getText();
            ipAddress = tfServerIP.getText();
            portNumber = Integer.parseInt(tfServerPort.getText());
            menuFrame.setVisible(false);
            drawGame();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int gridSize = 60;
        int wallSize = 5;
        int rectSize = gridSize-2*wallSize;

        for (int i=0; i<=8; i++) {
            for (int j=0; j<=8; j++) {
                g.setColor(Color.RED);
                g.fillRect(i*gridSize+wallSize, j*gridSize+wallSize, rectSize, rectSize);
            }
        }
        for (int i=0; i<2; i++) {
            if (gm.getGameModelData().getPlayerList()[i] != null) {
                g.setColor(gm.getGameModelData().getPlayerList()[i].getColor());
                g.fillOval(gm.getGameModelData().getPlayerList()[i].getActualPosition().x*gridSize+wallSize+rectSize/4,
                        gm.getGameModelData().getPlayerList()[i].getActualPosition().y*gridSize+wallSize+rectSize/4, 25,25);
            }
        }
    }
}
