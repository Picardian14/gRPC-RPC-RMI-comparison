package main

import (
	"io"
	"log"
	"net"

	pb "github.com/picardian14/distribuidos/maxupdater/protos"
	"google.golang.org/grpc"
)

type Server struct {
	pb.UnimplementedMaxUpdaterServer
}

func (s Server) UpdateMax(stream pb.MaxUpdater_UpdateMaxServer) error {
	var max int32
	ctx := stream.Context()

	for {
		select {
		case <-ctx.Done():
			return ctx.Err()
		default:
		}
		req, err := stream.Recv()
		if err == io.EOF {
			log.Println("Endend service")
			return nil
		}
		if err != nil {
			log.Fatalf("Error receiving: %v", err)
			continue
		}
		if req.Num <= max {
			continue
		}
		max = req.Num
		if err := stream.Send(&pb.Max{Max: max}); err != nil {
			log.Fatalf("Error sending: %v", err)
		}
		log.Printf("New max sendend: %d\n", max)
	}

}

func main() {
	lis, err := net.Listen("tcp", ":9000")
	if err != nil {
		log.Fatalf("Error starting server at 9000: %v", err)
	}
	s := Server{}
	grpcServer := grpc.NewServer()
	pb.RegisterMaxUpdaterServer(grpcServer, s)
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Error serving: %v", err)
	}
}
