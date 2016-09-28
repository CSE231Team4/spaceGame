package

import javax.swing.*;

public class Space extends JFrame{

	SpacePanel sp;
	private static final long serialVersionUID = 1L;
	public Space(){
	this.setTitle("Space");
	this.setSize(690, 400);

	sp = new SpacePanel();
		this.add(sp);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);

        }
	public static void main(String[] args){

		new Space();
	}
}