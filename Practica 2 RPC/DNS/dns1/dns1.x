
struct info_maquina{				
	string name<>;
	string network<>;
	string ip<>;
};

program DNS1PROG {
	version DNSVERS {
		string RESOLVER_NOMBRE_DNS (info_maquina) = 1;
		int REGISTRAR_EQUIPO_DNS (info_maquina) = 2;
		int BORRAR_EQUIPO_DNS (info_maquina) = 3;
		int MODIFICAR_EQUIPO_DNS (info_maquina) = 4;
	} = 1;
} = 0x20000001;
