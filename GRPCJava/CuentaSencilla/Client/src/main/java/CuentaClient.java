import cuenta.grpc.APIResponse;
import cuenta.grpc.calculatorGrpc;
import cuenta.grpc.operand;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class CuentaClient {
    public static void main(String args[]){
        ManagedChannel  channel = ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();

        //stub
        calculatorGrpc.calculatorBlockingStub stub = calculatorGrpc.newBlockingStub(channel);
        operand op = operand.newBuilder().setX(1).setY(2).build();
        APIResponse response = stub.add(op);
        System.out.println("Response: "+response.getResult());

    }
}
