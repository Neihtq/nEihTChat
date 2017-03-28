package Server; /**
 * Created by ThiEn on 27.03.2017.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main (String[] args) {
        Server server = new Server();
           try {
               server.test();
           } catch (IOException e) {
               e.printStackTrace();
           }
    }

    void test() throws IOException {
        int port = 11111;

            ServerSocket serverSocket = new ServerSocket(port);

            Socket client = waitforSignIn(serverSocket);
        while(true){
            String message = readMessage(client);

            System.out.println(message);

            Scanner sc = new Scanner(System.in);
            String reply = sc.nextLine();
            writeMessage(client, reply);
        }
    }

    Socket waitforSignIn(ServerSocket serverSocket) throws IOException {
            Socket socket = serverSocket.accept(); // blocks, until client has signed in
            System.out.println("A Client has logged in!");
            return socket;

    }

    String readMessage(Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
        char[] buffer = new char [256];
        int digitNumber = bufferedReader.read(buffer, 0, 200); // blocks until message received
        String message = new String(buffer, 0, digitNumber);
        return message;
    }

    void writeMessage(Socket socket, String message) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }
}
