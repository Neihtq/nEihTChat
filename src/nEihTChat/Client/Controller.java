package nEihTChat.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by thien on 14.04.17.
 */
public class Controller implements Initializable{
    @FXML private TextArea message_box;
    @FXML private TextArea chat_protocol;
    @FXML private TextField name_box;
    @FXML private Button connect_bt;
    @FXML private HBox hbox_bot;
    @FXML private Label name_label;
    @FXML private TextArea member_list;
    @FXML private ImageView profilepic;

    private String name;

    public static nEihTClient client;

    public Controller () {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/holifestival.jpg");
        try {
            Image image = new Image(file.toURI().toString());
            profilepic.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void connect() throws IOException{
        this.name = name_box.getText();
        this.client = new nEihTClient(this.name, this.chat_protocol, this.member_list);
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
            String msg_prot = this.name + " : " + message_box.getText() + "\n";
            chat_protocol.appendText(msg_prot);
            this.client.sendMessage(msg_prot);
            message_box.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
