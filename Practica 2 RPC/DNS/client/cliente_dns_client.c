
#include "cliente_dns.h"

void
dns2prog_1(char *host, char *operacion, info_maquina maquina)
{
	CLIENT *clnt;
	char * *result_1;
	int *result_2;

#ifndef	DEBUG
	clnt = clnt_create (host, DNS2PROG, DNS2VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	if (strcmp(operacion, "resolver") == 0) {
		result_1 = resolver_nombre_dns_1(maquina, clnt);
		if (result_1 == (char **) NULL) {
			clnt_perror (clnt, "call failed");
		}

		printf("IP: %s\n",*result_1);
	}
	else if (strcmp(operacion, "agregar") == 0) {
		result_2 = registrar_equipo_dns_1(maquina, clnt);
		if (result_2 == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		}

		if (*result_2 == 1) {
			printf("Maquina agregada\n");
		}
		else if (*result_2 == 0) {
			printf("No se encuentra ip\n");
		}
		else {
			printf("No se encuentra nombre\n");
		}
		
	}
	else if (strcmp(operacion, "borrar") == 0) {
		result_2 = borrar_equipo_dns_1(maquina, clnt);
		if (result_2 == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		}

		if (*result_2 == 1) {
			printf("Maquina eliminada\n");
		}
		else {
			printf("Maquina no encontrada\n");
		}
	}
	else if (strcmp(operacion, "modificar") == 0) {
		result_2 = modificar_equipo_dns_1(maquina, clnt);
		if (result_2 == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		}

		if (*result_2 == 1) {
			printf("Maquina modificada\n");
		}
		else {
			printf("Maquina no encontrada\n");
		}
	}
	

#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}

int
main (int argc, char *argv[])
{
	char *host, *operacion;
	info_maquina maquina;

	if (argc < 5) {
		printf ("Primer argumento: host\nSegundo argumento: operaciones --> [resolver, agregar, borrar]\nTercer argumento: nombre de la maquina\nCuarto argumento: red de la maquina\nQuinto argumento (solo para agregar o modificar): ip de la maquina o nueva ip de la maquina\n");
		exit (1);
	}
	host = argv[1];
	operacion = argv[2];
	maquina.name = argv[3];
	maquina.network = argv[4];
	if (argc > 5) {
		maquina.ip = argv[5];
	}
	dns2prog_1 (host, operacion, maquina);
exit (0);
}
