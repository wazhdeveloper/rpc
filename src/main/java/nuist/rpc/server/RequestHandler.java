package nuist.rpc.server;

import nuist.rpc.common.protocol.LeisureRequest;
import nuist.rpc.common.protocol.LeisureResponse;
import nuist.rpc.common.protocol.LeisureStatus;
import nuist.rpc.common.protocol.MessageProtocol;
import nuist.rpc.server.register.ServiceObject;
import nuist.rpc.server.register.ServiceRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理者，提供解组请求、编组响应等操作
 */
public class RequestHandler {
    private MessageProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
        super();
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        // 1、解组消息
        LeisureRequest req = this.protocol.unmarshallingRequest(data);

        // 2、查找服务对象
        ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

        LeisureResponse rsp = null;

        if (so == null) {
            rsp = new LeisureResponse(LeisureStatus.NOT_FOUND);
        } else {
            // 3、反射调用对应的过程方法
            try {
                Method m = so.getClazz().getMethod(req.getMethod(), req.getParameterTypes());
                Object returnValue = m.invoke(so.getObj(), req.getParameters());
                rsp = new LeisureResponse(LeisureStatus.SUCCESS);
                rsp.setReturnValue(returnValue);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException e) {
                rsp = new LeisureResponse(LeisureStatus.ERROR);
                rsp.setException(e);
            }
        }

        // 4、编组响应消息
        return this.protocol.marshallingResponse(rsp);
    }
    // getter setter ...
}
