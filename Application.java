/**
 * The main application 
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Application implements BoardEventListener, DiceEventListener, ActionListener{
	
	private static final int  STATE_INIT = 0;//initial state
	private static final int STATE_DECIDE = 1;//deciding who will play first state
	private static final int STATE_DECIDE_HUMAN = 2;//rolling for human to decide who will play first state
	private static final int STATE_DECIDE_MACHINE = 3;//rolling for machine to decide who will play first state

	private static final int STATE_HUMAN_DICE = 4;//human rolls state
	private static final int STATE_HUMAN_MOVE = 5;//human moves state

	private static final int STATE_MACHINE = 6;//machine rolls and moves state
	private static final int STATE_FINISH = 7;//human wins state

	
	private static int EVENT_TYPE_DICE = 0;//event type Dice
	private static int EVENT_TYPE_BOARD = 1;//event type Board

	
	private int state = STATE_INIT;//state of the program
	private int pointHuman = 0;//point of human
	private int pointMachine = 0;//point of machine
	private String inforMessage = "";//information message
	
	private int newPointHuman = 0;//new point of human
	private int newPointMachine = 0;//new point of machine
	
	private JFrame frame;//main frame of the app
	private Dice dice;//dice 
	private Board board;//board
	private Notification notification;//GUI component displaying infor message
	private JPanel panel;//panel contain dice and button
	
	private JButton button;//button for Reply/Start
	
	
	public Application() {
		//Establish GUI
		this.frame = new JFrame();
		this.frame.setBounds(200, 200, 1000, 600);
		this.frame.setTitle("Snake and Ladder");
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		
		this.dice = new Dice();
		this.dice.setEventListener(this);
				
		this.button = new JButton("Start");
		this.button.addActionListener(this);
		
		//Establish panel: dice and button
		panel = new JPanel();
		this.panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 128;
		this.panel.add(this.dice, c);	
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 30;
		this.panel.add(new JLabel("  Click the dice to roll"), c);
		
		c = new GridBagConstraints();
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.ipadx = 128;
		this.panel.add(this.button, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 30;
		this.panel.add(new JLabel(), c);
		
		this.panel.setPreferredSize(new Dimension(150, 300)); 		
		frame.add(panel, BorderLayout.WEST);
		
		//Establish board 
		this.board = new Board();
		this.frame.add(this.board, BorderLayout.CENTER);
		
		//Establish notification 
		this.notification = new Notification();
		this.frame.add(this.notification, BorderLayout.SOUTH);
		
		this.board.setEventListener(this);
		
	}
	/**
	 * program to handle event
	 * @param event type of event
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void eventHandler (int event, int number1, int number2) {
		switch (this.state) {
			case STATE_INIT: {
				this.handlerInitState();
				break;
			} 
			case STATE_DECIDE: {	
				this.handlerDecideState();
				break;
			} 
			case STATE_DECIDE_HUMAN: {	
				this.handlerDecideHumanState(event, number1, number2);
				break;
			} 
			case STATE_DECIDE_MACHINE: {	
				this.handlerDecideMachineState(event, number1, number2);
				break;
			} 
			case STATE_HUMAN_DICE: {
				this.handlerStateHumanDice(event, number1, number2);
				break;
			} 
			case STATE_HUMAN_MOVE: {
				this.handlerStateHumanMove(event, number1, number2);					
				break;
			}
			case STATE_MACHINE: {
				this.handlerStateMachine(event, number1, number2);
				break;
			} 
			case STATE_FINISH: {
				break;
			} 

			default: {
				break;
			}
		}
	}
	
	/**
	 * program to handle initial state
	 */
	public void handlerInitState() {
		this.inforMessage = this.inforMessage + "Press Start to play game";
		this.notification.updateLabel(this.inforMessage);
		this.inforMessage = "";
	}
	
	/**
	 * program to handle decide state
	 */
	public void handlerDecideState() {
		this.inforMessage = this.inforMessage + "Roll dice to decide who play first. Click to get the point for human";
		this.notification.updateLabel(this.inforMessage);
		this.inforMessage = "";
		this.state = STATE_DECIDE_HUMAN;
	}
	
	/**
	 * program to handle decide human state
	 * @param event event type
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void handlerDecideHumanState(int event, int number1, int number2) {
		if (event == EVENT_TYPE_DICE) {
			this.pointHuman = number1;
			this.inforMessage = this.inforMessage + "Point for human: " + this.pointHuman + ". Click to get the point for machine";
			this.notification.updateLabel(this.inforMessage);
			this.inforMessage = "";
			this.state = STATE_DECIDE_MACHINE;
		}
	}
	
	/**
	 * program to handle decide human state
	 * @param event type of event
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void handlerDecideMachineState(int event, int number1, int number2) {
		if (event == EVENT_TYPE_DICE) {
			this.pointMachine = number1;
			
			if (this.pointHuman == this.pointMachine) {
				this.inforMessage = this.inforMessage + "Human and machine have the same point. Roll again to decide who will play first.";
				this.notification.updateLabel(inforMessage);
				this.inforMessage = "";
				this.state = STATE_DECIDE_HUMAN;
			} else if (this.pointHuman > this.pointMachine) {
				this.inforMessage = this.inforMessage + "Point for human: " + this.pointHuman + "; Point for machine: " + this.pointMachine + 
						". Human plays first. Click to get the point for human";
				this.notification.updateLabel(this.inforMessage);
				this.inforMessage = "";
				this.state = STATE_HUMAN_DICE;
				this.board.setTurn(1);
			} else {
				this.inforMessage = this.inforMessage + "Point for human: " + this.pointHuman + "; Point for machine: " + this.pointMachine + 
						". Machine plays first. Click to get the point for machine";
				this.notification.updateLabel(this.inforMessage);
				this.inforMessage = "";
				this.state = STATE_MACHINE;
				this.board.setTurn(2);
			}
		}
	}
	
	/**
	 * program to handle human dice state
	 * @param event type of event
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void handlerStateHumanDice(int event, int number1, int number2) {
		if (event == EVENT_TYPE_DICE) {
			this.pointHuman = number1;
			this.newPointHuman = this.pointHuman + this.board.getCurrentNumHuman();
			
			//check if over the max number of boad
			if (this.newPointHuman > Board.ROWS * Board.COLS) {
				if (this.pointHuman != 6) {
					this.inforMessage = this.inforMessage + "Need exactly " + (Board.ROWS * Board.COLS - this.board.getCurrentNumHuman() + " to win. Click the dice to get the point for machine");
					this.notification.updateLabel(this.inforMessage);
					this.inforMessage = "";
					this.state = STATE_MACHINE;
					this.board.setTurn(2);
					return;
				}else {
					this.inforMessage = this.inforMessage + "Need exactly " + (Board.ROWS * Board.COLS - this.board.getCurrentNumHuman() + " to win. Have another turn. Click the dice to get the point for human");
					this.notification.updateLabel(this.inforMessage);
					this.inforMessage = "";
					return;
				}
			}
			
			this.inforMessage = this.inforMessage + "Click on number: " + this.newPointHuman + " to move";			
			this.notification.updateLabel(this.inforMessage);
			this.inforMessage = "";
			this.state = STATE_HUMAN_MOVE;
		} else {
			this.inforMessage = this.inforMessage + "Click the dice to get the point for human";
			this.notification.updateLabel(this.inforMessage);
			this.inforMessage = "";
		}
	}
	
	/**
	 * program to handle human move state
	 * @param event type of event
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void handlerStateHumanMove(int event, int number1, int number2) {
		if (event == EVENT_TYPE_BOARD) {
			int number = this.board.getNumber(number1, number2);
			if (this.newPointHuman != number) {
				this.inforMessage = this.inforMessage + "Wrong number. Click on number: " + this.newPointHuman + " to move";			
				this.notification.updateLabel(this.inforMessage);
				this.inforMessage = "";
			} else {							

				this.board.setCurrentNumHuman(this.newPointHuman);
				this.board.repaint();
				
				//check if go to start of ladder
				int length = Board.LADDER_START.length;
				for (int i = 0; i < length; i++) {
					if (this.newPointHuman == Board.LADDER_START[i]) {
					    	this.newPointHuman = Board.LADDER_END[i];
						this.inforMessage = this.inforMessage + "You are at the start of the ladder. You move to " + this.newPointHuman + ". ";			
						this.notification.updateLabel(this.inforMessage);
						
						this.board.setCurrentNumHuman(this.newPointHuman);
						this.board.repaint();
						break;
					}
				}
				
				//check if go to start of snake
				length = Board.SNAKE_START.length;
				for (int i = 0; i < length; i++) {
					if (this.newPointHuman == Board.SNAKE_START[i]) {
						this.newPointHuman = Board.SNAKE_END[i];
						this.inforMessage = this.inforMessage + "You are at the start of the snake. You move to " + this.newPointHuman + ". ";			
						this.notification.updateLabel(this.inforMessage);
						
						this.board.setCurrentNumHuman(this.newPointHuman);
						this.board.repaint();
						break;
					}
				}
						
				//check if human win
				if (this.newPointHuman == Board.ROWS * Board.COLS) {
					this.inforMessage = this.inforMessage + "Congratuation, human won. Click Replay to play again.";			
					this.notification.updateLabel(this.inforMessage);
					this.inforMessage = "";

					this.state = STATE_FINISH;
					return;
				}
							
				//check if human can roll another time
				if (this.pointHuman == 6) {
					this.inforMessage = this.inforMessage + "Have another turn. Click the dice to get the point for human";			
					this.notification.updateLabel(this.inforMessage);
					this.inforMessage = "";
					this.state = STATE_HUMAN_DICE;
					return;
				}
				
				this.state = STATE_MACHINE;
				this.board.setTurn(2);
				this.inforMessage = this.inforMessage + "Click the dice to get the point for machine";			
				this.notification.updateLabel(this.inforMessage);	
				this.inforMessage = "";
			}
		}
	}
	
	/**
	 * program to handle rolling and movement of machine
	 * @param event type of event
	 * @param number1 supplemental arguments passed in dice events or board events
	 * @param number2 supplemental arguments passed in dice events or board events
	 */
	public void handlerStateMachine(int event, int number1, int number2) {
		if (event == EVENT_TYPE_DICE) {
			
			this.pointMachine = number1;
			this.newPointMachine = this.pointMachine + this.board.getCurrentNumMachine();
			
			//check if over the max number of boad
			if (this.newPointMachine > Board.ROWS * Board.COLS) {
				this.inforMessage = this.inforMessage + "Need exactly " + (Board.ROWS * Board.COLS - this.board.getCurrentNumMachine() + " to win. Click the dice to get the point for human");			
				this.notification.updateLabel(this.inforMessage);	
				this.inforMessage = "";
				
				if (this.pointMachine == 6) {
					this.inforMessage = this.inforMessage + "Need exactly " + (Board.ROWS * Board.COLS - this.board.getCurrentNumMachine() + " to win. Have another turn. Click the dice to get the point for machine");			
					this.notification.updateLabel(this.inforMessage);	
					this.inforMessage = "";
					return;
				} else {
					this.state = STATE_HUMAN_DICE;
					this.board.setTurn(1);
					return;
				}
			}									
			
			//check if machine win
			if (this.newPointMachine == Board.ROWS * Board.COLS) {
				this.notification.updateLabel("Congratuation, machine won. Click Replay to play again");					
				this.state = STATE_FINISH;
				this.board.setCurrentNumMachine(this.newPointMachine);
				this.board.repaint();	
				return;
			}
			
			//check if go to start of ladder
			int length = Board.LADDER_START.length;
			for (int i = 0; i < length; i++) {
				if (this.newPointMachine == Board.LADDER_START[i]) {
					this.inforMessage = this.inforMessage + "Machine is at the start of the ladder. ";			
					this.notification.updateLabel(this.inforMessage);
					this.newPointMachine = Board.LADDER_END[i];
				}
			}
								
			//check if go to start of snake
			length = Board.SNAKE_START.length;
			for (int i = 0; i < length; i++) {
				if (this.newPointMachine == Board.SNAKE_START[i]) {
					this.inforMessage = this.inforMessage + "Machine is at the start of the snake. ";			
					this.notification.updateLabel(this.inforMessage);
					this.newPointMachine = Board.SNAKE_END[i];
				}
			}
			
			this.board.setCurrentNumMachine(this.newPointMachine);
			this.board.repaint();			
			this.inforMessage = this.inforMessage + "Machine move to: " + this.newPointMachine + ". Click the dice to get the point for human";			
			this.notification.updateLabel(this.inforMessage);	
			this.inforMessage = "";
			
			//check if machine can roll another time
			if (this.pointMachine == 6) {
				this.inforMessage = this.inforMessage + "Machine move to: " + this.newPointMachine + ". Have another turn. Click the dice to get the point for machine";			
				this.notification.updateLabel(this.inforMessage);	
				this.inforMessage = "";
				return;
			}
								
		this.state = STATE_HUMAN_DICE;
		this.board.setTurn(1);

	} else {
		this.notification.updateLabel("Click the dice to get the point for machine");
	}
	}
	
	
	/**
	 * starting point of application
	 */
	public void start() {
		this.frame.setVisible(true);
		this.eventHandler(0, 0, 0);
		
	}
	
	/**
	 * main entry
	 * @param args  list of param pasted from command line
	 */
	public static void main(String[] args) {
		Application app = new Application();
		app.start();
	}


	
	/**
	 * handle boardClick event
	 */
	@Override
	public void boardClick(int row, int col) {
		this.eventHandler(EVENT_TYPE_BOARD, row, col);
	}

	/**
	 * handle dice roll event
	 */
	@Override
	public void diceRoll(int value) {
		this.eventHandler(EVENT_TYPE_DICE, value, 0);
	}

	/**
	 * update button after init state
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.state = STATE_DECIDE;
		this.board.init();
		this.board.repaint();
		this.button.setText("Replay");
		this.eventHandler(0, 0, 0);
	}
	
	
}
