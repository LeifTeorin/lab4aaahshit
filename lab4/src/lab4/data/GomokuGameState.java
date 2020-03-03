package lab4.data;

import java.util.Observable;
import java.util.Observer;


import lab4.client.GomokuClient;
/**
 * Represents the state of a game
 */

public class GomokuGameState extends Observable implements Observer{

   // Game variables
	/**
	 * The default amount of squares
	 */
	public final int DEFAULT_SIZE = 5;
	private GameGrid gameGrid;
	
    //Possible game states
	/**
	 * The game is currently not started
	 */
	public final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
	private final int OTHERS_TURN = 2;
	private final int FINISHED = 3;
	
	private int currentState;
	
	private GomokuClient client;
	
	private String message;
	
	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc){
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}
	
	/**
	 * Returns the games current state
	 * @return The games current state
	 */
	public int currentState() {
		return currentState;
	}

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString(){
		return message;
	}
	
	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid(){
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y){
		if(currentState == MY_TURN) {
			if(gameGrid.move(x, y, gameGrid.me) == true) {
				client.sendMoveMessage(x, y);
				currentState = OTHERS_TURN;
				if(gameGrid.isWinner(gameGrid.me) == true) {
					currentState = FINISHED;
					message = "You're a win";
					setChangedandNotify();
				}else {
					message = "That is allowed, other players turn";
					currentState = OTHERS_TURN;
					setChangedandNotify();
				}
				
			}else {
				message = "Illegal move, by the law";
				setChangedandNotify();
			}
		}
	}
	
	/**
	 * Starts a new game with the current client
	 */
	public void newGame(){
		gameGrid.clearGrid();
		currentState = OTHERS_TURN;
		message = "New game has started";
		client.sendNewGameMessage();
		setChangedandNotify();
	}
	
	/**
	 * Other player has requested a new game, so the 
	 * game state is changed accordingly
	 */
	public void receivedNewGame(){
		gameGrid.clearGrid();
		currentState = MY_TURN;
		message = "New game has started";
		setChangedandNotify();
	}
	
	/**
	 * The connection to the other player is lost, 
	 * so the game is interrupted
	 */
	public void otherGuyLeft(){
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "Other player has disconnected :,(";
		setChangedandNotify();
		client.disconnect();
	}
	
	/**
	 * The player disconnects from the client
	 */
	public void disconnect(){
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "Disconnected";
		setChangedandNotify();
		client.disconnect();
	}
	
	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y){
		if(gameGrid.move(x, y, gameGrid.other)==true) {
			if(gameGrid.isWinner(gameGrid.other) == true) {
				currentState = FINISHED;
				message = "A player has won";
				setChangedandNotify();
			}else {
				message = "Other player has made their move, it is now your turn";
				currentState = MY_TURN;
				setChangedandNotify();
			}
				
		}else {
			message = "Illegal move, by the law";
			setChangedandNotify();
		}
	}
	
	private void setChangedandNotify() {
		setChanged();
		notifyObservers();
	}
	
	public void update(java.util.Observable o, java.lang.Object arg) {
		
		switch(client.getConnectionStatus()){
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHERS_TURN;
			break;
		}
		
	}
	
}