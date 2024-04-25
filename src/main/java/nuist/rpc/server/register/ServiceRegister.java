package nuist.rpc.server.register;

/**
 * @author wazh
 * @since 2024-04-24-21:03
 */
public interface ServiceRegister {
    void register(ServiceObject so) throws Exception;
    ServiceObject getServiceObject(String name) throws Exception;
}
