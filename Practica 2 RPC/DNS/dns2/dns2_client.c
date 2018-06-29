/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "dns2.h"


void
dns2prog_1(char *host)
{
	CLIENT *clnt;
	char * *result_1;
	info_maquina resolver_nombre_dns_1_arg1;
	int  *result_2;
	info_maquina registrar_equipo_dns_1_arg1;
	int  *result_3;
	info_maquina borrar_equipo_dns_1_arg1;
	int  *result_4;
	info_maquina modificar_equipo_dns_1_arg1;

#ifndef	DEBUG
	clnt = clnt_create (host, DNS2PROG, DNS2VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result_1 = resolver_nombre_dns_1(resolver_nombre_dns_1_arg1, clnt);
	if (result_1 == (char **) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_2 = registrar_equipo_dns_1(registrar_equipo_dns_1_arg1, clnt);
	if (result_2 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_3 = borrar_equipo_dns_1(borrar_equipo_dns_1_arg1, clnt);
	if (result_3 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_4 = modificar_equipo_dns_1(modificar_equipo_dns_1_arg1, clnt);
	if (result_4 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
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
	dns2prog_1 (host);
exit (0);
}
