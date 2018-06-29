

#ifndef _DNS1_H_RPCGEN
#define _DNS1_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct info_maquina {
	char *name;
	char *network;
	char *ip;
};
typedef struct info_maquina info_maquina;

#define DNS1PROG 0x20000002
#define DNSVERS 2

#if defined(__STDC__) || defined(__cplusplus)
#define RESOLVER_NOMBRE_DNS 1
//extern  char ** resolver_nombre_dns_1(info_maquina , CLIENT *);
extern  char ** resolver_nombre_dns_1_svc(info_maquina , struct svc_req *);
#define REGISTRAR_EQUIPO_DNS 2
//extern  int * registrar_equipo_dns_1(info_maquina , CLIENT *);
extern  int * registrar_equipo_dns_1_svc(info_maquina , struct svc_req *);
#define BORRAR_EQUIPO_DNS 3
//extern  int * borrar_equipo_dns_1(info_maquina , CLIENT *);
extern  int * borrar_equipo_dns_1_svc(info_maquina , struct svc_req *);
#define MODIFICAR_EQUIPO_DNS 4
//extern  int * modificar_equipo_dns_1(info_maquina , CLIENT *);
extern  int * modificar_equipo_dns_1_svc(info_maquina , struct svc_req *);
extern int dns1prog_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define RESOLVER_NOMBRE_DNS 1
//extern  char ** resolver_nombre_dns_1();
extern  char ** resolver_nombre_dns_1_svc();
#define REGISTRAR_EQUIPO_DNS 2
//extern  int * registrar_equipo_dns_1();
extern  int * registrar_equipo_dns_1_svc();
#define BORRAR_EQUIPO_DNS 3
//extern  int * borrar_equipo_dns_1();
extern  int * borrar_equipo_dns_1_svc();
#define MODIFICAR_EQUIPO_DNS 4
//extern  int * modificar_equipo_dns_1();
extern  int * modificar_equipo_dns_1_svc();
extern int dns1prog_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_info_maquina (XDR *, info_maquina*);

#else /* K&R C */
extern bool_t xdr_info_maquina ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_DNS1_H_RPCGEN */
