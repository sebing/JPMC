package jpmc.sebin.test;

import jpmc.sebin.impl.GatewayImpl;
import jpmc.sebin.impl.MessageImpl;

import org.junit.Assert;
import org.junit.Test;

public class TestGatewayImpl {

    /**
     * testing the implementation of the GatewayImpl
     */
    @Test
    public void testGatewaySend() {
        GatewayImpl gateway = new GatewayImpl();
        MessageImpl msg1 = new MessageImpl(1, 2);
        gateway.send(msg1);

        Assert.assertTrue(msg1.isMessageCompleted());
    }

}
