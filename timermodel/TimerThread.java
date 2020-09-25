package timer.timermodel;

import javax.swing.JLabel;
import timer.timermodel.Timer;

//TODO: Debug why the start must be run twice after it has been stoped
public class TimerThread extends Thread
{
    private Boolean runThread;
    private Boolean stopTimer;
    
    private Timer toUpdate;
    private JLabel toWriteTo;

    public TimerThread(Timer toUpdate, JLabel toWriteTo)
    {
        this.toUpdate = toUpdate;
        this.toWriteTo = toWriteTo;
        this.runThread = true;
        this.stopTimer = true; //stop after the thread is started
    }
    
    @Override
    public synchronized void run()
    {
        while(this.runThread)
        {
            if(this.stopTimer)
            {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    System.err.println("Can't wait: " + ex);
                    this.runThread = false;
                }
            }
            
            toWriteTo.setText(toUpdate.toString());
            try {
                Thread.sleep((long) 50);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    
    /*
     * The thread initially waits so this starts the timer if
     * not already and the thread, so we start changing the
     * JLable we are working with
     * @see JLable
     */
    public void startTimer()
    {
        //ignore if the timer is already stoped
        if(this.stopTimer)
        {
            synchronized(this)
            {
                this.stopTimer = false;
                this.toUpdate.start();
                this.notifyAll();
            }
        }
    }
    
    /**
     * stops the timer and waits the thread
     */
    public void stopTimer()
    {
        this.stopTimer = true;
        //stop the actuall timer
        this.toUpdate.stop();
    }
    
    /**
     * resets but does not stop the clock
     */
    public void resetTimer()
    {
        this.toUpdate.reset();
        if(!this.stopTimer)
        {
            this.toUpdate.start();
        }
        else
        {
            //update the new zeroed value on screen
            toWriteTo.setText(toUpdate.toString());
        }
    }
    
    /**
     * get the JLabel were updating
     * @return the JLable were working with
     * @see JLabel
     */
    public JLabel getLabel()
    {
        return this.toWriteTo;
    }
    
    @Override
    public void finalize()
    {
        //stop the timer so it may be used somewhere else
        this.runThread = false;
        this.startTimer();
        this.toUpdate.stop();
    }
}
