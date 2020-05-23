import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start by running
 * main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * @author JW, IL, JG.
 * 
 *
 */
public class Main {
	public static int frame_height = 900;
	public static int frame_width = 1500;
	private static HashMap<String, Integer> map;
	public static final int DELAY = 45;
	public static boolean hardMode = false;

	/**
	 * Starts up the game, displays the start screen
	 */
	public Main() {
		JFrame origFrame = new JFrame("WELCOME TO SUPER MARIO JOUST - Created by Justin Guilfoyle, Ian Lim, and Jacob Woodrome");
		origFrame.setLocation(100, 70);
		origFrame.setSize(frame_width, frame_height);
		JPanel panel = new JPanel();
		StartScreenComponent origComp = new StartScreenComponent(origFrame);
		JButton highscore = new JButton("High Score List");
		JButton easy = new JButton("Play Regular Game");
		JButton hard = new JButton("Play Insane Mode");
		JButton controls = new JButton("Display Controls");
		JButton directions = new JButton("Display Game Instructions");
		panel.add(easy);
		panel.add(hard);
		panel.add(controls);
		panel.add(directions);
		panel.add(highscore);
		origFrame.add(panel, BorderLayout.SOUTH);
		origFrame.add(origComp);
		origFrame.setVisible(true);
		/**
		 * Anonymous listener class for startgame button
		 */
		easy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				startGame();
				origFrame.dispose();
			}
			
		});
		/**
		 * Anonymous listener class for display directions button
		 */
		controls.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				displayDirections();
			}

		});
		/**
		 * Anonymous listener class for display game instructions button
		 */
		directions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				displayGameInstructions();
			}
			
		});
		/**
		 * Anonymous listener class for startgame button
		 */
		hard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				hardMode = true;
				startGame();
				origFrame.dispose();
			}
			
		});
		/**
		 * Anonymous listener class for highscores button
		 */
		highscore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				displayHighScores();
			}
			
		});
	}
	/**
	 * Calls main to get the ball rolling
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		new Main();}
	
	/**
	 * Starts the game
	 */
	public static void startGame() {
		JFrame frame = new JFrame();
		frame.setLocation(100, 70);
		frame.setSize(frame_width, frame_height + 50);
		ArcadeGameComponent comp = new ArcadeGameComponent(hardMode, frame);
		frame.add(comp);
		GameAdvanceListener advanceListener = new GameAdvanceListener(comp);
		Timer timer = new Timer(DELAY, advanceListener);
		/**
		 * Anonymous listener class to detect pause key, or level up/down keys
		 */
		frame.addKeyListener(new KeyListener() {
			private int pauseMode = 0;
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyChar() == 'u') {
					comp.levelMaker.lvlInc(comp);
					frame.revalidate();
				}
				if (e.getKeyChar() == 'd') {
					comp.levelMaker.lvlDec(comp);
					frame.revalidate();
				}
				if (e.getKeyChar() == 'p') {
					if (this.pauseMode  == 0) {
						timer.start();
						this.pauseMode = 1;
					}
					else {
						timer.stop();
						this.pauseMode = 0;}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		HeroMovementListener moveKeys = new HeroMovementListener(frame,comp);
		frame.addKeyListener(moveKeys);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * Displays the game instructions
	 */
	public static void displayGameInstructions() {
		JFrame f = new JFrame("INSTRUCTIONS");
		f.setLocation(800, 250);
		f.setSize(325, 500);
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		JPanel p = new JPanel();
		JLabel l = new JLabel("GAME STORY:");
		JLabel l1 = new JLabel("Playing as Mario, try");
		JLabel l2 = new JLabel("and defeat Goombas and ");
		JLabel l3 = new JLabel("MagiKoopas by jousting ");
		JLabel l4 = new JLabel("and being higher than them");
		JLabel l5 = new JLabel("on the map.  Finally, face");
		JLabel l6 = new JLabel("Bowser, the final boss, to");
		JLabel l7 = new JLabel("win the game & be a hero!");
		JLabel l9 = new JLabel("Play regular mode if you're");
		JLabel l10 = new JLabel("or play Insane mode if you");
		JLabel l11 = new JLabel("think you can handle it!");
		JLabel l12 = new JLabel("Best of luck! - The Devs :)");
		labels.add(l);
		labels.add(l1);
		labels.add(l2);
		labels.add(l3);
		labels.add(l4);
		labels.add(l5);
		labels.add(l6);
		labels.add(l7);
		labels.add(l9);
		labels.add(l10);
		labels.add(l11);
		labels.add(l12);
		for (JLabel jl : labels) {
		jl.setFont(new Font(l.getFont().toString(), Font.PLAIN, l.getFont().getSize() * 2));
		p.add(jl);}
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	/**
	 * Displays the game directions
	 */
	public static void displayDirections() {
		// TODO Auto-generated method stub
		JFrame directions = new JFrame("CONTROLS");
		directions.setLocation(1600, 70);
		directions.setSize(325, 400);
		JPanel dirPanel = new JPanel();
		JLabel label1 = new JLabel("Use arrow keys to move");
		JLabel label2 = new JLabel("Up to fly");
		JLabel label3 = new JLabel("Down to dive");
		JLabel label4 = new JLabel("Left and right to run");
		JLabel label5 = new JLabel("Press 'u' to skip level");
		JLabel label6 = new JLabel("Press 'd' to go back level");
		JLabel label7 = new JLabel("Press 'p' to pause/play");
		label1.setFont(new Font(label1.getFont().toString(), Font.PLAIN, label1.getFont().getSize() * 2));
		label2.setFont(new Font(label2.getFont().toString(), Font.PLAIN, label2.getFont().getSize() * 2));
		label3.setFont(new Font(label3.getFont().toString(), Font.PLAIN, label3.getFont().getSize() * 2));
		label4.setFont(new Font(label4.getFont().toString(), Font.PLAIN, label4.getFont().getSize() * 2));
		label5.setFont(new Font(label5.getFont().toString(), Font.PLAIN, label5.getFont().getSize() * 2));
		label6.setFont(new Font(label6.getFont().toString(), Font.PLAIN, label6.getFont().getSize() * 2));
		label7.setFont(new Font(label7.getFont().toString(), Font.PLAIN, label7.getFont().getSize() * 2));
		dirPanel.add(label1);
		dirPanel.add(label2);
		dirPanel.add(label3);
		dirPanel.add(label4);
		dirPanel.add(label5);
		dirPanel.add(label6);
		dirPanel.add(label7);
		dirPanel.setLayout(new BoxLayout(dirPanel, BoxLayout.Y_AXIS));
		directions.add(dirPanel);
		directions.setVisible(true);
		directions.pack();
	}
	
	/**
	 * Displays the highscores
	 */
	public static void displayHighScores() {
		JFrame hScores = new JFrame("Highscores! Play to get your name on the list!");
		hScores.setLocation(1600, 500);
		hScores.setSize(600, 600);
		JPanel panel = new JPanel();
		ArrayList<Integer> scores = getScoresSorted();
		ArrayList<String> names = getNamesSorted(scores);
		for (int i = 1; i <= 10; i++) {
			String s = "" + i + "." + " " + scores.get(i - 1) + " - " + names.get(i - 1);
			JLabel label = new JLabel(s);
			label.setFont(new Font(label.getFont().toString(), Font.PLAIN, label.getFont().getSize() * 2));
			panel.add(label);
		}
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		hScores.add(panel);
		hScores.setVisible(true);
		hScores.pack();
		
	}
	/**
	 * Sorts the names based off of the sorted scores
	 * @param scores
	 * @return
	 */
	public static ArrayList<String> getNamesSorted(ArrayList<Integer> scores) {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < Main.map.size(); i++) {
			for (String key : Main.map.keySet()) {
				if (map.get(key) == scores.get(i)) {
					names.add(key);
				}
			}
		}
		return names;
	}
	/**
	 * sorts the scores in the textfile 
	 * @return
	 */
	public static ArrayList<Integer> getScoresSorted() {
		int count = 0;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("highscores.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> scores = new ArrayList<Integer>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Main.map = map;
		while (scanner.hasNextLine()) {
			Integer i = Integer.parseInt(scanner.nextLine());
			String s = scanner.nextLine();
			if (map.containsKey(s)) {
				s = s + count;
				count++;
			}
			map.put(s, i);
		}
		for (String key : map.keySet()) {
			scores.add(Main.map.get(key));
		}
		Collections.sort(scores, Collections.reverseOrder());
		return scores;
	}

}
