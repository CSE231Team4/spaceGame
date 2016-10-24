package spacex33;

import java.awt.Button;
import java.awt.Label;
import java.io.InputStream;
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
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class SpaceX33 extends Application {   
    private Scene gameScene;
    private Scene hudScene;
    private AnimationTimer timer; 
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private List<Node> powerups = new ArrayList<>(); //array list of powerups
    private int ship_lane;
    private double rand_hold = 0;
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private ImageView astImg = new ImageView(); //create the imageviewer for the asteroids
    private ImageView slowImg = new ImageView();
    private final Ship shipObj = new Ship();
    private HeadsUpDisplay HUD = new HeadsUpDisplay();
    
    
    private final SlowTime slowObj = new SlowTime();
    private long startTime = 0;
    private long totalTime = 0;
    
    
    private boolean enableShip = true;
    private boolean enableSlow = true;
    
    final int NUM_LANES = 6; //number of lanes for obstacles to spawn
    final int WINDOW_WIDTH = 700;
    final int WINDOW_HEIGHT = 900;
    private double speed = 7; //speed of asteroids descending toward ship
    private int spawnCount = 0; //counts amount of asteroids spawned in
    private double speedAdd = 0; //used in algorithm to calculate difficulty. speedAdd = 1/10 of spawnCount
    private double spawnRate = 0.03; //spawn in speed of asteroids
    private double storeSpeed, storeSpawnRate;
    private int iframes = 0;
    private long istart = 0;
    private int shipFlash = 0;
    private long SHIP_FLASH_START = 0;
    private Node toRemAst = new Asteroid();
    private Node toRemSlow = new SlowTime();

    private Parent createContent(){
        root = new Pane(); //initialize the pane
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT); //set the size of the pane
        root.setId("pane"); //set the ID of the pane for the CSS file
        shipDisplay = shipObj.initGraphics(); //calls the initShip method to load in the graphics for the ship

        /*HBox left = new HBox();
        left.setPrefSize(700, 900);
        left.setAlignment(Pos.BOTTOM_LEFT);
        Text text = new Text(" ← A");
        text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", 40));
        text.setFill(Color.WHITE);
        left.getChildren().add(text);*/
        
        root.getChildren().add(shipDisplay); //adds the shipDisplay to the root so it can be displayed

        //root.getChildren().add(left);
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;  
    }
    
    private Parent createHUD(){
        return HUD.initHUD();
    }
    
    private Node spawnAsteroid() {
        Asteroid ast = new Asteroid();
        
        astImg = ast.initGraphics(); //gets the graphic loaded in the Asteroid class
        spawnCount++;
        
        int lane = getLane(); //gets a lane number between 0-5
       
        int obs_location = ast.getEdgeGap() + lane * (ast.getWidth() + ast.getMidGap()); //formula for calculating the X coordinate of the obstacle
        astImg.setTranslateY(-100);
        astImg.setTranslateX(obs_location); //sets the x coordinate of the obstacle to obs_location
        root.getChildren().add(astImg); //adds the graphic for the asteroid to the root
        refreshHUD();
        

        return astImg;
    }
    
    private Node spawnPowerup(){
        SlowTime st = new SlowTime();
        slowImg = st.initGraphics();
        int plane = getLane();
        int p_location = st.getEdgeGap() + plane * (st.getWidth() + st.getMidGap());
        slowImg.setTranslateX(p_location);
        root.getChildren().add(slowImg);
        refreshHUD();
        
        return slowImg;
    }
    
    private void refreshHUD(){
        /*heartDisplay1.toFront();
        heartDisplay2.toFront();
        heartDisplay3.toFront();*/
        hudScene.getRoot().toFront(); //makes sure the HUD is always on top of the obstacles/ship
    }
    
    
    private int getLane(){
        return (int)((Math.random() * 100) % NUM_LANES); //returns a number between 0-5 to decide which lane to spawn the asteroids
    }
    
    private void onUpdate() {
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            //asteroid.setRotate(asteroid.getRotate() + speed);
            if(asteroid.getTranslateY() > 900){
                toRemAst = asteroid;
                root.getChildren().remove(asteroid);
            }
        }
        asteroids.remove(toRemAst);
        for (Node powerup : powerups){
            powerup.setTranslateY(powerup.getTranslateY() + speed); //translates the powerUp down 12 pixels every update
            if(powerup.getTranslateY() > 900){
                toRemSlow = powerup;
                root.getChildren().remove(powerup);
            }
        }
        powerups.remove(toRemSlow);
        
        if (spawnRate < 2.0) {//sets limit on spawning
            if (spawnCount == 25) { //initial condition to add to spawnRate
                spawnRate = spawnRate + 0.005; //add to spawnRate if conditions are met
                spawnCount = 0; // reset spawn counter
                speedAdd = speedAdd + 0.05; // add 1 to speed add for every 10 spawn count
                speed += speedAdd;
                storeSpawnRate = spawnRate;
                storeSpeed = speed;
                /*if (speedAdd == 1) { //condition to add to speed of obsticles
                    speed = speed + 5; //add 5 to speed of falling obsticles
                    speedAdd = 0; //reset speed add counter
                }*/
            }   
        }
        
        rand_hold = Math.random();

        if (rand_hold < spawnRate && rand_hold > 0.002) {
            asteroids.add(spawnAsteroid()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
        }
        if(rand_hold < 0.002 && enableSlow == true)
            powerups.add(spawnPowerup());

        checkState();
    }
    
    private void checkState(){
        for (Node asteroid : asteroids) {
            if (System.currentTimeMillis() - istart > iframes) {
                shipObj.checkShipState();
                iframes = 0;
                if (isCollision(asteroid)) { //checks for an intersection between the asteroid and the ship 
                    iframes = 2000;
                    istart = System.currentTimeMillis();
                    root.getChildren().remove(asteroid);
                    
                    //shipObj.setHealth(shipObj.getHealth()-1);
                    shipObj.setShipState();
              
                    HUD.hasHit();
                
                    if(HUD.numHearts() <= 0){
                        enableShip = false;
                        timer.stop(); //stops the timer so asteroids no longer spawn
                
                        String lose = "YOU LOSE";
                        HBox hBox = new HBox();
                        hBox.setPrefSize(700, 900);
                        hBox.setAlignment(Pos.CENTER);
                        //hBox.setTranslateX(180); //sets x coordinate of the HBox
                        //hBox.setTranslateY(420); //sets y coordinate of the HBox
                        //I couldn't figure out how to center the text and just tried to eyeball it, if anyone has any fixes
                        root.getChildren().add(hBox); //adds the hBox object to the root
                
                        for (int i = 0; i < lose.toCharArray().length; i++) {
                            char letter = lose.charAt(i);
                            Text text = new Text(String.valueOf(letter));
                            //text.setFont(Font.font(50));
                            text.setFont(Font.loadFont("file:resource/Fonts/PressStart2P.ttf", 40));
                            
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
                    //return;
                } 
            }
            else {
                iframes--;
            }
        }
        storeSpeed = speed;
        storeSpawnRate = spawnRate;
        
        for(Node powerup : powerups){
            if(isCollision(powerup)){
                    enableSlow = false;
                    toRemSlow = powerup;
                    root.getChildren().remove(powerup);
                    speed = speed / 2;
                    spawnRate = spawnRate / 2;
                    timer();
            }
        }
        powerups.remove(toRemSlow);
}
    
    private boolean isCollision(Node hb){
        return hb.getBoundsInParent().intersects(shipDisplay.getBoundsInParent()); //returns whether or not the bounds intersect
    }
    
    private void timer() {
     Timeline POWER_UP_TIME = new Timeline (new KeyFrame(
     Duration.seconds(15.0),
     ae -> slowThings()));
     POWER_UP_TIME.play();
    }
    
    private void slowThings() {
        speed = speed*2;
        spawnRate = spawnRate*2;
        enableSlow = true;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        gameScene = new Scene(createContent()); //creates the game scene
        hudScene = new Scene(createHUD()); //creates the HUD overlay
        ((Pane)gameScene.getRoot()).getChildren().add(hudScene.getRoot()); //puts the HUD overlay over the game screen
        gameScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)
       
        stage.setScene(gameScene); 
        stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
        
        TranslateTransition shipSlide = new TranslateTransition(Duration.millis(100), shipDisplay); //sets up the ship to slide over the course of 100ms (0.1s). Change the Duration.millis to make the animation slower or faster
        ship_lane = 0;
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    if(shipDisplay.getTranslateX() != shipObj.getGap() && enableShip){ //checks if the ship is not at the edge (10px away)
                        shipSlide.setByX(-(shipObj.getWidth() + shipObj.getGap())); //formula to calculate position the ship should slide to (negative becuse moving to the left)
                        shipSlide.play(); //plays animation
                        ship_lane--;
                    }
                    break;
                case RIGHT:
                case D:
                    if(shipDisplay.getTranslateX() != (WINDOW_WIDTH-shipObj.getGap()-shipObj.getWidth()) && enableShip){ //700 (total window size) - one gap at the furthermost edge - size of one ship
                        shipSlide.setByX(shipObj.getWidth() + shipObj.getGap()); //same formula as previously but positive
                        shipSlide.play(); //plays animation
                        ship_lane++;
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