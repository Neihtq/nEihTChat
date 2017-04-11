package Client;

import java.io.IOException;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class execClient {
    public static void main(String[] args) {
        Client client = new Client("test");
        try {
            client.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
