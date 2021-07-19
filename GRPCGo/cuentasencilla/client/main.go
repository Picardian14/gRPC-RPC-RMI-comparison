package main

import (
	"context"
	"fmt"
	"log"
	"math"
	"time"

	pb "github.com/picardian14/distribuidos/cuentasencilla/protos"

	"google.golang.org/grpc"
)

func MeanStd(samples []int64) (float64, float64) {
	sum := 0.0
	for _, num := range samples {
		sum += float64(num)
	}
	mean := sum / float64(len(samples))
	total := 0.0
	for _, number := range samples {
		total += math.Pow(float64(number)-mean, 2)
	}
	variance := total / float64(len(samples)-1)

	return mean, math.Sqrt(variance)

}

func main() {
	conn, err := grpc.Dial(":9000", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("couldnotconnect")
	}
	defer conn.Close()
	c := pb.NewCalculatorClient(conn)
	var i int32 = 0
	times := make([]int64, 256)
	for i < 256 {
		start := time.Now()
		_, err := c.Add(context.Background(), &pb.Operands{X: i, Y: i})
		end := time.Now()
		if err != nil {
			log.Fatalf("couldnotcall")
		}
		times = append(times, int64(end.Sub(start)/time.Millisecond))
		//log.Printf("Response from calculator: %d", response.Response)

		i += 1
	}
	mean, std := MeanStd(times)
	fmt.Printf("%f %f\n", mean, std)
}
