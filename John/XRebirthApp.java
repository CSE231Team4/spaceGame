/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroid;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author 1234r_000
 */
public class XRebirthApp extends Application {
    //private Pane root;
    //private Node rock;
    private AnimationTimer timer;
    private Pane root;
    private List<Node> asteroids = new ArrayList<>();
    
    private final int STAGEWIDTH = 1280;
    private final int STAGEHEIGHT = 720;
    private int shipHeight = 50;
    private int shipWidth = 50;
    
    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(STAGEWIDTH, STAGEHEIGHT);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate(now);
            }
        };
        timer.start();

        return root;
    }
    
    private Node shipInit(int width, int height) {
        Rectangle r = new Rectangle (0,STAGEHEIGHT-height*2,width,height);
        r.setFill(Color.BLACK);
        
        return r;
    }
    
    @Override
    public void start(Stage stage) {
        root = new Pane();
        stage.setScene(new Scene(createContent()));
        //scene = new Scene(root, 500, 500, Color.BLACK);
        root.getChildren().add(shipInit(shipWidth,shipHeight));
        
        stage.setTitle("Asteroid Demo");
        stage.show();
    } 
    
    public void onUpdate(long now) {    
        for (Node asteroid : asteroids) 
            asteroid.setTranslateY(asteroid.getTranslateY() + 10);
        
        if (Math.random() < 1) {
            //random long between 0 and 5, multiplied by 100, dist between lanes
            long xPos = ((Math.round(Math.random()*10)) / 2 )*100;
            System.out.println(asteroids.size());
            asteroids.add(spawnAsteroid(xPos));
        }
    }
    
    public Node spawnAsteroid(long xPos) {
        Rectangle r = new Rectangle(xPos,0,10,10);
        r.setFill(Color.BLUE);
        r.setTranslateY(20);
        root.getChildren().add(r);
        
        return r;    
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
