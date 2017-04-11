package Threaded_Server.nEihTBackend;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTrun {
    public static void main (String[] args) {
        boolean hasStopped = false;
        nEihTServer nEihT = new nEihTServer(1995);

        new Thread(nEihT).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
