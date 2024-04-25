package nuist.rpc.client.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import nuist.rpc.client.net.handler.SendHandler;
import nuist.rpc.common.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty网络请求客户端，定义通过Netty实现网络请求的细则。
 * @author wazh
 * @since 2024-04-24-10:58
 */
public class NettyNetClient implements NetClient {
    private static Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    /**
     * 发送请求
     *
     * @param data    请求数据
     * @param service 服务信息
     * @return 响应数据
     * @throws InterruptedException 异常
     */
    @Override
    public byte[] sendRequest(byte[] data, Service service) throws InterruptedException {
        String[] addInfoArray = service.getAddress().split(":");
        String serverAddress = addInfoArray[0];
        String serverPort = addInfoArray[1];

        SendHandler sendHandler = new SendHandler(data);
        byte[] respData;
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(sendHandler);
                        }
                    });
            b.connect(serverAddress, Integer.parseInt(serverPort)).sync();
            respData = (byte[]) sendHandler.rspData();
            logger.info("SendRequest get reply: {}", respData);
        } finally {
            group.shutdownGracefully();
        }
        return respData;
    }


}
