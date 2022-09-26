import grpc_demo.news.proto.NewsProto;
import grpc_demo.news.proto.NewsServiceGrpc;
import grpc_demo.sms.proto.SmsProto;
import grpc_demo.sms.proto.SmsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.List;

public class GrpcClientServiceStream {

    public static final String host = "localhost";
    public static final int servicePort = 9999;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host,servicePort)
                .usePlaintext()
                //无需加密或认证
                .build();
        //BlockingStub支持服务器推流
        SmsServiceGrpc.SmsServiceBlockingStub blockingStub = SmsServiceGrpc.newBlockingStub(channel);

        //传入参数，服务器推流在客户端使用迭代器接收
        Iterator<SmsProto.SmsResponse> itr = blockingStub.sendSms(
                SmsProto.SmsRequest.newBuilder()
                        .setContent("马上国庆啦！")
                        .addPhoneNumber("123123123")
                        .addPhoneNumber("124189412498")
                        .addPhoneNumber("50839829371")
                        .build()
        );

        while (itr.hasNext()) {
            SmsProto.SmsResponse response = itr.next();
            System.out.println(response.getResult());
        }
    }
}
