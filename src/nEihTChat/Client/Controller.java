package nEihTChat.Client;

import Client.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by thien on 14.04.17.
 */
public class Controller{
    @FXML private TextArea message_box;
    @FXML private TextArea chat_protocol;
    @FXML private TextField name_box;
    @FXML private Button connect_bt;
    @FXML private TabPane tabpane;
    @FXML private HBox hbox_bot;

    public static nEihTClient client;

    public Controller () {
    }


    @FXML
    public void connect() throws IOException{
        String name = name_box.getText();
        this.client = new nEihTClient(name);
        (new Thread(new Helper(this.client))).start();

        if (client != null)
        {
            hbox_bot.getChildren().remove(connect_bt);
            hbox_bot.getChildren().remove(name_box);
            Label status = new Label();
            status.setText("Connected as: " + name);
            hbox_bot.getChildren().add(status);
        }
    }

    @FXML
    public void send(){
        try {
            chat_protocol.setText(chat_protocol.getText() + "\n" + "Du: " + message_box.getText());
            this.client.sendMessage(message_box.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
