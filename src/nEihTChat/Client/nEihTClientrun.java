package nEihTChat.Client;

import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by thien on 13.04.17.
 */
public class nEihTClientrun extends Application implements Runnable{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Test");
        Scene scene = new Scene(root, 599, 466);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        (new Thread(new nEihTClientrun())).start();
    }

    public void run(){
        launch();
    }
}
