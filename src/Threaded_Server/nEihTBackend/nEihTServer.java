package Threaded_Server.nEihTBackend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTServer implements Runnable {

    private int serverPort;
    private ServerSocket serverSocket;
    private boolean stopped = false;

    private final int max_socket = 8;

    Socket[] room = new Socket[max_socket];

    public nEihTServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        int c = 0;
        openSocket();
        System.out.println("Start Server...");
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
            System.out.println("Found client!");
            if(c == max_socket) {
                System.out.println("Room is full!");
                try {
                    writeMessage(clientSocket);
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                room[c] = clientSocket;
                System.out.println("Client connected");
                new Thread(new Reader(clientSocket, c)).start();
                c++;
            }
        }

        System.out.println("Server stopped");
    }

    private void writeMessage(Socket socket) throws IOException {
        OutputStreamWriter a = new OutputStreamWriter(socket.getOutputStream());
        PrintWriter b = new PrintWriter(a);

        PrintWriter printWriter = new PrintWriter(b);
        printWriter.print("Server too busy, try again later");
        printWriter.flush();
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

    private Socket client;
    private int index;

    public Reader(Socket client, int index) { this.client = client; this.index = index; }

        public void run() {
            while (true) {
               try
               {
                   System.out.print("Client " + this.index + " wrote: ");
                   String message = readMessage(client);
                   System.out.print(message + "\n");
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

