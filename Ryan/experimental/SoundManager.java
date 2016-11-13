/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

import javafx.scene.media.AudioClip;

/**
 *
 * @author asdas
 */
public class SoundManager {
    private AudioClip laserSound = new AudioClip("file:resource/Sounds/laser.wav");
    private AudioClip buttonSelect = new AudioClip("file:resource/Sounds/button_select.wav");
    private AudioClip pickUp = new AudioClip("file:resource/Sounds/pick_up.aiff");
    private AudioClip asteroidKill = new AudioClip("file:resource/Sounds/asteroid_kill.aiff");
    
    public SoundManager(){
        laserSound.setVolume(0.5);
        pickUp.setVolume(0.2);
        asteroidKill.setVolume(0.1);
    }
    
    public void playLaserSound(){
        laserSound.play();
    }
    
    public void playButtonSelect(){
        buttonSelect.play();
    }
    
    public void playPickUp(){
        pickUp.play();
    }
    
    public void playAsteroidKill(){
        asteroidKill.play();
    }
}
