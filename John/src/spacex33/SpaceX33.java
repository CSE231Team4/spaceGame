package spacex33;

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

public class SpaceX33 extends Application {   
    private AnimationTimer timer; 
    private Pane root; //declare the root pane
    private List<Node> asteroids = new ArrayList<>(); //array list of asteroids
    private Node shipDisplay = new Ship(); //create the display node for the ship
    private ImageView astImg = new ImageView(); //create the imageviewer for the ship
    private Ship shipObj = new Ship();
    private int boundX = 700; //x coordinate of frame
    private int boundY = 900;  //y coordinate of frame
    private int speed = 5; //speed of asteroids descending toward ship
    private int spawnCount = 0; //counts amount of asteroids spawned in
    private int speedAdd = 0; //used in algorithm to calculate difficulty. speedAdd = 1/10 of spawnCount
    private double spawnRate = 0.01; //spawn in speed of asteroids
    private boolean dSpeed = false; //used to check w/s speed modifier key activation
    
    final int NUM_LANES = 6; //number of lanes for obstacles to spawn
    
    private Parent createContent(){
        root = new Pane(); //initialize the pane
        root.setPrefSize(boundX, boundY); //set the size of the pane
        root.setId("pane"); //set the ID of the pane for the CSS file
        
        shipDisplay = initShip(); //calls the initShip method to load in the graphics for the ship
        
        root.getChildren().add(shipDisplay); //adds the shipDisplay to the root so it can be displayed
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;  
    }
    
    private ImageView initShip(){
        return shipObj.initShipGraphics(); //gets the graphic loaded in the Ship class
    }
    
    private Node spawnObstacle() {
        Asteroid ast = new Asteroid();
        astImg = ast.initAsteroidGraphics(); //gets the graphic loaded in the Asteroid class
        
        int lane = getLane(); //gets a lane number between 0-5
        int obs_location = ast.getEdgeGap() + lane * (ast.getWidth() + ast.getMidGap()); //formula for calculating the X coordinate of the obstacle
        astImg.setTranslateX(obs_location); //sets the x coordinate of the obstacle to obs_location

        root.getChildren().add(astImg); //adds the graphic for the asteroid to the root
        spawnCount++; //adds 1 to spawnCount when new obsticle is created
        return astImg;
    }
    
    private int getLane(){
        return (int)((Math.random() * 100) % NUM_LANES); //returns a number between 0-5 to decide which lane to spawn the asteroids
    }
    
    private void onUpdate() {
        for (Node asteroid : asteroids){
            asteroid.setTranslateY(asteroid.getTranslateY() + speed); //translates the asteroid down 12 pixels every update
            if(asteroid.getTranslateY() > 900){              
                System.out.println(asteroids.size());
                root.getChildren().remove(asteroid);
                asteroids.remove(asteroid);
                  //this was my attempt at saving memory... doesn't work too well but makes it slightly more efficient
            }
        }

        if (Math.random() < spawnRate) {
            asteroids.add(spawnObstacle()); //randomly spawns an obstacle, and adds the graphic for the asteroid to the asteroid list
        }

        checkState();
        if (spawnRate < 2.0) {//sets limit on spawning
        if (spawnCount == 25) { //initial condition to add to spawnRate
            spawnRate = spawnRate + 0.005; //add to spawnRate if conditions are met
            spawnCount = 0; // reset spawn counter
            speedAdd = speedAdd + 1; // add 1 to speed add for every 10 spawn count
                if (speedAdd == 1) { //condition to add to speed of obsticles
                    speed = speed + 5; //add 5 to speed of falling obsticles
                    speedAdd = 0; //reset speed add counter
                }
        }
      }
    }
    
    private void checkState() {
        for (Node asteroid : asteroids) {
            if (asteroid.getBoundsInParent().intersects(shipDisplay.getBoundsInParent())) { //checks for an intersection between the asteroid and the ship
                timer.stop(); //stops the timer so asteroids no longer spawn
                shipDisplay.setVisible(false); //sets the ship to be invisible (most likely will be changed, but this is the current solution)
                
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
                //the above section is used for printing the "YOU LOSE" font
                return;
            }
        } 
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //sets the stylesheet of the scene (used for the background image)
        
        stage.setScene(scene); 
        //stage.setResizable(false); //makes it not possible to resize the window since this breaks the game
        //BUG: with this enabled, the asteroids look like the overlap the edge of the window, check it yourself
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), shipDisplay); //sets up the ship to slide over the course of 100ms (0.1s). Change the Duration.millis to make the animation slower or faster
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    if(dSpeed == false) { //checks to see if you already doubled speed
                        speed = speed*2; //doubles speed if initial condition met
                        dSpeed = true; //set double speed to true
                    }
                    break;
                case S:
                    if(dSpeed == true) { //checks to see if you have doubled speed
                        speed = speed/2; //sets speed back to regular condition
                        dSpeed = false; //sets double speed to false
                    }
                    break;                
                case A:
                    if(shipDisplay.getTranslateX() != shipObj.getGap()){ //checks if the ship is not at the edge (10px away)
                        tt.setByX(-(shipObj.getWidth() + shipObj.getGap())); //formula to calculate position the ship should slide to (negative becuse moving to the left)
                        tt.play(); //plays animation
                    }
                    break;
                case D:
                    if(shipDisplay.getTranslateX() != (700-shipObj.getGap()-shipObj.getWidth())){ //700 (total window size) - one gap at the furthermost edge - size of one ship
                        tt.setByX(shipObj.getWidth() + shipObj.getGap()); //same formula as previously but positive
                        tt.play(); //plays animation
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
