/**
 * @author Wenhao Zhang 970012
 */

public class Knight extends Thread {
    // id of the knight begin, began from 1
    private int id;

    // the hall
    private Hall hall;

    // the quest currently assigned
    private Quest quest;

    public Knight(int id, Hall hall) {
        this.id = id;
        this.hall = hall;
        this.quest = null;
    }

    public void run() {
        while (!isInterrupted()) {
            entersHall();
            sitsDown();
            releaseQuest();
            acquireQuest();
            standUp();
            exitFromHall();
            setOffToQuest();
            completeQuest();
        }
    }

    /**
     * the knight enters the hall
     */
    private void entersHall() {
        try {
            hall.knightEnters(this);
            System.out.println(String.format("%s enters the hall.", this.toString()));
            sleep(Params.getMinglingTime());
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * sits down on the meeting
     */
    private void sitsDown() {
        System.out.println(String.format("%s sits at the Round Table.", this.toString()));
        hall.knightSitsDown(this);
    }

    /**
     * Release the quest on the meeting if have one
     */
    private void releaseQuest() {
        try {
            if (this.quest != null) {
                hall.knightReleasesQuest(this.quest);
                System.out.println(String.format("%s releases %s", this.toString(), this.quest.toString()));
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
            this.quest = hall.knightAcquiresQuest();
            System.out.println(String.format("%s acquires %s.", this.toString(), quest.toString()));
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * stand up on the meeting
     */
    private void standUp() {
        try {
            System.out.println(String.format("%s stands up", this.toString()));
            hall.knightStandsUp(this);

            // mingling after standing up
            sleep(Params.getMinglingTime());
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
            System.out.println(String.format("%s exits from the Great Hall.", this.toString()));
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * set off to complete the quest
     */
    private void setOffToQuest() {
        try {
            if (this.quest != null) {
                System.out.println(String.format("%s sets of to complete %s.", this.toString(), this.quest.toString()));
                sleep(Params.getQuestingTime());
            }
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    /**
     * the knight complete the current quest
     */
    private void completeQuest() {
        if (this.quest != null) {
            System.out.println(String.format("%s completes %s.", this.toString(), this.quest.toString()));
        }
    }

    @Override
    public String toString() {
        return "Knight " + id;
    }
}
