
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * LevelMaker class that reads text files to create our levels
 * @author woodrojc, limi, guilfojm
 *
 */
public class LevelMaker {
	public Scanner scanner;
	public ArrayList<File> filesList = new ArrayList<File>();
	public ArrayList<String> fileNames = new ArrayList<String>();
	public int level = 1;
	public ArcadeGameComponent comp;
	private Hero hero;
	public int numLevels = 5;
	private int monsterCount;
	private int numRegularMonsters;
	public int levelPoints = 0;
	public boolean hardMode = false;
	public boolean finalLevel = false;
/**
 * Constructs a new levelmaker object
 * @param comp
 * @param hero
 * @throws FileNotFoundException
 */
	public LevelMaker(ArcadeGameComponent comp, Hero hero) throws FileNotFoundException {
		this.hero = hero;
		for (int k = 1; k <= numLevels; k++) {
			String name = "level" + k + ".txt";
			fileNames.add(name);
		}
		makeFiles();
		this.comp = comp;
	}
/**
 * Makes our files with the appropriate names to be called laters
 */
	private void makeFiles() {
		// TODO Auto-generated method stub
		for (String name : fileNames) {
			File f = new File(name);
			filesList.add(f);
		}

	}
/**
 * Sets up the current level by reading and creating the floors, checking if the final level is current level, reading monster locations and HSL (hero start)
 */
	public void setUpLevel() {
		try {
			this.scanner = new Scanner(filesList.get(this.level - 1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adjustLevel();
		isFinalLevel();
		if (! this.finalLevel) {
		getRegularMonsterCount();}
		setHeroStart();
		makeFloorsAndMonsters();
		if (this.finalLevel) {
		makeFinalBoss();
		}
	}
/**
 * Makes the final boss, if it is the final level
 */
	public void makeFinalBoss() {
		// TODO Auto-generated method stub
		FinalBoss bowser = new FinalBoss(this.hero);
		this.comp.enemies.add(bowser);
		
	}
/**
 * reads the text file to determine if it is the final level
 */
	public void isFinalLevel() {
		if (scanner.nextLine().equals("FL")) {
			if (scanner.nextLine().equals("yes")) {
				this.levelPoints = 200;
				this.finalLevel = true;
			}
		}
		// TODO Auto-generated method stub
		
	}
/**
 * gets the count of monsters
 */
	public void getRegularMonsterCount() {
		if (scanner.nextLine().equals("Number of Monsters")) {
			this.numRegularMonsters = Integer.parseInt(scanner.nextLine());
		}
		// TODO Auto-generated method stub

	}
/**
 * sets the heros starting location 
 */
	public void setHeroStart() {
		if (scanner.nextLine().equals("HSL")) {
			this.hero.x = Integer.parseInt((scanner.nextLine()));
			this.hero.y = Integer.parseInt((scanner.nextLine()));
		}
		// TODO Auto-generated method stub

	}
/**
 * gets the level number from the text file
 */
	public void adjustLevel() {
		if (scanner.nextLine().equals("Level")) {
			this.level = Integer.parseInt((scanner.nextLine()));
		}
	}
/**
 * makes the floors and monsters from the text files
 */
	public void makeFloorsAndMonsters() {
		this.monsterCount = 0;
		while (scanner.hasNextLine()) {
			if (scanner.nextLine().equals("Floor")) {
				Floor floor = new Floor(Integer.parseInt((scanner.nextLine())), Integer.parseInt((scanner.nextLine())),
						Integer.parseInt((scanner.nextLine())), Integer.parseInt((scanner.nextLine())));
				this.comp.floors.add(floor);
			} else {
				if (this.monsterCount < this.numRegularMonsters) {
					Monster monster = new Monster(Integer.parseInt((scanner.nextLine())),
							Integer.parseInt((scanner.nextLine())), 1);
					this.levelPoints += 50;
					if(hardMode) {
						monster.hardMode = true;
						monster.xVelo = 2 * monster.xVelo;
						monster.yVelo = 2 * monster.yVelo;
					}
					this.comp.enemies.add(monster);
					this.monsterCount++;
				} else {
					Shooter shooter = new Shooter(Integer.parseInt((scanner.nextLine())),
							Integer.parseInt((scanner.nextLine())), 1, hero);
					this.levelPoints += 75;
					if(hardMode) {
						shooter.hardMode = true;
						shooter.xVelo = 2 * shooter.xVelo;
					}
					this.comp.enemies.add(shooter);
				}
			}
		}
	}

/**
 * tears down the current level, clears all arraylists
 */
	public void tearDown() {
		this.levelPoints = 0;
		this.comp.shootables.clear();
		this.comp.enemies.clear();
		this.comp.eggs.clear();
		this.comp.floors.clear();
		// TODO Auto-generated method stub

	}
/**
 * increases to next level
 * @param comp
 */
	public void lvlInc(ArcadeGameComponent comp) {
		comp.numLevelMoves = 0;
		comp.hero.health = 100;
		tearDown();
		this.level++;
		if (this.level > this.numLevels) {
			this.level = this.numLevels;
		}
		setUpLevel();
		// TODO Auto-generated method stub
		
	}
	/**
	 * goes down 1 level
	 * @param comp
	 */
	public void lvlDec(ArcadeGameComponent comp) {
		comp.numLevelMoves = 0;
		comp.hero.health = 100;
		tearDown();
		this.level--;
		if (this.level == this.numLevels - 1) {
			this.finalLevel = false;
		}
		if (this.level < 1) {
			this.level = 1;
		}
		setUpLevel();
		
	}
}