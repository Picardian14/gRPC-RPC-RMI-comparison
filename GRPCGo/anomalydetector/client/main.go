package main

import (
	"context"
	"log"

	pb "github.com/picardian14/distribuidos/anomalydetector/protos"
	"google.golang.org/grpc"
)

func main() {
	samples := []float64{1.15, 1.12, 1.16, 1.13, 1.13, 1.13, 1.13, 2.6, 1.14, 1.12, 0.6, 1.12, 1.12, 3.0}
	conn, err := grpc.Dial(":9000", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Could not connect")
	}
	defer conn.Close()
	c := pb.NewAnomalyDetectorClient(conn)
	stream, err := c.DetectAnomaly(context.Background())
	if err != nil {
		log.Fatalf("%v.DetectAnomaly(_) = _, %v", c, err)
	}
	for _, sample := range samples {
		if err := stream.Send(&pb.SingleAnomalyRequest{Sample: sample}); err != nil {
			log.Fatalf("%v.Send(%v) = %v", stream, sample, err)
		}

	}
	reply, err := stream.CloseAndRecv()
	if err != nil {
		log.Fatalf("%v.CloseAndRecv() got error %v, want %v", stream, err, nil)
	}

	log.Printf("Anomalies: %v", reply.GetAnomalousValues())

}
