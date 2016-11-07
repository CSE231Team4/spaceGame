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
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author asdas
 */
public class HeadsUpDisplay extends Node implements Screen{
    Pane HUD = new Pane();
    Ship shipVal = new Ship();
    Text score = new Text();
    ImageView lastHeart;
    Image cacheHeart = new Image("file:resource/Images/heartImg.png", true);
    List<ImageView> hearts = new ArrayList<>(); //keeps a list of all the hearts
    final double IMAGE_WIDTH = 70;
    final int TOP_HEART_GAP = 5;
    final int MID_HEART_GAP = 5;
    final int SIDE_HEART_GAP = 5;
    String scoreStr = "Score: ";
    String scoreVal = "0";
    int score_val = 0;
    int heart_num = 0;
    HBox scoreText = new HBox();
    int scoreChange = 0;
       
    
    
    public HeadsUpDisplay(){
        scoreText.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        scoreText.setTranslateY(15);
        scoreText.setTranslateX(400);
        
        initHearts();
        initScore();
    }
    
    public void initHearts(){
        for(int i = 0; i < shipVal.getHealth(); i++){ //initializes the hearts based on the number of health the ship has
            hearts.add(new ImageView(cacheHeart));
        }
        addHearts();
    }
    
    public void initScore(){
        score.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", 20));
        score.setFill(Color.WHITE);
        score.setText("Score: " + String.format("%09d", score_val));
        scoreText.getChildren().add(score);
        HUD.getChildren().add(scoreText);
    }
    
    public void updateScore(){
        scoreChange++;
        if(scoreChange % 3 == 0){
            HUD.getChildren().remove(scoreText);
            if(score_val < 999999999)
                score_val += 2;
            if(score_val > 999999999)
                score_val = 999999999;
            score.setText("Score:" + String.format("%09d", score_val));
            HUD.getChildren().add(scoreText);
            if(scoreChange == 10000)
                scoreChange = 0;
        }
        
    }
    
    public Pane initHUD(){
        return HUD; //returns the HUD overlay
    }
    
    public void hasHit(){
        shipVal.setHealth(shipVal.getHealth()-1); //reduces ship health by 1
        lastHeart = hearts.get(hearts.size()-1); //gets the last heart in the list
        heart_num--;
        HUD.getChildren().remove(lastHeart); //removes the last heart from the HUD
        hearts.remove(lastHeart); //removes the last hear from the list
    }
   
    
    public void addHearts(){
        for(ImageView heart : hearts){ //cycles trough the hearts and calculates their X position and adds them to the HUD
            heart.setTranslateY(TOP_HEART_GAP);
            heart.setTranslateX(SIDE_HEART_GAP + heart_num *(MID_HEART_GAP + IMAGE_WIDTH));
            heart_num++;
            HUD.getChildren().add(heart);
        }
    }
    
    public void hasHeartPower(){
        shipVal.setHealth(shipVal.getHealth()+1);
        updateHearts(new ImageView(cacheHeart));
        score_val += 100;
    }
    
    public void hasSlowPower(){
        score_val += 100;
    }
    
    public void setScore(int s){
        score_val = s;
    }
    
    public int getScore(){
        return score_val;
    }
    
    public void updateHearts(ImageView newHeart){
        if(heart_num < 5){
        newHeart.setTranslateY(TOP_HEART_GAP);
        newHeart.setTranslateX(SIDE_HEART_GAP + heart_num *(MID_HEART_GAP + IMAGE_WIDTH));
        hearts.add(newHeart);
        HUD.getChildren().add(newHeart);
        heart_num++;
        }
        else
            heart_num = 5;
    }
    
    public void reset(){
        setScore(0);
        for(Node heart : hearts){
            HUD.getChildren().remove(heart);
        }
        hearts.removeAll(hearts);
        
        heart_num = 0;
        shipVal.setHealth(3);
        initHearts();
        scoreChange = 0;
    }
    
    public int numHearts(){
        return heart_num; //returns the number of hearts
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

    @Override
    public void printer(String print, int size, HBox location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}