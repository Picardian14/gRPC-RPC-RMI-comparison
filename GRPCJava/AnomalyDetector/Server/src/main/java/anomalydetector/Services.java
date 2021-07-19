package anomalydetector;

import anomalydetector.AnomalyResponse;
import anomalydetector.SingleAnomalyRequest;
import anomalydetector.AnomalyDetectorGrpc;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;



public class Services extends AnomalyDetectorGrpc.AnomalyDetectorImplBase {
    @Override
    public StreamObserver<SingleAnomalyRequest> detectAnomaly(final StreamObserver<AnomalyResponse> responseObserver) {
        System.out.println("Llamado");
        return new StreamObserver<SingleAnomalyRequest>(){
            java.util.ArrayList<Double> anomalies = new ArrayList<Double>();


            @Override
            public void onNext(SingleAnomalyRequest req) {
                anomalies.add(req.getSample());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error occurred  ");
            }
            public Double getMedian(ArrayList<Double> l){
                int h = l.size()/2;
                return (h % 2 != 0) 
                    ? l.get((int) Math.ceil(h)) 
                    : (l.get(h) + l.get(h+1))/2;
            }

            public Double getMedianL(List<Double> l){
                int h = l.size()/2;
                return (h % 2 != 0) 
                    ? l.get((int) Math.ceil(h)) 
                    : (l.get(h) + l.get(h+1))/2;
            }

            public void onCompleted() {
                Collections.sort(anomalies);                
                Double median = getMedian(anomalies);
                Double mad = getMedianL(anomalies.stream().map(x -> Math.abs(x - median)).collect(Collectors.toList()));
                List<Double> anomalyList = new ArrayList();
                for (Double anomaly : anomalies) {
                    if (Math.abs(anomaly-median)>10*mad){
                        anomalyList.add(anomaly);                        
                    }                    
                }
                responseObserver.onNext(AnomalyResponse.newBuilder().addAllAnomalousValues(anomalyList).build());
                responseObserver.onCompleted();
                
                

            }
        };
    }
}