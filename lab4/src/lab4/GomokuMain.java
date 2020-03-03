package lab4;
import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

/**
 * The main class used to start an instance of the Gomoku program
 * @author Hjalmar & Tovah
 *
 */
public class GomokuMain extends java.lang.Object{
	
	/**
	 * 
	 * The main method
	 * @param args The port number the client should listen to
	 */
	public static void main(String[] args) {
		
		int portnr = 9001;
		if(args.length!=0) {
			portnr = Integer.parseInt(args[0]);
		}
		
		int portnr2 = 9002;
		if(args.length>=2) {
			portnr = Integer.parseInt(args[1]);
		}
		
		GomokuClient client = new GomokuClient(portnr);
		GomokuGameState game = new GomokuGameState(client);
		GomokuGUI gui = new GomokuGUI(game, client);
		
		GomokuClient client2 = new GomokuClient(portnr2);
		GomokuGameState game2 = new GomokuGameState(client2);
		GomokuGUI gui2 = new GomokuGUI(game2, client2);
		
	}
}
