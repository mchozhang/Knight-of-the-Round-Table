import java.util.*;

/**
 *
 * @author Wenhao Zhang 970012
 */


public class Agenda {

    // an FIFO queue structure to store the quests of the agenda
    private Queue<Quest> quests;

    // name of the agenda
    private String name;

    public Agenda(String name) {
        this.name = name;
        this.quests = new LinkedList<>();
    }

    /**
     *  add a quest to the agenda
     */
    public synchronized void addNew(Quest quest) {
        this.quests.add(quest);
        System.out.println(String.format("%s is added to %s.", quest, this.name));
        notifyAll();
    }

    /**
     * assign the first quest to a knight
     * @return first quest of the queue
     */
    public synchronized Quest assignQuest() throws InterruptedException {
        // keep the knight thread waiting until a new quest is added
        while (this.quests.isEmpty()) {
            wait();
        }
        return this.quests.poll();
    }

    /**
     * remove the first completed quest from the agenda
     */
    public synchronized void removeComplete() throws InterruptedException {
        // block the thread if no quest is in the agenda
        while (this.quests.isEmpty()) {
            wait();
        }

        // remove the first quest in the queue
        Quest quest = this.quests.remove();
        System.out.println(String.format("%s is removed from %s.", quest, this.name));
    }
}