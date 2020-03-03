package lab4.gui;
import javax.swing.*;
import java.util.Observable;
import java.util.Observer;
import java.awt.Dimension;
import java.awt.event.*;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;
import lab4.gui.GamePanel;

/*
 * The GUI class
 */
public class GomokuGUI implements Observer {

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JFrame frame;
	private GamePanel gameGridPanel;
	private JButton connectButton;
	private JButton disconnectButton;
	private JButton newGameButton;
	private JLabel messageLabel;
	private ConnectionWindow window;
	private static int windownr = 0;
	
	/**
	 * The constructor
	 * 
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		gameGridPanel.setVisible(true);
		client.addObserver(this);
		gamestate.addObserver(this);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(gamestate.DEFAULT_SIZE * gameGridPanel.UNIT_SIZE + 20, gamestate.DEFAULT_SIZE * gameGridPanel.UNIT_SIZE + 100);
		messageLabel = new JLabel();
		if(windownr == 0) {
			frame.setLocation(100, 150);
			windownr++;
		}else {
			frame.setLocation(500, 150);
			windownr++;
		}
		
		
		MouseAdapter mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				gamestate.move(gameGridPanel.getGridPosition(e.getX(), e.getY())[0],
						(gameGridPanel.getGridPosition(e.getX(), e.getY()))[1]);
			}
		};
		
		gameGridPanel.addMouseListener(mouseListener);
		
		connectButton = new JButton("Connect");
		
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				window = new ConnectionWindow(client);
				messageLabel.setText("Try to connect to a player");
			}
			
		});
			
		disconnectButton = new JButton("Disconnect");
		
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.disconnect();
				messageLabel.setText("disconnected from game");
			}
		});
		
		newGameButton = new JButton("New game");
		
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.newGame();
				messageLabel.setText("A new game has been started");
			}
		});
		
		frame.setVisible(true);
		
		if(gamestate.currentState()==gamestate.NOT_STARTED) {
			newGameButton.setEnabled(false);
			disconnectButton.setEnabled(false);
		}
		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(connectButton);
		buttonBox.add(newGameButton);
		buttonBox.add(disconnectButton);
		
		Box labelBox = Box.createHorizontalBox();
		labelBox.add(messageLabel);
		
		Box mainBox = Box.createVerticalBox();
		mainBox.add(gameGridPanel);
		mainBox.add(buttonBox);
		mainBox.add(labelBox);
		
		frame.add(mainBox);
		
	}
	
	
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
		
	}
	
}