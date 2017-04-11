package Threaded_Client;

/**
 * Created by ThiEn on 29.03.2017.
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Client {
    /*
    - Build instance of 'FutureTask', give it an instance of 'ClientHandler' as parameter, that implements the Interface 'Callable'
    (similar to 'Runnable')
    - Give 'FutureTask' a new thread and start it. 'call'-method from interface 'callable' of ClientHandler is executed
    - complete communication with the server is running. 'call'-method gives the result from server to 'FutureTask' where it's available
    in the main program. Any time and any place can be checked if the result has returned
     */

    String values;

    public static void main(String[] args) {
        /*if (args.length == 0) {
            System.out.println("Datum-Parameter fehlen !");
            System.exit(1);
        }*/

        StringBuffer sb = new StringBuffer();
        // wrap all arguments, separated by whitespaces
        sb.append(" TEST ");

        String values = sb.toString().trim();
        try {
            // irrelevant thread for illustrating the concurrency of execution
            Thread t1 = new Thread(new otherThread());
            t1.start();
            Client cl = new Client(values);
            cl.worker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client(String values) {

        this.values = values;
    }

    void worker() throws Exception {
        System.out.println("worker: " + Thread.currentThread());
        // class that implements 'Callable'
        ClientHandler ch = new ClientHandler(values);

        boolean cont = false;
        do { // 2 runs
            int j = 0;
            //call-method 'ch' from ClientHandler executed with 'FutureTask' asynchronous
            // result can be fetched from 'FutureTask'
            FutureTask<String> ft = new FutureTask<String>(ch);
            Thread tft = new Thread(ft);
            tft.start();

            // check if Thread has done its work
            while (!ft.isDone()) {
                j++; // counts change of Thread
                Thread.yield(); // other Threads
            }

            System.out.println("not isDone: " + j);
            System.out.println(ft.get()); // return result
            if (values.compareTo("Exit") == 0)
                break;
            cont = !cont;
            if (cont){
                // 2nd Call for Client-request, modify recent value
                ch.setValue (values.substring(0, values.length()-4) + "1813");
            }
        } while(cont);
    }
}
// Contains call-method for FutureTask (= run from Thread)
class ClientHandler implements Callable<String> {
    String ip = "127.0.0.1"; //localhost
    int port = 1995;
    String values;

    public ClientHandler(String values) {
        this.values = values;
    }

    void setValue(String s) {
        values = s;
    }

    public String call() throws Exception { // run the service
        System.out.println("ClientHandler:" + Thread.currentThread());
        // slows artificially the execution of request, for marking the change between Threads
        Thread.sleep(2000);
        return RequestServer(values);
    }

    // open Socket, send request, receive result, close Socket
    String RequestServer(String par) throws IOException {
        String rec_message;
        String toSend_message;

        Socket socket = new Socket(ip, port); //connects to Server
        toSend_message = par;
        //send request
        writeMessage(socket, toSend_message);
        //receive result
        rec_message = readMessage(socket);
        socket.close();
        return rec_message;
    }

    void writeMessage(Socket socket, String message) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()
                        )
                );
        printWriter.print(message);
        printWriter.flush();
    }

    String readMessage(Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

        char[] buffer = new char[100];
        // blocks until message received
        int numb_digits = bufferedReader.read(buffer, 0, 100);
        String message = new String(buffer, 0, numb_digits);
        return message;
    }
}

class otherThread implements Runnable {
    public void run() {
        System.out.println(" OtherThread:" + Thread.currentThread());
        int n = 0;
        int w = 25000000;
        // using enough CPU-Time
        for (int i = 1; i <= 10; i++)
            for (int j = 1; j <= w; j++){
            if (j% w == 0)
                System.out.println("   n =" + (++n));
            }
    }
}