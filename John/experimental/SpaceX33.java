package spacex33;

import java.awt.Button;
import java.awt.Label;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class SpaceX33 extends Application {   
    private Scene gameScene;
    private AnimationTimer timer; 
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private int ship_lane;
    //private List<Node> hitboxes = new ArrayList<>();
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private Node heartDisplay1 = new Heart();
    private Node heartDisplay2 = new Heart();
    private Node heartDisplay3 = new Heart();
    private ImageView astImg = new ImageView(); //create the imageviewer for the ship
    private final Asteroid astObj = new Asteroid();
    private final Ship shipObj = new Ship();
    
    private final Heart heartObj1 = new Heart();
    private final Heart heartObj2 = new Heart();
    private final Heart heartObj3 = new Heart();
    
    private boolean enableShip = true;
    
    final int NUM_LANES = 6; //number of lanes for obstacles to spawn
    final int WINDOW_WIDTH = 700;
    final int WINDOW_HEIGHT = 900;
    private int speed = 5; //speed of asteroids descending toward ship
    private int spawnCount = 0; //counts amount of asteroids spawned in
    private double speedAdd = 0; //used in algorithm to calculate difficulty. speedAdd = 1/10 of spawnCount
    private double spawnRate = 0.2; //spawn in speed of asteroids
    private int iframes = 0;
    private long istart = 0;
    
    private Parent createContent(){
        root = new Pane(); //initialize the pane
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT); //set the size of the pane
        root.setId("pane"); //set the ID of the pane for the CSS file
        shipDisplay = shipObj.initShipGraphics(); //calls the initShip method to load in the graphics for the ship
        heartDisplay1 = heartObj1.initHeartGraphics();
        heartDisplay2 = heartObj2.initHeartGraphics();
        heartDisplay2.setTranslateX(80);
        heartDisplay3 = heartObj3.initHeartGraphics();
        heartDisplay3.setTranslateX(155);
        
        root.getChildren().add(shipDisplay); //adds the shipDisplay to the root so it can be displayed
        root.getChildren().add(heartDisplay1);
        root.getChildren().add(heartDisplay2);
        root.getChildren().add(heartDisplay3);
        
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;  
    }
    
    
    private Node spawnObstacle() {
        Asteroid ast = new Asteroid();
        
        astImg = ast.initAsteroidGraphics(); //gets the graphic loaded in the Asteroid class
        spawnCount++;
        
        int lane = getLane(); //gets a lane number between 0-5
        int obs_location = ast.getEdgeGap() + lane * (ast.getWidth() + ast.getMidGap()); //formula for calculating the X coordinate of the obstacle
        astImg.setTranslateX(obs_location); //sets the x coordinate of the obstacle to obs_location
        root.getChildren().add(astImg); //adds the graphic for the asteroid to the root
        heartDisplay1.toFront();
        heartDisplay2.toFront();
        heartDisplay3.toFront();

        //return astImg;
        return astImg;
    }
    
    /*private Node spawnHitBox(){
        Circle hitBox = new Circle(47.5);
        hitBox.setVisible(false);
        
        int hbox_location = (int) (50 + current_lane * (2*hitBox.getRadius() + 20)); //formula for calculating the X coordinate of the obstacle
        hitBox.setTranslateX(hbox_location); //sets the x coordinate of the obstacle to obs_location
        hitBox.setTranslateY(50);

        root.getChildren().add(hitBox); //adds the graphic for the asteroid to the root

        return hitBox;
        
    }*/
    
    private int getLane(){
        return (int)((Math.random() * 100) % NUM_LANES); //returns a number between 0-5 to decide which lane to spawn the asteroids
    }
    
    private void onUpdate() {
        //int ast_index = 0;
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            //ast_index = asteroids.indexOf(asteroid);
            //hitboxes.get(ast_index).setTranslateY(hitboxes.get(ast_index).getTranslateY() + speed); //
            asteroid.setRotate(asteroid.getRotate() + speed);
            if(asteroid.getTranslateY() > 900){
                asteroids.remove(asteroid);
                //hitboxes.remove(hitboxes.get(ast_index));
                root.getChildren().remove(asteroid);
                //root.getChildren().remove(hitboxes.get(ast_index));
                
                //this was my attempt at saving memory... doesn't work too well but makes it slightly more efficient
            }
        }
        if (spawnRate < 2.0) {//sets limit on spawning
            if (spawnCount == 25) { //initial condition to add to spawnRate
                spawnRate = spawnRate + 0.005; //add to spawnRate if conditions are met
                spawnCount = 0; // reset spawn counter
                speedAdd = speedAdd + 0.2; // add 1 to speed add for every 10 spawn count
                if (speedAdd == 1) { //condition to add to speed of obsticles
                    speed = speed + 5; //add 5 to speed of falling obsticles
                    speedAdd = 0; //reset speed add counter
                }
            }   
        }

        if (Math.random() < 0.03) {
            asteroids.add(spawnObstacle()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
            //hitboxes.add(spawnHitBox());
        }

        checkState();
    }
    
    private void checkState(){
        for (Node asteroid : asteroids) {
            if (System.currentTimeMillis() - istart > iframes) {
                iframes = 0;
                if (isCollision(asteroid)) { //checks for an intersection between the asteroid and the ship
                    iframes = 2000;
                    istart = System.currentTimeMillis();
                    //root.getChildren().remove(hitboxes.get(asteroids.indexOf(asteroid)));
                    asteroids.remove(asteroid);
                    root.getChildren().remove(asteroid);
                
                    shipObj.setHealth(shipObj.getHealth()-1);
                    shipObj.resetLocation();
                    ship_lane = 0;
                
                    switch(shipObj.getHealth()){
                        case 0: heartDisplay1.setVisible(false);
                                break;
                        case 1: heartDisplay2.setVisible(false);
                                break;
                        case 2: heartDisplay3.setVisible(false);
                                break;
                        default:
                    }
                
                    if(shipObj.getHealth() <= 0){
                        enableShip = false;
                        timer.stop(); //stops the timer so asteroids no longer spawn
                
                        String lose = "YOU LOSE";
                        HBox hBox = new HBox();
                        hBox.setTranslateX(250); //sets x coordinate of the HBox
                        hBox.setTranslateY(420); //sets y coordinate of the HBox
                        //I couldn't figure out how to center the text and just tried to eyeball it, if anyone has any fixes
                        root.getChildren().add(hBox); //adds the hBox object to the root
                
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
                        }
                    }
                    //the above section is used for printing the "YOU LOSE" font
                    return;
                }
            }
            else {
                iframes -= 1;
            }
        }
    }
    
    private boolean isCollision(Node hb){
        return hb.getBoundsInParent().intersects(shipDisplay.getBoundsInParent()); //returns whether or not the bounds intersect
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        gameScene = new Scene(createContent());
        gameScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)
       
        stage.setScene(gameScene); 
        stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
        
        TranslateTransition shipSlide = new TranslateTransition(Duration.millis(100), shipDisplay); //sets up the ship to slide over the course of 100ms (0.1s). Change the Duration.millis to make the animation slower or faster
        ship_lane = 0;
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A:
                    if(shipDisplay.getTranslateX() != shipObj.getGap() && enableShip){ //checks if the ship is not at the edge (10px away)
                        shipSlide.setByX(-(shipObj.getWidth() + shipObj.getGap())); //formula to calculate position the ship should slide to (negative becuse moving to the left)
                        shipSlide.play(); //plays animation
                        ship_lane--;
                        System.out.println(ship_lane);
                    }
                    break;
                case D:
                    if(shipDisplay.getTranslateX() != (WINDOW_WIDTH-shipObj.getGap()-shipObj.getWidth()) && enableShip){ //700 (total window size) - one gap at the furthermost edge - size of one ship
                        shipSlide.setByX(shipObj.getWidth() + shipObj.getGap()); //same formula as previously but positive
                        shipSlide.play(); //plays animation
                        ship_lane++;
                        System.out.println(ship_lane);
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