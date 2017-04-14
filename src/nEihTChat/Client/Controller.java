package nEihTChat.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nEihTChat.Client.nEihTClient;

import java.io.IOException;

/**
 * Created by thien on 14.04.17.
 */
public class Controller extends Application implements Runnable {
    public Controller(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Test");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void run(){
        launch();
    }

    public static void send(){
        try {
            (new nEihTClient(null)).sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
