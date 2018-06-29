
struct info_maquina{				
	string name<>;
	string network<>;
	string ip<>;
};
	
program DNS2PROG {
	version DNS2VERS {
		string RESOLVER_NOMBRE_DNS (info_maquina) = 5;
		int REGISTRAR_EQUIPO_DNS (info_maquina) = 6;
		int BORRAR_EQUIPO_DNS (info_maquina) = 7;
		int MODIFICAR_EQUIPO_DNS (info_maquina) = 8;
	} = 1;
} = 0x20000001;


