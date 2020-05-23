import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
/**
 * Abstract class for our enemies, includes final boss, shooters, and monsters
 * @author woodrojc
 *
 */
public abstract class Enemies {
	public static int frameW = 1500;
	public static int frameH = 900;

	public int x;
	public int y;
	public int height;
	public int width;
	public int yVelo;
	public int xVelo = 10;
	public int lives;
	public int numMoves = 0;
	public Rectangle2D base;
	public BufferedImage image;
	public boolean hardMode = false;
	public boolean setFasterSpeed = true;
	/**
	 * Implements draw on since it is identical
	 * @param g2
	 */
	public void drawOn(Graphics2D g2) {
		g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
	}
	/**
	 * handleheroCollision and move are implemented in individual classes since they are different
	 * @param hero
	 */
	public abstract void handleHeroCollision(Hero hero);
	public abstract void move(ArrayList<Floor> floors);
	
}
