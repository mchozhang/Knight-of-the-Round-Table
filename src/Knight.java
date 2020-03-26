/**
 * Imitation of the Knight by encapsulating its high level actions
 * which should be run as a cycle in a loop.
 *
 * @author Wenhao Zhang 970012
 */

public class Knight extends Thread {

    /**
     * all possible status of a knight
     */
    public enum Status {
        // completing a quest
        Questing,

        // mingling before the meeting
        MinglingBeforeMeeting,

        // sitting at the table, either waiting for or having a meeting
        Sitting,

        // standing and mingling after the meeting
        Standing
    }

    // id of the knight begin, began from 1
    private int id;

    // the hall
    private Hall hall;

    // the quest currently assigned
    public Quest quest;

    // status of the knight
    public Status status;

    /**
     * create a Knight instances with an id and hall object
     *
     * @param id   knight's id
     * @param hall the great hall
     */
    public Knight(int id, Hall hall) {
        this.id = id;
        this.hall = hall;
        this.quest = null;
        this.status = Status.Questing;
    }

    /**
     * high level actions of the knight, which should be run as a cycle in a loop
     */
    public void run() {
        while (!isInterrupted()) {
            enterHall();
            sitDown();
            releaseQuest();
            acquireQuest();
            standUp();
            exitFromHall();
            setOffToQuest();
        }
    }

    /**
     * the knight enters the hall
     */
    private void enterHall() {
        try {
            hall.knightEnters(this);

            // imitate the knight mingling before sitting down
            status = Status.MinglingBeforeMeeting;
            sleep(Params.getMinglingTime());
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * sits down on the meeting
     */
    private void sitDown() {
        status = Status.Sitting;
        hall.knightSitsDown(this);
    }

    /**
     * Release the quest on the meeting if have one
     */
    private void releaseQuest() {
        try {
            if (this.quest != null) {
                hall.knightReleasesQuest(this);
            }
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * Acquire a quest from new agenda on the meeting
     */
    private void acquireQuest() {
        try {
            this.quest = hall.knightAcquiresQuest(this);
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * stand up on the meeting and start mingling
     */
    private void standUp() {
        try {
            status = Status.Standing;
            hall.knightStandsUp(this);

            // imitate the knight mingling after the meeting
            sleep(Params.MEAN_MINGLING_TIME);
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * exit the hall
     */
    private void exitFromHall() {
        try {
            hall.knightExit(this);
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * set off to complete the quest
     */
    private void setOffToQuest() {
        try {
            System.out.println(String.format("%s sets off to complete %s.", this.toString(), this.quest.toString()));
            status = Status.Questing;

            // imitate the quest is ongoing
            sleep(Params.getQuestingTime());

            // quest is completed
            System.out.println(String.format("%s completes %s.", this.toString(), this.quest.toString()));
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * produce an identity string for the knight
     *
     * @return string result
     */
    @Override
    public String toString() {
        return "Knight " + id;
    }
}
