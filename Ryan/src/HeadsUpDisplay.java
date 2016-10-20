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
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author asdas
 */
public class HeadsUpDisplay extends Node {
    Pane HUD = new Pane();
    Ship shipVal = new Ship();
    Heart hImg = new Heart();
    ImageView lastHeart;
    Image cacheHeart = hImg.getImage();
    List<ImageView> hearts = new ArrayList<>(); //keeps a list of all the hearts
    final double IMAGE_WIDTH = hImg.getWidth();
    final int TOP_HEART_GAP = 5;
    final int MID_HEART_GAP = 5;
    final int SIDE_HEART_GAP = 5;
    int heart_num = 0;
    
    
    public HeadsUpDisplay(){
        initHearts();
    }
    
    public void initHearts(){
        for(int i = 0; i < shipVal.getHealth(); i++){ //initializes the hearts based on the number of health the ship has
            hearts.add(new ImageView(cacheHeart));
        }
        addHearts();
    }
    
    public Pane initHUD(){
        return HUD; //returns the HUD overlay
    }
    
    public void hasHit(){
        shipVal.setHealth(shipVal.getHealth()-1); //reduces ship health by 1
        lastHeart = hearts.get(hearts.size()-1); //gets the last heart in the list
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
        heart_num = 0;
    }
    
    public int numHearts(){
        return hearts.size(); //returns the number of hearts
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
