/**
 * Board class 
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Board extends Canvas implements MouseInputListener, ActionListener{
	public static final int ROWS = 5;//Row
	public static final int COLS = 6;//Col
	public static final int WIDTH = 100;//width of square
		
	public static final int[] LADDER_START = {4, 12};//starting points of ladder
	public static final int[] LADDER_END = {14, 22};//ending points of ladder
	
	public static final int[] SNAKE_START = {20, 16};//starting points of snake
	public static final int[] SNAKE_END = {7, 5};//ending points of snake
	
	private int turn = 0;//1 for human, 2 for machine
	private int currentNumHuman;//current number of human
	private int currentNumMachine;//current number of machine
	
	private double mouseCol = -1; //col for mouse press
	private double mouseRow = -1; //row for mouse press
	
	private SpriteSheet humanSprite;  //sprite sheet for human piece animation
	private SpriteSheet machineSprite; //sprite sheet for machine piece animation
	
	private SpriteTransition spriteTransition; //timer engine for the animation
	
	private BufferedImage snake; //buffered image for snake
	private BufferedImage ladder; //buffered image for ladder
	
	private BoardEventListener listener = null;//boardEventLister object
	
	/**
	 * Constructor
	 */
	public Board() {
		this.setSize(this.COLS * this.WIDTH + 200, this.ROWS * this.WIDTH);
		this.addMouseListener(this);
		this.turn = 0;
		
		try {
			BufferedImage sheet = ImageIO.read(new File("human.png"));
			this.humanSprite = new SpriteSheet(sheet, 1, 4, 4, 0, 0);
			sheet = ImageIO.read(new File("machine.png"));
			this.machineSprite = new SpriteSheet(sheet, 1, 5, 5, 0, 0);
			this.spriteTransition = new SpriteTransition(10);
		
			this.spriteTransition.addActionListener(this);
			this.spriteTransition.start();
			
			this.snake = ImageIO.read(new File("snake60.png"));
			this.ladder = ImageIO.read(new File("ladder60.png"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		
		this.init();	
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	/**
	 * Initial number for human and machine
	 */
	public void init() {
		this.currentNumHuman = 0;
		this.currentNumMachine = 0;

	}
	
	/**
	 * set event listener
	 * @param listener listener object
	 */
	public void setEventListener(BoardEventListener listener) {
		this.listener = listener;		
	}
	
	/**
	 * draw border of board
	 * @param g graphic object used for drawing
	 */
	public void drawBorder(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.COLS * this.WIDTH, this.ROWS * this.WIDTH);
	}
	
	/**
	 * Draw grid of the board
	 * @param g graphic object used for drawing
	 */
	public void drawGrid(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 1; i < ROWS; i ++) {
			g.drawLine(0, i * WIDTH,this.COLS * this.WIDTH , i * WIDTH);
		}
		
		for (int i = 1; i < COLS; i ++) {
			g.drawLine(i * WIDTH, 0 , i * WIDTH, this.ROWS * this.WIDTH);
		}
		
		Color color = new Color(1.0F, 1.0F, 0.0F, 0.3F);
			
		int num = 1;
		for (int i = ROWS - 1; i >= 0; i --) {
			if ((i % 2 == 0)) {
				for (int j = 0; j < COLS; j++) {
					int y1 = (int)((i + 0.5) * WIDTH);
					int x1 = (int)((j + 0.4) * WIDTH);
					String string = Integer.toString(num);
					g.setColor(Color.BLACK);
					Font font = new Font("Verdana", Font.PLAIN, 20); 
					g.setFont(font);
					g.drawString(string, x1, y1);
					
					if ((num % 2) == 0) {
						g.setColor(color);
						g.fillRect(j * WIDTH, i * WIDTH, WIDTH, WIDTH);
					}
							
					num++;
				}
			} else {
				for (int j = COLS - 1; j >= 0; j--) {
					int y1 = (int)((i + 0.5) * WIDTH);
					int x1 = (int)((j + 0.4) * WIDTH);
					String string = Integer.toString(num);
					g.setColor(Color.BLACK);
					Font font = new Font("Verdana", Font.PLAIN, 20); 
					g.setFont(font);
					g.drawString(string, x1, y1);
					
					if ((num % 2) == 0) {
						g.setColor(color);
						g.fillRect(j * WIDTH, i * WIDTH, WIDTH, WIDTH);
					}
					
					num++;
				}
			}
				

		}
		//set colour for square for mouseClick
		if ((this.mouseRow > 0) && (this.mouseCol > 0) && (this.mouseRow <= ROWS) && (this.mouseCol <= COLS)) {
			
			g.setColor(new Color(1.0F, 1.0F, 1.0F, 0.8F));
			g.fillRect(((int)this.mouseCol - 1) * WIDTH, ((int)this.mouseRow -1 ) * WIDTH, WIDTH, WIDTH);
		}
		
	}
	/**
	 * get point from number
	 * @param num number of square
	 * @return point Point object
	 */
	public Point getPoint (int num) {
		int row = ROWS - (int)Math.ceil(((double)num/COLS));
		int col;
		if ((row % 2) == 0) {
			col = ((num % COLS) == 0)? (COLS-1) : (num % COLS) - 1;
		} else {
			col = ((num % COLS) == 0)? 0: (COLS - (num % COLS));
		}	


		Point point = new Point(col * WIDTH, row * WIDTH);
		return point;
	}
	
	/**
	 * get number from row and col
	 * @param row row
	 * @param col col
	 * @return number number of square
	 */
	public int getNumber (int row, int col) {
		int number = (ROWS - row) * COLS;
		if ((row % 2) != 0) {
			number = number + col;
		} else {
			number = number + (COLS - col) + 1;
		}
		return number;
	}
	
	/**
	 * draw the snake
	 * @param g graphic object used for drawing
	 */
	public void drawSnake(Graphics g) {
		Color colorSnake = new Color(0.5F, 0.6F, 0.2F, 0.6F);
		g.setColor(colorSnake);

		for (int i = 0; i < 2; i++) {
			Point pointStartSnake = this.getPoint(SNAKE_START[i]);
			
			Graphics2D g2d = (Graphics2D) g.create();
			int width = 50;
			int padding = (WIDTH-width)/2;
			g2d.drawImage(this.snake, pointStartSnake.x + padding , pointStartSnake.y + padding, width, width, this);
			g2d.dispose();
			
			Point pointEndSnake = this.getPoint(SNAKE_END[i]);	
			
			Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.draw(new Line2D.Float(pointStartSnake.x + WIDTH/2, pointStartSnake.y + WIDTH/2, pointEndSnake.x + WIDTH/2, pointEndSnake.y + WIDTH/2));			//g.drawLine(pointStartSnake.x + WIDTH/2, pointStartSnake.y + WIDTH/2, pointEndSnake.x + WIDTH/2, pointEndSnake.y + WIDTH/2);			
		}
	}
	
	/**
	 * draw ladder
	 * @param g graphic object used for drawing
	 */
	public void drawLadder (Graphics g) {
		Color colorLadder = new Color(0.8F, 0.4F, 0.4F, 0.6F);
		g.setColor(colorLadder);

		for (int i = 0; i < 2; i++) {
			Point pointStartLadder = this.getPoint(LADDER_START[i]);
			Graphics2D g2d = (Graphics2D) g.create();
			int width = 50;
			int padding = (WIDTH-width)/2;
			g2d.drawImage(this.ladder, pointStartLadder.x + padding , pointStartLadder.y + padding, width, width, this);
			g2d.dispose();
			
			Point pointEndLadder = this.getPoint(LADDER_END[i]);
			Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.draw(new Line2D.Float(pointStartLadder.x + WIDTH/2, pointStartLadder.y + WIDTH/2, pointEndLadder.x + WIDTH/2, pointEndLadder.y + WIDTH/2));
		}

	}
	
	/**
	 * draw piece for human player
	 * @param g graphic object used for drawing
	 * @param number number of square
	 */
	public void drawCirlceHuman (Graphics g, int number) {
		
		Point point = this.getPoint(number);

		BufferedImage sprite = null;
		
		if (turn == 1) {
			sprite = this.humanSprite.getSprite(this.spriteTransition.getProgress());
		} else {
			sprite = this.humanSprite.getSpriteByIndex(0);
		}
		Graphics2D g2d = (Graphics2D) g.create();
		int width = 50;
		int padding = (WIDTH-width)/2;
		g2d.drawImage(sprite, point.x + padding , point.y + padding, width, width, this);
		g2d.dispose();

	}
	
	/**
	 * draw piece for machine player
	 * @param g graphic object used for drawing
	 * @param number number of square
	 */
	public void drawCirlceMachine (Graphics g, int number) {
		Point point = this.getPoint(number);

		BufferedImage sprite = null;
		
		if (turn == 2) {
			sprite = this.machineSprite.getSprite(this.spriteTransition.getProgress());
		} else {
			sprite = this.machineSprite.getSpriteByIndex(0);
		}
		Graphics2D g2d = (Graphics2D) g.create();
		int width = 50;
		int padding = (WIDTH-width)/2;
		g2d.drawImage(sprite, point.x + padding , point.y + padding, width, width, this);
		g2d.dispose();

	}
	
	/**
	 * draw the legends
	 * @param g graphic object used for drawing
	 */
	public void drawLegends(Graphics g) {
		int leftMargin = COLS * WIDTH + 30;
		
		g.setColor(Color.BLACK);
		Font bigFont = new Font("Verdana", Font.PLAIN, 24); 
		g.setFont(bigFont);
		g.drawString("LEGENDS", leftMargin + 20, 30);
		Font smallFont = new Font("Verdana", Font.PLAIN, 14); 
		g.setFont(smallFont);

		Graphics2D g2d = (Graphics2D) g.create();
		
		int width = 20;
		g2d.drawImage(this.humanSprite.getSpriteByIndex(0), leftMargin, 60, width, width, this);
		g.drawString("Human piece", leftMargin + width + 20, 70);

		g2d.drawImage(this.machineSprite.getSpriteByIndex(0), leftMargin, 90, width, width, this);
		g.drawString("Machine piece", leftMargin + width + 20, 100);
		
		g2d.drawImage(this.snake, leftMargin, 120, width, width, this);
		g.drawString("Head of the snake", leftMargin + width + 20, 130);

		g2d.drawImage(this.ladder, leftMargin, 150, width, width, this);
		g.drawString("Start of the ladder", leftMargin + width + 20, 160);

		g.setFont(bigFont);
		g.setColor(Color.RED);
		if (this.currentNumHuman == COLS * ROWS) {
			g.drawString("HUMAN WON!!!", leftMargin, 250);
		} else if (this.currentNumMachine == COLS * ROWS) {
			g.drawString("MACHINE WON!!!", leftMargin, 250);
		}
		
		g2d.dispose();
	}
	
	/**
	 * paint the board with snake, ladder, pieces for human, machine
	 */
	public void paint (Graphics g) {
		this.drawGrid(g);
		this.drawBorder(g);
		this.drawLegends(g);
		this.drawSnake(g);
		this.drawLadder(g);
		if (this.currentNumHuman > 0) {
			this.drawCirlceHuman(g,this.currentNumHuman);
		}
		
		if (this.currentNumMachine > 0) {
			this.drawCirlceMachine(g,this.currentNumMachine);
		}
	}
	

	/**
	 * get current number of human
	 * @return current number of human
	 */
	public int getCurrentNumHuman() {
		return this.currentNumHuman;
	}
	
	/**
	 * set current number of human
	 * @param value: new current number
	 */
	public void setCurrentNumHuman(int value) {
		this.currentNumHuman = value;
	}
	
	/**
	 * get current number of machine
	 * @return current number of machine
	 */
	public int getCurrentNumMachine() {
		return this.currentNumMachine;
	}
	
	/**
	 * set current number of machine
	 * @param value: new current number 
	 */
	public void setCurrentNumMachine(int value) {
		this.currentNumMachine = value;
	}
	
	/**
	 * mouse click event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {	
		if (this.listener != null) {
			this.listener.boardClick((int)this.mouseRow, (int)this.mouseCol);
		}
		this.mouseCol = -1;
		this.mouseRow = -1;
		this.repaint();
	}


	/**
	 * mouse press event
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		this.mouseCol = Math.floor(p.x / WIDTH) + 1;
		this.mouseRow = Math.floor(p.y / WIDTH) + 1;
		this.repaint();
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * repaint the board after action performed
	 * @param e action event find by the sprite transition
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
		// TODO Auto-generated method stub
		
	}
	
	
}
