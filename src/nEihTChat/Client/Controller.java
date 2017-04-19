package nEihTChat.Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by thien on 14.04.17.
 */
public class Controller {
    @FXML private TextArea message_box;
    @FXML private TextArea chat_protocol;
    @FXML private TextField name_box;
    @FXML private Button connect_bt;
    @FXML private HBox hbox_bot;
    @FXML private Label name_label;
    @FXML private TextArea member_list;
    @FXML private ImageView profilepic;
    @FXML private TabPane tabPane;

    private String name;

    public static nEihTClient client;

    public Controller () {
    }


 /*   @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/holifestival.jpg");
        try {
            Image image = new Image(file.toURI().toString());
            profilepic.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    } */

    @FXML
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !message_box.getText().equals(" ")) {
                send();
                message_box.setText("");
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
    public void changePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                String imagepath = selectedFile.toURI().toURL().toString();
                Image image = new Image(imagepath);
                profilepic.setImage(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void send(){
        try {
            String msg_prot = this.name + " : " + message_box.getText() + "\n";
            chat_protocol.appendText(msg_prot);
            this.client.sendContainer(this.client.so)
            //this.client.sendMessage(msg_prot);
            message_box.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
