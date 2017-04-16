package nEihTChat.Client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.xml.soap.Text;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTClient implements Runnable{
    @FXML private TextArea chat_protocol;

    private static Socket clientSocket;
    private static PrintStream os;
    private static DataInputStream is;
    private String name;
    private static BufferedReader inputLine;
    private static boolean closed = false;
    private static int port = 1995;
    private static String ip = "127.0.0.1";

    public nEihTClient (String name, TextArea chat_protocol)
    {
        this.chat_protocol = chat_protocol;
        this.name = name;
        try {
            clientSocket = new Socket(ip, port);
    } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public void test() {

        try {
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream((clientSocket.getInputStream()));
            sendName(this.clientSocket);
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

    public void sendMessage(String msg) throws IOException{
        PrintWriter printWriter =
                new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
        printWriter.print(msg);
        printWriter.flush();
    }

    void sendName(Socket socket) throws IOException{
        PrintWriter printWriter =
                new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(this.name);
        printWriter.flush();
    }

    private String readMessage(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        char[] buffer = new char[256];
        int digitNumber = br.read(buffer, 0, 256); // blocks until message received
        String message = new String(buffer, 0, digitNumber);
        return message;
    }

    public void run() {

        try {
            /*while ((response = is.readLine()) != null) {
                System.out.println(response);*/
            String response;
            while(true)
            {
                 response = readMessage(this.clientSocket);

                System.out.println(response);
                chat_protocol.appendText(response);
                if (response.indexOf("You have left.") != -1) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
