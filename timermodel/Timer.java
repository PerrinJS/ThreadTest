package timer.timermodel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.time.Duration;

public interface Timer
{
    @Override public String toString();
    /* Both a stopwatch and a timer can start stop and reset */
    public void start();
    public void stop();
    public void reset();
    public boolean expired();
    
    static String formatDateTime(LocalDateTime toFormat)
    {
        DateTimeFormatter hourMinSecFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return toFormat.format(hourMinSecFormatter);
    }
    
    /*
     * Parses a String into a Duration object from the HH:mm:ss.SSS format
     * This function is restricted to time, it cannot parse days.
     * @param toParse a String to be parsed
     * @return a Duration derived from toParse
     * @see Duration, String
     */
    static Duration parseTimeString(String toParse) //throws IllegalArgumentException
    {
        StringTokenizer timeTokenizer = new StringTokenizer(toParse, ":");
        String toParseFormatted = new String();
        
        /*if(timeTokenizer.countTokens() > 3)
        { 
            throw new IllegalArgumentException("Too many time fields");
        }*/
        
        //P for period
        toParseFormatted += "PT";
        
        //Take one from the total so we can use it as our count
        for(int i = timeTokenizer.countTokens() - 1; i >= 0 ; i--)
        {
            switch(i)
            {
                case 2:
                    toParseFormatted += timeTokenizer.nextToken() + "H";
                    break;
                case 1:
                    toParseFormatted += timeTokenizer.nextToken() + "M";
                    break;
                case 0:
                    toParseFormatted += timeTokenizer.nextToken() + "S";
                    break;
            }
        }
        
        return Duration.parse(toParseFormatted);
    }
    
    /**
     * Parses a Duration object into HH:mm:ss.SSS format
     * @param durationToParse a Duration object to be parsed
     * @return the duration formatted into the HH:mm:ss.SSS format
     * @see Duration
     */
    static String parseDuration(Duration durationToParse)
    {
        String toParse = durationToParse.toString();
        String toRet = new String();
        
        //remove the prefix
        toParse = toParse.substring(2);
        
        if(toParse.contains("H"))
        {   
            StringTokenizer hourTokenizer = new StringTokenizer(toParse, "H");
            toRet += hourTokenizer.nextToken();
            toRet += ":";
            
            //For if we only have an hours value
            if(hourTokenizer.hasMoreTokens())
            {
                /* cut the hour section so it apears like it would
                 * if we didn't have hours */
                toParse = hourTokenizer.nextToken();
            }
        }
        
        if(toParse.contains("M"))
        {
            StringTokenizer minuteTokenizer = new StringTokenizer(toParse, "M");

            toRet += minuteTokenizer.nextToken();
            toRet += ":";
            
            //For if we only have a minutes value
            if(minuteTokenizer.hasMoreTokens())
            {
                /* cut the minute section so it apears like it would
                 * if we didn't have minutes */
                toParse = minuteTokenizer.nextToken();
            }
        }
        
        if(toParse.contains("S"))
        {
            //TODO: fix the formatting we may not have the desimal point here
            String tmpSecsMillis = (new StringTokenizer(toParse, "S")).nextToken();

            StringTokenizer secsMillisTok = new StringTokenizer(tmpSecsMillis, ".");
            secsMillisTok.nextToken(); //Skip the secconds
            
            //If the millisecconds are = 0 then the duration string drops the zeros
            if(secsMillisTok.hasMoreTokens())
            {
                //There should be three miliseccond zeros
                int millisLenth = secsMillisTok.nextToken().length();

                if(millisLenth < 3)
                {
                    for(int i = (3 - millisLenth); i != 0; i--)
                    {
                        tmpSecsMillis += "0";
                    }
                }
            }
            toRet += tmpSecsMillis;
        }
        
        return toRet;
    }
}
