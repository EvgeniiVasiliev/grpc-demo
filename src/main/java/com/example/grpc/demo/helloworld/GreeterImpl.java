package com.example.grpc.demo.helloworld;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GreeterImpl extends GreeterGrpc.GreeterImplBase{

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello " + req.getName())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
