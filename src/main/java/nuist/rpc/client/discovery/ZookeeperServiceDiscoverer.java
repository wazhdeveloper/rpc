package nuist.rpc.client.discovery;

import com.alibaba.fastjson.JSON;
import nuist.rpc.common.constants.LeisureConstant;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nuist.rpc.common.service.Service;
/**
 * 服务发现抽象类，定义服务发现规范
 * @author wazh
 * @since 2024-04-24-10:25
 */
public class ZookeeperServiceDiscoverer implements ServiceDiscoverer {

    private ZkClient zkclient;

    public ZookeeperServiceDiscoverer(String zkAddress) {
        zkclient = new ZkClient(zkAddress);
        zkclient.setZkSerializer(new SerializableSerializer());
    }

    @Override
    public List<Service> getService(String name) {
        String servicePath = LeisureConstant.ZK_SERVICE_PATH + LeisureConstant.PATH_DELIMITER + name + "/service";
        List<String> children = zkclient.getChildren(servicePath);
        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map(str -> {
            String deCh = null;
            try {
                deCh = URLDecoder.decode(str, LeisureConstant.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JSON.parseObject(deCh, Service.class);
        }).collect(Collectors.toList());
    }
}
