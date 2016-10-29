package spacex33;

import com.sun.javafx.application.LauncherImpl;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SpaceX33 extends Application {   
    private Scene gameScene;
    private Scene startScene;
    private Scene hudScene;
    
    private AnimationTimer timer;
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private List<ImageView> powerups = new ArrayList<>(); //array list of powerups
    private List<Node> background = new ArrayList<>();
    private List<Node> printers = new ArrayList<>();
    private double rand_hold = 0;
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private ImageView astImg = new ImageView(); //create the imageviewer for the asteroids
    //private ImageView slowImg = new ImageView();
    private ImageView powerupImage = new ImageView();
    private final Ship shipObj = new Ship();
    private HeadsUpDisplay HUD = new HeadsUpDisplay();   
    
    private Image bck = new Image("file:resource/Images/space_background.png",true);
    private ImageView b1, b2, b3;
    
    //private final SlowTime slowObj = new SlowTime();
    
    private boolean enableShip = true;
    private boolean enableSlow = true;
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int startCount = 0;
    final int NUM_LANES = 6; //number of lanes for obstacles to spawn
    private int window_width = 700;
    private int window_height = 900;
    private double speed = 7; //speed of asteroids descending toward ship
    private int spawnCount = 0; //counts amount of asteroids spawned in
    private double speedAdd = 0.05; //used in algorithm to calculate difficulty. speedAdd = 1/10 of spawnCount
    private double spawnRate = 0.03; //spawn in speed of asteroids
    private int iframes = 0;
    private long istart = 0;
    private Node toRemAst = new Asteroid();
    //private Node toRemSlow = new SlowTime();
    private Node toRemPower = new Powerup();
    private STATE state = STATE.START;
    HBox startMessage = new HBox();
    HBox starter = new HBox();
    HBox starter2 = new HBox();
    HBox pause = new HBox();
    HBox pause2 = new HBox();
           
    private Parent createContent(){ 
        root = new Pane(); //initialize the pane
        b1 = new ImageView(bck);
        b1.setScaleY(1.1);
        b1.setScaleX(1.1);
        background.add(b1);
        b2 = new ImageView(bck);
        b2.setScaleX(1.1);
        b2.setScaleY(1.1);
        b2.setTranslateY(50-window_height);
        background.add(b2);
        b3 = new ImageView(bck);
        b3.setScaleX(1.1);
        b3.setScaleY(1.1);
        b3.setTranslateY(50-2*window_height);
        background.add(b3);

        if(screenSize.getHeight() <= 900)
            window_height = 800;
        shipObj.setW_Height(window_height);
        
        root.setPrefSize(window_width, window_height); //set the size of the pane
        root.setId("game"); //set the ID of the pane for the CSS file
        shipDisplay = shipObj.initGraphics(); //calls the initShip method to load in the graphics for the ship
        
        root.getChildren().add(b1);
        root.getChildren().add(b2);
        root.getChildren().add(b3);

        //root.getChildren().add(left);
        //if(state == STATE.GAME) {
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
        astImg.setTranslateX(obs_location); //sets the x coordinate of the obstacle to obs_location
        astImg.setTranslateY(-50);
        root.getChildren().add(astImg); //adds the graphic for the asteroid to the root
        refreshHUD();
        

        return astImg;
    }
    
    private ImageView spawnPowerup(){
        /*SlowTime st = new SlowTime();
        slowImg = st.initGraphics();
        int plane = getLane();
        int p_location = st.getEdgeGap() + plane * (st.getWidth() + st.getMidGap());
        slowImg.setTranslateX(p_location);
        root.getChildren().add(slowImg);
        refreshHUD(); 
        
        return slowImg;*/
        Powerup powerup = new Powerup();
        powerupImage = powerup.initGraphics();
        //powerup.findLives(HUD.numHearts());
        powerupImage.setX(powerup.getType());
        int plane = getLane();
        int p_location = powerup.getEdgeGap() + plane * (powerup.getWidth() + powerup.getMidGap());
        powerupImage.setTranslateX(p_location);
        root.getChildren().add(powerupImage);
        refreshHUD();
        
        return powerupImage;
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
    
    private int choose(){
        return (int)((Math.random() * 100) % 2);
    }
    
    private void onUpdate()  {
        if (state == STATE.GAME) {
        enableShip = true;
        asteroidUpdate();
        powerupUpdate();
        spawnUpdate();
        backgroundUpdate();
        
        rand_hold = Math.random();

        if (rand_hold < spawnRate && rand_hold > 0.003) {
            asteroids.add(spawnAsteroid()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
        }
        if(rand_hold < 0.009 && enableSlow == true)
            powerups.add(spawnPowerup());

        checkState();
        }
        else if (state == STATE.START) {
            enableShip = false;
        }
    }
    
    private void asteroidUpdate(){
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            //asteroid.setRotate(asteroid.getRotate() + speed);
            if(asteroid.getTranslateY() > window_height){
                toRemAst = asteroid;
                root.getChildren().remove(asteroid);
            }
        }
        asteroids.remove(toRemAst);
    }
    
    private void powerupUpdate(){
        for (ImageView power : powerups){
            power.setTranslateY(power.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            //asteroid.setRotate(asteroid.getRotate() + speed);
            if(power.getTranslateY() > window_height){
                toRemPower = power;
                root.getChildren().remove(power);
            }
        }
        powerups.remove(toRemPower);
    }
    
    private void backgroundUpdate(){
        for(Node bg : background){
            bg.setTranslateY(bg.getTranslateY() + speed/5);
            
            if(bg.getTranslateY() > window_height)
                bg.setTranslateY(0-window_height);
        }
    }
    
    private void spawnUpdate(){
        if (spawnRate < 2.0) {//sets limit on spawning
            if (spawnCount == 25) { //initial condition to add to spawnRate
                spawnRate = spawnRate + 0.005; //add to spawnRate if conditions are met
                spawnCount = 0; // reset spawn counter
                speed += speedAdd;
                /*if (speedAdd == 1) { //condition to add to speed of obsticles
                    speed = speed + 5; //add 5 to speed of falling obsticles
                    speedAdd = 0; //reset speed add counter
                }*/
            }   
        }
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
                        HBox lose = new HBox();
                        lose.setAlignment(Pos.CENTER);
                        root.getChildren().add(lose);
                        enableShip = false;
                        timer.stop(); //stops the timer so asteroids no longer spawn
                        printer("YOU LOSE", 40, lose);
                        
                    }
                    //the above section is used for printing the "YOU LOSE" font
                    //return;
                } 
            }
            else {
                iframes--;
            }
        }
        
        for(ImageView power : powerups){
            if(isCollision(power)){
                toRemPower = power;
                root.getChildren().remove(power);
                
                switch((int)power.getX()){
                    case 0:
                        enableSlow = false;
                        speed = speed / 2;
                        spawnRate = spawnRate / 2;
                        timer(); 
                        break;
                    case 1: 
                        HUD.hasPower();
                    default: break;
                }
            }
        }
        powerups.remove(toRemPower);
    }
    
    private boolean isCollision(Node hb){
        return hb.getBoundsInParent().intersects(shipDisplay.getBoundsInParent()); //returns whether or not the bounds intersect
    }
    
    private void timer() {
        Timeline POWER_UP_TIME = new Timeline (new KeyFrame(
        Duration.seconds(5.0),
        ae -> slowThings()));
        POWER_UP_TIME.play();
    }
    
    private void slowThings() {
        speed = speed * 2;
        spawnRate = spawnRate * 2;
        enableSlow = true;
    }
    
    private void printer(String print, int size, HBox location) {
                        location.setPrefSize(window_width, window_height);
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
    public void start(Stage stage) throws Exception {
        gameScene = new Scene(createContent()); //creates the game scene
        hudScene = new Scene(createHUD()); //creates the HUD overlay
        ((Pane)gameScene.getRoot()).getChildren().add(hudScene.getRoot()); //puts the HUD overlay over the game screen
        //gameScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)

        stage.setScene(gameScene); 
        stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
            
        TranslateTransition shipSlide = new TranslateTransition(Duration.millis(100), shipDisplay); //sets up the ship to slide over the course of 100ms (0.1s). Change the Duration.millis to make the animation slower or faster
        
        // add start messages
        startMessage.setAlignment(Pos.TOP_CENTER);
        startMessage.setLayoutY(50);
        //startMessage.setPrefSize(window_width, window_height);
        root.getChildren().add(startMessage);
        printer("SPACEX33", 50, startMessage);
        
        starter.setAlignment(Pos.TOP_CENTER);
        starter.setLayoutY(350);
        //starter.setPrefSize(window_width, window_height);
        root.getChildren().add(starter);
        printer("PRESS 'SPACE' TO START", 25, starter);
        
        starter2.setAlignment(Pos.TOP_CENTER);
        starter2.setLayoutY(400);
        //starter2.setPrefSize(window_width, window_height);
        root.getChildren().add(starter2);
        printer("PRESS 'ESCAPE' TO QUIT", 25, starter2); 
        
        pause.setAlignment(Pos.TOP_CENTER);
        pause.setLayoutY(100);
        printer("GAME IS PAUSED", 45, pause);
        //pause.setPrefSize(window_width, window_height);
        
        pause2.setAlignment(Pos.TOP_CENTER);
        pause2.setLayoutY(680);
        printer("PRESS 'Q' TO RESUME", 35, pause2);
        
        
        
        
        
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    if(shipDisplay.getTranslateX() != shipObj.getGap() && enableShip){ //checks if the ship is not at the edge (10px away)
                        shipSlide.setByX(-(shipObj.getWidth() + shipObj.getGap())); //formula to calculate position the ship should slide to (negative becuse moving to the left)
                        shipSlide.play(); //plays animation
                    }
                    break;
                case RIGHT:
                case D:
                    if(shipDisplay.getTranslateX() != (window_width-shipObj.getGap()-shipObj.getWidth()) && enableShip){ //700 (total window size) - one gap at the furthermost edge - size of one ship
                        shipSlide.setByX(shipObj.getWidth() + shipObj.getGap()); //same formula as previously but positive
                        shipSlide.play(); //plays animation
                    }
                    break;
                    
                case SPACE:
                    if(state == STATE.START && startCount == 0) {
                        state = STATE.GAME;
                        root.getChildren().add(shipDisplay);
                        HUD.initHearts();
                        root.getChildren().remove(startMessage);
                        root.getChildren().remove(starter);
                        root.getChildren().remove(starter2);
                        startCount++;
                    }
                    break;
                    
                case Q:
                    if(state == STATE.GAME && startCount > 0) {
                        state = STATE.START;
                        root.getChildren().add(pause);
                        root.getChildren().add(pause2);
                        root.getChildren().add(starter2);
                    } else if (state == STATE.START && startCount > 0) {
                        state = STATE.GAME;
                        root.getChildren().remove(pause);
                        root.getChildren().remove(pause2);
                        root.getChildren().remove(starter2);
                    }
                    break;
                    
                case ESCAPE:
                    System.exit(3);
                    break;
                    
                default: 
                    break;
            }
        });

        stage.show();
    }
    
    public static enum STATE {
        START,
        GAME,
        PAUSE
    };
    
    public static void main(String[] args) { 
        launch(args);
    }
}