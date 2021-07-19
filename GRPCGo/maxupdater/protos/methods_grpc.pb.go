// Code generated by protoc-gen-go-grpc. DO NOT EDIT.

package maxupdater

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// MaxUpdaterClient is the client API for MaxUpdater service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type MaxUpdaterClient interface {
	UpdateMax(ctx context.Context, opts ...grpc.CallOption) (MaxUpdater_UpdateMaxClient, error)
}

type maxUpdaterClient struct {
	cc grpc.ClientConnInterface
}

func NewMaxUpdaterClient(cc grpc.ClientConnInterface) MaxUpdaterClient {
	return &maxUpdaterClient{cc}
}

func (c *maxUpdaterClient) UpdateMax(ctx context.Context, opts ...grpc.CallOption) (MaxUpdater_UpdateMaxClient, error) {
	stream, err := c.cc.NewStream(ctx, &MaxUpdater_ServiceDesc.Streams[0], "/protos.MaxUpdater/UpdateMax", opts...)
	if err != nil {
		return nil, err
	}
	x := &maxUpdaterUpdateMaxClient{stream}
	return x, nil
}

type MaxUpdater_UpdateMaxClient interface {
	Send(*Number) error
	Recv() (*Max, error)
	grpc.ClientStream
}

type maxUpdaterUpdateMaxClient struct {
	grpc.ClientStream
}

func (x *maxUpdaterUpdateMaxClient) Send(m *Number) error {
	return x.ClientStream.SendMsg(m)
}

func (x *maxUpdaterUpdateMaxClient) Recv() (*Max, error) {
	m := new(Max)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

// MaxUpdaterServer is the server API for MaxUpdater service.
// All implementations must embed UnimplementedMaxUpdaterServer
// for forward compatibility
type MaxUpdaterServer interface {
	UpdateMax(MaxUpdater_UpdateMaxServer) error
	mustEmbedUnimplementedMaxUpdaterServer()
}

// UnimplementedMaxUpdaterServer must be embedded to have forward compatible implementations.
type UnimplementedMaxUpdaterServer struct {
}

func (UnimplementedMaxUpdaterServer) UpdateMax(MaxUpdater_UpdateMaxServer) error {
	return status.Errorf(codes.Unimplemented, "method UpdateMax not implemented")
}
func (UnimplementedMaxUpdaterServer) mustEmbedUnimplementedMaxUpdaterServer() {}

// UnsafeMaxUpdaterServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to MaxUpdaterServer will
// result in compilation errors.
type UnsafeMaxUpdaterServer interface {
	mustEmbedUnimplementedMaxUpdaterServer()
}

func RegisterMaxUpdaterServer(s grpc.ServiceRegistrar, srv MaxUpdaterServer) {
	s.RegisterService(&MaxUpdater_ServiceDesc, srv)
}

func _MaxUpdater_UpdateMax_Handler(srv interface{}, stream grpc.ServerStream) error {
	return srv.(MaxUpdaterServer).UpdateMax(&maxUpdaterUpdateMaxServer{stream})
}

type MaxUpdater_UpdateMaxServer interface {
	Send(*Max) error
	Recv() (*Number, error)
	grpc.ServerStream
}

type maxUpdaterUpdateMaxServer struct {
	grpc.ServerStream
}

func (x *maxUpdaterUpdateMaxServer) Send(m *Max) error {
	return x.ServerStream.SendMsg(m)
}

func (x *maxUpdaterUpdateMaxServer) Recv() (*Number, error) {
	m := new(Number)
	if err := x.ServerStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

// MaxUpdater_ServiceDesc is the grpc.ServiceDesc for MaxUpdater service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var MaxUpdater_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "protos.MaxUpdater",
	HandlerType: (*MaxUpdaterServer)(nil),
	Methods:     []grpc.MethodDesc{},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "UpdateMax",
			Handler:       _MaxUpdater_UpdateMax_Handler,
			ServerStreams: true,
			ClientStreams: true,
		},
	},
	Metadata: "protos/methods.proto",
}
