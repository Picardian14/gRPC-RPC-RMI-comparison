import anomalydetector.AnomalyDetectorGrpc;
import anomalydetector.AnomalyResponse;
import anomalydetector.SingleAnomalyRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.*;
import java.util.concurrent.*;

public class AnomalyClient {
    public static void main(String[] args) throws InterruptedException{
           ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
           AnomalyDetectorGrpc.AnomalyDetectorStub stub = AnomalyDetectorGrpc.newStub(channel);
           final CountDownLatch finishLatch = new CountDownLatch(1);
           StreamObserver<AnomalyResponse> responseObserver = new StreamObserver<AnomalyResponse>() {
               @Override
               public void onNext(AnomalyResponse response) {
                    List<Double> responseList = response.getAnomalousValuesList();
                    for (Double anomaly : responseList ){
                        System.out.println("Anomamly: "+anomaly);
                    }
                    
               }
               @Override
               public void onError(Throwable t) {
                   System.out.println(t);
                   finishLatch.countDown();
               }
               @Override
               public void onCompleted() {
                   System.out.println("Finished serving");
                   finishLatch.countDown();
               }
           };
           StreamObserver<SingleAnomalyRequest> requestObserver = stub.detectAnomaly(responseObserver);
           try {
               List<Double> samples = List.of(1.1,1.1,1.1,1.1,1.1,1.1,1.4,1.5);
               for (Double sample : samples) {
                   SingleAnomalyRequest.Builder req = SingleAnomalyRequest.newBuilder().setSample(sample);
                   requestObserver.onNext(req.build());
                   System.out.println("Sended sample "+sample);
                   Thread.sleep(500);
                   if (finishLatch.getCount()==0){
                       return;
                   }                   
               }               
           } catch (RuntimeException e) {
                requestObserver.onError(e);
                throw e;
           }
           requestObserver.onCompleted();
           finishLatch.await(1, TimeUnit.MINUTES);
    }
}