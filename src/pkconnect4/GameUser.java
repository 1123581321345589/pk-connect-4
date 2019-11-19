/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkconnect4;

import javafx.scene.paint.Color;

/**
 *
 * @author wardt4
 */
public class GameUser {
    
    Color color = Color.BLUE;
    String name = "User 1: ";
    int id = 0;
    boolean yourTurn = false;
    boolean inGame = false;
    boolean hosting = false;
    
    public GameUser(){}
    public GameUser(String un, int up){}
    
    
}
