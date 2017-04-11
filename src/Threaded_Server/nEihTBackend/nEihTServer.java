package Threaded_Server.nEihTBackend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTServer implements Runnable {

    private int serverPort = 1995;
    private ServerSocket serverSocket = null;
    private boolean stopped = false;
    private Thread runningThread = null;

    public nEihTServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        openSocket();
        System.out.println("Starte Server...");
        while (!(this.stopped)) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();

            } catch (IOException e) {
                if (this.stopped) {
                    System.out.println("Server stopped");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            System.out.println("Client verbunden!");
            new Thread(new Reader(clientSocket)).start();
        }

        System.out.println("Server stopped");
    }


    private void openSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Can't open port " + this.serverPort, e);
        }
    }
}

    class Reader implements Runnable {
    private Socket client = null;

    public Reader(Socket client) { this.client = client; }

        public void run() {
            while (true) {
               try
               {
                   System.out.println("Empfange Nachrichtg");
                   String message = readMessage(client);
                   System.out.println(message);
               } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        }

        private String readMessage(Socket socket) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            char[] buffer = new char[256];
            int digitNumber = br.read(buffer, 0, 256); // blocks until message received
            String message = new String(buffer, 0, digitNumber);
            return message;
        }
    }

