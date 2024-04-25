package nuist.rpc.config;

import nuist.rpc.client.ClientProxyFactory;
import nuist.rpc.client.discovery.ZookeeperServiceDiscoverer;
import nuist.rpc.client.net.NettyNetClient;
import nuist.rpc.common.protocol.JavaSerializeMessageProtocol;
import nuist.rpc.common.protocol.MessageProtocol;
import nuist.rpc.properties.LeisureRpcProperty;
import nuist.rpc.server.DefaultRpcProcessor;
import nuist.rpc.server.NettyRpcServer;
import nuist.rpc.server.RequestHandler;
import nuist.rpc.server.RpcServer;
import nuist.rpc.server.register.ServiceRegister;
import nuist.rpc.server.register.ZookeeperExportServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring boot 自动配置类
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public DefaultRpcProcessor defaultRpcProcessor() {
        return new DefaultRpcProcessor();
    }

    @Bean
    public ClientProxyFactory clientProxyFactory(@Autowired LeisureRpcProperty leisureRpcProperty) {
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        // 设置服务发现者
        clientProxyFactory.setSid(new ZookeeperServiceDiscoverer(leisureRpcProperty.getRegisterAddress()));

        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put(leisureRpcProperty.getProtocol(), new JavaSerializeMessageProtocol());
        clientProxyFactory.setSupportMessageProtocols(supportMessageProtocols);

        // 设置网络层实现
        clientProxyFactory.setNetClient(new NettyNetClient());
        return clientProxyFactory;
    }

    @Bean
    public ServiceRegister serviceRegister(@Autowired LeisureRpcProperty leisureRpcProperty) {
        return new ZookeeperExportServiceRegister(
                leisureRpcProperty.getRegisterAddress(),
                leisureRpcProperty.getServerPort(),
                leisureRpcProperty.getProtocol());
    }

    @Bean
    public RequestHandler requestHandler(@Autowired ServiceRegister serviceRegister) {
        return new RequestHandler(new JavaSerializeMessageProtocol(), serviceRegister);
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandler requestHandler,
                               @Autowired LeisureRpcProperty leisureRpcProperty) {
        return new NettyRpcServer(leisureRpcProperty.getServerPort(),
                leisureRpcProperty.getProtocol(), requestHandler);
    }

    @Bean
    public LeisureRpcProperty leisureRpcProperty() {
        return new LeisureRpcProperty();
    }
}
