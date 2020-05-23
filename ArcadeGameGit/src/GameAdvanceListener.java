import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/**
 * GameAdvanceListener class to help our game advance forward in time
 * @author woodrojc, limi, guilfojm
 *
 */
public class GameAdvanceListener implements ActionListener {

	private ArcadeGameComponent gameComponent;
/**
 * Creates a new listener
 * @param gameComponent
 */
	public GameAdvanceListener(ArcadeGameComponent gameComponent) {
		this.gameComponent = gameComponent;
	}
/**
 * tries to advance the game forward
 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			advanceOneTick();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
/**
 * has the components state get updated and redraws the screen
 * @throws IOException
 */
	public void advanceOneTick() throws IOException {
		//System.out.println("Current time " + System.currentTimeMillis());
		// The component uses user actions from listeners
		// to change the state of the game.

		// update screen
		this.gameComponent.updateState();
		this.gameComponent.drawScreen();
	}
}
