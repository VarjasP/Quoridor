package hu.zpb.quoridor.network;

import java.io.*;
import java.net.*;

public class GameTRX extends Thread{
    public enum GameTRXType{
        SERVER, CLIENT
    }

    GameTRXType type;
    private ServerSocket serverSocket;
    private SocketThread socketThread;

    /* -- CALLBACK START -- */
    public interface NetworkEvent {
        public void networkEventCallback(String data);
    }
    private NetworkEvent networkEvent;

    public GameTRX(GameTRXType type) {
        init();
        this.type = type;

        switch(type)
        {
            case SERVER:
                try {

                    serverSocket = new ServerSocket(9090);
                    System.out.println("Server is listening on " + serverSocket.toString());
                    this.start();

                } catch (IOException ex) {

                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();

                }
                break;
            case CLIENT:
                try{

                    Socket socket = new Socket("localhost", 9090);
                    this.addSocket(socket);

                } catch (UnknownHostException ex) {

                    System.out.println("Server not found: " + ex.getMessage());

                } catch (IOException ex) {

                    System.out.println("I/O error: " + ex.getMessage());

                }
                break;
            default:
                break;
        }
    }

    public void setNetworkEvent(NetworkEvent networkEvent) {
        this.networkEvent = networkEvent;
    }

    // dummy fuggveny, hogy biztosan ne legyen nullpointer a callback
    private void init(){
        this.networkEvent = new NetworkEvent() {
            @Override
            public void networkEventCallback(String data) {
                ;
            }
        };
    }

    private void addSocket(Socket socket) {
        socketThread = new SocketThread(socket);
        socketThread.setCb(new SocketThread.SocketThreadCb() {
            @Override
            public void onReceived(String data) {
                System.out.println("Received: " + data);
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

            } catch (IOException ex){

                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();

            }
//        }
    }
}
