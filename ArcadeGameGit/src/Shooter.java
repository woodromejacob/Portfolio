
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
/**
 * Shooter class to deal with monsters that shoot, tracks x,y and hitbox
 * @author woodrojc, guilfojm, limi
 *
 */
public class Shooter extends Enemies{

	public boolean hardMode = false;
	public boolean setFasterSpeed = true;
	public Hero hero;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
	public boolean direction_left = false;
	public boolean shoot_moment = false;
	public int shoot_counter = 0;
	public File shooter_img = new File("shooter.png");
	public File shooter_attack_img = new File("shooter_attack.png");
/**
 * Constructs a new shooter object when read in a text file
 * @param xPos
 * @param yPos
 * @param lives
 * @param hero
 */
	public Shooter(int xPos, int yPos, int lives, Hero hero) {
		this.x = xPos;
		this.y = yPos;
		this.width = 60;
		this.height = 45;
		this.yVelo = 2;
		this.lives = lives;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		this.hero = hero;
		try {
			this.image = ImageIO.read(shooter_img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * Creates another shooter when egg isnt collected by hero	
 * @param hero
 * @param hardMode
 */
	public Shooter(Hero hero, boolean hardMode) {
		this.hardMode = hardMode;
		Random r = new Random();
		this.x = r.nextInt(1450);
		this.y = r.nextInt(740);
		this.yVelo = 2;
		this.width = 60;
		this.height = 45;
		this.lives = 1;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		this.hero = hero;
		try {
			this.image = ImageIO.read(shooter_img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * moves the shooter
 */
	public void move(ArrayList<Floor> floors) {
		try {
			shooterAnimation();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(setFasterSpeed) {
			checkHardMode();
			this.setFasterSpeed = false;
		}
		this.numMoves++;
		if (!hardMode) {
			if (this.numMoves % 60 == 0) {
				this.shoot();
			}
		}
		else {
			if (this.numMoves % 20 == 0) {
				this.shoot();
			}
		}
		this.y += this.yVelo;
		if (this.numMoves % 13 == 0) {
			this.yVelo *= -1;
		}
		checkFrameCollisionsX();
		checkFloorCollisionsX(floors);
		if(shoot_moment) {
			shoot_counter++;
		}
		if(shoot_counter%10 ==0) {
			this.shoot_moment = false;
			shoot_counter = 0;
		}
	}
/**
 * Checks the x frame collisions
 */
	private void checkFrameCollisionsX() {
		// TODO Auto-generated method stub
		if (this.xVelo > 0) {
			if (this.x + this.xVelo + this.width <= frameW - this.width) {
				this.x += this.xVelo;
			} else {
				this.x = Shooter.frameW - this.width - this.xVelo;
				this.xVelo = this.xVelo * (-1);
				this.direction_left = true;
			}
		}
		if (this.xVelo < 0) {
			if (this.x + this.xVelo >= 0) {
				this.x += this.xVelo;
			} else {
				this.x = 0;
				this.xVelo = this.xVelo * (-1);
				this.direction_left = false;
			}
		}
		
	}
/**
 * Checks the y frame collisions
 * @param floors
 */
	private void checkFloorCollisionsX(ArrayList<Floor> floors) {
		// TODO Auto-generated method stub
		for (Floor f : floors) {
			if (f.base.intersects(this.x, this.y, this.width, this.height)) {
				if (f.x <= this.x + this.width && f.x >= this.x) {
					this.x = f.x - this.width;
					this.xVelo = this.xVelo * (-1);
				} else if (this.x + this.width > f.x + f.width) {
					this.x = f.x + f.width;
					this.xVelo = this.xVelo * (-1);
				}
			}
		}
		
	}
/**
 * Has the shooter shoot a Bullet object
 */
	private void shoot() {
		this.shoot_moment = true;
		Bullet bullet = new Bullet(this, this.x + this.width, this.y + this.height / 2, hero);
		if (hardMode) {
			bullet.hardMode = true;
		}
		this.bullets.add(bullet);
		this.hero.comp.shootables.add(bullet);
		// TODO Auto-generated method stub

	}
/**
 * Moves the shooter with gravity
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
 * Removes the appropriate bullets
 */
	public void removeBullets() {
		for (Bullet b : bulletsToRemove) {
			bullets.remove(b);
		}
		bulletsToRemove.clear();
		// TODO Auto-generated method stub

	}
/**
 * Checks if hard mode is toggled
 */
	public void checkHardMode() {
		if (hardMode) {
			xVelo = 20;
		}
	}
/**
 * Flips the given image
 * @param image
 * @return
 */
	public BufferedImage flip(BufferedImage image) {
		BufferedImage fliped = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = image.getWidth() - 1; x > 0; x--) {
			for (int y = 0; y < image.getHeight(); y++) {
				fliped.setRGB(image.getWidth() - x, y, image.getRGB(x, y));
			}
		}
		return fliped;
	}
	/**
	 * Deals with animations for the shooter
	 * @throws IOException
	 */
	public void shooterAnimation() throws IOException {
		if (direction_left) {
			this.image = ImageIO.read(shooter_img);
		}
		if (!direction_left) {
			this.image = flip(ImageIO.read(shooter_img));
		}
		if (direction_left && shoot_moment) {
			this.image = flip(ImageIO.read(shooter_attack_img));
		}
		if (!direction_left && shoot_moment) {
			this.image = ImageIO.read(shooter_attack_img);
		}
	}
/**
 * Handles collisions with the hero
 */
	@Override
	public void handleHeroCollision(Hero hero) {
		// TODO Auto-generated method stub
		this.base.setRect(this.x, this.y, this.width, this.height);
		if (this.base.intersects(hero.x, hero.y, hero.width, hero.height)) {
			if (hero.y <= this.y) {
				hero.comp.enemiesToRemove.add(this);
				createShooterEgg(hero);
			}
			if (hero.y > this.y) {
				hero.health -= 100;
			}
		}
		
	}
	/**
	 * Creates an egg upon shooter death
	 * @param hero
	 */
	public void createShooterEgg(Hero hero) {
		Egg egg = new Egg(this.x, false);
		hero.comp.eggs.add(egg);
	}
}
