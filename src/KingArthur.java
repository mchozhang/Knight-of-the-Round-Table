/**
 * @author Wenhao Zhang 970012
 */
public class KingArthur extends Thread {
    private Hall hall;

    public KingArthur(Hall hall) {
        this.hall = hall;
    }

    public void run() {
        while (!isInterrupted()) {
            entersHall();
            exitFromHall();
        }
    }

    /**
     * king enters the great hall
     */
    public void entersHall() {
        try {
            sleep(Params.getKingWaitingTime());
            System.out.println("King Arthur enters the Great Hall.");
            hall.kingEnters();
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
            System.out.println("King Arthur exits from the Great Hall.");
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }
}
