package Threaded_Server;

/**
 * Created by ThiEn on 28.03.2017.
 */

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.net.NetworkServer;

import java.security.spec.ECField;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.*;

public class Server {
    /* - any client-request is handled by a thread from created Thread-Pool
       - Server-Socket can be closed with Strg+C or by client with value 'Exit'
     */
    public static void main(String[] args) throws IOException {
        final ExecutorService pool;
        final ServerSocket serverSocket;
        int port = 3141;
        String var = "C";
        String zusatz;
        if (args.length > 0) var = args[0].toUpperCase();
        if (var == "C") {
            // Delivers a thread-pool, where new Threads can be added.
            // Free Threads have higher priority, they're are rather used
            pool = Executors.newCachedThreadPool();
            zusatz = "CachedThreadPool";
        } else {
            int poolSize = 4;
            // Delivers a thread-pool with maximal poolSize
            pool = Executors.newFixedThreadPool(poolSize);
            zusatz = "poolsize="+poolSize;
        }

        serverSocket = new ServerSocket(port);
        //Thread for handling the client-server.communication, Thread-parameter gives us the Runnable-Interface (run-method for t1).
        Thread t1 = new Thread(new NetworkService(pool, serverSocket));
        System.out.println("Start NetworkService(Multiplikation), " + zusatz + ", Thread: "+Thread.currentThread());
        //Start run-method from NetworkService: wait fro client-request
        t1.start();

        //reacts on Strg+C, Thread(Parameter) may not be started
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run() {
                        System.out.println("Strg+C, pool.shutdown");
                        pool.shutdown(); //no new requests
                        try {
                            // wait up to 4 sec for closing all requests
                            pool.awaitTermination(4L, TimeUnit.SECONDS);
                            if (!serverSocket.isClosed()) {
                                System.out.println("ServerSocket close");
                                serverSocket.close();
                            }
                        } catch (IOException e) {}
                        catch (InterruptedException ei) {}
                    }
                }
        );
    }

}

//Thread / Runnable for accepting client requests
class NetworkService implements Runnable { // or "extends Thread"
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    public NetworkService(ExecutorService pool, ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.pool = pool;
    }

    public void run() { // run the service
        try {
            // Infinite Loop: waiting for client requests
            // Cancel by Strg+C or Client-Request 'Exit'
            // -> ends ServerSocket, leading to an IOException -> ends run-method with previous work
            // of finally-clause
            while (true) {
                /*
                Accepting client request (.accept()). Then ExecutorService pool gives a thread, whose run method
                is done by run method of Handler-Instance.
                 Handler takes following parameters: ServerSocket and Socket of requesting Client
                 */
                Socket cs = serverSocket.accept(); // waiting for client request

                // start Handler-Thread for executing client request
                pool.execute(new Handler(serverSocket, cs));
            }
        } catch (IOException ex) {
            System.out.println("--- Interrupt NetworkService-run");
        } finally {
            System.out.println("--- End NetworkService (pool.shutdown)");
            pool.shutdown(); // no accept of new requests
            try {
                //wait up to 4 seconds for closing all requests
                pool.awaitTermination(4L, TimeUnit.SECONDS);
                if (!serverSocket.isClosed()) {
                    System.out.println("--- Ende NetworkService:SerserSocket close");
                    serverSocket.close();
                }
            } catch (IOException e) {
            } catch (InterruptedException ei) {
            }
        }
    }
 }

 // Thread / Runnable for executing client requests
 class Handler implements Runnable { // or 'extends Thread'
     private final Socket client;
     private final ServerSocket serverSocket;

     Handler(ServerSocket serverSocket, Socket client) { // Server/CLient-Socket
         this.client = client;
         this.serverSocket = serverSocket;
     }

     public void run() {
         StringBuffer sb = new StringBuffer();
         PrintWriter out = null;

         try {
             // read and service request on client
             System.out.println("running service, " + Thread.currentThread());
             out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             char[] buffer = new char[100];
             int digitnumber = bufferedReader.read(buffer, 0, 100); // blocked until message received
             String message = new String(buffer, 0, digitnumber);
             String[] values = message.split("\\s"); // Whitespace
             if (values[0].compareTo("Exit") == 0) {
                 out.println("Server ended");
                 if (!serverSocket.isClosed()) {
                     System.out.println("--- End Handler: ServerSocket close");
                     try {
                         serverSocket.close();
                     } catch (IOException e) {
                     }
                 }
             } else { // normal client request
                 for (int i = 0; i < values.length; i++) {
                     String rt = getWday(values[i]); // Find day
                     sb.append(rt + "\n");
                 }
                 sb.deleteCharAt(sb.length() - 1);
             }
         } catch (IOException e) {
             System.out.println("IOException, Handler-riun");
         } finally {
             out.println(sb); // Return output to client
             if (!client.isClosed()) {
                 System.out.println("****** Handler: Client close");
                 try {
                     client.close();
                 } catch (IOException e) {
                 }
             }
         }
     } // End run

     String getWday(String s) { //  Date with weekday
         SimpleDateFormat sdf = new SimpleDateFormat(("EEEE, dd.MM.yyyy"));
         String res = "";
         try {
             // Parameter type is Date
             res = sdf.format(DateFormat.getDateInstance().parse(s));
         } catch (ParseException p) {
         }
         return res;
     }
 }