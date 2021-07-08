/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "cuenta.h"
#include "time.h"
#include "math.h"

void
cuenta_1(char *host, int x, int y, char * operation)
{
	CLIENT *clnt;
	int  *result;
	operands  suma_1_arg;
	suma_1_arg.x = x;
	suma_1_arg.y = y;	

#ifndef	DEBUG
	clnt = clnt_create (host, CUENTA, CUENTA_VERSION, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */
	if (!strcmp(operation,"sum")){
		result = suma_1(&suma_1_arg, clnt);
		if (result == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		}
	} else {
		result = resta_1(&suma_1_arg, clnt);
		if (result == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		}
	}
	
	
	
	
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char *host;
	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	
	char * operation = "sum";			
	
	if (((strcmp(operation,"sum")) && (strcmp(operation,"dif")))) {
		printf("Wrong operation. Just sum and dif. Got %s\n", operation );		
		exit (1);
	}	
	double times;
	
	clock_t start_t, end_t;
	start_t = clock();
	cuenta_1 (host, 1, 1, operation);
	end_t = clock();
	times = (double) ((end_t - start_t) * 1000) / CLOCKS_PER_SEC;				
	printf("%f\n", times);
	exit (0);
}
