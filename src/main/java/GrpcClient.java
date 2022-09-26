import grpc_demo.news.proto.NewsProto;
import grpc_demo.news.proto.NewsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class GrpcClient {

    public static final String host = "localhost";
    public static final int servicePort = 9999;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host,servicePort)
                .usePlaintext()
                //无需加密或认证
                .build();
        NewsServiceGrpc.NewsServiceBlockingStub blockingStub = NewsServiceGrpc.newBlockingStub(channel);
        NewsProto.NewsRequest request = NewsProto.NewsRequest.newBuilder()
                .setDate(System.currentTimeMillis())
                .build();
        try {
            NewsProto.NewsResponse response = blockingStub.list(request);
            List<NewsProto.News> newsList = response.getNewsList();
            for (NewsProto.News item: newsList) {
                System.out.println(item.getTitle()+" "+item.getContent());
            }
        } finally {
            channel.shutdown();
        }
    }
}
