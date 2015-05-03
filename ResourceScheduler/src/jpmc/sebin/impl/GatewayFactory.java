package jpmc.sebin.impl;

import jpmc.external.Gateway;

/**
 * @author Sebin
 * A factory implementation to get the actual implementation of the Gateway
 *
 */
public class GatewayFactory {
    private static Gateway gateway = null;

    /**
     * @return
     * returns an implementation of the gateway.
     */
    public static Gateway getGatewayInstance() {
        if (gateway == null) {
            gateway = new GatewayImpl();
        }

        return gateway;
    }

}
