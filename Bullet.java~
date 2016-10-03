package edu.oakland.production;
import edu.oakland.helper.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Bullet implements KeyListener, ActionListener{

	public int x, y, yInc;
	Timer timer;
	Image image;
	ImageIcon icon;
	public Rectangle crayon;
	boolean condition;
	
	public Bullet(){
		this.x = 200;
		this.y = 270;
		this.condition = false;

		icon = new ImageIcon("crayon.png");
		
		image = icon.getImage();
		timer = new Timer(1, this);
		timer.start();
		}
	public void paint(Graphics g){
		draw(g);
	}
	public void draw(Graphics g){
		
		if(condition == true){
		g.drawImage(image, this.x+13, y, null);
		g.drawImage(image, this.x-13, y, null);
		crayon = new Rectangle(x, y, 30, 30);
		}
		else{
		y=270;
		x=SpacePanel.newX;
		g.drawImage(image, this.x+13, y, null);
		g.drawImage(image, this.x-13, y, null);
		crayon = new Rectangle(x,y,30,30);
		}
     }
	@Override
	public void keyPressed(KeyEvent e){
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(condition == false){
				this.x -= 3;
		}	
	}
	if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(condition == false){
				this.x += 3;
		}
	}
	if(e.getKeyCode() == KeyEvent.VK_UP){
			condition = true;
			}
	}
	@Override
	public void keyReleased(KeyEvent arg0){
	// TODO Auto generated method stub
	}
	@Override
	public void keyTyped(KeyEvent arg0){
	// TODO Auto generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent arg0){
	// TODO Auto generated method stub
		if(condition == true){
			y = y-3;
		}
		if(y == -48){
			condition = false;
		}
}
}