package main

import (
	"bytes"
	"io"
	"log"
	"net"
	"os"

	pb "github.com/picardian14/distribuidos/filesystem/proto"
	"google.golang.org/grpc"
)

type Server struct {
	pb.UnimplementedFileSystemServer
}

func (s Server) Write(stream pb.FileSystem_WriteServer) error {
	req, err := stream.Recv()
	if err != nil {
		log.Fatalf("Something happened with metadata: %v\n", err)
		return err
	}
	fileData := bytes.Buffer{}
	fileSize := 0
	status := "Ok"
	for {
		req, err := stream.Recv()
		if err == io.EOF {
			log.Println("Done receiving")
			break
		}
		if err != nil {
			log.Fatalf("Something happened with data chunk: %v\n", err)
			status = "Error receiving"
			return err
		}
		chunk := req.GetContent()
		bytesRead := len(chunk)
		log.Printf("Received %d bytes\n", bytesRead)
		fileSize += bytesRead
		_, writeErr := fileData.Write(chunk)
		if writeErr != nil {
			log.Fatalf("Something happened writing chunk: %v\n", err)
			status = "Error writing"
			return err
		}

	}
	if status != "Ok" {
		stream.SendAndClose(&pb.Response{Status: status, BytesRead: int32(fileSize)})
	}
	file, err := os.Create(req.GetMeta().GetName() + "." + req.GetMeta().Type)
	if err != nil {
		log.Fatalf("Error creating file: %v\n", err)
		return err
	}
	_, localWriteErr := fileData.WriteTo(file)
	if localWriteErr != nil {
		log.Fatalf("Error writing to local file: %v", localWriteErr)
		stream.SendAndClose(&pb.Response{Status: "Error writing local", BytesRead: int32(fileSize)})
	}
	stream.SendAndClose(&pb.Response{Status: status, BytesRead: int32(fileSize)})
	return nil
}

func main() {
	lis, err := net.Listen("tcp", ":9000")
	if err != nil {
		log.Fatalf("Failed to serve server: %v", err)
	}
	log.Println("Server listening on port 9000")
	grpcServer := grpc.NewServer()
	s := Server{}
	pb.RegisterFileSystemServer(grpcServer, s)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Failed grpcServer: %v", err)
	}
}
