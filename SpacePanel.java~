package edu.oakland.helper;
import edu.oakland.production.*;

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

public class SpacePanel extends JPanel implements KeyListener, ActionListener{
// this is creates the paint and draw

	private static final long serialVersionUID = 1L;
	private int alienX;
	private int x, wait;
	public static int newX = 200;
	boolean left;
	boolean right;
	Timer time;
	

	public Image img; // the images, spaceships, and google images
	public Image img2;
	public Image img3;
	public ImageIcon icon;
	public ImageIcon icon2;
	public ImageIcon icon3;
	public Bullet bullet;

	public Rectangle enemyRect;
	public boolean explode;
	public Graphics g;

	public SpacePanel(){

		alienX = 0;
		left = false;
		right = true;
		x = 200;
		wait = 0;
		explode = false;
		this.setSize(900, 350);

		this.setVisible(true);
		this.setFocusable(true);
		icon = new ImageIcon("spaceship1.png");
		icon2 = new ImageIcon("image/weeSpaceShip.png");
		icon3 = new ImageIcon("image/explode12.png");
		img = icon.getImage();
		img2 = icon2.getImage();
		img3 = icon3.getImage();
		bullet = new Bullet();
		time = new Timer(5, this);
		time.start();
		addKeyListener(this);
		addKeyListener(bullet);

	}
	
	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, 700, 375);
		bullet.draw(g);
		if(explode == false){
			g.drawImage(img, alienX, 20, this);
		}else if(explode == true){
			g.drawImage(img3, alienX, 20, this);
			wait++;
		}
		if(wait == 100){explode = false; wait = 0;}
		
		enemyRect = new Rectangle(alienX, 20, 134, 134);
		g.drawImage(img2, x, 300, this);
		//collision
		if(enemyRect.x < bullet.crayon.x && enemyRect.x + enemyRect.width > bullet.crayon.x
			&& enemyRect.y < bullet.crayon.y && enemyRect.y + enemyRect.height > bullet.crayon.y){
		explode = true;
		}

		alienMovement();
		repaint();
	}
	public void alienMovement(){
	// this is the big ship
		if(alienX < 700 && right == true){
			if(explode == false){
				alienX += 1;
				if(alienX == 699){
				left = true;
				right = false;
			}
		}
	}
	if(alienX < 700 && left == true){
		if(explode == false){
		alienX -= 1;
		if(alienX == -200){
			right = true;
			left = false;
		}	
	}
 }
}

	@Override
	public void keyPressed(KeyEvent e){
	// TODO Auto-generated method stub

	
	if(e.getKeyCode() == KeyEvent.VK_LEFT){
		x -= 5;
		newX = x;
	}
	if(e.getKeyCode()== KeyEvent.VK_RIGHT){
		x += 5;
		newX = x;
	}
	if(e.getKeyCode()== KeyEvent.VK_UP){
	}
   }
	@Override
	public void keyReleased(KeyEvent e){
		// TODO Auto-generated method stub
	}
	@Override
	public void keyTyped(KeyEvent e){
	// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent e){
	// TODO Auto-generated method stub
	}




}