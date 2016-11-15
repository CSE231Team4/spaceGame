/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

/**
 *
 * @author asdas
 */
public class State {
    private STATE state;
    
    public State(){
        state = STATE.START;
    }
    
    public static enum STATE{
        START,
        GAME,
        PAUSE,
        OVER,
        CONTROL,
        SCORE
    };
    
    
    public STATE getState(){
        return state;
    }
    
    public void setState(STATE s){
        state = s;
    }
    
    public STATE game(){
        return STATE.GAME;
    }
    
    public STATE start(){
        return STATE.START;
    }
    
    public STATE pause(){
        return STATE.PAUSE;
    }
    
    public STATE over(){
        return STATE.OVER;
    }
   
    public STATE control(){
        return STATE.CONTROL;
    }
    
    public STATE score(){
        return STATE.SCORE;
    }
    
}
