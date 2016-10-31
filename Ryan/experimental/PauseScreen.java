/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static spacex33.Screen.WINDOW_HEIGHT;
import static spacex33.Screen.WINDOW_WIDTH;

/**
 *
 * @author asdas
 */
public class PauseScreen implements Screen {
    Rectangle bck = new Rectangle();
    HBox pause = new HBox();
    HBox pause2 = new HBox();
    HBox pause3 = new HBox();
    Pane pauseScreen = new Pane();
    
    public PauseScreen(){
        super();
        bck.setHeight(WINDOW_HEIGHT + 50);
        bck.setWidth(WINDOW_WIDTH + 50);
        bck.setFill(Color.BLACK);
        pauseScreen.getChildren().add(bck);
        pause.setAlignment(Pos.TOP_CENTER);
        pause.setLayoutY(100);
        printer("GAME IS PAUSED", 45, pause);
        pauseScreen.getChildren().add(pause);
        //pause.setPrefSize(window_width, window_height);
        
        pause2.setAlignment(Pos.TOP_CENTER);
        pause2.setLayoutY(175);
        printer("PRESS 'Q' TO RESUME", 20, pause2);
        pauseScreen.getChildren().add(pause2);
        
        pause3.setAlignment(Pos.TOP_CENTER);
        pause3.setLayoutY(215);
        printer("PRESS 'R' TO RESTART", 20, pause3);
        pauseScreen.getChildren().add(pause3);
    }
    
    public Pane initPauseScreen(){
        return pauseScreen;
    }
    
    private void printer(String print, int size, HBox location) {
        location.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        //root.getChildren().add(location); //adds the hBox object to the root

        for (int i = 0; i < print.toCharArray().length; i++) {
            char letter = print.charAt(i);
            Text text = new Text(String.valueOf(letter));
            text.setFont(Font.font(size));
            text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", size));

            text.setOpacity(100);
            text.setFill(Color.WHITE);
            location.getChildren().add(text);
        }
    }
}