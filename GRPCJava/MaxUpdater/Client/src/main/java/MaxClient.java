import maxupdater.Max;
import maxupdater.Number;
import maxupdater.MaxUpdaterGrpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.*;
import java.util.*;

public class MaxClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        MaxUpdaterGrpc.MaxUpdaterStub stub = MaxUpdaterGrpc.newStub(channel);

        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Number> requestObserver = stub.updateMax(new StreamObserver<Max>(){
            @Override
            public void onNext(Max m) {
                System.out.println("Got a new max: "+m.getMax());
            }
            @Override
            public void onError(Throwable t) {
                System.out.print("Got error from Server");
                t.printStackTrace();
            }
            @Override 
            public void onCompleted(){
                System.out.println("Finsihed");
                finishLatch.countDown();
            }

        });
        List<Integer> numbers = List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,4,1,5);
        try {
            for (Integer num : numbers) {
                Number req = Number.newBuilder().setNum(num).build();
                requestObserver.onNext(req);
                Thread.sleep(500);
            }    
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
        finishLatch.await(1, TimeUnit.MINUTES);
        

    }
}