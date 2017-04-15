package nEihTChat.Client;

/**
 * Created by ThiEn on 15.04.2017.
 */
public class Helper implements Runnable{

    private nEihTClient client;

    public Helper (nEihTClient client) {
        this.client = client;
    }

    public void run()
    {
        client.test();
    }
}
