package Threaded_Server.nEihTChat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTClient implements Runnable{
    private static Socket clientSocket;
    private static PrintStream os;
    private static DataInputStream is;

    private static BufferedReader inputLine;
    private static boolean closed;

    public static void main(String [] args) {
        int port = 1995;
        String ip = "127.0.0.1";

        try {
            clientSocket = new Socket(ip, port);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream((clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(clientSocket != null && os != null && is != null) {
            try {
                new Thread(new nEihTClient()).start();
                while (!closed) {
                    os.println(inputLine.readLine().trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        String response;
        try {
            while ((response = is.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
