package raiznewton;


import io.grpc.Server;
import io.grpc.ServerBuilder;
//import services.Services;

import java.io.IOException;

public class GRPCServer {
    public static void main(String args[]) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9091).addService(new Services()).build();
        server.start();

        System.out.println("Server started on: "+server.getPort());

        server.awaitTermination();

    }
}
