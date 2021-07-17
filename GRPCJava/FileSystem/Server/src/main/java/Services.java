import filesystem.grpc.FileSystemGrpc;
import filesystem.grpc.Chunk;
import filesystem.grpc.Response;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;

import java.nio.file.*;
import java.io.*;

public class Services extends FileSystemGrpc.FileSystemImplBase {
    private static final Path SERVER_BASE_PATH = Paths.get("src/main/resources/output");
    private OutputStream getFilePath(Chunk request) throws IOException {
        var fileName = request.getMeta().getName() + "." + request.getMeta().getType();
        return Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private void writeFile(OutputStream writer, ByteString content) throws IOException {
        writer.write(content.toByteArray());
        writer.flush();
    }

    private void closeFile(OutputStream writer){
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StreamObserver<Chunk> write(final StreamObserver<Response> responseObserver) {
        

        return new StreamObserver<Chunk>() {

            OutputStream writer;
            
            @Override
            public void onNext(Chunk chunk) {
                try{
                    
                    if(chunk.hasMeta()) {                        
                        writer = getFilePath(chunk);
                    } else {
                        writeFile(writer, chunk.getContent());
                    }
                }catch (Exception e){
                    this.onError(e);
                }

            }

            @Override
            public void onError(Throwable T) {
                System.out.println("Error in server");
                T.printStackTrace();       
                Response response = Response.newBuilder()
                        .setStatus("Error!").setBytesRead(1)
                        .build();         
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted(){
                closeFile(writer);                
                Response response = Response.newBuilder()
                        .setStatus("Ok!").setBytesRead(1)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}
