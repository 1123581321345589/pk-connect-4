/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkconnect4;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author wardt4
 */
public class RunningGame extends VBox{
    
    public String user, p1, p2, tp;
    public final int CELL_SIZE = 100;
    
    VBox main = new VBox();
    int[][] slots = new int[7][6];
    Rectangle[][] squares = new Rectangle[7][6];
    Circle[][] redPieces = new Circle[7][7];
    Circle[][] bluePieces = new Circle[7][7];
    
    
    
    RunningGame(String u, String u1, String u2){
        user = u;
        p1 = u1;
        p2 = u2;
        tp = p1;
        this.getChildren().add(boardSetup(slots, squares, redPieces, bluePieces));
    }
    
        //Runs through possible connect-4 scenarios using 2-dimensional integer array
    public int gameCheck(int[][] s){
        
        int combo = 0;
        
        
        //Check rows, blue
        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 7; x++){
                if(s[x][y] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        
        
        //Check colums, blue
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 6; y++){
                if(s[x][y] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        
        //Check diagonals, blue
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 6; x++){
                if(s[x][x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 5; x++){
                if(s[x][x+1] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 4; x++){
                if(s[x][x+2] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 6; x++){
                if(s[x+1][x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 5; x++){
                if(s[x+2][x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 4; x++){
                if(s[x+3][x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 0; x--){
                if(s[x][6-x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 1; x--){
                if(s[x][7-x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 2; x--){
                if(s[x][8-x] == 1){
                    combo++;
                    if(combo >= 4)return 1;
                }
                else combo = 0;
            }
        }
  
        //Check rows, red
        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 7; x++){
                if(s[x][y] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        
        
        //Check columns, red
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 6; y++){
                if(s[x][y] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
       
        
        //Check diagonals, red
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 6; x++){
                if(s[x][x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 5; x++){
                if(s[x][x+1] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 4; x++){
                if(s[x][x+2] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 6; x++){
                if(s[x+1][x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 5; x++){
                if(s[x+2][x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 0; x < 4; x++){
                if(s[x+3][x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 0; x--){
                if(s[x][6-x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 1; x--){
                if(s[x][7-x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        for(int i = 0; i < 1; i++){
            for(int x = 6; x > 2; x--){
                if(s[x][8-x] == 2){
                    combo++;
                    if(combo >= 4)return 2;
                }
                else combo = 0;
            }
        }
        
        return 0;
    }
    
    //Code to be set on piece arrays
    void piecePlacer(Circle[][] redPieces, Circle[][] bluePieces, Rectangle[][] squares, int[][] slots, int c){
                        if(!tp.equals("0") && tp == user){
                            if(tp == p1){
                                int pc = 0;
                                for(int i2 = 5; i2 >= 0; i2--){
                                    if(slots[c][i2] != 0){
                                        pc++;
                                    }
                                }
                                if(pc <= 5 && slots[c][5-pc] == 0){
                                    slots[c][5-pc] = 1;
                                    bluePieces[c][5-pc].setVisible(true);
                                    tp = p2;
                                    bluePieces[c][6].setVisible(false);
                                    redPieces[c][6].setVisible(true);
                                    
                                    if(gameCheck(slots)==1 || gameCheck(slots)==2){
                                        tp = "0";
                                        gameOver(slots);
                                    }
                                } 
                            }
                            
                            else {
                                int pc = 0;
                                for(int i2 = 5; i2 >= 0; i2--){
                                    if(slots[c][i2] != 0){
                                        pc++;
                                    }
                                }
                                if(pc <= 5 && slots[c][5-pc] == 0){
                                    slots[c][5-pc] = 2;
                                    redPieces[c][5-pc].setVisible(true);
                                    tp = p1;
                                    bluePieces[c][6].setVisible(true);
                                    redPieces[c][6].setVisible(false);
                                    if(gameCheck(slots)==1 || gameCheck(slots)==2){
                                        tp = "0";
                                        gameOver(slots);
                                    }
                                }  
                            }
                        }
    }
    
    //Code for revealing piece selection
    void pieceShower(Circle[][] redPieces, Circle[][] bluePieces, int c){
        if(!tp.equals("0") && tp == user){ 
            if(tp.equals(p1)){
                bluePieces[c][6].setVisible(true);
            }
            else {
                redPieces[c][6].setVisible(true);
            }
        }
    }
    
    //Code for hiding piece preview when leaving row
    void pieceHider(Circle[][] redPieces, Circle[][] bluePieces, int c){
        if(!tp.equals("0") && tp == user){ 
            if(tp.equals(p1)){
                bluePieces[c][6].setVisible(false);
            }
            else {
                redPieces[c][6].setVisible(false);
            }
        }
    }
    
    //Code for setting events on squares, redPieces and bluePieces
    void eventSetter(int[][]slots, Rectangle[][] squares, Circle[][] redPieces, Circle[][] bluePieces){
        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 7; x++){
                final int c = x;
                final int r = y;
                //Make top pieces visible if mouse is in their columb
                squares[x][y].setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceShower(redPieces, bluePieces, c);
                    }
                });
                redPieces[x][y].setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceShower(redPieces, bluePieces, c);
                    }
                });
                bluePieces[x][y].setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceShower(redPieces, bluePieces, c);
                    }
                });
                //Make pieces invisible if mouse leaves column
                squares[x][y].setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceHider(redPieces, bluePieces, c);
                    }
                });
                redPieces[x][y].setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceHider(redPieces, bluePieces, c);
                    }
                });
                bluePieces[x][y].setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pieceHider(redPieces, bluePieces, c);
                    }
                });
                
                squares[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        piecePlacer(redPieces, bluePieces, squares, slots, c);
                    }
                });
                
                redPieces[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        piecePlacer(redPieces, bluePieces, squares, slots, c);
                    }
                });
                
                bluePieces[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        piecePlacer(redPieces, bluePieces, squares, slots, c);
                    }
                });
                
            }
        }
    }
    
    //Code to set up gameboard for Connect-4
    Pane boardSetup(int[][] slots, Rectangle[][] squares, Circle[][] redPieces, Circle[][] bluePieces){
        
        Pane gamePane = new Pane();
        arrayInitializer(slots, squares, redPieces, bluePieces);
        eventSetter(slots, squares, redPieces, bluePieces);
        
        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 7; x++){
                gamePane.getChildren().add(squares[x][y]);
                squares[x][y].xProperty().set(CELL_SIZE*x);
                squares[x][y].yProperty().set((CELL_SIZE*1.5)+(CELL_SIZE*y));
                
                gamePane.getChildren().add(redPieces[x][y]);
                redPieces[x][y].setCenterX((CELL_SIZE/2)+(CELL_SIZE*x));
                redPieces[x][y].setCenterY((CELL_SIZE/2)+(CELL_SIZE*1.5)+(CELL_SIZE*y));
                redPieces[x][y].setVisible(false);
                
                gamePane.getChildren().add(bluePieces[x][y]);
                bluePieces[x][y].setCenterX((CELL_SIZE/2)+(CELL_SIZE*x));
                bluePieces[x][y].setCenterY((CELL_SIZE/2)+(CELL_SIZE*1.5)+(CELL_SIZE*y));
                bluePieces[x][y].setVisible(false);
                
            }
        }
        
        for(int i = 0; i < 7; i++){
            gamePane.getChildren().add(redPieces[i][6]);
                redPieces[i][6].setCenterX((CELL_SIZE/2)+(CELL_SIZE*i));
                redPieces[i][6].setCenterY((CELL_SIZE/2)+(CELL_SIZE*1.5)-CELL_SIZE);
                redPieces[i][6].setVisible(false);
                
            gamePane.getChildren().add(bluePieces[i][6]);
                bluePieces[i][6].setCenterX((CELL_SIZE/2)+(CELL_SIZE*i));
                bluePieces[i][6].setCenterY((CELL_SIZE/2)+(CELL_SIZE*1.5)-CELL_SIZE);
                bluePieces[i][6].setVisible(false);
        };
        
        return gamePane;
        
    }
    
    //Code for initializing game-critical arrays
    void arrayInitializer(int[][] slots, Rectangle[][] squares, Circle[][] redPieces, Circle[][] bluePieces){
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 6; y++){
                slots[x][y] = 0;
            }
        }
        
        for(int x = 0; x < 7; x++){
            for(int y = 0; y < 6; y++){
                squares[x][y] = new Rectangle(CELL_SIZE,CELL_SIZE, Color.WHITE);
                squares[x][y].setStroke(Color.BLACK);
            }
        }
        
        for(int y = 0; y < 7; y++){
            for(int x = 0; x < 7; x++){
                redPieces[x][y] = new Circle(CELL_SIZE/2, Color.RED);
                bluePieces[x][y] = new Circle(CELL_SIZE/2, Color.BLUE);
           }
        }
    }
    
    //Alert message informing user game is over
    void gameOver(int[][] s){
        Alert quit = new Alert(Alert.AlertType.ERROR, "", 
                               ButtonType.OK);
        quit.setGraphic(null);
        quit.setTitle("Player " +gameCheck(s)+ " wins!");
        quit.setHeaderText(winner(gameCheck(s))+ " wins!");
        quit.showAndWait();
        
        if(quit.getResult() == ButtonType.OK){
            quit.close();
        }
    }
    
    public String winner(int w){
        switch(w){
            case 1: 
                return p1; 
            case 2: 
                return p2; 
            default: return user;
        }
    }
    
    void remotePlace(int xVal, int yVal, String u){
        
        if(u.equals(p1)){
            this.slots[xVal][yVal] = 1;
            this.bluePieces[xVal][yVal].setVisible(true);
            tp = p2;
            if(gameCheck(slots)==1 || gameCheck(slots)==2){
                tp = "0";
                gameOver(slots);
            }
        }
        
        else {
            this.slots[xVal][yVal] = 2;
            this.redPieces[xVal][yVal].setVisible(true);
            tp = p1;
            if(gameCheck(slots)==1 || gameCheck(slots)==2){
                tp = "0";
                gameOver(slots);
            }
        }
    }
}
