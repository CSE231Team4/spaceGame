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
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author asdas
 */
public class Heart extends Node {
    private int width; //width of the image
    private int height; //height of the image
    private Image heart_image = new Image("file:resource/Images/heartImg.png", true); //sets the path of the heart image
    private ImageView heartImage = new ImageView(heart_image); //sets up the imageviewer for the heart image

    public Heart(){
        super();
        width = 70;
        height = 70;
        //default width and height are 95 because that's what we're using, but this is subject to change
    }
    
    public Heart(int w, int h){
        super();
        width = w;
        height = h;
    }
    
    public ImageView initGraphics(){
        heartImage.setFitWidth(width);
        heartImage.setFitHeight(height);
        heartImage.setTranslateX(5);
        heartImage.setTranslateY(5);
        //sets the width and height of the image
        return heartImage;
    }
    
    public int getWidth(){
        return width;
    }
    
    public void setWidth(int w){
        width = w;
    }
    
    public Image getImage(){
        return heart_image;
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
