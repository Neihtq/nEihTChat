package Server; /**
 * Created by ThiEn on 27.03.2017.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        String message = readMessage(client);

        System.out.println("The message is: ");
        System.out.println(message);
        writeMessage(client, message);
    }

    Socket waitforSignIn(ServerSocket serverSocket) throws IOException {
        System.out.println("I am listening now for any clients connecting to me (server)!");

        Socket socket = serverSocket.accept(); // blocks, until client has signed in
        return socket;
    }

    String readMessage(Socket socket) throws IOException {
        System.out.println("I will read its message now...");

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
