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
            enterHall();
            startMeeting();
            endMeeting();
            exitFromHall();
        }
    }

    /**
     * king enters the great hall
     */
    public void enterHall() {
        try {
            // imitate the king waiting outside of the hall
            sleep(Params.getKingWaitingTime());

            hall.kingEnters();
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * try to start the meeting
     */
    public void startMeeting() {
        try {
            hall.startMeeting();
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * try to end the meeting
     */
    public void endMeeting() {
        try {
            hall.endMeeting();
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * king exit from the great hall
     */
    public void exitFromHall() {
        try {
            hall.kingExits();
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }
}
