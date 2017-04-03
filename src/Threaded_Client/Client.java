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
        if (args.length == 0) {
            System.out.println("Datum-Parameter fehlen !");
            System.exit(1);
        }

        StringBuffer sb = new StringBuffer();
        // wrap all arguments, separated by whitespaces
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i] + ' ');
        }

        String values = sb.toString().trim();
        try {
            // irrelevant thread for illustrating the concurrency of execution
            Thread t1 = new Thread(new OtherThread());
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
        ClientHandler ch = new ClientHandler(value);
        boolean continue = false;


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
            continue = !continue;
            if (continue){
                // 2nd Call for Client-request, modify recent value
                ch.setValue (values.substring(0, values.length()-4) + "1813");
            }
        } while(continue);
    }
}
// Contains call-method for FutureTask (= run from Thread)
class ClientHandler implements Callable<String> {
    String ip = "127.0.0.1"; //localhost
    int port = 3141;
    String values;

    public ClientHandler(String werte) {
        this.values = values;
    }

    voit setValue(String s) {
        values = s;
    }

    public String call() throws Exception { // run the service
        System.out.println("ClientHandler:" + Thread.currentThread());
        // slows artificially the execution of request, for marking the change between Threads
        Thread.sleep(2000);
        return RequestServer(values);
    }

}
