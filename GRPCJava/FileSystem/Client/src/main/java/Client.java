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
        for(int i=0;i<60;i++){
            ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.0.144", 9000).usePlaintext().build();        
            FileSystemGrpc.FileSystemStub stub = FileSystemGrpc.newStub(channel);                    
            final CountDownLatch finishLatch = new CountDownLatch(1);
            StreamObserver<Response> responseObserver = new StreamObserver<Response>() {
                @Override
                public void onNext(Response response) {
                        //System.out.println("Status: "+response.getStatus());
                        
                }
                @Override
                public void onError(Throwable t) {
                    System.out.println(t);
                    finishLatch.countDown();
                    
                }
                @Override
                public void onCompleted() {
                    //System.out.println("Finished serving");                           
                    finishLatch.countDown();
                }

            };
            
            
            InputStream inputstream;
            long start, end;
            StreamObserver<Chunk> requestObserver = stub.write(responseObserver);
            start = System.nanoTime();     
            try {            
                Path path = Paths.get("src/main/resources/input/50MB.zip");
                Chunk metadata = Chunk.newBuilder().
                    setMeta(MetaInfo.newBuilder()
                        .setName("50MB")
                        .setType("zip").build())
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
            finishLatch.await(100, TimeUnit.SECONDS);
            end = System.nanoTime();
            channel.shutdown();
            channel.awaitTermination(100, TimeUnit.MILLISECONDS);
            long elapsed = (end - start);
            System.out.println(elapsed);
        }
    }    
        
}