package jpmc.sebin.test;

import jpmc.sebin.impl.MessageImpl;

import org.junit.Assert;
import org.junit.Test;

public class TestMessageImpl {

    /**
     * Testing the creation of MessageIMPL class
     */
    @Test
    public void testMessageObjectCreation() {
        MessageImpl msg1 = new MessageImpl(1, 2);
        MessageImpl msg2 = new MessageImpl(1, 2);
        Assert.assertEquals(msg1, msg2);

        MessageImpl msg3 = new MessageImpl(2, 2);
        Assert.assertNotEquals(msg1, msg3);

        MessageImpl msg4 = new MessageImpl(1, 2, true);
        Assert.assertNotEquals(msg1, msg4);

    }

    /**
     * Tests the message completion
     */
    @Test
    public void testMessageCompletion() {
        MessageImpl msg1 = new MessageImpl(1, 2);
        msg1.completed();
        Assert.assertTrue(msg1.isMessageCompleted());

    }

    /**
     * Tests the message Terminaton Type
     */
    @Test
    public void testMessageTerminatonType() {
        MessageImpl msg1 = new MessageImpl(1, 2, true);
        Assert.assertTrue(msg1.isTerminationType());

        MessageImpl msg2 = new MessageImpl(2, 2, false);
        Assert.assertFalse(msg2.isTerminationType());

        MessageImpl msg3 = new MessageImpl(3, 2);
        Assert.assertFalse(msg3.isTerminationType());

    }

}
