package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 */

public class GamePanel extends JPanel implements Observer{
	/**
	 * The size in pixels of one square
	 */
	public final int UNIT_SIZE = 20;
	private lab4.data.GameGrid grid;
	
	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 */
	public GamePanel(GameGrid grid){
		this.grid = grid;
		grid.addObserver(this);
		Dimension d = new Dimension(grid.getSize()*UNIT_SIZE+1, grid.getSize()*UNIT_SIZE+1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Returns a grid position given pixel coordinates
	 * of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y){
		int w = x/(UNIT_SIZE);
		int h = y/(UNIT_SIZE);
		return new int[] {h, w};
	}
	
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	private void paintBoard(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, grid.getSize()*UNIT_SIZE, grid.getSize()*UNIT_SIZE);
	}
	
	private void paintRects(Graphics g) {
		g.setColor(Color.gray);
		for(int i = 0; i < grid.getSize(); i++) {
			for(int j = 0; j < grid.getSize(); j++) {
				g.fillRect(2+i*UNIT_SIZE, 2+j*UNIT_SIZE, UNIT_SIZE-2, UNIT_SIZE-2);
			}
		}
	}
	
	private void paintPlayers(Graphics g) {
		
		for(int i = 0; i < grid.getSize(); i++) {
			for(int j = 0; j < grid.getSize(); j++) {
				if(grid.getLocation(i, j) == grid.me) {
					g.setColor(Color.white);
					g.fillOval(j*UNIT_SIZE+2, i*UNIT_SIZE+2, UNIT_SIZE-2, UNIT_SIZE-2);
				}else if(grid.getLocation(i, j) == grid.other){
					g.setColor(Color.red);
					g.fillOval(j*UNIT_SIZE+2, i*UNIT_SIZE+2, UNIT_SIZE-2, UNIT_SIZE-2);
				}
				
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		paintBoard(g);
		paintRects(g);
		paintPlayers(g);
	}
	
}