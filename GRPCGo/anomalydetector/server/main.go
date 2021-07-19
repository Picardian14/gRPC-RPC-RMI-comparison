package main

import (
	"io"
	"log"
	"math"
	"net"
	"sort"

	pb "github.com/picardian14/distribuidos/anomalydetector/protos"
	"google.golang.org/grpc"
)

type Server struct {
	pb.UnimplementedAnomalyDetectorServer
}

func MeanStd(samples []float64) (float64, float64) {
	sum := 0.0
	for _, num := range samples {
		sum += num
	}
	mean := sum / float64(len(samples))
	total := 0.0
	for _, number := range samples {
		total += math.Pow(number-mean, 2)
	}
	variance := total / float64(len(samples)-1)

	return mean, math.Sqrt(variance)

}

func Median(samples []float64) float64 {
	middle := len(samples) / 2
	result := samples[middle]
	if len(samples)%2 == 0 {
		result = (result + samples[middle-1]) / 2
	}
	return result
}

func (s Server) DetectAnomaly(stream pb.AnomalyDetector_DetectAnomalyServer) error {
	var samples []float64
	for {
		sample, err := stream.Recv()
		if err == io.EOF {

			_, std := MeanStd(samples)
			sort.Float64s(samples)
			median := Median(samples)
			var anomalies []float64
			for _, num := range samples {
				if math.Abs(median-num) > 2*std {
					anomalies = append(anomalies, num)
				}
			}
			return stream.SendAndClose(&pb.AnomalyResponse{
				AnomalousValues: anomalies,
			})
		}
		if err != nil {
			return err
		}
		samples = append(samples, sample.GetSample())
	}

}

func main() {
	lis, err := net.Listen("tcp", ":9000")
	if err != nil {
		log.Fatalf("Failed to serve server: %v", err)
	}
	log.Println("Server listening on port 9000")
	grpcServer := grpc.NewServer()
	s := Server{}
	pb.RegisterAnomalyDetectorServer(grpcServer, s)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Failed grpcServer: %v", err)
	}
}
