# gRPC-RPC-RMI-comparison
This repo contains different toy applications with client-server communication implemented in four different ways: gRPC with golang, with Java, ONC RPC and RMI. For gRPC there are extra examples that show some of its functionalities. 
Code in golang uses my github username and should be replaced.
Sorry for the Spanglish.

## Code Guide
For gRPC there are a couple of examples:
### "CuentaSencilla"
A normal single request single response example. 
### "RaizNewton" 
sends to the server a single request with a root to calculate with newton's method and how many iterations to perform for it. Originally it was going to ask for a precision, hence the float type. The server then return a stream with the intermediate values of the calcultation.
### "AnomalyDetector" 
Sends data to the server with a stream, and receives the anomalies percieved by the server. These are calculated by its distance to the median. It's not fancy, it's an example. To differenciate from a stream y used a repeated field to return the values from the server
### "MaxUpdater"
This sends a stream of numbers to the server and receives the max number sent on real time. It concurrently sends and receives data on both end via a bidirectional stream.

### "FileSystem" | "FileServer"
Sends a file to a server with a stream.

RPC and RMI only have the first and the last examples. These also have code to measure time to execute each remote call on different ways.
On all cases "CuentaSencilla" and "FileSystem" were implemented to execute on different computers on the same Wi-Fi, so it will try to send to a private IP.
