package timer.timermodel;

import java.time.Duration;
import java.time.LocalDateTime;

public class StopWatch implements Timer
{
    Duration currElapsed = null;
    LocalDateTime startTime = null;
    /* This is used to record the time at which the stop function was
     * execuited so as to be able to subtract the difference between
     * when the timer was stopped and when it was started again.
     * The purpose of this is to correct between 'now' and the 
     * start time of the timer. */
    LocalDateTime pauseStart = null;
    Duration stoppedDuration = null;

    @Override
    public String toString()
    {
        if(this.startTime != null)
        {
            //if we have never stopped before or we are stopped for the first time
            if(this.stoppedDuration == null)
            {
                if(this.pauseStart != null)
                {
                    //if the timer is stopped
                    this.currElapsed = Duration.between(this.startTime,
                        this.pauseStart);
                }
                else
                {
                    this.currElapsed = Duration.between(this.startTime,
                        LocalDateTime.now());
                }
            }
            else
            {
                //If we are not in a paused stat at all
                if(this.pauseStart == null)
                {
                    this.currElapsed = Duration.between(this.startTime,
                        LocalDateTime.now().minus(this.stoppedDuration));
                }
                //if we are in a paused state and have previously stopped
                else
                {
                    this.currElapsed = Duration.between(this.startTime,
                        this.pauseStart.minus(this.stoppedDuration));
                }
            }
            
            return Timer.parseDuration(this.currElapsed);
        }
        else return "00:00:00.000";
    }
    
    @Override
    public void start()
    {
        //if the timer has never been started or has been reset
        if(this.startTime == null)
        {
            this.startTime = LocalDateTime.now();
        }
        //if we are starting from a stopped state
        else if(this.pauseStart != null)
        {
            if(this.stoppedDuration == null)
            {
                this.stoppedDuration = Duration.between(this.pauseStart,
                    LocalDateTime.now());
            }
            //if we have stopped previously
            else
            {
                /* this is to account for the previous difference between
                 * the start time and now */
                this.stoppedDuration = Duration.between(this.pauseStart,
                    LocalDateTime.now()).plus(this.stoppedDuration);
            }
            this.pauseStart = null;
        }
    }

    @Override
    public void stop()
    {
        //check if the timer has been started
        if(this.startTime != null)
        {   
            /* we reset pause start when we call start so this checks if
             * we have already pressed stop */
            if(this.pauseStart == null)
            {
                this.pauseStart = LocalDateTime.now();
            }
        }
    }

    @Override
    public void reset()
    {
        this.currElapsed = null;
        this.startTime = null;
        this.pauseStart = null;
        this.stoppedDuration = null;
    }
    
    @Override
    public boolean expired() { return false; } //stopwatches cannot expire

}
