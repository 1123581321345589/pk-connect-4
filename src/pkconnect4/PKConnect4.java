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

import java.io.IOException;
import java.net.ConnectException;
/**
 *
 * @author wardt4
 */
 import java.util.ArrayList;
  import java.util.List;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class PKConnect4 extends Application{
	
	/* author trevor ward */

private final TabPane lobbyTabs = new TabPane();
private ArrayList<Thread> threads;
public final int CELL_SIZE = 100;

private String conIp = "10.220.49.118"; // server ip connection
private int conPort = 9999; // server port number
public boolean taken;
public final String style = "-fx-background: white;";
public Stage stage;

@Override
public void start(Stage stage) throws Exception{
    
    //Creates TextField for Username Input with button for submission
	threads = new ArrayList<Thread>();
	stage.setTitle("PKConnect4");
	stage.setScene(welcomeScreen(stage));
        stage.setResizable(false);
	stage.show();

}
public void setNameTaken(boolean pls) {
	
	this.taken = pls;

}
public boolean getNameTaken() {
	return taken;
}

//Sets up Chatroom UI layout
public Scene initChatBox(Client client){
        //Create TabPane and Tabs for Main Lobby
        Tab tab1 = new Tab("CHAT");
        Tab tab2 = new Tab("GAMES");
        tab1.setClosable(false);
        tab2.setClosable(false);
        lobbyTabs.getTabs().addAll(tab1, tab2);
        //
        
        //Set up layout for Chat Lobby
	GridPane rootPane = new GridPane();
	rootPane.setPadding(new Insets(20));
	rootPane.setAlignment(Pos.CENTER);
	rootPane.setHgap(10);
	rootPane.setVgap(10);
        
        /*
	 * Make the Chat's listView and set it's source to the Client's chatLog
	 * ArrayList
	 */
	ListView<String> chatListView = new ListView<>();
	chatListView.setItems(client.chatLog);
	
	//Code for Chat Lobby Outgoing Messages
	TextField chatTextField = new TextField();
	chatTextField.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			client.writeToServer("c","0", client.name +": "+ chatTextField.getText());
                        
			chatTextField.clear();
		}
	});

	/* Add the components to the root pane */
	rootPane.add(chatListView, 0, 0);
	rootPane.add(chatTextField, 0, 1);
        tab1.setContent(rootPane);
        
         //Set up layout for Host Lobby
        GridPane hostPane = new GridPane();
	hostPane.setPadding(new Insets(20));
	hostPane.setAlignment(Pos.CENTER);
	hostPane.setHgap(10);
	hostPane.setVgap(10);
        
        /*
	 * Make the Hosting Game listView and set it's source to the Client's hostList
	 * ArrayList
	 */
	ListView<Label> hostListView = new ListView<>();
	hostListView.setItems(client.hostList);
        
        //Code for Host Game Button
        Button gameBtn = new Button("HOST");
        gameBtn.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
                    if(!client.waiting){
                        client.writeToServer("h", client.name, "0");
                        client.waiting = true;
                    }
                    else
                        hosting();
		}
	});
        
        /* Add the components to the hostPane */
	hostPane.add(hostListView, 0, 0);
	hostPane.add(gameBtn, 0, 1);
        tab2.setContent(hostPane);

	/* Make and return the scene */
	return new Scene(lobbyTabs, 400, 400);
    
}

//Returns Login Screen
public Scene welcomeScreen(Stage stage){
    
	VBox rootPane = new VBox();
	rootPane.setPadding(new Insets(20));
	rootPane.setSpacing(10);
	rootPane.setAlignment(Pos.CENTER);

	
	Label errorLabel = new Label();
	
	
	/* Make the button and its handler */
	Button submitClientInfoButton = new Button("SUBMIT");
	submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent Event) {
			// TODO Auto-generated method stub
			/* Instantiate the client class and start it's thread */
			Client client;
			try {
				
				client = new Client(conIp, conPort);
				
                                
                                
				/* Change the scene of the primaryStage */
				stage.close();
				stage.setScene(userNameScreen(stage, client));
                                stage.setTitle("PKChat - User Screen");
				stage.show();
			}
	
			catch(ConnectException e){
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("Invalid host name, try again");
			}
			catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("Invalid port number, try again");
			}
			
			
		}
	});

	/*
	 * Add the components to the root pane arguments are (Node, Column
	 * Number, Row Number)
	 */

	rootPane.getChildren().addAll(submitClientInfoButton,errorLabel);
	
	
	/* Make the Scene and return it */
	return new Scene(rootPane, 400, 400);
	
}

public Scene userNameScreen(Stage stage, Client client) {
	
	this.stage = stage;
	
	
	VBox rootPane = new VBox();
	rootPane.setPadding(new Insets(20));
	rootPane.setSpacing(10);
	rootPane.setAlignment(Pos.CENTER);

	/* Make the text fields and set properties */
	TextField nameField = new TextField();
	
	/* Make the labels and set properties */
	Label nameLabel = new Label("Please choose a User Name");
	
	Label errorLabel = new Label();
	Button submitClientInfoButton = new Button("SUBMIT");
	submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent Event) {
			// TODO Auto-generated method stub
			/* Instantiate the client class and start it's thread */
			
			try {
				
                
				compute(nameField.getText());
				
				client.addName(nameField.getText());
				
               
				
				Thread clientThread = new Thread(client);
				clientThread.setDaemon(true);
				clientThread.start();
				threads.add(clientThread);
				
				client.writeToServer("r", conIp, "0");
			
				
					stage.close();
					
					stage.setScene(initChatBox(client));
					         stage.setTitle("PKChat - " + client.name);
					
					stage.show();
					
				
				
			
				
			}
	
			
			catch (nameException e) {
				tooShort();
				
				
			}
			
		}
	});

	/*
	 * Add the components to the root pane arguments are (Node, Column
	 * Number, Row Number)
	 */

	rootPane.getChildren().addAll(nameLabel,nameField,submitClientInfoButton,errorLabel);
	
	
	/* Make the Scene and return it */
	return new Scene(rootPane, 400, 400);
	
	
}








// Error Message for Invalid Username input
void tooShort(){
        Alert quit = new Alert(Alert.AlertType.ERROR, "", 
                               ButtonType.OK);
        quit.setGraphic(null);
        quit.setTitle("INVALID USERNAME");
        quit.setHeaderText("Please enter a valid username.\n"
                     + "Usernames should be between 8 and 16 characters long with no semi-colons.");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }

//public TabPane
	
	

public static void compute(String name) throws nameException {
    if (name.length() < 8 || name.length() > 16) {
    	
       throw new nameException(name);
 }
    for(int i = 0; i < name.length(); i++) {
    	
    	if(name.charAt(i) == ';') {
    		
    	throw new nameException(name);
    		
    	}
    	
    	
    }

}








    //Alert message informing user game is over
    void hosting(){
        Alert quit = new Alert(Alert.AlertType.ERROR, "", 
                               ButtonType.OK);
        quit.setGraphic(null);
        quit.setTitle("Already Hosting...");
        quit.setHeaderText("You can only host one game at a time!");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }
    
    
    
   


public static void main(String[] args) {
    launch(args); 
	}

}
