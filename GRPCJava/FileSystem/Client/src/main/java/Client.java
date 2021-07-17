import filesystem.grpc.FileSystemGrpc;
import filesystem.grpc.Chunk;
import filesystem.grpc.Response;
import filesystem.grpc.MetaInfo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;

import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;
import java.io.*;



public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();        
        FileSystemGrpc.FileSystemStub stub = FileSystemGrpc.newStub(channel);        
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Response> responseObserver = new StreamObserver<Response>() {
               @Override
               public void onNext(Response response) {
                    System.out.println("Status: "+response.getStatus());
                    
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
        
        StreamObserver<Chunk> requestObserver = stub.write(responseObserver);
        
        InputStream inputstream;
        try {            
            Path path = Paths.get("src/main/resources/input/paper.pdf");
            Chunk metadata = Chunk.newBuilder().
                setMeta(MetaInfo.newBuilder()
                    .setName("paper")
                    .setType("pdf").build())
                .build();
            requestObserver.onNext(metadata);
            
            inputstream = Files.newInputStream(path);
            byte[] bytes = new byte[1024];
            int size;
            size = inputstream.read(bytes);
            
            while(size > 0) {
                Chunk chunk = Chunk.newBuilder()
                    .setContent(ByteString.copyFrom(bytes, 0, size)).build();
                requestObserver.onNext(chunk);
                if (finishLatch.getCount()==0){
                       return;
                }
                size = inputstream.read(bytes);
                
                
            }
        } catch (RuntimeException e) {
                requestObserver.onError(e);
                throw e;
        }
        inputstream.close();
        requestObserver.onCompleted();  
        finishLatch.await(500, TimeUnit.MILLISECONDS);
    }    
        
}