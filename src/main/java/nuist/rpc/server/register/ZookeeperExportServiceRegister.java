package nuist.rpc.server.register;

import com.alibaba.fastjson.JSON;
import nuist.rpc.common.serializer.ZookeeperSerializer;
import nuist.rpc.common.service.Service;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

/**
 * Zookeeper服务注册器，提供服务注册、服务暴露的能力
 */
public class ZookeeperExportServiceRegister extends DefaultServiceRegister {
    private ZkClient client;

    /**
     * zk客户端
     */
    public ZookeeperExportServiceRegister(String zkAddress, Integer port, String protocol) {
        client = new ZkClient(zkAddress);
        client.setZkSerializer(new ZookeeperSerializer());
        this.port = port;
        this.protocol = protocol;
    }

    @Override
    public void register(ServiceObject so) throws Exception {
        super.register(so);
        Service service = new Service();
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        service.setAddress(address);
        service.setName(so.getClass().getName());
        service.setProtocol(protocol);
        this.exportService(service);
    }

    /**
     * 服务暴露
     */
    private void exportService(Service serviceResource) {
        String serviceName = serviceResource.getName();
        String uri = JSON.toJSONString(serviceResource);
        try {
            uri = URLEncoder.encode(uri, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = LeisureConstant.ZK_SERVICE_PATH + LeisureConstant.PATH_DELIMITER + serviceName + "/service";
        if (!client.exists(servicePath)) {
            client.createPersistent(servicePath, true);
        }
        String uriPath = servicePath + LeisureConstant.PATH_DELIMITER + uri;
        if (client.exists(uriPath)) {
            client.delete(uriPath);
        }
        client.createEphemeral(uriPath);
    }
}
