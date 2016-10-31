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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author asdas
 */
public class StartScreen extends Node implements Screen{
    Pane startScreen = new Pane();
    HBox startMessage = new HBox();
    HBox starter = new HBox();
    HBox starter2 = new HBox();
    
    
    public StartScreen(){
        super();
        // add start messages
        startMessage.setAlignment(Pos.TOP_CENTER);
        startMessage.setLayoutY(50);
        //startMessage.setPrefSize(window_width, window_height);
        startScreen.getChildren().add(startMessage);
        printer("SPACEX33", 50, startMessage);
        
        starter.setAlignment(Pos.TOP_CENTER);
        starter.setLayoutY(350);
        //starter.setPrefSize(window_width, window_height);
        startScreen.getChildren().add(starter);
        printer("PRESS 'SPACE' TO START", 25, starter);
        
        starter2.setAlignment(Pos.TOP_CENTER);
        starter2.setLayoutY(400);
        //starter2.setPrefSize(window_width, window_height);
        startScreen.getChildren().add(starter2);
        printer("PRESS 'ESCAPE' TO QUIT", 25, starter2); 
    }
    
    public Pane initStartScreen(){
        return startScreen;
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
