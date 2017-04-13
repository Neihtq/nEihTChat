package Threaded_Server.nEihTChat.Client;

import Threaded_Server.nEihTChat.Client.nEihTClient;

import java.util.Scanner;

/**
 * Created by thien on 13.04.17.
 */
public class nEihTClientrun {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        nEihTClient neiht = new nEihTClient(name);
        neiht.test();
    }
}
