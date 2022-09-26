import grpc_demo.sms.proto.SmsProto;
import grpc_demo.sms.proto.SmsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcClientAndServiceStream {

    private SmsServiceGrpc.SmsServiceStub asyncStub = null;
    public static final String host = "localhost";
    public static final int servicePort = 9999;

    public static void main(String[] args) {
        GrpcClientAndServiceStream client = new GrpcClientAndServiceStream();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host,servicePort)
                .usePlaintext()
                //无需加密或认证
                .build();
        //客户端流式RPC通信模式必须创建异步Stub实现
        client.asyncStub = SmsServiceGrpc.newStub(channel);
        try {
            client.createPhone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPhone() throws InterruptedException {
        StreamObserver<SmsProto.PhoneNumberRequest> requestPhoneNumber = asyncStub.createAndSendSms(responseObserver);

        for (int i = 0; i < 10; i++) {
            SmsProto.PhoneNumberRequest request = SmsProto.PhoneNumberRequest.newBuilder()
                    .setPhoneNumber(String.valueOf(i))
                    .build();
            requestPhoneNumber.onNext(request);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                requestPhoneNumber.onError(e);
                throw e;
            }
        }

        requestPhoneNumber.onCompleted();
        Thread.sleep(1000);
    }

    private StreamObserver<SmsProto.PhoneNumberResponse> responseObserver = new StreamObserver<SmsProto.PhoneNumberResponse>() {
        @Override
        public void onNext(SmsProto.PhoneNumberResponse phoneNumberResponse) {
            System.out.println(phoneNumberResponse.getResult());
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onCompleted() {
            System.out.println("处理完毕！");
        }
    };
}
