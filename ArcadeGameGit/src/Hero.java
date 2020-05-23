
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * Hero class (mario) to be the hero in our game
 * @author woodrojc, limi, guilfojm
 *
 */
public class Hero {

	private static int frameW = 1500;
	private static int frameH = 900;
	public int x;
	public int y;
	public int height = 75;
	public int width = 50;
	public int yVelo = 30;
	public int xVelo = 20;
	public int lives;
	public int health = 100;
	public Rectangle2D base;
	public boolean left = false;
	public boolean right = false;
	public boolean up = false;
	public boolean down = false;
	public boolean direction_left = false;

	private BufferedImage image;
	public File stand = new File("mario_stand.png");
	public File walk = new File("mario_walk.png");
	public File walk2 = new File("mario_walk2.png");
	public File jump = new File("mario_jump.png");
	public int animation_count = 0;
	public ArcadeGameComponent comp;
/**
 * Creates the hero for the game
 * @param xPos
 * @param yPos
 * @param lives
 * @param comp
 */
	public Hero(int xPos, int yPos, int lives, ArcadeGameComponent comp) {
		this.x = xPos;
		this.comp = comp;
		this.y = yPos;
		this.lives = lives;
		this.base = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
		try {
			this.image = ImageIO.read(stand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * draws the hero on the screen
	 * @param g2
	 */
	public void drawOn(Graphics2D g2) {
		// g2.drawRect(this.x, this.y, this.width, this.height);
		g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
		// TODO Auto-generated method stub

	}
/**
 * Moves the hero 
 * @param floors
 * @throws IOException
 */
	public void move(ArrayList<Floor> floors) throws IOException {
		heroAnimation();
		moveGravity(floors);
		// TODO Auto-generated method stub
		if (up) {
			if (this.y - this.yVelo >= 0) {
				this.y -= this.yVelo;
			} else {
				this.y = 0;
			}
		}
		if (left) {
			if (this.x - this.xVelo >= 0) {
				this.x -= this.xVelo;
			} else {
				this.x = 0;
			}
		}
		if (down) {
			if (this.y + this.yVelo + this.height <= 855) {
				this.y += this.yVelo;
			} else {
				this.y = Hero.frameH - this.height - 45;
			}
		}
		if (right) {
			if (this.x + this.xVelo + this.width <= 1482) {
				this.x += this.xVelo;
			} else {
				this.x = Hero.frameW - this.width - 18;
			}
		}

		checkFloors(floors);

	}
/**
 * Chekcs collisions with floors upon key movement
 * @param floors
 */
	private void checkFloors(ArrayList<Floor> floors) {
		for (Floor f : floors) {
			if (f.base.intersects(this.x, this.y, this.width, this.height)) {
				if (down && this.y < f.y) {
					this.y = (int) (f.y - this.height);
				} else if (left && this.x + this.width > f.x + f.width) {
					this.x = (int) (f.x + f.width);
				} else if (right && this.x < f.x) {
					this.x = (int) (f.x - this.width);
				} else if (up && this.y > f.y) {
					this.y = (int) (f.y + f.height);
				}
			}
		}
	
}
	/**
	 * Moves the hero down with gravity checks floor collisions with gravity
	 * @param floors
	 */
	public void moveGravity(ArrayList<Floor> floors) {
		this.y += 10;
		for (Floor f : floors) {
			if (f.base.intersects(this.x, this.y, this.width, this.height)) {
				this.y = (int) (f.y - this.height);
			}
		}
	}
/**
 * Flips the image for the animations
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
 * Handles the hero animations
 * @throws IOException
 */
	public void heroAnimation() throws IOException {
		// animation
		this.animation_count++;
		if (left) {
			//has the hero walk left appropriately, so on and so forth
			if (this.animation_count % 3 == 0) {
				this.image = ImageIO.read(stand);
			}
			if (this.animation_count % 3 == 1) {
				this.image = ImageIO.read(walk);
			}
			if (this.animation_count % 3 == 2) {
				this.image = ImageIO.read(walk2);
			}
			this.direction_left = true;
		}
		if (right) {
			if (this.animation_count % 3 == 0) {
				this.image = flip(ImageIO.read(stand));
			}
			if (this.animation_count % 3 == 1) {
				this.image = flip(ImageIO.read(walk));
			}
			if (this.animation_count % 3 == 2) {
				this.image = flip(ImageIO.read(walk2));
			}
			this.direction_left = false;
		}
		if (down) {
			if (direction_left) {
				this.image = ImageIO.read(walk2);
			}
			if (!direction_left) {
				this.image = flip(ImageIO.read(walk2));
			}
		}
		if (up) {
			if (direction_left) {
				this.image = ImageIO.read(jump);
			}
			if (!direction_left) {
				this.image = flip(ImageIO.read(jump));
			}
		}
		if (!left && !right && !down && !up) {
			if (direction_left) {
				this.image = ImageIO.read(stand);
			}
			if (!direction_left) {
				this.image = flip(ImageIO.read(stand));
			}
		}
	}
}
