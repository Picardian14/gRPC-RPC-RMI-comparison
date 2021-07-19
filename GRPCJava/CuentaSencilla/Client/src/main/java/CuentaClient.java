import cuenta.grpc.APIResponse;
import cuenta.grpc.calculatorGrpc;
import cuenta.grpc.operand;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class CuentaClient {
    public static void main(String args[]) throws InterruptedException{
        ManagedChannel  channel = ManagedChannelBuilder.forAddress("192.168.0.144", 9000).usePlaintext().build();

        //stub
        long start, end;
        calculatorGrpc.calculatorBlockingStub stub = calculatorGrpc.newBlockingStub(channel);
        operand op = operand.newBuilder().setX(1).setY(2).build();
        for(int i=0;i < 30;i++){

            start = System.nanoTime();        
            APIResponse response = stub.add(op);
            end = System.nanoTime();
            long elapsed = (end - start);
            System.out.println(elapsed);
            Thread.sleep(5000);
        }
        

    }
}
