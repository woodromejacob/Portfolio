
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 * ArcadeGameComponent class that handles drawing all of our game features on the screen
 * and removes them when necessary
 * 
 * @author woodrojc, limi, guilfojm
 * 
 */
public class ArcadeGameComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	public Hero hero;
	public LevelMaker levelMaker;
	ArrayList<Enemies> enemies = new ArrayList<Enemies>();
	ArrayList<Enemies> enemiesToRemove = new ArrayList<Enemies>();
	ArrayList<Shootables> shootables = new ArrayList<Shootables>();
	ArrayList<Shootables> shootablesToRemove = new ArrayList<Shootables>();
	ArrayList<Egg> eggs = new ArrayList<Egg>();
	ArrayList<Egg> eggsToRemove = new ArrayList<Egg>();
	ArrayList<Floor> floors = new ArrayList<Floor>();
	public int numLevelMoves = 0;
	public boolean hardMode = false;
	public int score = 0;
	public String name;
	private BufferedImage backgroundImage;
	public int ticksIn30Seconds = 660;
	public int numTicksIn1Sec = 22;

/**
 * Sets up everything to be drawn, creates the level maker and loads in the background image
 * @param hard
 * @param frame
 */
	public ArcadeGameComponent(boolean hard, JFrame frame) {
		this.frame = frame;
		this.hardMode = hard;
		Hero hero = new Hero(750, 500, 3, this);
		this.hero = hero;
		LevelMaker levelMaker;
		try {
			levelMaker = new LevelMaker(this, this.hero);
			this.levelMaker = levelMaker;
			if(hardMode) {
				this.levelMaker.hardMode = true;
			}
			this.levelMaker.setUpLevel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.backgroundImage = ImageIO.read(new File("framebackground.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * paint component method that draws everything on the screen
 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.backgroundImage, 0, 0, 1500, 900, null);
		hero.drawOn(g2);
		this.levelMaker.makeFloorsAndMonsters();
		drawFloors(g2);
		drawEnemiesAndShootablesAndEggs(g2);
		
	}
/**
 * handles drawing our 'enemy' objects on the screen
 * @param g2
 */
	private void drawEnemiesAndShootablesAndEggs(Graphics2D g2) {
		for (Enemies e : enemies) {
			e.drawOn(g2);
		}
		for (Shootables s : shootables) {
			s.drawOn(g2);
		}
		for (Egg e : eggs) {
			e.drawOn(g2);
		}
	}
/**
 * draws the floors
 * @param g2
 */
	private void drawFloors(Graphics2D g2) {
		for (Floor f : floors) {
			f.drawOn(g2);
		}
	}
/**
 * Based on arraylists containing the enemy objects (eggs, monsters, shooters, and final boss)
 * determines if the next level needs to be loaded when they are empty (all killed)
 */
	public void nextLvl() {
		if (enemies.isEmpty() && eggs.isEmpty() && this.levelMaker.level == this.levelMaker.numLevels) {
			endGame();
		}
		if (enemies.isEmpty() && eggs.isEmpty()) {
			this.score += this.levelMaker.levelPoints * getMyTimeFactor();
			this.levelMaker.lvlInc(this);
			this.hero.health = 100;
		}
	}
/**
 * Checks the time factor for added the scoring, quicker level is completed higher of a multiplier the base score gets
 * @return
 */
	private int getMyTimeFactor() {
		int timeFactor;
		int x = this.ticksIn30Seconds - this.numLevelMoves;
		if (x <= 0) {
			timeFactor = 1;
		}
		else {
			timeFactor = x / this.numTicksIn1Sec ;
		}
		return timeFactor;
	}
/**
 * Updates the state after every tick is fired from the game advance listener
 * @throws IOException
 */
	public void updateState() throws IOException {
		// move hero
		this.numLevelMoves++;
		this.hero.move(floors);
		for (Enemies e : enemies) {
			e.move(floors);
		}
		for (Shootables s : shootables) {
			s.move(this.floors, this.hero);
		}
		for (Egg e : eggs) {
			e.move(this.floors);
		}
		checkHeroCollision();
		heroDeath();	
		nextLvl();
		frame.setTitle("JOUST! Level: " + this.levelMaker.level + " - Health: " + this.hero.health + " - Lives: "
				+ this.hero.lives + " - Score: " + this.score);
	}
/**
 * Checks if the hero is out of health and resets the level
 */
	public void heroDeath() {
		if (hero.health <= 0) {
			hero.lives--;
			hero.health = 100;
			this.numLevelMoves = 0;
			this.eggs.clear();
			this.levelMaker.tearDown();
			this.levelMaker.setUpLevel();
		}
		if (hero.lives == 0) {
			this.levelMaker.tearDown();
			endGame();
		}
	}
/**
 * disposes the frame and ends the current game, prompts for the name and score to be written in our text file
 */
	public void endGame()  {
		frame.dispose();
		try {
		Thread.sleep(100);}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		writeNameAndScore(this.score);
		// TODO Auto-generated method stub
		
	}
/**
 * displays a text window to put the name and the users score in a text file for the highscore system
 * @param score
 */
	public void writeNameAndScore(int score) {
		String name = JOptionPane.showInputDialog("Thanks for playing Super Mario Joust! Please enter your name so we can put you in our highscore database:");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("highscores.txt", true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.hero.lives = -1;
		pw.println(score);
		pw.println(name);
		pw.flush();
		pw.close();
		}
		// TODO Auto-generated method stub
		
/**
 * also called after every tick to redraw the component and revalidate the frame to make sure everything is updated
 */
	public void drawScreen() {
		this.frame.revalidate();
		this.repaint();
		// TODO Auto-generated method stub
	}
/**
 * Calls all the enemy objects handleHeroCollision method which determines what to do if the hero makes contact with it.  
 */
	public void checkHeroCollision() {
		for (Enemies e : enemies) {
			e.handleHeroCollision(this.hero);
		}
		for (Egg e : eggs) {
			e.handleHeroCollision(this.hero);
			e.handleRespawn(this.hero);
		}
		for (Egg e : eggsToRemove) {
			eggs.remove(e);
		}
		eggsToRemove.clear();
		for (Enemies e : enemiesToRemove) {
			enemies.remove(e);
		}
		for (Shootables s : shootablesToRemove) {
			shootables.remove(s);
		}
		enemiesToRemove.clear();
	}
}
