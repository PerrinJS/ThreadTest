package timer;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JLabel;
import timer.gui.TimerGui;
import timer.timermodel.StopWatch;
import timer.timermodel.TimerThread;


public class Main
{
    public static void main(String[] args)
    {
        TimerGui MainWindow;
        TimerThread[] threadModles = new TimerThread[2]; //TODO: convert thease to the threads
        StopWatch stopWatch = new StopWatch();
        
        threadModles[0] = null;
        threadModles[1] = new TimerThread(stopWatch, new JLabel(stopWatch.toString()));
        //start thread
        threadModles[1].start();

        if(args.length > 0)
        {
            
            
            if(args[0].equals("-Stop"))
            {
                MainWindow = new TimerGui(TimerGui.Screen.STOP_WATCH, threadModles);
            }
            else if(args[0].equals("-Timer"))
            {
                MainWindow = new TimerGui(TimerGui.Screen.COUNT_DOWN, threadModles);
            }
            //The arg was unintelagable
            else
            {
                System.err.println("ERROR: not valid argument, valid arguments\n" +
                    "include:\n" +
                    "-Stop & -Timer. Both of thease choose weather the program\n" +
                    "starts with the timer screen open first or the stopwatch.");

                MainWindow = new TimerGui(threadModles);
            }
        }
        else
        {
            MainWindow = new TimerGui(threadModles);
        }
        
        MainWindow.addWindowListener(new WindowListener()
        {
            @Override
            public void windowClosed(WindowEvent we)
            {
                //close all the timers
                for(TimerThread modle : threadModles)
                {
                    if(modle != null)
                    {
                        try {
                            modle.stopTimer();
                            modle.join();
                        } catch (InterruptedException ex) {
                            System.err.println(ex);
                        }
                    }
                }                
            }

            @Override
            public void windowOpened(WindowEvent we)
            {
                
            }

            @Override
            public void windowClosing(WindowEvent we)
            {

            }

            @Override
            public void windowIconified(WindowEvent we)
            {
                
            }

            @Override
            public void windowDeiconified(WindowEvent we)
            {
                
            }

            @Override
            public void windowActivated(WindowEvent we)
            {
                
            }

            @Override
            public void windowDeactivated(WindowEvent we)
            {
                
            }
        });
        
        //close all the timers
        for(TimerThread modle : threadModles)
        {
            if(modle != null)
            {
                try {
                    modle.stopTimer();
                    modle.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
            }
        }
    }


}
