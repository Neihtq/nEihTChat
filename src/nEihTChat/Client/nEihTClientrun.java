package nEihTChat.Client;

import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * Created by thien on 13.04.17.
 */
public class nEihTClientrun extends Thread{


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = "test";
        nEihTClient neiht = new nEihTClient(name);
        new Thread(new Controller()).start();
        neiht.test();
    }



}
