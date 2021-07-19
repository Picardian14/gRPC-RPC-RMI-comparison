package main

import (
	"context"
	"io"
	"log"
	"time"

	pb "github.com/picardian14/distribuidos/maxupdater/protos"
	"google.golang.org/grpc"
)

func main() {

	nums := []int32{5, 4, 2, 6, 8, 2, 5, 2, 9, 1, 1}
	slicelen := len(nums)
	conn, err := grpc.Dial(":9000", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Error connecting with server: %v", err)
	}
	defer conn.Close()
	c := pb.NewMaxUpdaterClient(conn)
	stream, err := c.UpdateMax(context.Background())
	if err != nil {
		log.Fatalf("Error opening stream: %v", err)
	}

	var max int32
	ctx := stream.Context()
	done := make(chan bool)

	go func() {
		for i := 1; i < slicelen; i++ {
			if err := stream.Send(&pb.Number{Num: nums[i]}); err != nil {
				log.Fatalf("Error sending %d: %v", nums[i], err)
			}
			log.Printf("Sent %d\n", nums[i])
			time.Sleep(time.Millisecond * 500)
		}
		if err := stream.CloseSend(); err != nil {
			log.Fatalf("%v\n", err)
		}
	}()

	go func() {
		for {
			resp, err := stream.Recv()
			if err == io.EOF {
				close(done)
				return
			}
			if err != nil {
				log.Fatalf("Error receiving: %v", err)
			}
			max = resp.GetMax()
			log.Printf("New Max received: %d", max)
		}
	}()

	go func() {
		<-ctx.Done()
		if err := ctx.Err(); err != nil {
			log.Fatalln(err)
		}
	}()

	<-done
	log.Printf("Finished with max: %d\n", max)
}
