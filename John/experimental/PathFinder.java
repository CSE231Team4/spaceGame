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
    private int reqSpots; //open spots wanted in row
	private int spots;
    private int block; //tracks sets of adjacent trues in rows of array
    
    public PathFinder(Ship ship, int laneNum) {
        lanes = laneNum;
        shipHeight = ship.getHeight();
        x = lanes;
        y = 5;
        spawnGrid = new boolean[x][y];
        reqSpots = 3;
        block = 0;
		spots = 0;
    }
    
    public boolean[][] pathGen() {
        //TODO: randomly select certain spots in each row to be true, path
            //of true spots is the path. Each row must have at least one spot
            //that is in previous row. Difficulty setting variable??
        //fill first row of chunk
        for(int j=0; j<x; j++) {
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
                if(spawnGrid[j][i-1]) {
                    block += 1;
					spots++;
                }
                else {
                    if(block == 1) {
						System.out.println("the part where block == 1");
                        spawnGrid[j][i] = true;
                    }
                    if(block > 1) {
						int z = chooseFromBlock(block);
						System.out.println("choice = " + z + " j = " + j + " block = " + block);
						spawnGrid[z + j - block + 1][i] = true;
						spots++;
                    }
                    spawnGrid[j][i] = false;
                    block = 0;
                }
				
				System.out.println("block = " + block);
                
				if(j == x - 1 && spots < reqSpots) {
					//code that adds in the last open spots
				}
//                if(spawnGrid[j][i])
//                    System.out.print("o");
//                else
//                    System.out.print("x");
            }
			
			for(int j=0; j<x; j++) {
				if(spawnGrid[j][i])
					System.out.print("o");
				else
					System.out.print("x");
			}
			spots = 0;
            System.out.println();
        }
        return spawnGrid;
    }
	
	private int chooseFromBlock(int size) {
		int choice;
		choice = ((int) Math.random() * 1000) % size;
		return choice;
	}
}
