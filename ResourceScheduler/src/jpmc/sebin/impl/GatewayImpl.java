/**
 * 
 */
package jpmc.sebin.impl;

import java.util.Random;

import jpmc.external.Gateway;
import jpmc.external.Message;

import org.apache.log4j.Logger;

/**
 * @author Sebin
 *
 */
public class GatewayImpl implements Gateway {
    private final static Logger logger = Logger.getLogger(GatewayImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see jpmc.external.Gateway#send(jpmc.external.Message)
     */
    @Override
    public void send(Message msg) {
        try {
            // fake simulation of random wait
            Thread.sleep(new Random().nextInt(2000));
            msg.completed();
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
        }

    }

}
