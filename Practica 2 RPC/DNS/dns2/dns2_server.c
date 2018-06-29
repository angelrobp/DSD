

#include "dns2.h"
#include "global.h"

int REGISTROS_DNS2_SIZE =10;
int UTILES_DNS2 =2;

int const MAX_TAM =30;
char buf[MAX_TAM];
info_maquina *direcciones_dns_2;

char **
resolver_nombre_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{

	static char * result;
	result = NULL;
	
	printf("-----------------------------------\n");
	printf("Utiles %d\n", UTILES_DNS2);
	for(int i = 0; i < UTILES_DNS2; i++){

		printf("%s --",direcciones_dns_2[i].name);
		printf("%s -- ",direcciones_dns_2[i].network);
		printf("%s\n",direcciones_dns_2[i].ip);
	}

	if(strcmp(maquina.network, "red2") == 0){
		for(int i = 1; i < UTILES_DNS2; i++){
			if ( strcmp(maquina.name, direcciones_dns_2[i].name) == 0 ){
				result = direcciones_dns_2[i].ip;
			} 
		}
	} else {
		printf("Este dns solo conoce los equipos de la red2\nPreguntando al DNS superior\n");	
		CLIENT *clnt;

	#ifndef	DEBUG
		clnt = clnt_create ("localhost", DNS1PROG, DNSVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror ("localhost");
			exit (1);
		}
	#endif	
	
		result = *resolver_nombre_dns1_1(maquina, clnt);
		if ( result == (char*) NULL) {
			clnt_perror (clnt, "call failed");
		}

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	

	}

	if(result == NULL){
		printf("No se ha encontrado el equipo especificado en ningun dns\n");	
		result = "Not found";
	}

	return &result;
}

int *
registrar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int *result;

	result = new int;
	*result = 1;

	if(strcmp(maquina.network, "red2") == 0){
		for(int i = 0; i < UTILES_DNS2; i++){
			if ( strcmp(maquina.name, direcciones_dns_2[i].name) == 0 ){
				*result = -1;
				i = UTILES_DNS2;
			} 
			else if ( strcmp(maquina.ip, direcciones_dns_2[i].ip) == 0 ){
				*result = 0;
				i = UTILES_DNS2;
			} 
		}
		if (*result = 1) {
			direcciones_dns_2[UTILES_DNS2].name = maquina.name;
			direcciones_dns_2[UTILES_DNS2].network = maquina.network;
			direcciones_dns_2[UTILES_DNS2].ip = maquina.ip;
			UTILES_DNS2++;

			for(long int i = 0; i < UTILES_DNS2; i++){

				printf("%s --",direcciones_dns_2[i].name);
				printf("%s -- ",direcciones_dns_2[i].network);
				printf("%s\n",direcciones_dns_2[i].ip);
			}
		}
		else {
			printf("No se puede registrar maquina\n");
		}
	} else {
		printf("Este DNS solo registra los equipos de la red2\n");

		CLIENT *clnt;

	#ifndef	DEBUG
		clnt = clnt_create ("localhost", DNS1PROG, DNSVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror ("localhost");
			exit (1);
		}
	#endif	
	
		result = registrar_equipo_dns1_1(maquina, clnt);
		if ( result == (int*) NULL) {
			clnt_perror (clnt, "call failed");
		}

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	

	}
	return result;
}

int *
borrar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int  *result;

	result = new int;
	*result = 0;

	if(strcmp(maquina.network, "red2") == 0){
		int indice = 0;
		for(int i = 0; i < UTILES_DNS2; i++){
			if ( strcmp(maquina.name, direcciones_dns_2[i].name) == 0 ){
				*result = 1;
				indice = i;
				i = UTILES_DNS2;
			} 
			else if ( strcmp(maquina.ip, direcciones_dns_2[i].ip) == 0 ){
				*result = 1;
				indice = i;
				i = UTILES_DNS2;
			} 
		}
		if (*result = 1) {

			for(int i = indice; i < UTILES_DNS2-1; i++){
				direcciones_dns_2[i] = direcciones_dns_2[i+1];
			}
			UTILES_DNS2--;
		}
		else {
			printf("No se encuentra maquina\n");
		}
	} else {
		printf("Este DNS solo elimina los equipos de la red2\n");

		CLIENT *clnt;

	#ifndef	DEBUG
		clnt = clnt_create ("localhost", DNS1PROG, DNSVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror ("localhost");
			exit (1);
		}
	#endif	
	
		result = borrar_equipo_dns1_1(maquina, clnt);
		if ( result == (int*) NULL) {
			clnt_perror (clnt, "call failed");
		}

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	

	}

	return result;
}

int *
modificar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int  *result;

	result = new int;
	*result = 0;

	if(strcmp(maquina.network, "red2") == 0){
		int indice = 0;
		for(int i = 0; i < UTILES_DNS2; i++){
			if ( strcmp(maquina.name, direcciones_dns_2[i].name) == 0 ){
				*result = 1;
				indice = i;
				i = UTILES_DNS2;
			} 
		}
		if (*result = 1) {
			direcciones_dns_2[indice].ip = maquina.ip;	
		}
		else {
			printf("No se encuentra maquina\n");
		}
	} else {
		printf("Este DNS solo modifica los equipos de la red2\n");

		CLIENT *clnt;

	#ifndef	DEBUG
		clnt = clnt_create ("localhost", DNS1PROG, DNSVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror ("localhost");
			exit (1);
		}
	#endif	
	
		result = borrar_equipo_dns1_1(maquina, clnt);
		if ( result == (int*) NULL) {
			clnt_perror (clnt, "call failed");
		}

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	

	}

	return result;
}
