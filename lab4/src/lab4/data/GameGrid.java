package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid
 */

public class GameGrid extends Observable {

	/**
	 * An empty square
	 */
	public static final int empty = 0;
	
	/**
	 * A square occupied by me
	 */
	public static final int me = 1;
	
	/**
	 * A square occupied by the enemy
	 */
	public static final int other = 2;
	int[][] board;
	
	/**
	 * The amount of squares needed in a row
	 */
	private static int inrow = 3;

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size) {
		board = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				board[i][j]=empty;
			}
		}
	}

	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return board[x][y];
	}

	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return board.length;
	}

	/**
	 * Enters a move in the game grid
	 * 
	 * @param x      the x position
	 * @param y      the y position
	 * @param player The player making a move
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		if(board[x][y] == empty) {
			board[x][y] = player;
			update();
			return true;
		}
		return false;
	}

	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid() {
		for(int i = 0; i < getSize(); i++) {
			for(int j = 0; j < getSize(); j++) {
				board[i][j]=empty;
			}
		}
		update();
	}

	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player) {
		for(int i = 0; i < getSize(); i++) {
			for(int j = 0; j < getSize(); j++) {
				if(checkRow(board[i], player, j)) {
					return true;
				}
				if(checkDown(board, player, j, i)) {
					return true;
				}
				if(checkDownRight(board, player, j, i)) {
					return true;
				}
				if(checkDownLeft(board, player, j, i)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkDownRight(int[][] a, int player, int startx, int starty) {
		for(int i=0; i < inrow; i++) {
			try {
				if(a[startx + i][starty + i]==player) {
					continue;
				}else {
					return false;
				}
			}catch(IndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkDownLeft(int[][] a, int player, int startx, int starty) {
		for(int i=0; i < inrow; i++) {
			try {
				if(a[startx - i][starty + i]==player) {
					continue;
				}else {
					return false;
				}
			}catch(IndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRow(int[] a, int player, int start) {
		for(int i = 0; i < inrow; i++) {
			try {
				if(a[start + i]==player) {
					continue;
				}else {
					return false;
				}
			}catch(IndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkDown(int[][] a, int player, int x, int y) {
		for(int i = 0; i < inrow; i++) {
			try {
				if(a[y + i][x] == player) {
					continue;
				}else {
					return false;
				}
			}catch(IndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;
	}
	
	private void update() {
		setChanged();
		notifyObservers();
	}

}