package WebCrawler;

public class Runway
{
    private int minutesForTakeOff; // Minutes for a single takeoff
    private int minutesForLanding; // Minutes for a single landing

    private int runwayIsClearTime_L;
    private int runwayIsClearTime_T; // Minutes until this runway is no longer busy

    public Runway(int landingMinutes, int takeOffMinutes)
    {
        minutesForLanding = landingMinutes;
        minutesForTakeOff = takeOffMinutes;

        runwayIsClearTime_L = 0;
        runwayIsClearTime_T = 0;

    }

    public boolean isBusy( )
    {

        return (runwayIsClearTime_L > 0 || runwayIsClearTime_T > 0);
    }


    public void reduceRemainingTime( )
    {
        if (runwayIsClearTime_L > 0)
        {
            runwayIsClearTime_L--; //this might overlap may need two seperate clear times to avoid that

        }
        if(runwayIsClearTime_T > 0)
        {
            runwayIsClearTime_T--;
        }
    }

    public void startTakingOff( ) //if the landing queue is not empty then startLanding() Otherwise startTakingOff()
    {
        if (runwayIsClearTime_T > 0)
            throw new IllegalStateException("Runway is already busy.");
        runwayIsClearTime_T = minutesForTakeOff;

    }

    public void startLanding( ) //if the landing queue is empty then startTakingOff() Otherwise startLanding()
    {
        if (runwayIsClearTime_L > 0)
            throw new IllegalStateException("Runway is already busy.");
        runwayIsClearTime_L = minutesForLanding;

    }
}


/*
public class Runway
{
    private int MinutesForTakeOff; // Minutes for a single takeoff
    private int MinutesForLanding; // Minutes for a single landing
    private int runwayIsClearTime; // Minutes until this runway is no longer busy

    public Runway(int takeOffMinutes, int landingMinutes)
    {
        MinutesForTakeOff = takeOffMinutes;
        MinutesForLanding = landingMinutes;
        runwayIsClearTime = 0;
    }

    public boolean isBusy( )
    {
        return (runwayIsClearTime > 0);
    }

    public void reduceRemainingTime( )
    {
        if (runwayIsClearTime > 0)
            runwayIsClearTime--; //this might overlap may need two seperate clear times to avoid that
    }

    public void startTakingOff( ) //if the landing queue is not empty then startLanding() Otherwise startTakingOff()
    {
        if (runwayIsClearTime > 0)
            throw new IllegalStateException("Runway is already busy.");
        runwayIsClearTime = MinutesForTakeOff;
    }

    public void startLanding( ) //if the landing queue is empty then startTakingOff() Otherwise startLanding()
    {
        if (runwayIsClearTime > 0)
            throw new IllegalStateException("Runway is already busy.");
        runwayIsClearTime = MinutesForLanding;
    }
}
 */