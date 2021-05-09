package hu.zpb.quoridor.network;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import hu.zpb.quoridor.data.*;

import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import hu.zpb.quoridor.model.GameModel;

/*  TODO **
* memory leak megszüntetése a createServe(), createClient() függvényeknél
* hibakezelés magasabb szintre
 */

public class GameTRX extends Thread{
    public enum GameTRXType{
        SERVER, CLIENT
    }

    private static GameTRX instance;
    private String myPublicIPv4 = "0.0.0.0";
    private GameTRXType type;
    private ServerSocket serverSocket;
    private SocketThread socketThread;
    private Gson gson;

    /* -- CALLBACK START -- */
    public interface NetworkEvent {
        public void networkEventCallback(GameModel data);
        public void playerJoined(Player player);
    }
    private NetworkEvent networkEvent;

    private GameTRX(){
    }

    public static GameTRX getInstance() {
        if(instance == null) {
            instance = new GameTRX();

            instance.networkEvent = new NetworkEvent() { // hogy biztosan ne legyen nullpointer a callback
                @Override
                public void networkEventCallback(GameModel data) {
                    ;
                }

                @Override
                public void playerJoined(Player player) {
                    ;
                }
            };

            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            instance.gson = builder.create();

            // ip lekérdezése
            instance.myPublicIPv4 = instance.getIpV4();
        }

        return instance;
    }

    public String getMyIP() {
        return myPublicIPv4;
    }

    public void setNetworkEvent(NetworkEvent networkEvent) {
        if (networkEvent != null) {
            this.networkEvent = networkEvent;
        }
        else{
            instance.networkEvent = new NetworkEvent() { // hogy biztosan ne legyen nullpointer a callback
                @Override
                public void networkEventCallback(GameModel data) {
                    ;
                }

                @Override
                public void playerJoined(Player player) {
                    ;
                }
            };
        }
    }

    public void createServer()
    {
        this.createServer(51247);
    }

    public void createServer(int port)
    {
        this.type = GameTRXType.SERVER;
        try {
            serverSocket = new ServerSocket(port,1/*, address*/);
            System.out.println("Server is listening on " + serverSocket.toString());
            this.start();  // új szálon figyeljük a csatlakozásokat
        }
        catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void createClient()
    {
        this.createClient("localhost", 52147);
    }

    public void createClient(int port)
    {
        this.createClient("localhost", port);
    }

    public void createClient(String address, int port)
    {
        this.type = GameTRXType.CLIENT;
        try{
            Socket socket = new Socket(address, port);
            this.addSocket(socket);
        }
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        }
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private String getIpV4(){
        URL url = null;
        BufferedReader in = null;
        String ip = "0.0.0.0";
        try {
            url = new URL("http://checkip.amazonaws.com");
            in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            ip = in.readLine();
            return ip;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ip;
    }

    private void addSocket(Socket socket) {
        socketThread = new SocketThread(socket);
        socketThread.setCb(new SocketThread.SocketThreadCb() {
            @Override
            public void onReceived(String data) {
                System.out.println("GameTRX received: " + data);

                String splitData[] = data.split("#", 2);
                if(splitData.length != 2){
                    return; // nem kaptunk elég adatot, kilépünk
                }

                switch(splitData[0])
                {
                    case "GameModel":
                        try {
                            GameModel gm = gson.fromJson(splitData[1], GameModel.class);
                            networkEvent.networkEventCallback(gm);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "JoinPlayer":
                        try {
                            Player p = gson.fromJson(splitData[1], Player.class);
                            networkEvent.playerJoined(p);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    default:
                        break;
                }

            }
        });
        socketThread.start();
    }

    public void sendGameEvent(GameModel data)
    {
        String msg = "GameModel#";
        msg += gson.toJson(data, GameModel.class);
        socketThread.send(msg);
    }

    public void joinPlayer(Player player)
    {
        if(GameTRX.getInstance().type == GameTRXType.CLIENT) {
            String msg = "JoinPlayer#";
            msg += gson.toJson(player, Player.class);
            socketThread.send(msg);
        }
    }

    public void run() {
        // szerver eseteben figyeljuk a csatlakozasokat
//        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                this.addSocket(socket);

            }
            catch (IOException ex){
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
//        }
    }
}
