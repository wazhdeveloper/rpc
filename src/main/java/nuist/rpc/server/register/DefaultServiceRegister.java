package nuist.rpc.server.register;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认服务注册器
 * @author wazh
 * @since 2024-04-24-21:05
 */
public class DefaultServiceRegister implements ServiceRegister {
    private Map<String, ServiceObject> serviceMap = new HashMap<>();
    protected String protocol;
    protected Integer port;
    @Override
    public void register(ServiceObject so) throws Exception {
        if (so == null) throw new IllegalAccessException("Parameter cannot be empty.");
        this.serviceMap.put(so.getName(), so);
    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return this.serviceMap.get(name);
    }
}
