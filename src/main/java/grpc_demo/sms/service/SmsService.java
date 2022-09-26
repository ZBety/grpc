package grpc_demo.sms.service;

import com.google.protobuf.ProtocolStringList;
import grpc_demo.sms.proto.SmsProto;
import grpc_demo.sms.proto.SmsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SmsService extends SmsServiceGrpc.SmsServiceImplBase {
    @Override
    public void sendSms(SmsProto.SmsRequest request, StreamObserver<SmsProto.SmsResponse> responseObserver) {
        ProtocolStringList phoneNumbers = request.getPhoneNumberList();
        for (String phoneNumber: phoneNumbers) {
            SmsProto.SmsResponse response = SmsProto.SmsResponse.newBuilder()
                    .setResult(request.getContent() + "," + phoneNumber + "已发送！")
                    .build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<SmsProto.PhoneNumberRequest> createPhone(StreamObserver<SmsProto.PhoneNumberResponse> responseObserver) {
        //异步通信基于responseObserver事件回调
        return new StreamObserver<SmsProto.PhoneNumberRequest>() {
            int i = 0;

            @Override
            public void onNext(SmsProto.PhoneNumberRequest phoneNumberRequest) {
                System.out.println(phoneNumberRequest.getPhoneNumber() + "已登记");
                i++;
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(SmsProto.PhoneNumberResponse.newBuilder()
                        .setResult("本次导入 " + i + "个号码！")
                        .build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<SmsProto.PhoneNumberRequest> createAndSendSms(StreamObserver<SmsProto.PhoneNumberResponse> responseObserver) {
        return new StreamObserver<SmsProto.PhoneNumberRequest>() {
            int i = 0;
            @Override
            public void onNext(SmsProto.PhoneNumberRequest phoneNumberRequest) {
                System.out.println(phoneNumberRequest.getPhoneNumber() + "已登记！");
                responseObserver.onNext(SmsProto.PhoneNumberResponse.newBuilder()
                        .setResult(phoneNumberRequest.getPhoneNumber() + "发送给领导1")
                        .build());
                responseObserver.onNext(SmsProto.PhoneNumberResponse.newBuilder()
                        .setResult(phoneNumberRequest.getPhoneNumber() + "发送给领导2")
                        .build());
                i++;
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
