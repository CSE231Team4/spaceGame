/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacex33;

/**
 * Backwards pathfinding algorithm
 * @author Johnathon Robertson
 */

public class PathFinder {
    //generate a path to spawn asteroids around
    private int lanes;
    private float shipHeight;
    private boolean spawnGrid[][];
    private int x;
    private int y;
    private int openSpots; //max open spots wanted in row
    
    public PathFinder(Ship ship, int laneNum) {
        lanes = laneNum;
        shipHeight = ship.getHeight();
        x = lanes;
        y = 50;
        spawnGrid = new boolean[x][y];
    }
    
    public boolean[][] pathGen() {
        //TODO: randomly select certain spots in each row to be true, path
            //of true spots is the path. Each row must have at least one spot
            //that is in previous row. Difficulty setting variable??
        //fill first row of chunk
        for(int j=x; j>0; j--) {
            if((int)(Math.random() * 100) % 2 == 0)
                spawnGrid[j][0] = true;
            if(spawnGrid[j][0])
                System.out.print("o");
            else
                System.out.print("x");
        }
        System.out.println();
        //iterates through every spot in the grid, by row and every lane in row)
        for(int i=1; i<y; i++) {
            //iterate through lanes
            for(int j=0; j<x; j++) {
                if(spawnGrid[j][i-1])
                    spawnGrid[j][i] = true;
                else
                    spawnGrid[j][i] = false;
                
                if(spawnGrid[j][i])
                    System.out.print("o");
                else
                    System.out.print("x");
            }
            System.out.println();
        }
        return spawnGrid;
    }
}
