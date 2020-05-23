
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
/**
 * Monster class for the regular monster
 * @author woodrojc, guilfojm, limi
 *
 */
public class Monster extends Enemies {

	public boolean hardMode = false;
	public boolean setFasterSpeed = true;
	public int frameWidthAdj = 18;
/**
 * Creates a new monster based off of coordinates in the text files
 * @param xPos
 * @param yPos
 * @param lives
 */
	public Monster(int xPos, int yPos, int lives) {
		this.x = xPos;
		this.y = yPos;
		this.height = 50;
		this.width = 50;
		this.yVelo = 10;
		this.lives = lives;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		try {
			this.image = ImageIO.read(new File("monster.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Creates a monster when egg isnt collected
	 * @param hardMode
	 */
	public Monster(boolean hardMode) {
		this.hardMode = hardMode;
		Random r = new Random();
		this.x = r.nextInt(1450);
		this.height = 50;
		this.width = 50;
		this.yVelo = 10;
		this.y = r.nextInt(740);
		this.lives = 1;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		try {
			this.image = ImageIO.read(new File("monster.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**
 * Moves the monsters
 */
	public void move(ArrayList<Floor> floors) {
		if(setFasterSpeed) {
			checkHardMode();
			this.setFasterSpeed = false;
		}
		if (this.yVelo > 0) {
			if (this.y + this.yVelo + this.height < frameH) {
				this.y += this.yVelo;
				this.numMoves++;
			} else {
				this.y = frameH - this.height;
				this.yVelo = this.yVelo * (-1);
				this.numMoves = 0;
			}
		} else if (this.yVelo < 0) {
			if (this.y + this.yVelo > 0) {
				this.y += this.yVelo;
				this.numMoves++;
			} else {
				this.y = 0;
				this.yVelo = this.yVelo * (-1);
				this.numMoves = 0;
			}
		}
		if (this.xVelo > 0) {
			if (this.x + this.xVelo + this.width <= frameW - this.frameWidthAdj) {
				this.x += this.xVelo;
			} else {
				this.x = Monster.frameW - this.width - this.frameWidthAdj;
				this.xVelo = this.xVelo * (-1);
			}
		} else if (this.xVelo < 0) {
			if (this.x + this.xVelo >= 0) {
				this.x += this.xVelo;
			} else {
				this.x = 0;
				this.xVelo = this.xVelo * (-1);
			}}
		checkFloorCollisionsX(floors);
		checkFloorCollisionsY(floors);}
	/**
	 * Checks vertical floor collisions
	 * @param floors
	 */
	private void checkFloorCollisionsY(ArrayList<Floor> floors) {
		// TODO Auto-generated method stub
		for (Floor f : floors) {
			if (xVelo > 0 && yVelo > 0 && this.y < f.y && this.x + this.width > f.x) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.y = f.y - this.height;
					this.yVelo = this.yVelo * (-1);
					this.numMoves = 0;
				}
			}
			if (xVelo < 0 && yVelo > 0 && this.y < f.y && this.x < f.x + f.width) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.y = f.y - this.height;
					this.yVelo = this.yVelo * (-1);
					this.numMoves = 0;
				}
			}
			if (xVelo > 0 && yVelo < 0 && this.y > f.y && this.x + this.width > f.x) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.y = f.y + f.height;
					this.yVelo = this.yVelo * (-1);
					this.numMoves = 0;
				}
			}
			if (xVelo < 0 && yVelo < 0 && this.y > f.y && this.x < f.x + f.width) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.y = f.y + f.height;
					this.yVelo = this.yVelo * (-1);
					this.numMoves = 0;}}}}
/**
 * Checks floor collisions in horizontal direction
 * @param floors
 */
	private void checkFloorCollisionsX(ArrayList<Floor> floors) {
		for (Floor f : floors) {
			if (xVelo > 0 && yVelo > 0 && this.x < f.x && this.y + this.height > f.y) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.x = f.x - this.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo > 0 && this.x + this.width > f.x + f.width && this.y + this.height > f.y) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.x = f.x + f.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo > 0 && yVelo < 0 && this.x < f.x && this.y < f.y + f.height) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.x = f.x - this.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
			if (xVelo < 0 && yVelo < 0 && this.x + this.width > f.x + f.width && this.y < f.y + f.height) {
				if (f.base.intersects(this.x, this.y, this.width, this.height)) {
					this.x = f.x + f.width;
					this.xVelo = this.xVelo * (-1);}}}}
/**
 * Moves the monsters based on gravity
 * @param floors
 */
	public void moveGravity(ArrayList<Floor> floors) {
		this.y += 2;
		for (Floor f : floors) {
			if (f.base.intersects(this.x, this.y, this.width, this.height)) {
				this.y = (int) (f.y - this.height);
			}
		}
	}
/**
 * Checks if hard mode is toggled
 */
	public void checkHardMode() {
		if (hardMode) {
			yVelo = 20;
			xVelo = 20;
		}

	}
/**
 * Handles monster collisions with heros
 */
	@Override
	public void handleHeroCollision(Hero hero) {
		// TODO Auto-generated method stub
		this.base.setRect(this.x, this.y, this.width, this.height);
		if (this.base.intersects(hero.x, hero.y, hero.width, hero.height)) {
			if (hero.y <= this.y) {
				hero.comp.enemiesToRemove.add(this);
				createMonsterEgg(hero);
			}
			if (hero.y > this.y) {
				hero.health -= 100;
			}
		}	
	}
	/**
	 * Creates an egg if the monster dies
	 * @param hero
	 */
	public void createMonsterEgg(Hero hero) {
		Egg egg = new Egg(this.x, true);
		hero.comp.eggs.add(egg);
	}
}
