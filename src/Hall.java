import java.util.HashMap;
import java.util.Map;

/**
 * imitate the Great Hall having meeting, encapsulate events and its causal result such as
 * the king enter, the knight sits down and the meeting begins and so forth.
 * @author Wenhao Zhang 970012
 */

public class Hall {
    private Agenda newAgenda;
    private Agenda completeAgenda;

    // the mapping of knights that are in the hall and whether they have sat down.
    // Key: the Knight object,
    // Value: Boolean of is the knight sitting down
    private Map<Knight, Boolean> knightMap;

    // whether the king is in the hall
    private boolean isKingIn;

    // whether the meeting is ongoing
    private boolean isMeetingBegan;

    public Hall(Agenda newAgenda, Agenda completeAgenda) {
        this.newAgenda = newAgenda;
        this.completeAgenda = completeAgenda;
        isKingIn = false;
        isMeetingBegan = false;
        knightMap = new HashMap<>();
    }

    /**
     * a knight enters when the king is not in the hall
     * @param knight knight enters the hall
     */
    public synchronized void knightEnters(Knight knight) throws InterruptedException {
        // wait until the king is not in
        while(isKingIn){
            wait();
        }
        knightMap.put(knight, false);
    }

    /**
     * a knight exits when is king is not in the hall
     * @param knight the knight exits the hall
     */
    public synchronized void knightExit(Knight knight) throws InterruptedException {
        // wait until the king is not in
        while (isKingIn) {
            wait();
        }
        knightMap.remove(knight);
    }

    /**
     * A knight sits down
     * @param knight the knight sits down
     */
    public synchronized void knightSitsDown(Knight knight) {
        // update the knight's status
        knightMap.replace(knight, true);

        // meeting begins if the king and the knights are ready
        if (isKingIn && haveAllKnightsSatDown()) {
            isMeetingBegan = true;
            System.out.println("Meeting begins!");
        }

        // wake up the threads that are waiting for the lock
        notifyAll();
    }

    /**
     * A knight stands up, the meeting ends if no knight is sitting.
     * @param knight the knight stands up
     */
    public synchronized void knightStandsUp(Knight knight) {
        // update the knight's status
        knightMap.replace(knight, false);

        // meeting ends if no knight is sitting
        if (isKingIn && haveAllKnightsStoodUp()) {
            isMeetingBegan = false;
            System.out.println("Meeting ends!");
        }

        // wake up the threads that are waiting for the lock
        notifyAll();
    }

    /**
     * a knight releases a quest on meeting, add it to the complete agenda
     * @param quest completed quest
     */
    public synchronized void knightReleasesQuest(Quest quest) throws InterruptedException {
        // wait until the meeting begins
        while (!isMeetingBegan) {
            wait();
        }

        // add completed quest to complete agenda
        completeAgenda.addNew(quest);
    }

    /**
     * assign a new quest to a knight
     * @return new quest in the new agenda
     */
    public synchronized Quest knightAcquiresQuest() throws InterruptedException {
        // wait until the meeting begins
        while (!isMeetingBegan) {
            wait();
        }

        return newAgenda.assignQuest();
    }

    /**
     * king enters the hall, meeting begins at once if at least 1 knight is ready
     */
    public synchronized void kingEnters() {
        isKingIn = true;

        // meeting begins if knights are ready
        if (haveAllKnightsSatDown()) {
            isMeetingBegan = true;
            System.out.println("Meeting begins!");
        }

        notifyAll();
    }

    /**
     * the king exit from the hall
     */
    public synchronized void kingExits() throws InterruptedException {
        // wait until all knights have stood up
        while (!haveAllKnightsStoodUp()) {
            wait();
        }
        if (isMeetingBegan) {
            isMeetingBegan = false;
            System.out.println("Meeting ends!");
        }
        isKingIn = false;
        notifyAll();
    }

    /**
     * utility to judge whether all knights have stood up
     * @return true if all knights have stood up
     */
    public boolean haveAllKnightsStoodUp() {
        for (Map.Entry<Knight, Boolean> element: knightMap.entrySet()) {
            if (element.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * utility to judge whether all knights have stood up
     * @return true if all knights have stood up
     */
    public boolean haveAllKnightsSatDown() {
        for (Map.Entry<Knight, Boolean> element: knightMap.entrySet()) {
            if (!element.getValue()) {
                return false;
            }
        }
        return true;
    }
}
