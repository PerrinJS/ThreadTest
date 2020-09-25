package timer.gui;

import timer.timermodel.TimerThread;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
//import javax.swing.JMenuBar;
//import javax.swing.JMenu;
//import javax.swing.JSeparator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class TimerGui extends JFrame
{
    private static final String DEFAULT_WIN_NAME = "Timer / Stopwatch";
    private static final String COUNT_DOWN_CARD = "Countdown Timer";
    private static final String STOP_WATCH_CARD = "Stopwatch";
    private static final int[] DEFAULT_WIN_SIZE = {640, 480};

    public enum Screen
    {
        COUNT_DOWN, STOP_WATCH
    }
    
    private Screen currentScr = null;

    private JTabbedPane mainLayout;

    /* This panel stores the timer and stopwatch cards in the center of
     * the window */
    private JPanel mainPanel;
    private JPanel timerPanel;
    private JPanel stopWatchPanel;

    /* Here is our model we may want to refactor this
     * to be more generic in future */
    private TimerThread[] timerControllers;
    
    //This is not nessisary for our scope
    //private JMenuBar windowToolBar;


    /* CONSTRUCTORS */
    public TimerGui(TimerThread[] timerControllers)
    {
        this(DEFAULT_WIN_NAME, timerControllers);
    }

    public TimerGui(Screen startingScr, TimerThread[] timerControllers)
    {
        this(DEFAULT_WIN_NAME, DEFAULT_WIN_SIZE[0], DEFAULT_WIN_SIZE[1],
            timerControllers);
    }

    public TimerGui(String title, int xSize, int ySize,
        TimerThread[] timerControllers)
    {
        this(Screen.COUNT_DOWN, DEFAULT_WIN_NAME, xSize, ySize,
            timerControllers);
    }
    
    public TimerGui(Screen startingScr, int xSize, int ySize,
        TimerThread[] timerControllers)
    {
        this(startingScr, DEFAULT_WIN_NAME, xSize, ySize, timerControllers);
    }

    public TimerGui(String title, TimerThread[] timerControllers)
    {
        this(title, DEFAULT_WIN_SIZE[0], DEFAULT_WIN_SIZE[1], timerControllers);
    }
    
    public TimerGui(Screen startingScr, String title, int xSize, int ySize,
        TimerThread[] timerControllers)
    {
        super(title);
        this.currentScr = startingScr;
        
        //we don't want to throw an exception in the constructor
        if(timerControllers.length != 2)
        {
            this.timerControllers = null;
        }
        else this.timerControllers = timerControllers;
        
        setupInterface(xSize, ySize);
    }
   
    
    /* This function exists puly so we can set the currentScr
     * before starting the window, which we cannot do with a
     * call to super */
    private void setupInterface(int xSize, int ySize)
    {
        //If currentScr is yet to be set, set the default
        if(currentScr == null)
        {
            currentScr = Screen.COUNT_DOWN;
        }

        setSize(xSize, ySize);
        
        //set cross to close the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //fill the window with the three screens
        setupWidgets();

        if(currentScr == Screen.STOP_WATCH)
        {
            /* using getParent() as the panels where intialized in a function
             * thus are not associated with 'this' aka the Interface object */
            //mainLayout.show(stopWatchPanel.getParent(), STOP_WATCH_CARD);
        }

        //show the window
        setVisible(true);
    }

    private boolean askDialog(String qText, String boxTitle)
    {
        /* Creates a JOptionPane dialog that contaions the *\
         * question message text (qText) from the arg and
         * has the title from the arg boxTitle, this dialog
         * having only the yes no options.
         * The resault of this being tested to see if the
        \* yes option was selected or not.                 */
        return JOptionPane.showConfirmDialog
        (null,
        qText,
        boxTitle,
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE)
        == JOptionPane.YES_OPTION;// ? true : false;
    }

    /* Widget setup */
    private void setupWidgets()
    {
        mainPanel = new JPanel();
        //Sets the window such that we can have multiple tabbed frames
        mainLayout = new JTabbedPane();
        /* setting this to a jpanel that holds the cards insted */
        //mainPanel.setLayout(mainLayout);

        //TODO: refactor so the font can be selected elseware
        timerPanel = createTimerPanel(new Font("Calibri", Font.BOLD, 80));
        stopWatchPanel = createStopWatchPanel(new Font("Calibri", Font.BOLD, 80));

        mainLayout.add(COUNT_DOWN_CARD, timerPanel);
        mainLayout.add(STOP_WATCH_CARD, stopWatchPanel);
        add(mainLayout, BorderLayout.CENTER);

        //I think a tool bar is out of scope
        //windowToolBar = new JMenuBar();
        //windowToolBar.add(createFileMenu());
        //windowToolBar = new JMenuBar();add(windowToolBar, BorderLayout.NORTH);
    }

    /* TODO: use card layout so that when the timer is stopped it shows a textbox
     * Then whilst running, show the label
     */
    private JPanel createTimerPanel(Font fontToUse)
    {
        //for some reason it doesnt default to border layout like its suposed to
        JPanel countdownTabContence = new JPanel(new BorderLayout());
        //This will hold both the timer text and the input box, switching between them
        JPanel countdownViewArea = new JPanel(new CardLayout());
        JPanel buttonArea = new JPanel(new FlowLayout());
        
        JLabel time = timerControllers[Screen.STOP_WATCH.ordinal()].getLabel();
        time.setFont(fontToUse);
        
        //Center the text
        time.setVerticalAlignment(JLabel.CENTER);
        time.setHorizontalAlignment(JLabel.CENTER);
        
        countdownTabContence.add(time, BorderLayout.CENTER);
        
        countdownTabContence.add(buttonArea, BorderLayout.PAGE_END);
        
        //countdown button setup
        {
            
            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");
            JButton resetButton = new JButton("Reset");

            //Set the action listners via lambda functions as it is cleaner
            startButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.COUNT_DOWN.ordinal()].startTimer();
            });
            
            stopButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.COUNT_DOWN.ordinal()].stopTimer();
            });
            
            resetButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.COUNT_DOWN.ordinal()].resetTimer();
            });
            
            buttonArea.add(startButton);
            buttonArea.add(stopButton);
            buttonArea.add(resetButton);
        }
        
        return countdownTabContence;
    }

    private JPanel createStopWatchPanel(Font fontToUse)
    {
        //for some reason it doesnt default to border layout like its suposed to
        JPanel stopWatchView = new JPanel(new BorderLayout());
        JPanel buttonArea = new JPanel(new FlowLayout());
        
        JLabel time = timerControllers[Screen.STOP_WATCH.ordinal()].getLabel();
        time.setFont(fontToUse);
        
        //Center the text
        time.setVerticalAlignment(JLabel.CENTER);
        time.setHorizontalAlignment(JLabel.CENTER);
        
        stopWatchView.add(time, BorderLayout.CENTER);
        
        stopWatchView.add(buttonArea, BorderLayout.PAGE_END);
        
        //stopwatch button setup
        {
            
            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");
            JButton resetButton = new JButton("Reset");

            //Set the action listners via lambda functions as it is cleaner
            startButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.STOP_WATCH.ordinal()].startTimer();
            });
            
            stopButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.STOP_WATCH.ordinal()].stopTimer();
            });
            
            resetButton.addActionListener((ActionEvent ae) -> {
                timerControllers[Screen.STOP_WATCH.ordinal()].resetTimer();
            });
            
            buttonArea.add(startButton);
            buttonArea.add(stopButton);
            buttonArea.add(resetButton);
        }
        
        return stopWatchView;
    }

    //Again this is not needed in the current context
    /*
        private JMenu createFileMenu()
        {
            JMenu fileMenu = new JMenu("File");

            JButton exitMenuButton = new JButton("Quit");
            exitMenuButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(askDialog("Are you sure you want to quit?", "Exit?"))
                    {
                        setVisible(false);
                        dispose();
                    }
                }
            });

            fileMenu.add(new JSeparator());
            fileMenu.add(exitMenuButton);

            return fileMenu;
        }
    */
}
