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
    @FXML private HBox hbox_bot;
    @FXML private Label name_label;

    private String name;

    public static nEihTClient client;

    public Controller () {
    }


    @FXML
    public void connect() throws IOException{
        name = name_box.getText();
        this.client = new nEihTClient(name);
        (new Thread(new Helper(this.client))).start();

        if (client != null)
        {
            hbox_bot.getChildren().remove(connect_bt);
            hbox_bot.getChildren().remove(name_box);
            Label status = new Label();
            status.setText("Connected as: " + name);
            hbox_bot.getChildren().add(status);
            name_label.setText(name);
        }
    }

    @FXML
    public void send(){
        try {
            String msg_prot = "\n" + this.name + " : " + message_box.getText();
            chat_protocol.appendText(msg_prot);
            this.client.sendMessage(message_box.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
