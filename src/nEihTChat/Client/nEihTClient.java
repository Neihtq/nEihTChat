package nEihTChat.Client;

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
    private String name;
    private static BufferedReader inputLine;
    private static boolean closed = false;
    private static int port = 1995;
    private static String ip = "127.0.0.1";

    public nEihTClient (String name){
        this.name = name;
    }

    public void test() {

        try {
            clientSocket = new Socket(ip, port);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream((clientSocket.getInputStream()));
            sendName(clientSocket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(clientSocket != null && os != null && is != null) {
            try {
                new Thread(this).start(); // Thread that reads from the server
                while (!closed) {
                    os.println(inputLine.readLine().trim());
                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendName(Socket socket) throws IOException{
        PrintWriter printWriter =
                new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(this.name);
        printWriter.flush();
    }

    public void run() {
        String response;
        try {
            while ((response = is.readLine()) != null) {
                System.out.println(response);
                if (response.indexOf("You have left.") != -1) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
