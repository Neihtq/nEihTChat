package nEihTChat.Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    @FXML private TextArea member_list;

    private String name;

    public static nEihTClient client;

    public Controller () {
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
