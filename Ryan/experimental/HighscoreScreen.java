/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

import com.sun.deploy.util.StringUtils;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
public class HighscoreScreen extends Node implements Screen {
    
    Pane highscore = new Pane();
    Rectangle bck = new Rectangle();
    HBox leaderboard = new HBox();
    HBox no_scores = new HBox();
    ArrayList<HBox> scoreDisplay = new ArrayList<>();
    HBox remove = new HBox();
    int[] scores = new int[10];
    File hs = new File("./resource/Text/highscores.txt");
    String initials = "AAA";
    String format = "";
    int length = 0;
    int k = 0;
    
    public HighscoreScreen(){
        super();
        bck.setHeight(WINDOW_HEIGHT + 50);
        bck.setWidth(WINDOW_WIDTH + 50);
        bck.setFill(Color.BLACK);
        highscore.getChildren().add(bck);
        
        leaderboard.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        leaderboard.setAlignment(Pos.TOP_CENTER);
        leaderboard.setLayoutY(100);
        Text hold_text = new Text("HIGHSCORES");
        hold_text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", 50));
        hold_text.setFill(Color.WHITE);
        leaderboard.getChildren().add(hold_text);
        highscore.getChildren().add(leaderboard);
        
        
        
        
        //updateScores();
    }
    
    public void updateScores(){
        clearScores();
        //if(length >= 1){
        for(int i = 0; i < length; i++){
            HBox h = new HBox();
            h.setAlignment(Pos.TOP_CENTER);
            h.setLayoutY(200 + 30*i);
                
            if(i == 9)
                format = (i+1) + ".  " + String.format("%-12s", initials).replace(' ', '.') + String.format("%-9s", " " + String.valueOf(scores[i]));
            else
                format = (i+1) + ".   " + String.format("%-12s", initials).replace(' ', '.') + String.format("%-9s", " " + String.valueOf(scores[i]));
            printer(format, 20, h);
        }
        //}
        
        highscore.getChildren().addAll(scoreDisplay);
    }
    
    public void setHighscores(int[] s){
        clearScores();
         for(int i = 0; i < length; i++){
             if(scores[i] != s[i])
             scores[i] = s[i];
         }
         updateScores();
    }
    
    public void getHighscores(){
        String str = "";
        for(int i = 0; i < length; i++)
            System.out.println(scores[i]);
    }
    
    public void clearScores(){
        highscore.getChildren().removeAll(scoreDisplay);
        scoreDisplay.clear();
    }
    
    public Pane initHighscoreScreen(){
        return highscore;
    }
    public void printer(String print, int size, HBox location){
        location.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Text hold_text = new Text(print);
        hold_text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", size));
        hold_text.setFill(Color.WHITE);
        location.setAlignment(Pos.TOP_CENTER);
        location.getChildren().add(hold_text);
        //highscore.getChildren().add(location);
        scoreDisplay.add(location);
    }
    
    public void setLength(int l){
        if(l < 10)
            length = l;
        else
            length = 10;
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
