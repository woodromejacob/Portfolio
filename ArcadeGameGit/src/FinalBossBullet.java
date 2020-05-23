import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
/**
 * FinalBoss bullet class to track x and y of fireballs that bowser shoots
 * @author woodrojc, limi, guilfojm
 *
 */
public class FinalBossBullet extends Shootables {
	public FinalBoss boss;
/**
 * Creates a new bullet 
 * @param finalBoss
 * @param xPos
 * @param yPos
 * @param hero
 */
	public FinalBossBullet(FinalBoss finalBoss, int xPos, int yPos, Hero hero) {
		// TODO Auto-generated constructor stub
		this.boss = finalBoss;
		this.x = xPos;
		this.y = yPos;
		this.hero = hero;
		this.wH = 40;
		this.base = new Rectangle2D.Double(this.x, this.y, this.wH, this.wH);
		try {
			this.image = ImageIO.read(new File("bowser_bullet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		detectHero();
	}
	/**
	 * Moves the bullet based off of the heros location
	 */
	@Override
	public void move(ArrayList<Floor> floors, Hero hero) {
		this.x += this.xVelo;
		this.y += this.yVelo;
		this.hero.base.setRect(hero.x, hero.y, hero.width, hero.height);
		if (hero.base.intersects(this.x, this.y, this.wH, this.wH)) {
			hero.health -= 100;
			this.boss.bulletsToRemove.add(this);
			this.hero.comp.shootablesToRemove.add(this);
		}
		if (this.x > this.frameXAndAdjustment || this.x < 0 || this.y < 0 || this.y > this.frameYAndAdjustment) {
			this.boss.bulletsToRemove.add(this);
			this.hero.comp.shootablesToRemove.add(this);
		}
		if (hardMode) {
			checkFloorCollisions(floors);
			}
		}
		// TODO Auto-generated method stub
		
	}


