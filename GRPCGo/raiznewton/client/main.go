package main

import (
	"context"
	"io"
	"log"
	"time"

	pb "github.com/picardian14/distribuidos/raiznewton/protos"

	"google.golang.org/grpc"
)

func main() {
	conn, err := grpc.Dial(":9091", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("couldnotconnect")
	}
	defer conn.Close()
	c := pb.NewNewtonCalculatorClient(conn)
	log.Println("Connected with server")
	duration := time.Now().Add(100 * time.Millisecond)
	ctx, cancel := context.WithDeadline(context.Background(), duration)
	defer cancel()
	stream, err := c.RootMethod(ctx, &pb.IntRootRequest{RequestedRoot: 2, Precision: 5.0})
	if err != nil {
		log.Fatalf("Error in Server: %v", err)
	}
	iter := 1
	for {
		feature, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatalf("Err in a item: %v", err)
		}
		log.Printf("Iter %d: Recieved root %f with delta %f\n", iter, feature.Root, feature.Delta)
		iter = iter + 1

	}

}
