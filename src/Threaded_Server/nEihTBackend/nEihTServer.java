package Threaded_Server.nEihTBackend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

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
                System.out.println("Found client!");

                if(c == max_socket) {
                    System.out.println("Room is full!");
                        toFull(clientSocket);
                        clientSocket.close();
                } else {
                    room[c] = clientSocket;
                    System.out.println("Client connected");
                    new Thread(new Client(clientSocket, c, room)).start();
                    c++;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                if (this.stopped) {
                    System.out.println("Server stopped");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

        }

        System.out.println("Server stopped");
    }

    private void toFull(Socket socket) throws IOException {
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

    class Client implements Runnable {
    private DataInputStream is;
    private PrintStream os;
    private Socket client;
    private int maxClients;
    private final Socket[] clients;
    private final int index;

    public Client(Socket client, int index, Socket[] clients) {
        this.index = index;
        this.client = client;
        this.clients = clients;
    }

        public void run() {
            int maxClients = this.maxClients;
            Socket[] clients = this.clients;

               try
               {
                   is = new DataInputStream(client.getInputStream());
                   os = new PrintStream(client.getOutputStream());
                   while(true) {
                       String message = readMessage(client);
                       System.out.println("Client " + this.index + " wrote: " + message + "\n");
                       for (int i = 0; i < 8; i++){
                           if(clients[i] != null) {
                               writeMessage(clients[i], message);
                           } else { break;}
                       }
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
        }

        private void writeMessage(Socket socket, String message) throws IOException {
            OutputStreamWriter a = new OutputStreamWriter(socket.getOutputStream());
            PrintWriter b = new PrintWriter(a);

            PrintWriter printWriter = new PrintWriter(b);
            printWriter.print(message);
            printWriter.flush();
        }

        private String readMessage(Socket socket) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            char[] buffer = new char[256];
            int digitNumber = br.read(buffer, 0, 256); // blocks until message received
            String message = new String(buffer, 0, digitNumber);
            return message;
        }
    }

