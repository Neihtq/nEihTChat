package nEihTChat.Client;

import Client.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by thien on 14.04.17.
 */
public class Controller extends Application implements Runnable {
    @FXML
    private TextArea message_box;


    public static nEihTClient client;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
        primaryStage.setTitle("Test");
        Scene scene = new Scene(root, 599, 466);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void run(){
        launch();
    }

    @FXML
    public void send(ActionEvent e){
        try {
            client.sendMessage(message_box.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
