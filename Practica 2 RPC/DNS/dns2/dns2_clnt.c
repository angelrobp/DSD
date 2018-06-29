

#include <memory.h> /* for memset */
#include "dns2.h"

/* Default timeout can be changed using clnt_control() */
static struct timeval TIMEOUT = { 25, 0 };

char **
resolver_nombre_dns1_1(info_maquina arg1,  CLIENT *clnt)
{
	static char *clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	if (clnt_call (clnt, RESOLVER_NOMBRE_DNS1,
		(xdrproc_t) xdr_info_maquina, (caddr_t) &arg1,
		(xdrproc_t) xdr_wrapstring, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

int *
registrar_equipo_dns1_1(info_maquina arg1,  CLIENT *clnt)
{
	static int clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	if (clnt_call (clnt, REGISTRAR_EQUIPO_DNS1,
		(xdrproc_t) xdr_info_maquina, (caddr_t) &arg1,
		(xdrproc_t) xdr_int, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

int *
borrar_equipo_dns1_1(info_maquina arg1,  CLIENT *clnt)
{
	static int clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	if (clnt_call (clnt, BORRAR_EQUIPO_DNS1,
		(xdrproc_t) xdr_info_maquina, (caddr_t) &arg1,
		(xdrproc_t) xdr_int, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

int *
modificar_equipo_dns1_1(info_maquina arg1,  CLIENT *clnt)
{
	static int clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	if (clnt_call (clnt, MODIFICAR_EQUIPO_DNS1,
		(xdrproc_t) xdr_info_maquina, (caddr_t) &arg1,
		(xdrproc_t) xdr_int, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}
