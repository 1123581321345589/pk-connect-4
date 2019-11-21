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
private ArrayList<Thread> threads;

public final int WIN_SIZE = 480;

public final String style = "-fx-background: white;";

@Override
public void start(Stage stage) throws Exception{
    
    //Creates TextField for Username Input with button for submission
	threads = new ArrayList<Thread>();
	stage.setTitle("PKConnect4");
	stage.setScene(welcomeScreen(stage));
	stage.show();

}

//Sets up Chatroom UI layout
private Scene initChatBox(Client client){

	GridPane rootPane = new GridPane();
	rootPane.setPadding(new Insets(20));
	rootPane.setAlignment(Pos.CENTER);
	rootPane.setHgap(10);
	rootPane.setVgap(10);

	/*
	 * Make the Chat's listView and set it's source to the Client's chatLog
	 * ArrayList
	 */
	ListView<String> chatListView = new ListView<String>();
	chatListView.setItems(client.chatLog);

	/*
	 * Make the chat text box and set it's action to send a message to the
	 * server
	 */
	TextField chatTextField = new TextField();
	chatTextField.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			client.writeToServer(chatTextField.getText());
			chatTextField.clear();
		}
	});

	/* Add the components to the root pane */
	rootPane.add(chatListView, 0, 0);
	rootPane.add(chatTextField, 0, 1);

	/* Make and return the scene */
	return new Scene(rootPane, 400, 400);
    
}

//Returns Login Screen
public Scene welcomeScreen(Stage stage){
    
	GridPane rootPane = new GridPane();
	rootPane.setPadding(new Insets(20));
	rootPane.setVgap(10);
	rootPane.setHgap(10);
	rootPane.setAlignment(Pos.CENTER);

	/* Make the text fields and set properties */
	TextField nameField = new TextField();
	TextField hostNameField = new TextField();
	TextField portNumberField = new TextField();

	/* Make the labels and set properties */
	Label nameLabel = new Label("User Name ");
	Label hostNameLabel = new Label("Host Name");
	Label portNumberLabel = new Label("Port Number");
	Label errorLabel = new Label();
	Label title = new Label("PK-Connect4");
	
	/* Make the button and its handler */
	Button submitClientInfoButton = new Button("Done");
	submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent Event) {
			// TODO Auto-generated method stub
			/* Instantiate the client class and start it's thread */
			Client client;
			try {
				
				client = new Client(hostNameField.getText(), Integer
						.parseInt(portNumberField.getText()), nameField
						.getText());
				Thread clientThread = new Thread(client);
				clientThread.setDaemon(true);
				clientThread.start();
				threads.add(clientThread);
				compute(nameField.getText());
				
				/* Change the scene of the primaryStage */
				stage.close();
				stage.setScene(initChatBox(client));
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
			catch (nameException e) {
				errorLabel.setTextFill(Color.RED);
				errorLabel.setText("Invalid name, try again");
				
				
			}
			
		}
	});

	/*
	 * Add the components to the root pane arguments are (Node, Column
	 * Number, Row Number)
	 */

	rootPane.add(nameField, 0, 0);
	rootPane.add(nameLabel, 1, 0);
	rootPane.add(hostNameField, 0, 1);
	rootPane.add(hostNameLabel, 1, 1);
	rootPane.add(portNumberField, 0, 2);
	rootPane.add(portNumberLabel, 1, 2);
	rootPane.add(submitClientInfoButton, 0, 3, 2, 1);
	rootPane.add(errorLabel, 0, 4);
	
	
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
                     + "Usernames should be between 8 and 16 characters long.");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }

	
	

public static void compute(String name) throws nameException {
    if (name.length() < 8 || name.length() > 16) {
    	
       throw new nameException(name);
 }

}




public static void main(String[] args) {
    launch(args); 
	}

}
