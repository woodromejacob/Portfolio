import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
/**
 * HeroMovementListener to detect keystrokes to move the hero
 * @author woodrojc, limi, guilfojm
 *
 */
public class HeroMovementListener implements KeyListener {
	JFrame frame;
	ArcadeGameComponent comp;
/**
 * Creates a new HeroMovement listener
 * @param frame
 * @param comp
 */
	public HeroMovementListener(JFrame frame, ArcadeGameComponent comp) {
		this.frame = frame;
		this.comp = comp;
	}
/**
 * detects a key pressed and perform the appropriate code when pressed 
 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		// hero movements
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			comp.hero.left = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			comp.hero.right = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP) {
			comp.hero.up = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			comp.hero.down = true;}
	}
/**
 * detects a key released to sense simultaneous key presses
 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		// hero movements
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			comp.hero.left = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			comp.hero.right = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP) {
			comp.hero.up = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			comp.hero.down = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
