/**
 * Imitate King Arthur by encapsulating his high level action,
 * which should be run in a loop constantly.
 *
 * @author Wenhao Zhang 970012
 */
public class KingArthur extends Thread {
    // the great hall
    private Hall hall;

    public KingArthur(Hall hall) {
        this.hall = hall;
    }

    /**
     * The cycle of the high level actions of King Arthur
     */
    public void run() {
        while (!isInterrupted()) {
            waitingForNextMeeting();
            entersHall();
            exitFromHall();
        }
    }

    /**
     * king enters the great hall
     */
    public void entersHall() {
        System.out.println("King Arthur enters the Great Hall.");
        hall.kingEnters();
    }

    /**
     * king exit from the great hall
     */
    public void exitFromHall() {
        try {
            hall.kingExits();
            System.out.println("King Arthur exits from the Great Hall.");
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * imitate the king is outside of the hall waiting for the next meeting
     */
    public void waitingForNextMeeting() {
        try {
            sleep(Params.getKingWaitingTime());
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }
}
