package Threaded_Server.nEihTChat.nEihTBackEnd;

import Threaded_Server.nEihTChat.nEihTBackEnd.nEihTServer;

/**
 * Created by ThiEn on 11.04.2017.
 */
public class nEihTrun {
    public static void main (String[] args) {

        nEihTServer nEihT = new nEihTServer(1995);

        new Thread(nEihT).start();

    }
}
