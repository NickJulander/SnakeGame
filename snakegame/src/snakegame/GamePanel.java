package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;



public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	boolean startScreen = true;
	Timer timer = new Timer(DELAY, this);
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.white);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	
	public void startGame() {
		startScreen = true;
		newApple();
		timer.start();
	}
	
	public void newGame() {
		applesEaten = 0;
		bodyParts = 6;
		direction = 'R';
		timer.stop();		
		x = null;
		x = new int[GAME_UNITS];
		y = null;
		y = new int[GAME_UNITS];
		startGame();
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if (startScreen) {
			g.setColor(Color.black);
			g.setFont(new Font("Monaco", Font.PLAIN, 60));
			FontMetrics metrics1 = getFontMetrics(g.getFont());
			g.drawString("Snake Game", (SCREEN_WIDTH - metrics1.stringWidth("Snake Game"))/2, SCREEN_HEIGHT/2 - 60);
			
			g.setColor(Color.black);
			g.setFont(new Font("Monaco", Font.ITALIC, 15));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Use the arrow keys to move Up, Down, Left or Right", (SCREEN_WIDTH - metrics2.stringWidth("Use the arrow keys to move Up, Down, Left or Right"))/2, SCREEN_HEIGHT/2);
			
			g.setColor(Color.black);
			g.setFont(new Font("Monaco", Font.BOLD, 30));
			FontMetrics metrics3 = getFontMetrics(g.getFont());
			g.drawString("Press Space to start!", (SCREEN_WIDTH - metrics3.stringWidth("Press Space to start!"))/2, SCREEN_HEIGHT/2 + 60);

		}
		
		else if(running) {

			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.gray);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(Color.lightGray);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.darkGray);
			g.setFont(new Font("Moraco", Font.PLAIN, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
			
		}
		else if (!running) {
			gameOver(g);
		}

		
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i=bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
		
		
		
	}
	
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i>0; i--) {
			//checks if head touches body
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}		
		}
		//checks if head touches borders
		if(x[0] < 0) { ///left border
			running = false;
		}
		else if(x[0] > SCREEN_WIDTH) { //right border
			running = false;
		}
		else if(y[0] > SCREEN_HEIGHT) { //lower border
			running = false;
		}
		else if(y[0] < 0){ //upper border
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Game Over text 
		g.setColor(Color.black);
		g.setFont(new Font("Monaco", Font.PLAIN, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.darkGray);
		g.setFont(new Font("Monaco", Font.ITALIC, 25));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2 + g.getFont().getSize() + 10);
		
		g.setColor(Color.lightGray);
		g.setFont(new Font("Monaco", Font.BOLD, 25));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press space to restart!", (SCREEN_WIDTH - metrics3.stringWidth("Press space to restart!"))/2, SCREEN_HEIGHT/2 + metrics1.getFont().getSize() + metrics2.getFont().getSize() + 10);
		
		
		
	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE:
				if(startScreen) {
					startScreen = false;
					running = true;
				}
				if(!startScreen && !running) {
					startScreen = true;
					newGame();
				}
				break;
			}
		}
		
	}
	
	
}
