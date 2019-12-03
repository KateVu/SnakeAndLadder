/**
 * Building a timer engine to fire up events based on the input frame per second
 * @author Quyen Vu Thi Tu SID 102418320
 * verion 0.1 
 */

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpriteTransition implements ActionListener {

    private Timer timer;//timer
    private int framesPerSecond;//frame per second
    
    private Long startTime;//start time
    private double progress;//cycle progress: range from 0.0 - 1.0

    private List<ActionListener> listeners;//listener

    /**
     * constructor with a frame per second
     * @param framesPerSecond frame per second
     */
    public SpriteTransition(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        this.listeners = new ArrayList<>();
    }
    
    /**
     * get a cycle progress
     * @return cycle progress
     */
    public double getProgress() {
        return this.progress;
    }

    /**
     * reset cycle progress
     */
    protected void resetProgress() {
        this.progress = 0;
        this.startTime = null;
    }

    /**
     * stop timer and reset progress
     */
    public void stop() {
        if (this.timer != null) {
            this.timer.stop();
        }
        resetProgress();
    }

    /**
     * start of timer based on the frame per second
     */
    public void start() {
        stop();
        this.timer = new Timer(1000 / framesPerSecond, this);
        this.timer.start();
    }

    public void addActionListener(ActionListener actionListener) {
        this.listeners.add(actionListener);
    }
    
    /**
     * get past time since the start
     * @return past time since the start
     */
    public long getPastTime() {
        long diff = (System.currentTimeMillis() - this.startTime);
        return diff;
    }

    
    /**
     * events fire up of the timer
     * Calculate cycle progress and notify all listeners
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (startTime == null) {
            startTime = System.currentTimeMillis();
        }
        
        long diff = (System.currentTimeMillis() - startTime) % 1000;
        this.progress = diff / 1000.0;
        
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand());
        for (ActionListener listener : this.listeners) {
            listener.actionPerformed(ae);
        }
    }
}