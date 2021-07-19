package CuentaSencilla;
import cuenta.grpc.APIResponse;
import cuenta.grpc.operand;
import cuenta.grpc.calculatorGrpc;
import io.grpc.stub.StreamObserver;

public class Services extends calculatorGrpc.calculatorImplBase {
    @Override
    public void add(operand op, StreamObserver<APIResponse> responseObserver) {
        int x = op.getX();
        int y = op.getY();
        APIResponse.Builder response =  APIResponse.newBuilder();
        response.setResult(x + y);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }
    @Override
    public void subs(operand op, StreamObserver<APIResponse> responseObserver) {
        int x = op.getX();
        int y = op.getY();
        APIResponse.Builder response =  APIResponse.newBuilder();
        response.setResult(x - y);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
            
    }
}