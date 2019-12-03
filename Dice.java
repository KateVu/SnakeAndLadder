
/**
 * Dice class 
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Dice extends JPanel implements MouseListener, ActionListener {
	private int value;// value of dice
	// private JPanel panel;
	private DiceEventListener listener = null; // dice event listener
	private boolean isRolling = false;
	private SpriteSheet spriteSheet;
	private SpriteTransition spriteTransition;

	/**
	 * Constructor
	 */
	public Dice() {

		try {
			BufferedImage sheet = ImageIO.read(new File("dice.png"));
			this.spriteSheet = new SpriteSheet(sheet, 1, 6, 6, 0, 0);

			this.spriteTransition = new SpriteTransition(10);
			this.spriteTransition.addActionListener(this);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		this.addMouseListener(this);
		this.value = 1;

	}

	
	/**
	 * action performed: roll the dice no more than 2s
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		Random random = new Random();
		this.value = random.nextInt(6) + 1;
		repaint();
		
		if (this.spriteTransition.getPastTime() > 2000) {
			this.spriteTransition.stop();
			this.listener.diceRoll(this.value);
			this.isRolling = false;
		}
	}
	
	/**
	 * get dimension of object, return a preferred size for this component
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(128, 128);
	}

	/**
	 * Display the dice
	 *
	 */
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		BufferedImage sprite = spriteSheet.getSpriteByIndex(this.value - 1);

		int x = (this.getWidth() - 128) / 2;
		int y = (this.getHeight() - 128) / 2;
		g2d.drawImage(sprite, x, y, 128, 128, this);
		g2d.dispose();
	}

	/**
	 * set event listener
	 * @param listener listener
	 */
	public void setEventListener(DiceEventListener listener) {
		this.listener = listener;

	}

	/**
	 * get value of dice
	 * @return value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * start to roll the dice
	 */
	public void roll() {
		this.spriteTransition.start();	
	}

	/**
	 * click to roll the dice event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.isRolling) {
			return;
		}
		this.isRolling = true;
		this.roll();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}