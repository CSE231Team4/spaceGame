/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
/**
 *
 * @author asdas
 */
public class SpaceX33 extends Application {
    private AnimationTimer timer;
    
    private Pane root;
    
    private List<Node> asteroids = new ArrayList<>();
    private Node ship;
    private Parent createContent(){
        root = new Pane();
        root.setPrefSize(700, 900);
        root.setId("pane");
        
        ship = initShip();
        
        root.getChildren().add(ship);
        
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;  
    }
    
    private ImageView initShip() {
	Image img = new Image("file:resource/ship.png", true);
	ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setTranslateY(675);
        imgView.setTranslateX(10);
        return imgView;
    }
    
    private Node spawnAsteroid() {
        //Rectangle rect = new Rectangle(95, 95, Color.BROWN);
        Image ast = new Image("file:resource/asteroid.png", true);
	ImageView iv = new ImageView();
        iv.setImage(ast);
        iv.setFitWidth(90);
        iv.setPreserveRatio(true);
        int lane = (int)(Math.random() * 100) % 6;
        switch(lane){
            case 0: 
                iv.setTranslateX(15); 
                break;
            case 1: 
                iv.setTranslateX(130); 
                break;
            case 2: 
                iv.setTranslateX(245); 
                break;
            case 3: 
                iv.setTranslateX(360); 
                break;
            case 4: 
                iv.setTranslateX(475); 
                break;
            case 5: 
                iv.setTranslateX(590); 
                break;
            default:
        }
        //rect.setTranslateX((int)(Math.random() * 14) * 155);

        root.getChildren().add(iv);
        return iv;
    }
    
    private void onUpdate() {
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + 12);
        }

        if (Math.random() < 0.03) {
            asteroids.add(spawnAsteroid());
        }

        checkState();
    }
    
    private void checkState() {
        for (Node asteroid : asteroids) {
            if (asteroid.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                ship.setTranslateX(10);
                ship.setTranslateY(675);
            /*if (asteroid.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                //ship.setTranslateX(10);
                //ship.setTranslateY(675);
                timer.stop();
                String lose = "YOU LOSE";
                HBox hBox = new HBox();
                hBox.setTranslateX(200);
                hBox.setTranslateY(400);
                root.getChildren().add(hBox);
                
                for (int i = 0; i < lose.toCharArray().length; i++) {
                char letter = lose.charAt(i);

                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(50));
                text.setOpacity(0);
                text.setFill(Color.WHITE);

                hBox.getChildren().add(text);

                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.15));
                ft.play();
            }*/
                return;
            }
        }

        /*if (ship.getTranslateY() <= 0) {
            timer.stop();
            String win = "YOU WIN";

            HBox hBox = new HBox();
            hBox.setTranslateX(300);
            hBox.setTranslateY(250);
            root.getChildren().add(hBox);

            for (int i = 0; i < win.toCharArray().length; i++) {
                char letter = win.charAt(i);

                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);

                hBox.getChildren().add(text);

                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.15));
                ft.play();
            }
        }*/
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), ship);
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A:
                    if(ship.getTranslateX() != 10){
                        //ship.setTranslateX(ship.getTranslateX() - 115);
                        tt.setByX(-115);
                        tt.play();
                    }
                    break;
                case D:
                    if(ship.getTranslateX() != 585){
                        //ship.setTranslateX(ship.getTranslateX() + 115);
                        tt.setByX(115);
                        tt.play();
                    }
                    break;
                default:
                    break;
            }
        });

        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
