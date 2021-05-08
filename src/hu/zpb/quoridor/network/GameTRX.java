package hu.zpb.quoridor.network;

import java.io.*;
import java.net.*;

public class GameTRX extends Thread{
    public enum GameTRXType{
        SERVER, CLIENT
    }

    private static GameTRX instance;
    private String myPublicIPv4 = "0.0.0.0";
    private GameTRXType type;
    private ServerSocket serverSocket;
    private SocketThread socketThread;

    /* -- CALLBACK START -- */
    public interface NetworkEvent {
        public void networkEventCallback(String data);
    }
    private NetworkEvent networkEvent;

    private GameTRX(){
    }

    public static GameTRX getInstance() {
        if(instance == null) {
            instance = new GameTRX();

            instance.networkEvent = new NetworkEvent() { // hogy biztosan ne legyen nullpointer a callback
                @Override
                public void networkEventCallback(String data) {
                    ;
                }
            };

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
                public void networkEventCallback(String data) {
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
                networkEvent.networkEventCallback(data);
            }
        });
        socketThread.start();
    }

    public void sendGameEvent(String data)
    {
        socketThread.send(data);
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
