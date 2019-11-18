/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkconnect4;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author wardt4
 */
 import java.util.ArrayList;
  import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class PKConnect4 extends Application{

private TabPane mainPage = new TabPane();
private VBox root = new VBox();
private Scene scene;
private final Button send = new Button("Send");
private final VBox chatBox = new VBox(5);
private final TextField chatField = new TextField();
private List<Label> messages = new ArrayList<>();
private ScrollPane container = new ScrollPane();
private int index = 0;
private GameUser user = new GameUser();
private VBox btnBox = new VBox();

public final int WIN_SIZE = 480;

public final String style = "-fx-background: white;";

@Override
public void start(Stage stage) throws Exception{
    
    //Creates TextField for Username Input with button for submission
    TextField userField = new TextField();
    userField.setMaxWidth(WIN_SIZE*0.75);
    Button ent = new Button("ENTER");
    //
    
    //Sets button event
    ent.setOnAction(e -> {
        try{
            if(userField.getText().length() >= 8 
            && userField.getText().length() <= 16){
                //Server UN verification goes here
                user.name = userField.getText(); 
                System.out.println(user.name);
                initChatBox();
                scene = new Scene(root,WIN_SIZE,WIN_SIZE*1.5);
                stage.setScene(scene);
            }
            else{tooShort();}
        }
        catch(Exception ex){tooShort();}
    });
    
    send.setOnAction(evt->{
        if(chatField.getText().length() > 0){
        messages.add(new Label(user.name + ": " + chatField.getText()));
        messages.get(index).setAlignment(Pos.CENTER_LEFT);
        chatField.clear();
        chatBox.getChildren().add(messages.get(index));
        index++;
        }
    });

    //Set scene to Login Screen
    scene = welcomeScreen(userField, ent);
    stage.setScene(scene);
    stage.setTitle("PK Connect-4");
    stage.setResizable(false);
    stage.show();

}

//Sets up Chatroom UI layout
private void initChatBox(){

    container.setPrefSize(WIN_SIZE*0.8, WIN_SIZE*1.2);
    container.setContent(chatBox);
    container.setStyle(style);
    chatBox.setBackground(Background.EMPTY);
    chatBox.setStyle(style);
                
    btnBox.getChildren().addAll(container, chatField);
    btnBox.setAlignment(Pos.CENTER);
                
    root.getChildren().addAll(btnBox,send);
    root.setSpacing(10);
    root.setAlignment(Pos.CENTER);
    
}

//Returns Login Screen
public Scene welcomeScreen(TextField userField, Button ent){
    
    VBox welcomeMain = new VBox();
    Label[] welLabs = new Label[]{new Label("Welcome to Connect-4"), 
                                  new Label("Please create a username...")};
    welLabs[0].setTextFill(Color.RED);
    welLabs[0].setFont(Font.font("Impact", FontWeight.BOLD, 32));
    welLabs[1].setFont(Font.font("Impact", 16));
    welcomeMain.getChildren().addAll(welLabs[0], welLabs[1], userField, ent);
    welcomeMain.setSpacing(20);
    welcomeMain.setAlignment(Pos.CENTER);
    Scene s = new Scene(welcomeMain, WIN_SIZE, WIN_SIZE*0.5);

    return s;
}

// Error Message for Invalid Username input
void tooShort(){
        Alert quit = new Alert(Alert.AlertType.ERROR, "", 
                               ButtonType.OK);
        quit.setGraphic(null);
        quit.setTitle("INVALID USERNAME");
        quit.setHeaderText("Please enter a valid username.\n"
                     + "Usernames should be between 8 and 16 characters long.");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }

public static void main(String[] args) {
    launch(args); 
}

}

