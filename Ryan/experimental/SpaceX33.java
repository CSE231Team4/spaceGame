package spacex33;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

public class SpaceX33 extends Application {   
    private Scene gameScene;
    private Scene startScene;
    private Scene hudScene;
    private Scene pauseScene;
    private Scene gameOverScene;
    private Scene controlScene;
    private Scene highscoreScene;
    
    private Button startGame;
    
    private AnimationTimer timer; 
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private List<ImageView> powerups = new ArrayList<>(); //array list of powerups
    private List<Node> background = new ArrayList<>(); //array of 3 background images used for cycling
    private List<ImageView> shots = new ArrayList<>();
    private double rand_hold = 0;
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private ImageView astImg = new ImageView(); //create the imageviewer for the asteroids
    //private ImageView slowImg = new ImageView();
    private ImageView powerupImage = new ImageView();
    private final Ship shipObj = new Ship();
    private HeadsUpDisplay HUD = new HeadsUpDisplay();   
    private StartScreen startScreen = new StartScreen();
    private PauseScreen pauseScreen = new PauseScreen();
    private GameOver gameOver = new GameOver();
    private ControlScreen controlScreen = new ControlScreen();
    private HighscoreScreen highscoreScreen = new HighscoreScreen();
    File highscores = new File("./resource/Text/highscores.txt");
    
    private Image bck = new Image("file:resource/Images/space_background.png",true);
    private ImageView b1, b2;
    
    private Image start = new Image("file:resource/Images/start_game.png", true);
    private Image laser = new Image("file:resource/Images/laser.png", true);
    private SoundManager sounds = new SoundManager();
    
    //private final SlowTime slowObj = new SlowTime();
    
    private boolean enableShip = false;
    private boolean enableSlow = true;
    private int start_pause_none = 0;
    
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
    private double powerSpawn = 0.002;
    private double powerSpawnHold = 0;
    private boolean pickedUp = false;
    private Node toRemAst = new Asteroid();
    //private Node toRemSlow = new SlowTime();
    private Node toRemPower = new Powerup();
    private Node toRemShot = new Rectangle();
    private int shotsLeft = 10;
    int backnum = 0;
    Timeline POWER_UP_TIME;
    Duration time_hold;
    int[] scores = new int[101];
    
    private STATE state = STATE.START;

    private Parent createContent(){
        root = new Pane(); //initialize the pane
        initBackground();
        root.setId("pane"); //set the ID of the pane for the CSS file
        HUD.setShots(shotsLeft);
        HUD.updateAmmo();
        
        if(screenSize.getHeight() <= 950)
            window_height = 800;
        shipObj.setW_Height(window_height);
        HUD.setHeight(window_height);
        
        shipObj.setW_Height(window_height);
        
        root.setPrefSize(window_width, window_height); //set the size of the pane
        
        shipDisplay = shipObj.initGraphics(); //calls the initShip method to load in the graphics for the ship
        root.getChildren().add(b1);
        root.getChildren().add(b2);
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
    
    private Parent createStartScreen(){
        return startScreen.initStartScreen();
    }
    
    private Parent createPauseScreen(){
        return pauseScreen.initPauseScreen();
    }
    
    private Parent createGameOverScreen(){
        return gameOver.initGameOver();
    }
    
    private Parent createControlScreen() {
        return controlScreen.initControlScreen();
    }
    
    private Parent createHighscoreScreen(){
        return highscoreScreen.initHighscoreScreen();
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
        

        return astImg;
    }
    
    private void spawnShot() {
        if(shotsLeft > 0) {
            sounds.playLaserSound();
            ImageView shot = new ImageView(laser);
            shots.add(shot);
            shot.setTranslateX(shipDisplay.getTranslateX() + 45);
            shot.setTranslateY(shipDisplay.getTranslateY());
            root.getChildren().add(shot);
            shotsLeft--;
            HUD.setShots(shotsLeft);
            HUD.updateAmmo();
        }
    }
    
    private ImageView spawnPowerup(){
        Powerup powerup = new Powerup();
        powerupImage = powerup.initGraphics();
        powerupImage.setX(powerup.getType());
        int plane = getLane();
        int p_location = powerup.getEdgeGap() + plane * (powerup.getWidth() + powerup.getMidGap());
        powerupImage.setTranslateX(p_location);
        root.getChildren().add(powerupImage);
        
        return powerupImage;
    }
    
    private void refreshHUD(){
        hudScene.getRoot().toFront(); //makes sure the HUD is always on top of the obstacles/ship
    }
    
    
    private int getLane(){
        return (int)((Math.random() * 100) % NUM_LANES); //returns a number between 0-5 to decide which lane to spawn the asteroids
    }
    
    private void onUpdate() {
        if(state == STATE.GAME){
            sounds.pauseStartMusic();
            sounds.loopGameMusic();
            enableShip = true;
            shipDisplay.setVisible(true);
            asteroidUpdate();
            powerupUpdate();
            spawnUpdate();
            shotUpdate();
            refreshHUD();

            rand_hold = Math.random();

            if (rand_hold < spawnRate && rand_hold > 0.002) {
                asteroids.add(spawnAsteroid()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
            }
            if(rand_hold < powerSpawn && enableSlow == true){
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
            sounds.loopStartMusic();
            shipDisplay.setVisible(false);
        }
        
    }
    
    private void asteroidUpdate(){
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            //asteroid.setRotate(asteroid.getRotate() + speed);
            if(asteroid.getTranslateY() > window_height){
                root.getChildren().remove(asteroid);
                HUD.setScore(HUD.getScore() + 50);
                toRemAst = asteroid;
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
    
     private void shotUpdate() {
        for(ImageView shot : shots) {
            shot.setTranslateY(shot.getTranslateY() - 15);
            if(shot.getTranslateY() < 15) {
                toRemShot = shot;
                root.getChildren().remove(shot);
            }
        }
        shots.remove(toRemShot);
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
        checkAsteroidState();
        checkPowerupState();
        checkShotState();
    }
    
    private void checkAsteroidState(){
        for (Node asteroid : asteroids) {
            if (System.currentTimeMillis() - istart > iframes) {
                shipObj.checkShipState();
                iframes = 0;
                if (isCollision(asteroid, shipDisplay)) { //checks for an intersection between the asteroid and the ship 
                    iframes = 2000;
                    istart = System.currentTimeMillis();
                    root.getChildren().remove(asteroid);
                    sounds.playShipHit();
                    
                    //shipObj.setHealth(shipObj.getHealth()-1);
                    shipObj.setShipState();
              
                    HUD.hasHit();
                    if(HUD.numHearts() <= 0){
                        state = STATE.OVER;
                        enableShip = false;
                        timer.stop(); //stops the timer so asteroids no longer spawn
                        saveScore();
                        gameOverScene.getRoot().setVisible(true);
                        gameOverScene.getRoot().toFront();
                    }
                } 
            }
            else {
                iframes--;
            }
        }
    }
    
    private void checkPowerupState(){
        for(ImageView power : powerups){
            if(isCollision(power, shipDisplay)){
                sounds.playPickUp();
                toRemPower = power;
                root.getChildren().remove(power);
                
                switch((int)power.getX()){
                    case 0:
                        if(!pickedUp){
                        pickedUp = true;
                        HUD.hasSlowPower();
                        enableSlow = false;
                        speed = speed / 2;
                        spawnRate = spawnRate / 2;
                        powerSpawnHold = powerSpawn;
                        powerSpawn = 0;
                        timer();
                        }
                        break;
                    case 1: 
                        if(HUD.numHearts() < 5)
                        HUD.hasHeartPower();
                        break;
                    case 2:
                        if(shotsLeft < 9999)
                            shotsLeft += 10;
                        if(shotsLeft >= 9999)
                            shotsLeft = 9999;
                        HUD.setShots(shotsLeft);   
                        HUD.updateAmmo();
                        break;
                    default: break;
                }
            }
        }
        powerups.remove(toRemPower);
    }
    
    private void checkShotState(){
        
        for(ImageView shot : shots) {
            for(Node ast:asteroids){
                if(isCollision(shot, ast)){
                    sounds.playAsteroidKill();
                    toRemShot = shot;
                    toRemAst = ast;
                    HUD.setScore(HUD.getScore() + 100);
                    root.getChildren().remove(shot);
                    root.getChildren().remove(ast);
                }
            }
        }
        asteroids.remove(toRemAst);
        shots.remove(toRemShot);
    }
    
    private boolean isCollision(Node obstacle1, Node obstacle2){
        return obstacle1.getBoundsInParent().intersects(obstacle2.getBoundsInParent()); //returns whether or not the bounds intersect
    }
    
    
    
    int i;
    
    private void loadScore(){
        int store = 0;
        
        BufferedReader br;
        i = 1;
        scores[0] = HUD.getScore();
        try {
            br = new BufferedReader(new FileReader(highscores));
            String line = null;
            try {
                while((line = br.readLine()) != null){
                    scores[i] = Integer.valueOf(line);
                    if((line.equals("")) == false);
                    i++;
                }
                highscoreScreen.setLength(i);
                if(i >= 100)
                    i--; //i counts an extra time when leaving loop
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(SpaceX33.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpaceX33.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int j = 0; j < i; j++){
            for(int k = 0; k < i; k++){
                if(scores[k+1] > scores[k]){
                    store = scores[k];
                    scores[k] = scores[k+1];
                    scores[k+1] = store;
                }
            }
        }
    }
    
    private void saveScore(){
       loadScore();
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
    
    private void setHighscores(){
        highscoreScreen.setHighscores(scores);
    }

    private void reset(){
        for(Node asteroid : asteroids){
            root.getChildren().remove(asteroid);
        }
        for(Node powerup: powerups){
            root.getChildren().remove(powerup);
        }
        
        for(ImageView shot : shots)
            root.getChildren().remove(shot);
        
        asteroids.removeAll(asteroids);
        powerups.removeAll(powerups);
        root.getChildren().remove(HUD);
        speed = 7;
        spawnCount = 0;
        shotsLeft = 10;
        speedAdd = 0.05;
        spawnRate = 0.03; 
        
        iframes = 0;
        istart = 0;
        time_hold = null;
        enableSlow = true;
        shipDisplay.setTranslateX(shipObj.getGap());
        if(shipObj.isHit())
        shipObj.setShipState();
        shipDisplay.setVisible(false);
        HUD.setShots(shotsLeft);
        HUD.reset();
        highscoreScreen.setHighscores(scores);
    }
    
    private void timer() {
        POWER_UP_TIME = new Timeline (new KeyFrame(
        Duration.millis(5000),
        ae -> slowThings()));
        POWER_UP_TIME.play();
    }
    
    private void slowThings() {
        speed = speed * 2;
        spawnRate = spawnRate * 2;
        enableSlow = true;
        powerSpawn = powerSpawnHold;
        pickedUp = false;
    }
    
    private Button buttonFormatting(int x, int y){
        Button b = new Button();
        b.setLayoutX(x);
        b.setLayoutY(y);
        b.setAlignment(Pos.CENTER);
        return b;
    }
    

    
    @Override
    public void start(Stage stage) throws Exception {
        loadScore();
        setHighscores();
        gameScene = new Scene(createContent()); //creates the game scene
        hudScene = new Scene(createHUD()); //creates the HUD overlay
        startScene = new Scene(createStartScreen());
        pauseScene = new Scene(createPauseScreen());
        gameOverScene = new Scene(createGameOverScreen());
        controlScene = new Scene(createControlScreen());
        highscoreScene = new Scene(createHighscoreScreen());

        ((Pane)gameScene.getRoot()).getChildren().add(startScene.getRoot()); //adds the start scene
        ((Pane)gameScene.getRoot()).getChildren().add(hudScene.getRoot());
        ((Pane)gameScene.getRoot()).getChildren().add(pauseScene.getRoot());
        ((Pane)gameScene.getRoot()).getChildren().add(gameOverScene.getRoot());
        ((Pane)gameScene.getRoot()).getChildren().add(controlScene.getRoot());
        ((Pane)gameScene.getRoot()).getChildren().add(highscoreScene.getRoot());
        
        //the only scene that is visible to begin with is the start scene
        hudScene.getRoot().setVisible(false);
        pauseScene.getRoot().setVisible(false);
        gameOverScene.getRoot().setVisible(false);
        controlScene.getRoot().setVisible(false);
        highscoreScene.getRoot().setVisible(false);
        
        Button s = startScreen.getStartButton();
                s.setOnAction((ActionEvent event) -> {
                    start_to_game();
                });
        
        gameScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)

        stage.setScene(gameScene); 
        stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
        
        switch(state){
            case START:
                if(s.isPressed())
                    start_to_game();
                break;
            case GAME:
                startScene.getRoot().setVisible(false);
                break;
            default: 
                break;
                
        }
        
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
                    if(state == STATE.START)
                        start_to_game();
                    else if(state == STATE.GAME)
                        spawnShot();
                    break;
                case Q:
                    if(state == STATE.GAME)
                        game_to_pause();
                        
                    else
                        if(state == STATE.PAUSE)
                            pause_to_game();
                    break;
                case R:
                    if(state == STATE.PAUSE)
                        pause_to_start();  
                    
                    if(state == STATE.OVER)
                        over_to_start();
                    break;
                case C:
                    if(state == STATE.START || state == STATE.PAUSE)
                        to_control();
                    else
                        if(state == STATE.CONTROL)
                            exit_control();
                    break;
                case H:
                    if(state == STATE.START)
                        to_highscores();
                    else
                        if(state == STATE.SCORE)
                            exit_highscores();
                    break;
                case ESCAPE:
                    if(state == STATE.START || state == STATE.PAUSE){
                        sounds.stopAllSounds();
                        System.exit(3);
                    }
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
        CONTROL,
        SCORE
    };
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    //the methods from this point are used for when hotkeys/buttons are pressed
    
    //for going from the start scene to the game scene 
    private void start_to_game(){
        sounds.playButtonSelect();
        startScene.getRoot().setVisible(false);
        hudScene.getRoot().setVisible(true);

        state = STATE.GAME;
        start_pause_none = -1;
    }
    
    //for going from the game scene to the pause scene
    private void game_to_pause(){
        state = STATE.PAUSE;
        sounds.pauseGameMusic();
        start_pause_none = 1;
        
        pauseScene.getRoot().setVisible(true);
        pauseScene.getRoot().toFront();
        if(!enableSlow){
            time_hold = POWER_UP_TIME.getCurrentTime();
            POWER_UP_TIME.stop();
        }
    }
    
    //for going between the pause scene and the game scene
    private void pause_to_game(){
        state = STATE.GAME;
        start_pause_none = -1;
        pauseScene.getRoot().setVisible(false);
        controlScene.getRoot().setVisible(false);
        if(!enableSlow)
            POWER_UP_TIME.playFrom(time_hold);
    }
    
    //for going between the pause scene and the start scene
    private void pause_to_start(){
        reset();
        pauseScene.getRoot().setVisible(false);
        hudScene.getRoot().setVisible(false);
        startScene.getRoot().setVisible(true);
        state = STATE.START;
        start_pause_none = 0;
        if(!enableSlow)
            time_hold = null;
    }
    
    //for going from the over scene to the start scene
    private void over_to_start(){
        reset();
        gameOverScene.getRoot().setVisible(false);
        hudScene.getRoot().setVisible(false);
        state = STATE.START;
        start_pause_none = 0;
        gameOver.reset();
        startScene.getRoot().setVisible(true);
        timer.start();
    }
    
    //for going to the control scene
    private void to_control(){
        controlScene.getRoot().setVisible(true);
        controlScene.getRoot().toFront();
        state = STATE.CONTROL;
    }
    
    //for exiting the control scene
    private void exit_control(){
        controlScene.getRoot().setVisible(false);
        if(start_pause_none == 0)
            state = STATE.START;
        if(start_pause_none == 1)
            state = STATE.PAUSE;
    }
    
    //for going to the highscore scene
    private void to_highscores(){
        highscoreScene.getRoot().setVisible(true);
        state = STATE.SCORE;
    }
    
    //for exiting the highscore scene
    private void exit_highscores(){
        highscoreScene.getRoot().setVisible(false);
        state = STATE.START;
    }
}

