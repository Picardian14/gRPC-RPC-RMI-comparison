package main

import (
	"context"
	"io"
	"log"
	"os"

	pb "github.com/picardian14/distribuidos/filesystem/proto"
	"google.golang.org/grpc"
)

func main() {
	conn, err := grpc.Dial(":9000", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Could not connect")
	}
	defer conn.Close()
	c := pb.NewFileSystemClient(conn)
	stream, err := c.Write(context.Background())
	if err != nil {
		log.Fatalf("%v.Write(_) = _, %v", c, err)
	}
	file, err := os.Open("10.1.1.69.682.pdf")
	if err != nil {
		log.Fatalf("Error Opening: %v", err)
	}
	buf := make([]byte, 1024)
	req := &pb.Chunk{
		Data: &pb.Chunk_Meta{
			Meta: &pb.MetaInfo{
				Name: "10.1.1.69.682",
				Type: "pdf",
			},
		}}
	stream.Send(req)
	writing := true
	for writing {
		n, err := file.Read(buf)
		if err == io.EOF {
			log.Println("Endend reading file")
			writing = false
			continue
		}
		if err != nil {
			log.Fatalf("Error when reading: %v", err)
		}
		stream.Send(&pb.Chunk{
			Data: &pb.Chunk_Content{
				Content: buf[:n],
			},
		})
	}
	status, err := stream.CloseAndRecv()
	if err != nil {
		log.Fatalf("Got error when closing: %v\n", err)
	}
	log.Printf("Ended with status: %v\n", status)
}
