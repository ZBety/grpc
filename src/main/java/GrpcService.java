import grpc_demo.news.service.NewsService;
import grpc_demo.sms.service.SmsService;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcService {
    public static final int port = 9999;

    public static void main(String[] args) throws IOException, InterruptedException {
        io.grpc.Server server = ServerBuilder.forPort(port)
                .addService(new NewsService())
                .addService(new SmsService())
                .build()
                .start();

        System.out.printf("GRPC服务启动成功 端口为： %d.%n",port);
        server.awaitTermination();
    }
}
