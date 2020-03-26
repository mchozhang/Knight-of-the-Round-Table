import java.util.ArrayList;
import java.util.List;

/**
 * imitate the Great Hall having meeting, encapsulate events and its causal result such as
 * the king enter, the knight sits down and the meeting begins and so forth.
 *
 * @author Wenhao Zhang 970012
 */

public class Hall {
    // agenda of new quests
    private Agenda newAgenda;

    // agenda of completed quests
    private Agenda completeAgenda;

    // the list of knights that are in the hall
    private List<Knight> knightList;

    // whether the king is in the hall
    private boolean isKingIn;

    // whether the meeting is ongoing
    private boolean isMeetingBegan;

    public Hall(Agenda newAgenda, Agenda completeAgenda) {
        this.newAgenda = newAgenda;
        this.completeAgenda = completeAgenda;
        isKingIn = false;
        isMeetingBegan = false;
        knightList = new ArrayList<>();
    }

    /**
     * a knight enters when the king is not in the hall
     *
     * @param knight knight enters the hall
     */
    public synchronized void knightEnters(Knight knight) throws InterruptedException {
        // wait until the king is not in
        while (isKingIn) {
            wait();
        }

        knightList.add(knight);
        System.out.println(String.format("%s enters the hall.", knight.toString()));
    }

    /**
     * a knight exits when is king is not in the hall
     *
     * @param knight the knight exits the hall
     */
    public synchronized void knightExit(Knight knight) throws InterruptedException {
        // wait until the king is not in
        while (isKingIn) {
            wait();
        }

        // remove him from the knight list
        knightList.remove(knight);
        System.out.println(String.format("%s exits from the Great Hall.", knight.toString()));

        // wake up the king that are waiting to enter
        notifyAll();
    }

    /**
     * A knight sits down
     *
     * @param knight the knight wants to sit down
     */
    public synchronized void knightSitsDown(Knight knight) {
        System.out.println(String.format("%s sits at the Round Table.", knight.toString()));

        // wake up the king thread that are waiting to start the meeting
        if (haveAllKnightsSatDown()) {
            notifyAll();
        }
    }

    /**
     * A knight stands up
     *
     * @param knight the knight wants to stand up
     */
    public synchronized void knightStandsUp(Knight knight) {
        System.out.println(String.format("%s stands up", knight.toString()));

        // wake up the king thread that are waiting to end the meeting
        if (haveAllKnightsStoodUp()) {
            notifyAll();
        }
    }

    /**
     * a knight releases a quest on meeting, add it to the complete agenda
     *
     * @param knight the knight who releases the quest
     */
    public synchronized void knightReleasesQuest(Knight knight) throws InterruptedException {
        // wait until the meeting begins
        while (!isMeetingBegan) {
            wait();
        }

        System.out.println(String.format("%s releases %s", knight.toString(), knight.quest.toString()));

        // add completed quest to complete agenda
        completeAgenda.addCompleted(knight.quest);
    }

    /**
     * assign a new quest to a knight
     *
     * @param knight the knight acquiring the quest
     * @return new quest in the new agenda
     */
    public synchronized Quest knightAcquiresQuest(Knight knight) throws InterruptedException {
        // wait until the meeting begins
        while (!isMeetingBegan) {
            wait();
        }

        // assign a quest to the knight
        Quest quest = newAgenda.assignQuest();
        System.out.println(String.format("%s acquires %s.", knight.toString(), quest.toString()));

        return quest;
    }

    /**
     * king enters the hall, meeting begins at once if at least 1 knight is sitting or no knight is in the hall
     */
    public synchronized void kingEnters() throws InterruptedException {
        // wait until all knight from the last meeting has exited
        while (!haveAllKnightsExited()) {
            wait();
        }

        // the king successfully enters the hall
        System.out.println("King Arthur enters the Great Hall.");
        isKingIn = true;
    }

    /**
     * the king tries to start the meeting
     */
    public synchronized void startMeeting() throws InterruptedException {
        // wait until all the knights have sat down
        while (!haveAllKnightsSatDown()) {
            wait();
        }

        // update status of the hall
        isMeetingBegan = true;
        System.out.println("Meeting begins!");

        // wake up knight threads that are waiting
        notifyAll();
    }

    /**
     * the king tries to end the meeting
     */
    public synchronized void endMeeting() throws InterruptedException {
        // wait until all the knights have sat down
        while (!haveAllKnightsStoodUp()) {
            wait();
        }

        // update status of the hall
        isMeetingBegan = false;
        System.out.println("Meeting ends!");

        // wake up knight threads that are waiting
        notifyAll();
    }

    /**
     * the king exit from the hall
     */
    public synchronized void kingExits() throws InterruptedException {
        // update hall's status
        isKingIn = false;
        System.out.println("King Arthur exits from the Great Hall.");

        // wake up knight threads that are waiting to exit
        notifyAll();
    }

    /**
     * utility to judge whether all knights have stood up
     *
     * @return true if all knights have stood up
     */
    public boolean haveAllKnightsStoodUp() {
        for (Knight knight : knightList) {
            if (knight.status != Knight.Status.Standing) {
                return false;
            }
        }
        return true;
    }

    /**
     * utility to judge whether all knights have stood up
     *
     * @return true if all knights have stood up
     */
    public boolean haveAllKnightsSatDown() {
        for (Knight knight : knightList) {
            if (knight.status != Knight.Status.Sitting) {
                return false;
            }
        }
        return true;
    }

    /**
     * whether all knights from the last meeting have exited
     *
     * @return true if all knight have exited
     */
    public boolean haveAllKnightsExited() {
        for (Knight knight : knightList) {
            // the knight before the current meeting should be either mingling or sitting
            if (!(knight.status == Knight.Status.MinglingBeforeMeeting || knight.status == Knight.Status.Sitting)) {
                return false;
            }
        }
        return true;
    }
}
