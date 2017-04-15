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
public class Controller{
    @FXML private TextArea message_box;


    public static nEihTClient client;

    public Controller () {
    }


    @FXML
    public void connect(){
        String name = "test";
        this.client = new nEihTClient(name);
        (new Thread(new Helper(this.client))).start();
    }

    @FXML
    public void send(){
        try {
            this.client.sendMessage(message_box.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
