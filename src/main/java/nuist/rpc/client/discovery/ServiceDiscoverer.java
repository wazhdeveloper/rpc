package nuist.rpc.client.discovery;
import nuist.rpc.common.service.Service;

import java.util.List;

/**
 * @author wazh
 * @since 2024-04-24-10:25
 */
public interface ServiceDiscoverer {
    List<Service> getService(String name);
}
