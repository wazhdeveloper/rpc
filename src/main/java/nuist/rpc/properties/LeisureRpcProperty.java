package nuist.rpc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 参数配置类，实现用户自定义参数
 */
@EnableConfigurationProperties(LeisureRpcProperty.class)
@ConfigurationProperties("nuist.rpc")
public class LeisureRpcProperty {

    /**
     * 服务注册中心
     */
    private String registerAddress = "8.130.8.46:2181";

    /**
     * 服务端暴露端口
     */
    private Integer serverPort = 19000;

    /**
     * 服务协议
     */
    private String protocol = "leisure";

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
