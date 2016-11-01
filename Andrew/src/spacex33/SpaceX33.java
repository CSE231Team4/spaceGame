package spacex33;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpaceX33 extends Application {   
    private Scene gameScene;
    private Scene startScene;
    private Scene hudScene;
    private Scene pauseScene;
    private Scene controlScene;
    
    private AnimationTimer timer; 
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private List<ImageView> powerups = new ArrayList<>(); //array list of powerups
    private List<Node> background = new ArrayList<>(); //array of 3 background images used for cycling
    private double rand_hold = 0;
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private ImageView astImg = new ImageView(); //create the imageviewer for the asteroids
    //private ImageView slowImg = new ImageView();
    private ImageView powerupImage = new ImageView();
    private final Ship shipObj = new Ship();
    private HeadsUpDisplay HUD = new HeadsUpDisplay();   
    private StartScreen startScreen = new StartScreen();
    private PauseScreen pauseScreen = new PauseScreen();
    private ControlScreen controlScreen = new ControlScreen();
    File highscores = new File("./resource/Text/highscores.txt");
    
    private Image bck = new Image("file:resource/Images/space_background.png",true);
    private ImageView b1, b2;
    
    //private final SlowTime slowObj = new SlowTime();
    
    private boolean enableShip = false;
    private boolean enableSlow = true;
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
    private int backnum = 0;
    private int cNum;
    
    private STATE state = STATE.START;

    private Parent createContent(){
        root = new Pane(); //initialize the pane
        initBackground();
        root.setId("pane"); //set the ID of the pane for the CSS file
        
       
        if(screenSize.getHeight() <= 900)
            window_height = 800;
        shipObj.setW_Height(window_height);
        
        root.setPrefSize(window_width, window_height); //set the size of the pane
        
        shipDisplay = shipObj.initGraphics(); //calls the initShip method to load in the graphics for the ship
        root.getChildren().add(b1);
        root.getChildren().add(b2);
        root.getChildren().add(shipDisplay); //adds the shipDisplay to the root so it can be displayed
        shipDisplay.setVisible(false);

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
    
    private Parent createStartScreen(){
        return startScreen.initStartScreen();
    }
    
    private Parent createPauseScreen(){
        return pauseScreen.initPauseScreen();
    }
    
    private Parent createControlScreen() {
        return controlScreen.initControlScreen();
    }
    
    private void initBackground(){
        b1 = new ImageView(bck);
        b1.setScaleY(1.1);
        b1.setScaleX(1.1);
        background.add(b1);
        b2 = new ImageView(bck);
        b2.setScaleX(1.1);
        b2.setScaleY(1.1);
        b2.setTranslateY(window_height);
        background.add(b2);
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
    
    private void onUpdate() {
        if(state == STATE.GAME){
            enableShip = true;
            shipDisplay.setVisible(true);
            asteroidUpdate();
            powerupUpdate();
            spawnUpdate();

            rand_hold = Math.random();

            if (rand_hold < spawnRate && rand_hold > 0.002) {
                asteroids.add(spawnAsteroid()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
            }
            if(rand_hold < 0.002 && enableSlow == true){
                powerups.add(spawnPowerup());
            }
            checkState();
        }
        
        if(state != STATE.PAUSE){
            backgroundUpdate();
        }
        
        if(state == STATE.PAUSE){
            enableShip = false;
        }
        if(state == STATE.START){
            shipDisplay.setVisible(false);
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
            bg.setTranslateY(bg.getTranslateY() + speed/3);
            
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
        HUD.updateScore();
        for (Node asteroid : asteroids) {
            if (System.currentTimeMillis() - istart > iframes) {
                shipObj.checkShipState();
                iframes = 0;
                if (isCollision(asteroid, shipDisplay)) { //checks for an intersection between the asteroid and the ship 
                    iframes = 2000;
                    istart = System.currentTimeMillis();
                    root.getChildren().remove(asteroid);
                    
                    //shipObj.setHealth(shipObj.getHealth()-1);
                    shipObj.setShipState();
              
                    HUD.hasHit();
                
                    if(HUD.numHearts() <= 0){
                        state = STATE.OVER;
                        enableShip = false;
                        timer.stop(); //stops the timer so asteroids no longer spawn
                        saveScore();
                        gameOverText();                      
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
            if(isCollision(power, shipDisplay)){
                toRemPower = power;
                root.getChildren().remove(power);
                
                switch((int)power.getX()){
                    case 0:
                        HUD.hasSlowPower();
                        enableSlow = false;
                        speed = speed / 2;
                        spawnRate = spawnRate / 2;
                        timer(); 
                        break;
                    case 1: 
                        if(HUD.numHearts() < 5)
                        HUD.hasHeartPower();
                    default: break;
                }
            }
        }
        powerups.remove(toRemPower);
    }
    
    private boolean isCollision(Node obstacle1, Node obstacle2){
        return obstacle1.getBoundsInParent().intersects(obstacle2.getBoundsInParent()); //returns whether or not the bounds intersect
    }
    
    private void saveScore(){
        int store = 0;
        int[] scores = new int[100];
        BufferedReader br;
        int i = 1;
        scores[0] = HUD.getScore();
        try {
            br = new BufferedReader(new FileReader(highscores));
            String line = null;
        
            try {
                while((line = br.readLine()) != null){
                    scores[i] = Integer.valueOf(line);
                    i++;
                }
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(SpaceX33.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpaceX33.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int j = 0; j < i-1; j++){
            for(int k = 0; k < i; k++){
                if(scores[k+1] > scores[k]){
                    store = scores[k];
                    scores[k] = scores[k+1];
                    scores[k+1] = store;
                }
            }
        }

        FileWriter writer;
        try {
            writer = new FileWriter(highscores, false);
            for(int k = 0; k < i; k++){
                writer.write(scores[k] + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SpaceX33.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gameOverText(){
        String lose = "GAME OVER";
        HBox hBox = new HBox();
        hBox.setPrefSize(window_width, window_height);
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
    
    private void reset(){
        for(Node asteroid : asteroids){
            root.getChildren().remove(asteroid);
        }
        for(Node powerup: powerups){
            root.getChildren().remove(powerup);
        }
        asteroids.removeAll(asteroids);
        powerups.removeAll(powerups);
        root.getChildren().remove(HUD);
        speed = 7;
        spawnCount = 0;
        speedAdd = 0.05;
        spawnRate = 0.03; 
        iframes = 0;
        istart = 0;
        HUD.reset();
    }
    
    private void timer() {
        Timeline POWER_UP_TIME = new Timeline (new KeyFrame(
        Duration.seconds(5.0),
        ae -> slowThings()));
        POWER_UP_TIME.play();
    }
    
    /*private void scoreTimer() {
        Timeline SCORE_INCREMENT = new Timeline (new KeyFrame(
        Duration.seconds(2.0),
        ae -> score()));
        SCORE_INCREMENT.play();
    }*/
    
    private void slowThings() {
        speed = speed * 2;
        spawnRate = spawnRate * 2;
        enableSlow = true;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        gameScene = new Scene(createContent()); //creates the game scene
        hudScene = new Scene(createHUD()); //creates the HUD overlay
        startScene = new Scene(createStartScreen());
        pauseScene = new Scene(createPauseScreen());
        controlScene = new Scene(createControlScreen());
        ((Pane)gameScene.getRoot()).getChildren().add(startScene.getRoot()); //puts the HUD overlay over the game screen
        gameScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)

        stage.setScene(gameScene); 
        stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
        
        TranslateTransition shipSlide = new TranslateTransition(Duration.millis(100), shipDisplay); //sets up the ship to slide over the course of 100ms (0.1s). Change the Duration.millis to make the animation slower or faster
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
                    if(state == STATE.START && cNum % 2 == 0){
                    ((Pane)gameScene.getRoot()).getChildren().remove(startScene.getRoot());
                    ((Pane)gameScene.getRoot()).getChildren().remove(controlScene.getRoot());
                    ((Pane)gameScene.getRoot()).getChildren().add(hudScene.getRoot());
                    state = STATE.GAME;
                    }
                     
                    break;
                case Q:
                    if(state == STATE.GAME){
                        state = STATE.PAUSE;
                        //((Pane)gameScene.getRoot()).getChildren().remove(hudScene.getRoot()); 
                        ((Pane)gameScene.getRoot()).getChildren().add(pauseScene.getRoot());
                    }
                    else{
                        if(state != STATE.START && state != STATE.OVER && cNum % 2 == 0){
                            state = STATE.GAME;
                            ((Pane)gameScene.getRoot()).getChildren().remove(pauseScene.getRoot());
                            ((Pane)gameScene.getRoot()).getChildren().remove(controlScene.getRoot());
                            cNum = 0;
                            //((Pane)gameScene.getRoot()).getChildren().add(hudScene.getRoot()); 
                        }
                    }
                    break;
                case R:
                    if(state == STATE.PAUSE && cNum % 2 == 0){
                        reset();
                        ((Pane)gameScene.getRoot()).getChildren().remove(pauseScene.getRoot());
                        ((Pane)gameScene.getRoot()).getChildren().remove(hudScene.getRoot());
                        ((Pane)gameScene.getRoot()).getChildren().remove(controlScene.getRoot());
                        state = STATE.START;
                        ((Pane)gameScene.getRoot()).getChildren().add(startScene.getRoot());
                        cNum = 0;
                        
                    }   
                    break;
                    
                case C:
                    if(state == STATE.START || state == state.PAUSE) {
                        if(cNum % 2 == 0) {
                        ((Pane)gameScene.getRoot()).getChildren().add(controlScene.getRoot());
                        } else
                            ((Pane)gameScene.getRoot()).getChildren().remove(controlScene.getRoot());
                        }
                    cNum++;
                    break;
                    
                case ESCAPE:
                    if(state != STATE.GAME)
                    System.exit(3);
                    break;
                default:
                    break;
            }
        });
        stage.show();
    }
    
    public static enum STATE{
        START,
        GAME,
        PAUSE,
        OVER,
    };
    
    public static void main(String[] args) {
        launch(args);
    }
}

