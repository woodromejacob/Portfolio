import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * FinalBoss class for our final boss object (Bowser) keeps track of x, y and hitbox
 * @author woodrojc, limi, guilfojm
 *
 */
public class FinalBoss extends Enemies {
	
	private boolean moveX = false;
	private boolean moveY = true;
	private boolean shoot_moment = false;
	public Hero hero;
	ArrayList<FinalBossBullet> bullets = new ArrayList<FinalBossBullet>();
	ArrayList<FinalBossBullet> bulletsToRemove = new ArrayList<FinalBossBullet>();
	public int animationCount = 0;
	public int shootCount = 0;
	File stand = new File("bowser_stand.png");
	File shoot1 = new File("bowser_shoot1.png");
	File shoot2 = new File("bowser_shoot2.png");
	File walk1 = new File("bowser_walk1.png");
	File walk2 = new File("bowser_walk2.png");
	File walk3 = new File("bowser_walk3.png");
/**
 * Constructs a new final boss object
 * @param hero
 */
	public FinalBoss(Hero hero) {
		this.x = 50;
		this.y = 250;
		this.height = 200;
		this.width = 250;
		this.yVelo = 10;
		this.hero = hero;
		this.lives = 1;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		try {
			this.image = ImageIO.read(new File("bowser_stand.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**
 * Moves the boss based off of the heros location and changes the animation
 */
	public void move(ArrayList<Floor> floors) {
		try {
			finalBossAnimation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.numMoves++;
		if (this.moveY) {
			this.y += this.yVelo;
		}
		if (this.moveX) {
			followHero();
			this.x += this.xVelo;
			if (this.numMoves % 20 == 0) {
				shoot();
			}
		}
		for (Floor f : floors) {
			if (this.y + this.height > f.y) {
				this.y = f.y - this.height;
				this.numMoves = 0;
				this.moveX = true;
				this.moveY = false;
			}
		}
		
		// shoot once
		if (shoot_moment) {
			shootCount++;
		}
		if (shootCount % 7 == 0) {
			this.shoot_moment = false;
			shootCount = 0;
		}
	}
/**
 * Tracks the heros location and changes bowsers location based off of it
 */
	public void followHero() {
		// TODO Auto-generated method stub
		if (hero.x > this.x + this.width) {
			this.xVelo = 10;
		}
		if (hero.x < this.x) {
			this.xVelo = -10;
		}
	}
/**
 * Shoots a FinalBoss bullet object
 */
	private void shoot() {
		this.shoot_moment = true;
		FinalBossBullet b = new FinalBossBullet(this, this.x + this.width / 2, this.y + this.height / 2, this.hero);
		bullets.add(b);
		this.hero.comp.shootables.add(b);
		// TODO Auto-generated method stub

	}
/**
 * Flips the image
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
 * deals with the animation for bowser
 * @throws IOException
 */
	public void finalBossAnimation() throws IOException {
		if (this.moveX) {
			this.animationCount++;
			if (xVelo > 0) {
				if (this.animationCount % 12 == 0) {
					this.image = ImageIO.read(walk1);
				}
				if (this.animationCount % 12 == 3 || this.animationCount % 10 == 9) {
					this.image = ImageIO.read(walk2);
				}
				if (this.animationCount % 12 == 6) {
					this.image = ImageIO.read(walk3);
				}
				if (this.shoot_moment) {
					this.image = ImageIO.read(shoot2);
				}
			}
			if (xVelo < 0) {
				if (this.animationCount % 12 == 0) {
					this.image = flip(ImageIO.read(walk1));
				}
				if (this.animationCount % 12 == 3 || this.animationCount % 10 == 9) {
					this.image = flip(ImageIO.read(walk2));
				}
				if (this.animationCount % 12 == 6) {
					this.image = flip(ImageIO.read(walk3));
				}
				if (this.shoot_moment) {
					this.image = flip(ImageIO.read(shoot2));
				}
			}
		}
	}
/**
 * Handles when mario collides with Bowser
 */
	@Override
	public void handleHeroCollision(Hero hero) {
		// TODO Auto-generated method stub
		this.base.setRect(this.x, this.y, this.width, this.height);
		if (this.base.intersects(hero.x, hero.y, hero.width, hero.height)) {
			if (hero.y <= this.y) {
				hero.comp.endGame();
			}
			if (hero.y > this.y) {
				hero.health -= 100;
			}
		}
		
	}
}
