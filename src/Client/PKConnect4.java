/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
/**
 *
 * @author wardt4
 */
 import java.util.ArrayList;
  import java.util.List;

import javax.swing.JButton;

import Comms.ClientShared;
import Comms.CommonSoundClass;
import Comms.MultiChatConstants;
import Comms.Playback;
import Comms.Queue;
import Comms.Recorder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class PKConnect4 extends Application{
	
	/* author trevor ward */

private final TabPane lobbyTabs = new TabPane();
private ArrayList<Thread> threads;
public final int CELL_SIZE = 100;


private Socket socket;
private String conIp = "10.220.50.158"; // server ip connection
private int conPort = 9999; // server port number

public final String style = "-fx-background: white;";
public boolean debug = false; // standard debugging boolean variable

private InputStream in;
private OutputStream out;
// voice chat dava variables

public Comms.CommonSoundClass cs;
Comms.Recorder r;
Comms.Playback playback;

boolean imRunning = true;

final int offset = 1;//1;
int peacespersecond = 8; // number of peaces per second
boolean recording = false;

int currentsize = 0; // sent size for this second in bytes
int avgsize = 0; // everadge sent size
int avgcounter = 0; // counter varible for send ++

int recievecounter = 0; // counter variable for recieve ++
int currentsecondSound = 0; // how much data i recieved this second in bytes

boolean connected = false;
boolean canrecord = false;
// compression variables

int compression = 0; // level of compression
int cmpressto = Comms.ClientShared.bytesize / 2; // sets the compression to half of the byte size
int leftover = 0; // how much is left over to spair out of cmpressto value
boolean useleftover = true;
int MaximumCompression = 9; //maximum amout of compression we want

byte reduceBy = 1;  // reduces the sample rate by N used with reduceSamplerate
byte increaseBy = 1;// increases the sample rate by N retrieveSampleRate


//min amd max of transmission used for stats
int minval = Comms.ClientShared.bytesize * peacespersecond;
int maxval = 0;
int recievedMinval = Comms.ClientShared.bytesize * peacespersecond;
int recievedMaxval = 0;

int packetnumber = 9999;
double multiplyer = 3.0;

// threads variables
boolean IAmMute = false;

int size = 0;
ProcessRecordedSoundThread mt = null;
byte[] breaker = Comms.MultiChatConstants.BREAKER.getBytes();



@Override
public void start(Stage stage) throws Exception{
    
    //Creates TextField for Username Input with button for submission
	threads = new ArrayList<Thread>();
	stage.setTitle("PKConnect4");
	stage.setScene(welcomeScreen(stage));
        stage.setResizable(false);
	stage.show();

}

//Sets up Chatroom UI layout
private Scene initChatBox(Client client){
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
	
	
	Button voipBtn = new Button("TALK");
	voipBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			 if( recording ){
		            Recorder.stopRecording();
		            voipBtn.setText("Talk");
		            
		        }else{
		            if( canrecord && IAmMute != true ){
		                Recorder.startRecording();
		                voipBtn.setText("Stop Talk");
		                
		            }else{
		                recording = !recording;
		            }
		        }
		        recording = !recording;
		}
		
		
	});
	
    
	
	
	
	
	
        
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
	rootPane.add(voipBtn, 0, 2);
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

	/* Make the text fields and set properties */
	TextField nameField = new TextField();
	
	/* Make the labels and set properties */
	Label nameLabel = new Label("Please choose a User Name");
	
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
				
                                compute(nameField.getText());
				client = new Client(conIp, conPort, nameField
						.getText());
				Thread clientThread = new Thread(client);
				clientThread.setDaemon(true);
				clientThread.start();
				threads.add(clientThread);
                                client.name = nameField.getText();
                                
				/* Change the scene of the primaryStage */
				stage.close();
				stage.setScene(initChatBox(client));
                                stage.setTitle("PK-Chat - User: "+ client.name);
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
                     + "Usernames should be between 8 and 16 characters long.");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }

//public TabPane
	

private void startRecording(){
    if(cs == null || r == null) {
        if( cs == null){
            cs = new Comms.CommonSoundClass();
        }
        r = new Comms.Recorder( cs);
    } else {
        r.startRecording();
    }

    if( mt == null ){
        mt = new ProcessRecordedSoundThread();
        mt.start();
    }
}


private class ProcessRecordedSoundThread extends Thread{
    public void run() {
        // Runs while org.multichat.CommonSoundClass is not null
        while ( true ) {
            while ( cs != null ) {
                byte[] b = (byte[]) cs.readbyte();


                if (socket != null) {
                    try {
                        if (b.length < 10) {
                            out.write(b);
                            out.flush();
                            // Clearing out the buffer???

                            while (cs.vec.size() > 0) {
                                b = (byte[]) cs.readbyte();
                            }
                        } else {
                            

                            avgcounter++; // add one to the avgcounter

                            byte[] localbyte = encode(b);


                            out.write(localbyte, 0, getDataSize());
                            out.flush();

                            try {
                                synchronized (this) {

                                }
                            } catch (Exception mexp) {
                                mexp.printStackTrace();
                            }

                            //calculate avg bytes a second

                            avgsize += getDataSize();
                            currentsize += getDataSize();

                            if (avgcounter % peacespersecond == 0) {
                                minval = Math.min(currentsize, minval);
                                maxval = Math.max(currentsize, maxval);

                                

                                currentsize = 0;
                                if (avgcounter > 500) {
                                    avgcounter = 0;
                                    avgsize = 0;
                                }
                            }
                        }

                        out.write(breaker);
                        out.flush();

                    } catch (java.net.UnknownHostException uhkx) {
                        System.out.println("unknown host");
                    } catch (java.io.IOException iox) {
                        if (canrecord && IAmMute != true) {
                           
                        }
                    }
                }
            }
            try {
                wait(100);
            } catch (InterruptedException ie) {
            }
        }
    }
    
    public byte[] reduceSamplerate( byte[] b , int LowerSampleRateBy ){
    	if( LowerSampleRateBy <= 1 || LowerSampleRateBy >= 127 ){
    		return b;
    	}

		byte[] returnThis = new byte[ClientShared.bytesize - (ClientShared.bytesize / LowerSampleRateBy )];

        int j = 0;

        if( b == null ){
			setDataSize(0);
			return b;
		}
        for(  int i = 0; j < returnThis.length && i < b.length ; i++ ){
    		returnThis[j] = (byte)(b[i] );
    		if( i % LowerSampleRateBy == 0 ){
    			i++;
    		}
    		j++;
        }

        return returnThis;
    }
    
    public byte[] retrieveSampleRate( byte[] b, int LowerSampleRateBy ){
    	if( LowerSampleRateBy <= 1 || LowerSampleRateBy >= 127 ){
    		return b;
    	}

    	byte returnThis[] = new byte[ClientShared.bytesize];
    	int j = 0;
		for( int i = 0; i < returnThis.length; i++ ){
			returnThis[i] = (byte)(b[j] );
			if( i % LowerSampleRateBy == 0 ){
				i++;
				if( i < returnThis.length ){
					returnThis[i] = returnThis[i-1];
				}
			}
			j++;
        }
    	return returnThis;
    }
    
    public byte[] compress( byte[] b ){
        

        if( b == null ){
			setDataSize(0);
			return b;
		}

        byte[] returnThis = new byte[b.length];
        int j = 0;
        for( int i = 0; b != null && i < b.length; i++){
            int same = areTheySame(b, i);
            if( (same > 3 && (b[i] >= 10 || ( b[i] > -6 && b[i] < 0 )  ) ) || ( same > 4 ) ){
                returnThis[j] = (byte)59;// keeps track of it by this number
                j++;
                returnThis[j] = b[i];// the actual value
                j++;
                returnThis[j] = (byte)same;// how many are repeating
                i+=(same - 1);
            }else{
                if( b[i] < (byte)59 && b[i] > -59 ){ // gets rid of numbers higher then 59
                    returnThis[j] = b[i];
                }else{
                    if( b[i] > -59 ){
                        returnThis[j] = (byte)((int)b[i] / 2.2);
                    }else{
                        returnThis[j] = (byte)((int)b[i] / 2.2);
                    }
                }
            }
            j++;
        }
        setDataSize( j );
        return returnThis;
    }
    
    private int areTheySame( byte[] b, int start ){
        for(int i=start+1; i < b.length; i++ ){

            if( getCompression() >= 1 && ( b[start] == b[i] + (byte)1 || b[start] == b[i] - (byte)1  ) ){
            }else if( getCompression() >= 2 && (b[start] == ( b[i] + (byte)2 ) || b[start] ==  b[i] - (byte)2 ) ){
            }else if( getCompression() >= 3 && (b[start] == ( b[i] + (byte)3 ) || b[start] ==  b[i] - (byte)3 ) ){
            }else if( getCompression() >= 4 && (b[start] == ( b[i] + (byte)4 ) || b[start] ==  b[i] - (byte)4 ) ){
            }else if( getCompression() >= 5 && (b[start] == ( b[i] + (byte)5 ) || b[start] ==  b[i] - (byte)5 ) ){
            }else if( getCompression() >= 6 && (b[start] == ( b[i] + (byte)6 ) || b[start] ==  b[i] - (byte)6 ) ){
            }else if( getCompression() >= 7 && (b[start] == ( b[i] + (byte)7 ) || b[start] ==  b[i] - (byte)7 ) ){
            }else if( getCompression() >= 8 && (b[start] == ( b[i] + (byte)8 ) || b[start] ==  b[i] - (byte)8 ) ){
            }else if( getCompression() >= 9 && (b[start] == ( b[i] + (byte)9 ) || b[start] ==  b[i] - (byte)9 ) ){
            }else if( b[start] != b[i] ){
                return (i - start );
            }

            if( i - start >= 59 ){
                return (i - start );
            }
        }
        return b.length - start;
    }
    
    


    
    private byte[] encode(byte[] b) {
        int localcompression = 0;
        byte[] localbyte = new byte[ClientShared.bytesize]; // create a byte
        do{
            setCompression(localcompression); //sets compression to N
            for( int i = 0; i < localbyte.length && i < b.length; i++ ){ // copy the byte from the b
                localbyte[i] = b[i];
            }

            setDataSize( ClientShared.bytesize );
            localbyte = reduceSamplerate( localbyte, reduceBy  ); // reduce samples by 2
            localbyte = compress( localbyte ); // compression
            localbyte = merge( localbyte ); // second layer compression
            localcompression++;
        } while ((getDataSize() - leftover) > getCompressto()
                && getCompression() < MaximumCompression);

        if( debug == true ){
            printbyte(localbyte);
        }

        if( useleftover == false ){
            leftover = 0;
        }

        if( getCompressto() - getDataSize() > 0 ){
            leftover += (getCompressto() - getDataSize()); // sets the left over value which is reset every second
        } else {
            leftover -= (getDataSize() - getCompressto());
            if( leftover < 0){
                leftover = 0;
            }
        }

        if( avgcounter % peacespersecond == 0 ){
            leftover = 0;
        }

        byte[] b2 = null;
        if (localbyte == null) { // if i'm not talking and using hands free mode
            localbyte = ("NT|".getBytes());
            setDataSize(localbyte.length);
        } else {
            b2 = new byte[getDataSize() + 1];
            for (int i = 0; i < getDataSize(); i++) {
                b2[i] = localbyte[i];
            }

            b2[getDataSize()] = reduceBy;
            localbyte = b2;
            setDataSize(getDataSize() + 1);
        }
        return localbyte;
    }
}

public byte[] merge( byte[] b ){
	if( b == null ){
		setDataSize(0);
		return null;
	}

    byte[] returnThis = new byte[b.length];
    int j = 0;

    for( int i = 0; b != null && i < getDataSize() && j < returnThis.length; i++){
        if( b.length > i+1  ){
            if( b[i] < (byte)10 && b[i+1] < (byte)10 && b[i] >= (byte)0 && b[i+1] >= (byte)0  ){
                if (b[i] > (byte) 5) {                                                            // 6 - 9
                    returnThis[j] = (byte) (((int) b[i] * 10) + (int) b[i + 1]);
                } else if (b[i] < (byte) 2) {                                                    //0 - 1
                    returnThis[j] = (byte) (100 + ((int) b[i] * 10) + (int) b[i + 1]);
                } else if (b[i] == (byte) 2 && b[i + 1] <= 7) {                                        //2 and 0 - 7
                    returnThis[j] = (byte) (100 + ((int) b[i] * 10) + (int) b[i + 1]);
                } else if (b[i] == (byte) 3) {                                                    //3 -100
                    returnThis[j] = (byte) (-1 * (100 + (int) b[i + 1]));
                } else if (b[i] == (byte) 4) {                                                    //4 -111
                    returnThis[j] = (byte) (-1 * (110 + (int) b[i + 1]));
                } else
                if (b[i] == (byte) 5 && b[i + 1] <= 7) {                                        //5 and 0 - 7 -122
                    returnThis[j] = (byte) (-1 * (120 + (int) b[i + 1]));
                } else {
                    //if cant compress
                    returnThis[j] = b[i];
                    i--;
                }
                i++; //increment the i to skip next record
            }else if( b[i] <= (byte)-6 && b[i+1] <= (byte)0 && b[i] >= (byte)-9 && b[i+1] >= (byte)-9 ){
                returnThis[j] = (byte)( ((int)b[i]  * 10) + (int)b[i+1]);
                i++; //increment the i to skip next record
            }else{
                returnThis[j] = b[i];
            }
        }
        j++;
    }
    setDataSize( j );
    return returnThis;

}


public void printbyte( byte[] b ){
    for( int i = 0; i < b.length; i++ ){
        System.out.print( b[i] + " " );
    }
    System.out.println("\n\n");
    debug = true;
}


public void compute(String name) throws nameException {
    if (name.length() < 8 || name.length() > 16) {
    	
       throw new nameException(name);
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
    
    public int getDataSize(){
        return this.size;
    }

    /**
     *Sets the data size called form merge and compress functions
     */
    public void setDataSize( int size ){
        this.size = size;
    }
    
    public void setCompression( int number){
        compression = number;
    }

    /**
     *Called by Compress's areTheySame() function when it has to deside how much compression to use
     */
    public int getCompression(){
        return compression;
    }
    
    public void setCompressto( int number ){
        cmpressto = number;
    }

    /**
     * Gets the maxumum compression that the program will try before reaching the desired file size
     */
    public int getCompressto() {
        return cmpressto;
    }
    
   
public static void main(String[] args) {
    launch(args); 
	}

}
