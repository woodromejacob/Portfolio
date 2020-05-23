
import java.io.File;
import java.io.IOException;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * Bullet class for our shooting monster, keeps track of the bullets x and y info as well as its hitbox
 * @author woodrojc, limi, guilfojm
 *
 */
public class Bullet extends Shootables {

	public Shooter shooter;
/**
 * Constructs a new bullet object for the shooting monster
 * @param shooter
 * @param xPos
 * @param yPos
 * @param hero
 */
	public Bullet(Shooter shooter, int xPos, int yPos, Hero hero) {
		this.hero = hero;
		this.shooter = shooter;
		this.x = xPos;
		this.y = yPos;
		this.wH = 20;
		this.base = new Rectangle2D.Double(this.x, this.y, this.wH, this.wH);
		try {
			this.image = ImageIO.read(new File("fireball.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		detectHero();
	}
	/**
	 * moves the bullet in the direction that the hero was upon creation
	 */
	@Override
	public void move(ArrayList<Floor> floors, Hero hero) {
		this.x += this.xVelo;
		this.y += this.yVelo;
		this.hero.base.setRect(hero.x, hero.y, hero.width, hero.height);
		if (!hardMode) {
			for (Floor f : floors) {
				if (f.base.intersects(this.x, this.y, this.wH, this.wH)) {
					this.shooter.bulletsToRemove.add(this);
					this.hero.comp.shootablesToRemove.add(this);
				}
			}
		}
		if (hero.base.intersects(this.x, this.y, this.wH, this.wH)) {
			hero.health -= 50;
			this.shooter.bulletsToRemove.add(this);
			this.hero.comp.shootablesToRemove.add(this);
		}
		if (this.x > this.frameXAndAdjustment || this.x < 0 || this.y < 0 || this.y > this.frameYAndAdjustment) {
			this.shooter.bulletsToRemove.add(this);
			this.hero.comp.shootablesToRemove.add(this);
		}
		if (hardMode) {
			checkFloorCollisions(floors);
			}
		}
		// TODO Auto-generated method stub

	}


