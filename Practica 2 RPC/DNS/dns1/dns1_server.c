

#include "dns1.h"
#include "global.h"

int REGISTROS_DNS1_SIZE =10;
int UTILES_DNS1 =2;

int const MAX_TAM =30;
char buf[MAX_TAM];
info_maquina *direcciones_dns_1;

char **
resolver_nombre_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static char* result;
	result = NULL;

	printf("-----------------------------------\n");
	
	for(long int i = 0; i < UTILES_DNS1; i++){

		printf("%s --",direcciones_dns_1[i].name);
		printf("%s -- ",direcciones_dns_1[i].network);
		printf("%s\n",direcciones_dns_1[i].ip);
	}

	if(strcmp(maquina.network, "red1") == 0){
		for(int i = 1; i < UTILES_DNS1; i++){
			if ( strcmp(maquina.name, direcciones_dns_1[i].name) == 0 ){
				result = direcciones_dns_1[i].ip;
			} 
		}
	} else {
		printf("Este DNS solo conoce los equipos de la red1\n");

	}

	if(result == NULL){
		printf("No se ha encontrado el equipo especificado en ningun DNS");	
		result = "Not found";
	}

	return &result;
}
int *
registrar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int  result;

	result = 1;

	if(strcmp(maquina.network, "red1") == 0){
		for(int i = 0; i < UTILES_DNS1; i++){
			if ( strcmp(maquina.name, direcciones_dns_1[i].name) == 0 ){
				result = -1;
				i = UTILES_DNS1;
			} 
			else if ( strcmp(maquina.ip, direcciones_dns_1[i].ip) == 0 ){
				result = 0;
				i = UTILES_DNS1;
			} 
		}
		if (result = 1) {
			direcciones_dns_1[UTILES_DNS1].name = maquina.name;
			direcciones_dns_1[UTILES_DNS1].network = maquina.network;
			direcciones_dns_1[UTILES_DNS1].ip = maquina.ip;
			UTILES_DNS1++;

			printf("-----------------------------------\n");
	
			for(long int i = 0; i < UTILES_DNS1; i++){

				printf("%s -- %s -- %s\n",direcciones_dns_1[i].name, direcciones_dns_1[i].network,direcciones_dns_1[i].ip);
			}
		}
		else {
			printf("No se puede registrar maquina\n");
		}
	} else {
		printf("Este DNS solo registra los equipos de la red1\n");
		result = -1;

	}

	return &result;
}

int *
borrar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int  result;

	result = 0;

	if(strcmp(maquina.network, "red1") == 0){
		int indice = 0;
		for(int i = 0; i < UTILES_DNS1; i++){
			if ( strcmp(maquina.name, direcciones_dns_1[i].name) == 0 ){
				result = 1;
				indice = i;
				i = UTILES_DNS1;
			} 
			else if ( strcmp(maquina.ip, direcciones_dns_1[i].ip) == 0 ){
				result = 1;
				indice = i;
				i = UTILES_DNS1;
			} 
		}
		if (result = 1) {

			for(int i = indice; i < UTILES_DNS1-1; i++){
				direcciones_dns_1[i].name = direcciones_dns_1[i+1].name;
				direcciones_dns_1[i].network = direcciones_dns_1[i+1].network;
				direcciones_dns_1[i].ip = direcciones_dns_1[i+1].ip;
			}
			UTILES_DNS1--;
		}
		else {
			printf("No se encuentra maquina\n");
		}
	} else {
		printf("Este DNS solo registra los equipos de la red1\n");

	}

	return &result;
}
int *
modificar_equipo_dns_1_svc(info_maquina maquina,  struct svc_req *rqstp)
{
	static int  result;

	result = 0;

	if(strcmp(maquina.network, "red1") == 0){
		int indice = 0;
		for(int i = 0; i < UTILES_DNS1; i++){
			if ( strcmp(maquina.name, direcciones_dns_1[i].name) == 0 ){
				result = 1;
				indice = i;
				i = UTILES_DNS1;
			}
		}
		if (result = 1) {

			direcciones_dns_1[indice].ip = maquina.ip;	
		}
		else {
			printf("No se encuentra maquina\n");
		}
	} else {
		printf("Este DNS solo modifica los equipos de la red1\n");

	}

	return &result;
}
