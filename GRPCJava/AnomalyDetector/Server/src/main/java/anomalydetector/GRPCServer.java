package anomalydetector;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GRPCServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        
        Server server = ServerBuilder.forPort(9000).addService(new Services()).build();
        server.start();
        System.out.println("Server started on: "+server.getPort());
        server.awaitTermination();
        
    }
}