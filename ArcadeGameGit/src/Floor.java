
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Floor class for the brick walls drawn on the screen 
 * @author woodrojc, guilfojm, limi
 *
 */
public class Floor {
	public int x;
	public int y;
	public int width;
	public int height;
	public Rectangle2D base;
	private BufferedImage image;
	public int numImages;
	public int imageWidth = 50;
/**
 * Creates a new platform based off parameters in our text file
 * @param x
 * @param y
 * @param width
 * @param height
 */
	public Floor(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.base = new Rectangle2D.Double(this.x,this.y,this.width,this.height);
		try {
			this.image = ImageIO.read(new File("brick2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.numImages = this.width / 50;
		
	}
	/**
	 * draws on the images in small increments so images dont look stretched
	 * @param g2
	 */
	public void drawOn(Graphics2D g2) {
		//g2.fillRect(this.x, this.y, this.width, this.height);
		for (int k = 0; k < numImages; k++) {
		g2.drawImage(this.image, this.x + k * this.imageWidth, this.y, this.imageWidth, this.height, null);
		}
		// TODO Auto-generated method stub

	}

}
