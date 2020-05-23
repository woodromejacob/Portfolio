
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
/**
 * Start Screen Component class to be the component for our start screen
 * @author woodrojc, guilfojm, limi
 *
 */
public class StartScreenComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage marioLogo;
	private BufferedImage joustLogo;
	private BufferedImage multiply;
	private BufferedImage marioLetters;
	private BufferedImage joustLetters;
/**
 * Creates a new start screen comp
 * @param frame
 */
	public StartScreenComponent(JFrame frame) {
		try {
			this.marioLogo = ImageIO.read(new File("mariobg.png"));
			this.joustLogo = ImageIO.read(new File("joustlogo.png"));
			this.multiply = ImageIO.read(new File("multiply.png"));
			this.marioLetters = ImageIO.read(new File("marioletters.png"));
			this.joustLetters = ImageIO.read(new File("joustletters.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	@Override
	/**
	 * Paints the images on the screen
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.joustLetters, 30, 20, 450, 300, null);
		g2.drawImage(this.marioLogo, 1000, 20, 450, 300, null);
		g2.drawImage(this.multiply, 640, 70, 200, 200, null);
		g2.drawImage(this.marioLetters, 450, 600, 300, 150, null);
		g2.drawImage(this.joustLogo, 765, 600, 300, 150, null);
		
	}
}
