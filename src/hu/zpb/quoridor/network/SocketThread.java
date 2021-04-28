package hu.zpb.quoridor.network;

import java.io.*;
import java.net.*;

public class SocketThread extends Thread{
    private Socket socket;
    private BufferedReader rx;
    private PrintWriter tx;

    /* -- CALLBACK  -- */
    public interface SocketThreadCb {
        public void onReceived(String data) ;
    }
    private SocketThreadCb cb;

    public SocketThread(Socket socket) {
        this.socket = socket;
        try {
            rx = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            tx = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Socket IO exception: " + ex.getMessage());
            ex.printStackTrace();
        }

        this.cb = new SocketThreadCb() {
            @Override
            public void onReceived(String data) {
                ;
            }
        };
    }

    public void setCb(SocketThreadCb cb) {
        this.cb = cb;
    }

    public void send(String data)
    {
        tx.println(data);
    }

    // rx
    public void run() {
        try {
            String text;

            while(true) {
                text = rx.readLine();
                cb.onReceived(text);
            }

        } catch (IOException ex) {
            System.out.println("Socket IO exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
