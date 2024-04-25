package nuist.rpc.client.net;


import nuist.rpc.common.service.Service;

/**
 * @author wazh
 * @since 2024-04-24-10:58
 */
public interface NetClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;
}
