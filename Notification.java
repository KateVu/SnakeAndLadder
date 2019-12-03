/**
 * Notification class
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */
import javax.swing.*;

import java.awt.Font;
import java.util.*;

public class Notification extends JPanel{
	private JLabel label = new JLabel(); //content of notification
	
	/**
	 * add label
	 */
	public Notification() {
		Font font = new Font("Verdana", Font.PLAIN, 18); 
		this.label.setFont(font);
		this.add(this.label);
	}
	
	/**
	 * update content of notification
	 * @param message new content of the notification
	 */
	public void updateLabel(String message) {
		this.label.setText(message);
	}
}
