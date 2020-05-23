import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * Egg class to create eggs upon monster and shooter deaths, keeps track of x,y and hitbox
 * @author woodrojc, limi, guilfojm
 *
 */
public class Egg {
	
	
	public int x;
	public int y = 0;
	public int width = 60;
	public int height = 45;
	public Rectangle2D base;
	public int numMoves = 1;
	public BufferedImage image;
	public boolean ms;
	/**
	 * Creates a new egg above the monsters death location
	 * @param x
	 * @param ms
	 */
	public Egg(int x, boolean ms) {
		this.ms = ms;
		this.x = x;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		try {
			this.image = ImageIO.read(new File("mushroom01.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * Draws the egg on the screen	
 * @param g2
 */
	public void drawOn(Graphics2D g2) {
		//g2.drawOval(this.x, this.y, this.width, this.height);
		g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
	}
/**
 * Checks if the monster or shooter gets respawned if the egg hasnt been collected yet
 * @param hero
 */
	public void handleRespawn(Hero hero) {
		if (this.numMoves % 175 == 0) {
			if (this.ms) {
				hero.comp.eggsToRemove.add(this);
				Monster m = new Monster(hero.comp.hardMode);
				hero.comp.enemies.add(m);
			}
			else {
				hero.comp.eggsToRemove.add(this);
				Shooter s = new Shooter(hero, hero.comp.hardMode);
				hero.comp.enemies.add(s);
			}
		}
	}
/**
 * Moves the egg down with gravity
 * @param floors
 */
	public void move(ArrayList<Floor> floors) {
		this.numMoves++;
		this.y += 10;
		for (Floor f : floors) {
			if (f.base.intersects(this.x, this.y, this.width, this.height)) {
				this.y = f.y - this.height;
			}
		}
		// TODO Auto-generated method stub
		
	}
/**
 * handles collision with hero
 * @param hero
 */
	public void handleHeroCollision(Hero hero) {
		// TODO Auto-generated method stub
		this.base.setRect(this.x, this.y, this.width, this.height);
		if (this.base.intersects(hero.x, hero.y, hero.width, hero.height)) {
			hero.comp.eggsToRemove.add(this);
	}
		
	}
}
