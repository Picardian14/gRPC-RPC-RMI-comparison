package maxupdater;

import maxupdater.MaxUpdaterGrpc;
import maxupdater.Max;
import maxupdater.Number;
import io.grpc.stub.StreamObserver;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Services extends MaxUpdaterGrpc.MaxUpdaterImplBase {
    @Override
    public StreamObserver<Number> updateMax(final StreamObserver<Max> responseObserver) {
        return new StreamObserver<Number>() {
            int max = Integer.MIN_VALUE;
            @Override
            public void onNext(Number n) {
                if (n.getNum() > max) {
                    max = n.getNum();
                    responseObserver.onNext(Max.newBuilder().setMax(max).build());
                }
            }
            @Override
            public void onError(Throwable t) {
                System.out.println("Error in server");
            }

            @Override
            public void onCompleted(){
                responseObserver.onCompleted();
            }
        };
    }


}
