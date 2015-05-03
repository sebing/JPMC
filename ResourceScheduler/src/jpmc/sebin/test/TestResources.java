package jpmc.sebin.test;

import java.util.Random;

import jpmc.sebin.impl.MessageImpl;
import jpmc.sebin.resource.Resources;
import jpmc.sebin.resource.TerminatedGroupException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestResources {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * A test case to verify the resources class is working with a configuration of one single resource.
     * and multiple messages.
     * Expecting all the messages complete.
     */
    @Test
    public void testSingleResource() {
        Resources resources = new Resources(1);
        MessageImpl msg1 = new MessageImpl(1, 2);
        MessageImpl msg2 = new MessageImpl(2, 1);
        MessageImpl msg3 = new MessageImpl(3, 2);
        MessageImpl msg4 = new MessageImpl(4, 3);
        try {
            resources.recieveMessage(msg1);
            resources.recieveMessage(msg2);
            resources.recieveMessage(msg3);
            resources.recieveMessage(msg4);
        } finally {
            resources.close();
        }

        Assert.assertTrue(msg1.isMessageCompleted());
        Assert.assertTrue(msg2.isMessageCompleted());
        Assert.assertTrue(msg3.isMessageCompleted());
        Assert.assertTrue(msg4.isMessageCompleted());

    }

    /**
     * This is the test case for verifying the messages are queued and completed.
     */
    @Test
    public void testMessageQueing() {
        MessageImpl[] msgs = new MessageImpl[100];

        Resources resources = new Resources(3);
        Random random = new Random();

        try {
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = new MessageImpl(i, random.nextInt(3));
                resources.recieveMessage(msgs[i]);
            }
        } finally {
            resources.close();
        }

        for (MessageImpl msg : msgs) {
            Assert.assertTrue(msg.isMessageCompleted());
        }

    }

    /**
     * Tests the Termination Messages.
     * 
     * creates 7 messages, the message id 5 is the termination signal for the group 5.
     * Expect an exception in message 6. But message 7 should work fine.
     * 
     */
    @Test
    public void testMessageTermination() {
        Resources resources = new Resources(3);

        MessageImpl msg1 = new MessageImpl(1, 2);
        MessageImpl msg2 = new MessageImpl(2, 1);
        MessageImpl msg3 = new MessageImpl(3, 2);
        MessageImpl msg4 = new MessageImpl(4, 3);
        MessageImpl msg5 = new MessageImpl(5, 2, true);
        MessageImpl msg6 = new MessageImpl(6, 2);
        MessageImpl msg7 = new MessageImpl(7, 1);

        try {
            resources.recieveMessage(msg1);
            resources.recieveMessage(msg2);
            resources.recieveMessage(msg3);
            resources.recieveMessage(msg4);
            resources.recieveMessage(msg5);

            exception.expect(TerminatedGroupException.class);
            resources.recieveMessage(msg6);
            resources.recieveMessage(msg7);
        } finally {
            resources.close();
        }

    }

    /**
     * Test the cancelling of the scheduled jobs working.
     * Create an array of messages, which is posted the resource class for scheduling.
     * Then cancel the scheduled messages queue
     * Expecting some of the messages are not completed.
     * three threads are running so three sleeping thread in this implementation should be interrupted. 
     */
    @Test
    public void testMessageCancelling() {
        MessageImpl[] msgs = new MessageImpl[100];
        Resources resources = new Resources(3);
        Random random = new Random();

        try {
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = new MessageImpl(i, random.nextInt(3));
                resources.recieveMessage(msgs[i]);
            }
            resources.cancel();
        } finally {
            resources.close();
        }

        boolean isCompete = false;
        for (MessageImpl msg : msgs) {
            isCompete = msg.isMessageCompleted();
            if (!isCompete) {
                break;
            }
        }
        Assert.assertFalse(isCompete);

    }

}
