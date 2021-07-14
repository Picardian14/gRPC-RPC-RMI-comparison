import com.distribuidos.grpc.RootResponse;
import com.distribuidos.grpc.IntRootRequest;
import com.distribuidos.grpc.NewtonCalculatorGrpc;

import java.util.Iterator;
import java.util.List;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RootClient {
    public static void main(String args[]){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost" ,9091).usePlaintext().build();

        NewtonCalculatorGrpc.NewtonCalculatorBlockingStub stub = NewtonCalculatorGrpc.newBlockingStub(channel);
        IntRootRequest request = IntRootRequest.newBuilder().setRequestedRoot(2).setPrecision((float) 15).build();                        
        Iterator<RootResponse> responses = stub.rootMethod(request);        
        
        while(responses.hasNext()) {            
            System.out.println(responses.next());
        }
    }
}