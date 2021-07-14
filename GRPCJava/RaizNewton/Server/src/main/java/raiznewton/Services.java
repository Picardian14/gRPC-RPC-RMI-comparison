package raiznewton;

import com.distribuidos.grpc.RootResponse;
import com.distribuidos.grpc.IntRootRequest;
import com.distribuidos.grpc.NewtonCalculatorGrpc;
import io.grpc.stub.StreamObserver; 


public class Services extends NewtonCalculatorGrpc.NewtonCalculatorImplBase {
    @Override
    public void rootMethod(IntRootRequest req, StreamObserver<RootResponse> responseObserver){
        float z = 1;
        float z_prime =  1;
        int iterations = (int) req.getPrecision();
        float x = (float) req.getRequestedRoot();
        float delta;
        RootResponse.Builder response =  RootResponse.newBuilder();

        for (int i=0; i < iterations;i++) {
            z_prime = z - ((z*z) - x)/(2*z);
            delta = z_prime-z;
            response.setRoot(z_prime).setDelta(delta);
            responseObserver.onNext(response.build());
            z = z_prime;
            try {
                Thread.currentThread().sleep(1000);    
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

        responseObserver.onCompleted();
    }
}

