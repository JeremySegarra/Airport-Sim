package WebCrawler;

import java.util.LinkedList;
import java.util.Queue;

public class AirportSim
{

    public static void main(String[] args)
    {
        System.out.println("Flight Sim #1\n");
        airPortSim(4,2,0.05, 0.05, 2, 6000);
        System.out.println();
        System.out.println("Flight Sim # 2\n");
        airPortSim(4,2,0.1, 0.1, 5, 6000);

    }

    public static void airPortSim(int landingTime, int takeOffTime, double arrivalTimeProb, double departureProb, int crashingTime, int totalSimTime)
    {
        Queue<Integer> takeOffQueue = new LinkedList<>(); //queue to hold each plane time to take off
        Queue<Integer> landingsQueue = new LinkedList<>(); //queue to hold each plane time to land (which takes Priority!)

        Runway runway = new Runway(landingTime, takeOffTime);//convert to Minutes //I should only need 1 runway to take care of both

        Averager avgLandingWaitTimes = new Averager(); //Average wait time for landings
        Averager avgTakeOffWaitTimes = new Averager(); //Average wait time for take offs

        BooleanSource landingProbability = new BooleanSource(arrivalTimeProb); //The Probability of a new Arrival plane
        BooleanSource takeOffProbability = new BooleanSource(departureProb); //the Probability of a plane Departing

        int currentMinute;
        int crashCount = 0;

        //check precondition before running sim
/*
        if(landingTime <= 0 || takeOffTime <= 0 || arrivalTimeProb > 1 || departureProb > 1 || totalSimTime < 0)
        {
            throw new IllegalArgumentException("Values out of range");
        }
*/

        for(currentMinute = 0; currentMinute < totalSimTime; currentMinute++)
        {

                if(landingProbability.query()) //is a new plane ready to land?
                {
                    landingsQueue.add(currentMinute); //if there is a landing we add the current second
                }

                if(takeOffProbability.query()) //is a new plane ready to depart?
                {
                    takeOffQueue.add(currentMinute);//if there is a takeOff we add the copy current second
                }

            //we need to check if the runway is not busy we can land a plane
            //if the runway is not busy and the landing queue is empty then we can take off
            //if the runway is not busy and the take off queue is full and landing queue is full then prioritize the landing

            //if planes wait > 5 in landing cue they crash
            //sim will not discover this until the plane is removed
            //at that time record the crash, that plane is discarded and the next plane is processed
            //crashed planes are not considered in wait time (avgLandingWaitTimes)

            if(!runway.isBusy() && !landingsQueue.isEmpty())//if runway is not busy and landing queue is not empty
            {
                //remove from the landing queue
                int nextLanding =0;
                boolean hasCrashed = true;
                int waitingTime = 0;

                while(hasCrashed && !landingsQueue.isEmpty())
                {
                    nextLanding = landingsQueue.remove();
                    waitingTime = currentMinute - nextLanding;
                    hasCrashed = waitingTime > crashingTime;
                    if(hasCrashed) //if it has crashed then increase the count
                    {
                        crashCount++;
                        //System.out.println("next landing crashed: " + nextLanding);
                    }
                   // System.out.println("next landing: " + nextLanding);
                }

                if(!hasCrashed) //if it has not crashed then we found a plane
                {

                    avgLandingWaitTimes.addNumber(waitingTime);
                    runway.startLanding(); // start the cooldown for landing
                    //System.out.println("plane landed: "+ nextLanding);
                }


            }
            else if(!runway.isBusy() && !takeOffQueue.isEmpty())
            {

                int nextTakeOff = takeOffQueue.remove();
                avgTakeOffWaitTimes.addNumber(currentMinute - nextTakeOff);
                runway.startTakingOff();
                //System.out.println("plane take off: "+ nextTakeOff);
            }


            runway.reduceRemainingTime(); //reduce the time remaining by 1
            //System.out.print("Current Minute: " + currentMinute + " ");
            //System.out.print("Landing Queue Size: " + landingsQueue.size() + " ");
            //System.out.println("Take off Queue Size: " + takeOffQueue.size());
        }




        //these will be at the end of simulation after we get all correct values
        System.out.println("Number of planes taken off: " + avgTakeOffWaitTimes.howManyNumbers());
        System.out.println("Number of planes landed: " + avgLandingWaitTimes.howManyNumbers());
        System.out.println("Number of planes crashed: " + crashCount);
        System.out.println("Average waiting time for taking off: " + avgTakeOffWaitTimes.average());
        System.out.println("Average waiting time for landing: " + avgLandingWaitTimes.average());

    }
}
