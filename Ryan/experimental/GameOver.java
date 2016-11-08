/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static spacex33.Screen.WINDOW_HEIGHT;
import static spacex33.Screen.WINDOW_WIDTH;

/**
 *
 * @author asdas
 */
public class GameOver extends Node implements Screen {
    Pane gameOver = new Pane();
    HBox overMessage = new HBox();
    HBox restartMessage = new HBox();
    HBox initials = new HBox();
    String init = null;
    TextField tf = new TextField();
    
    public GameOver(){
        super();
        gameOver.setBackground(Background.EMPTY);
        overMessage.setAlignment(Pos.CENTER);
        printer("GAME OVER", 70, overMessage);
        
        
        
        initials.setAlignment(Pos.CENTER);
        initials.setTranslateY(100);
        printer("ENTER YOUR INITIALS", 20, initials);
        
        restartMessage.setAlignment(Pos.CENTER);
        restartMessage.setTranslateY(100);
        printer("PRESS 'R' TO RESET", 20, restartMessage);
        restartMessage.setVisible(false);
        
        tf.setBackground(Background.EMPTY);
        tf.setStyle("-fx-text-inner-color: white; -fx-display-caret: false;");
        tf.setAlignment(Pos.CENTER);
        tf.setMaxWidth(200);
        tf.setTranslateX(250);
        tf.setTranslateY(600);
        tf.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", 40));
        gameOver.getChildren().add(tf);
        
        tf.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                if(ke.getCode().equals(KeyCode.ENTER)){
                    gameOver.getChildren().remove(initials);
                    gameOver.getChildren().remove(tf);
                    restartMessage.setVisible(true);
                    
                }
            }
        });
        
    }
    
    public Pane initGameOver(){
        return gameOver;
    }
    
    public void printer(String print, int size, HBox location){
        location.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Text hold_text = new Text(print);
        hold_text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", size));
        hold_text.setFill(Color.WHITE);
        location.setAlignment(Pos.CENTER);
        location.getChildren().add(hold_text);
        gameOver.getChildren().add(location);
    }
    
    public void reset(){
        tf.setText(null);
        gameOver.getChildren().add(tf);
        gameOver.getChildren().add(initials);
        restartMessage.setVisible(false);
    }
    
    public String getInitials(){
        return init;
    }

    @Override
    protected NGNode impl_createPeer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
