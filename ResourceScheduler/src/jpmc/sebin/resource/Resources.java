package jpmc.sebin.resource;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jpmc.external.Message;
import jpmc.sebin.impl.GatewayFactory;
import jpmc.sebin.impl.MessageImpl;

/**
 * @author Sebin
 * 
 * This class implements the Resource scheduling logic.
 * A priority blocking queue with the threadpool executer is used for the as available resources permit.
 *
 */
public class Resources {

    private ThreadPoolExecutor exe; // The thread pool executor object

    /*
     * a counter map for the goups wich holds the data for finding the priority of the groups
     */
    private Map<Integer, Integer> groupPriorityMap = new HashMap<Integer, Integer>();

    /*
     * A set to keep track of the terminated Groups
     */
    private Set<Integer> terminatedGroupSet = new HashSet<Integer>();

    /**
     * @param availableResouces
     * A constructor for creating the Resources. The configuration of number of available resource is set in the constructor
     */
    public Resources(int availableResouces) {
        /*
         * A priority blocking queue with the threadpool executer is used for the as available resources permit.
         */
        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(availableResouces, new ComparePriority());
        exe = new ThreadPoolExecutor(availableResouces, availableResouces, 10, TimeUnit.SECONDS, queue);
    }

    /**
     * @param availableResouces
     * @param comparator
     * This constructor will create with the configuration of available resouces and an alternate logic for priority.
     */
    public Resources(int availableResouces, Comparator comparator) {
        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(availableResouces, comparator);
        exe = new ThreadPoolExecutor(availableResouces, availableResouces, 10, TimeUnit.SECONDS, queue);
    }

    /**
     * @param message
     * The method actually calls the Gateway with the message.
     * If the thread is interrupted, don't send the message.
     */
    private void doProcessMessage(Message message) {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }
        GatewayFactory.getGatewayInstance().send(message);

    }

    /**
     * @param msg
     * This method receives a message and schedule it in the thread pool
     */
    public void recieveMessage(MessageImpl msg) {
        if (checkForTerminatedGroup(msg)) {
            throw new TerminatedGroupException("Invalid message: this group is terminated." + msg);
        }
        exe.execute(new RunWithGroupPriority(getPriority(msg.getGroupId())) {

            @Override
            public void run() {
                doProcessMessage(msg);

            }
        });

    }

    private boolean checkForTerminatedGroup(MessageImpl msg) {
        if (terminatedGroupSet.contains(msg.getGroupId())) {
            return true;
        }
        if (msg.isTerminationType()) {
            terminatedGroupSet.add(msg.getGroupId());
        }
        return false;
    }

    /**
     * @param groupId
     * @return
     * This method calculate the priority of the Queue from the groupid
     */
    private synchronized int getPriority(Integer groupId) {
        Integer count = groupPriorityMap.get(groupId);
        if (count == null) {
            groupPriorityMap.put(groupId, 1); // add a new entry to the map
            return 1; // High priority
        }

        count = count.intValue() + 1;

        groupPriorityMap.put(groupId, count); // update the value in the map
        return count;
    }

    /**
     * @author Sebin
     *
     * @param <T>
     * A default implementation of the sorting of the Queue
     * 
     */
    private static class ComparePriority<T extends RunWithGroupPriority> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            return o1.getPriority().compareTo(o2.getPriority());
        }

    }

    /**
     * This method will close the Resources.
     */
    public void close() {
        exe.shutdown();
        try {
            exe.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void cancel() {
        exe.shutdownNow();
    }

    /**
     * @author Sebin
     * A simple class to store the priority
     *
     */
    private static abstract class RunWithGroupPriority implements Runnable {

        private int priority;

        public RunWithGroupPriority(int priority) {
            this.priority = priority;
        }

        public Integer getPriority() {
            return priority;
        }
    }

}
