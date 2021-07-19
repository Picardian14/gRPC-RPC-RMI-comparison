package main

import (
	"context"
	"log"
	"net"

	pb "github.com/picardian14/distribuidos/cuentasencilla/protos"
	"google.golang.org/grpc"
)

type Server struct {
	pb.UnimplementedCalculatorServer
}

func (s Server) Add(ctx context.Context, ops *pb.Operands) (*pb.APIResponse, error) {
	var x = ops.GetX()
	var y = ops.GetY()
	return &pb.APIResponse{Response: x + y}, nil
}

func main() {
	lis, err := net.Listen("tcp", ":9000")
	if err != nil {
		log.Fatalf("Failed server port 9000")
	}
	grpcServer := grpc.NewServer()
	s := Server{}
	pb.RegisterCalculatorServer(grpcServer, s)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Failed grpcserver port 9000")
	}

}
