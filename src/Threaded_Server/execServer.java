package Threaded_Server;

/**
 * Created by thien on 08.04.17.
 */
public class execServer {
    public static void main (String[] args) {
        Server2 server = new Server2(9000);
        new Thread(server).start();

        try{
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}
