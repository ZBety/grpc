package grpc_demo.news.service;

import grpc_demo.news.proto.NewsProto;
import grpc_demo.news.proto.NewsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class NewsService extends NewsServiceGrpc.NewsServiceImplBase {

    @Override
    public void list(NewsProto.NewsRequest request, StreamObserver<NewsProto.NewsResponse> responseObserver) {
        long date = request.getDate();
        NewsProto.NewsResponse newsList = null;

        try {
            NewsProto.NewsResponse.Builder newListBuilder = NewsProto.NewsResponse.newBuilder();
            for (int i = 0; i < 100; i++) {
                NewsProto.News news = NewsProto.News.newBuilder()
                        .setId(i)
                        .setTitle("新闻标题是： title-" + i)
                        .setTimestamp(System.currentTimeMillis())
                        .setContent("新闻内容是： " + i)
                        .build();
                newListBuilder.addNews(news);
            }
            newsList = newListBuilder.build();
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onNext(newsList);
        }

        responseObserver.onCompleted();
    }
}
