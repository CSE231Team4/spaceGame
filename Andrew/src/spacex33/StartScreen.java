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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
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
    HBox starter3 = new HBox();
    HBox starter4 = new HBox();
    
    public StartScreen(){
        super();
        
        // add start messages
        startMessage.setAlignment(Pos.TOP_CENTER);
        startMessage.setLayoutY(200);
        printer("SPACEX33", 70, startMessage);
        
        starter.setAlignment(Pos.TOP_CENTER);
        starter.setLayoutY(350);
        printer("PRESS 'SPACE' TO START", 25, starter);
        
        starter2.setAlignment(Pos.TOP_CENTER);
        starter2.setLayoutY(400);
        printer("PRESS 'ESCAPE' TO QUIT", 25, starter2);
        
        starter3.setAlignment(Pos.TOP_CENTER);
        starter3.setLayoutY(450);
        printer("PRESS 'C' FOR CONTROLS", 25, starter3);
        
        starter4.setAlignment(Pos.TOP_CENTER);
        starter4.setLayoutY(500);
        printer("PRESS 'H' FOR HIGHSCORES", 25, starter4);

    }
    
    public Pane initStartScreen(){
        return startScreen;
    }
    
    public void printer(String print, int size, HBox location) {
        location.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Text hold_text = new Text(print);
        hold_text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", size));
        hold_text.setFill(Color.WHITE);
        location.setAlignment(Pos.TOP_CENTER);
        location.getChildren().add(hold_text);
        startScreen.getChildren().add(location);
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
