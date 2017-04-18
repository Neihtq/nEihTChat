package nEihTChat.nEihTBackEnd;

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
    private static Socket clientSocket;

    private final int max_socket = 8;

    String[] names = new String[max_socket];
    Client[] room = new Client[max_socket];

    public nEihTServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        int c = 0;
        openSocket();
        System.out.println("Start Server...");
        while (!(this.stopped)) {
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("Found client!");

                if(c == max_socket) {
                    System.out.println("Room is full!");
                        toFull(clientSocket);
                        clientSocket.close();
                }
                if (room[c] == null){
                    String name = readMessage(clientSocket);
                    names[c] = name;
                    (room[c] = new Client(clientSocket, room, names, c)).start();
                    System.out.println("Client connected");
                    c++;
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

    private String readMessage(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        char[] buffer = new char[256];
        int digitNumber = br.read(buffer, 0, 256); // blocks until message received
        String message = new String(buffer, 0, digitNumber);
        return message;
    }

    private void openSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Can't open port " + this.serverPort, e);
        }
    }
}

class Client extends Thread {
    private DataInputStream is;
    private PrintStream os = null;
    private Socket client;
    private int maxClients;
    private final Client[] clients;
    private final String[] names;
    private int index;
    private String command;

    public Client(Socket client, Client[] clients, String[] names, int index) {
        this.index = index;
        this.names = names;
        this.client = client;
        this.clients = clients;
        maxClients = clients.length;
    }

        public void run() {
            int maxClients = this.maxClients;
            Client[] clients = this.clients;

               try
               {
                   is = new DataInputStream(client.getInputStream());
                   os = new PrintStream(client.getOutputStream());

                   // Client's name
                   //String name = readMessage(this.client);
                   System.out.println(names[index] + " has entered the room.");


                   for (int i = 0; i < maxClients; i++) {
                       if (clients[i] != null && clients[i] != this) {
                           clients[i].names[this.index] = this.names[this.index];
                       }
                       if (clients[i] != null) {
                           String[] container = {"setlbl", names[index], names[index] + " has joined the room.\n"};
                           //sendMessage(clients[i].client, "setlbl" + names[index] + " has joined the room.\n");
                           sendContainer(clients[i].client, container);

                           try {
                               Thread.sleep(500);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }

                           sendContainer(clients[i].client, names);
                       }
                   }
                   // reading message from client and sending it to other clients in the room
                   while(true) {
                       String[] line = new String[2];
                       line[0] = "sendmsg";
                       line[1] = readMessage(this.client);

                       if (line[1].startsWith(":q")) break;

                       System.out.println(names[index] + ": " + line);

                       // Sending the message to the chat room/everyone
                       for (int i = 0; i < maxClients; i++){
                           if(clients[i] != null && clients[i] != this) {
                               sendContainer(clients[i].client, line);
                               //sendMessage(clients[i].client, line);
                               //clients[i].os.println( name + ": " + line);
                           }
                       }
                   }
                   os.println("You have left.");

                   // Clean up
                   for (int i = 0; i < maxClients; i++){
                       if (clients[i] == this) {
                           clients[i] = null;
                       }
                       clients[i].names[this.index] = null;
                   }

                   is.close();
                   os.close();
                   client.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
        }


    private String readMessage(Socket socket) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            char[] buffer = new char[256];
            int digitNumber = br.read(buffer, 0, 256); // blocks until message received
            String message = new String(buffer, 0, digitNumber);
            return message;
        }

    public void sendMessage(Socket client, String msg) throws IOException{
        PrintWriter printWriter =
                new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        printWriter.print(msg);
        printWriter.flush();
    }

    private void sendContainer(Socket socket, String[] container) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(container);
    }

}

