package main

import (
	"log"
	"math"
	"net"
	"time"

	pb "github.com/picardian14/distribuidos/raiznewton/protos"
	"google.golang.org/grpc"
)

type Server struct {
	pb.UnimplementedNewtonCalculatorServer
}

func (s Server) RootMethod(root_req *pb.IntRootRequest, stream pb.NewtonCalculator_RootMethodServer) error {
	if root_req.RequestedRoot == 0 {
		if err := stream.Send(&pb.RootResponse{Root: 0, Delta: 0}); err != nil {
		}
	}
	iter := 1
	var z_prime, z float32 = 1.0, 1.0
	for iter <= int(root_req.Precision) {
		z_prime = z - ((z*z)-float32(root_req.RequestedRoot))/(2*z)
		err := stream.Send(&pb.RootResponse{Root: z_prime, Delta: float32(math.Abs(float64(z_prime) - float64(z)))})
		time.Sleep(100 * time.Millisecond)
		if err != nil {
			return err
		}
		z = z_prime
		log.Printf("Finshed iteration %d\n", iter)
		iter = iter + 1
	}
	return nil

}

func main() {
	lis, err := net.Listen("tcp", ":9000")
	if err != nil {
		log.Fatalf("Failed server port 9000")
	}
	log.Println("Server listening on port 9000")
	grpcServer := grpc.NewServer()
	s := Server{}
	pb.RegisterNewtonCalculatorServer(grpcServer, s)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Failed grpcServer port 9000")
	}
}
