import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
/**
 * Abstract class for bullet and final boss bullet
 * @author woodrojc, guilfojm, limi
 *
 */
public abstract class Shootables {
	public Rectangle2D base;
	public int xVelo;
	public int yVelo;
	public int mag;
	public int x;
	public int y;
	public Hero hero;
	public int bulletSpeed = 30;
	public BufferedImage image;
	public int frameXAndAdjustment = 1550;
	public int frameYAndAdjustment = 950;
	public boolean hardMode = false;
	public int wH;
	/**
	 * move method implemented in individual classes
	 * @param floors
	 * @param hero
	 */
	public abstract void move(ArrayList<Floor> floors, Hero hero);
	/**
	 * method to detect the hero to toggle the xvelo and yvelo
	 */
	public void detectHero() {
		// TODO Auto-generated method stub
		this.xVelo = (int) (bulletSpeed * (hero.x - this.x)
				/ (Math.sqrt(Math.pow((hero.y - this.y), 2) + Math.pow((hero.x - this.x), 2))));
		this.yVelo = (int) (bulletSpeed * (hero.y - this.y)
				/ (Math.sqrt(Math.pow((hero.y - this.y), 2) + Math.pow((hero.x - this.x), 2))));
		
	}
	/**
	 * draws the shootable object on the screen
	 * @param g2
	 */
	public void drawOn(Graphics2D g2) {
		g2.drawImage(this.image, this.x, this.y, this.wH, this.wH, null);
		// TODO Auto-generated method stub
		
	}
	/**
	 * Checks bullet collisions with floors
	 * @param floors
	 */
	public void checkFloorCollisions(ArrayList<Floor> floors) {
		for (Floor f : floors) {
			if (xVelo > 0 && yVelo > 0 && this.y < f.y && this.x + this.wH > f.x) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.y = f.y - this.wH;
					this.yVelo = this.yVelo * (-1);
				}
			}
			if (xVelo > 0 && yVelo > 0 && this.x < f.x && this.y + this.wH > f.y) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.x = f.x - this.wH;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo > 0 && this.y < f.y && this.x < f.x + f.width) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.y = f.y - this.wH;
					this.yVelo = this.yVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo > 0 && this.x + this.wH > f.x + f.width && this.y + this.wH > f.y) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.x = f.x + f.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo > 0 && yVelo < 0 && this.y > f.y && this.x + this.wH > f.x) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.y = f.y + f.height;
					this.yVelo = this.yVelo * (-1);
				}
			}
			if (xVelo > 0 && yVelo < 0 && this.x < f.x && this.y < f.y + f.height) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.x = f.x - this.wH;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo < 0 && this.y > f.y && this.x < f.x + f.width) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.y = f.y + f.height;
					this.yVelo = this.yVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo < 0 && this.x + this.wH > f.x + f.width && this.y < f.y + f.height) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.x = f.x + f.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
		// TODO Auto-generated method stub
		
	}
	}

}
