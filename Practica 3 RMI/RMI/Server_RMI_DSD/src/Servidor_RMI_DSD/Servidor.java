package Servidor_RMI_DSD;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Interfaz_Cliente.Cliente_I;
import Interfaz_Servidor.Servidor_I;
import java.rmi.Naming;
import Implementacion_Servidor_Cliente_RMI_DSD.Implementacion_Servidor_Cliente;
import static java.lang.System.exit;
import java.rmi.server.UnicastRemoteObject;

public class Servidor {

    public static void main(String[] args) {

        String direccion_servidor;
        int puerto_servidor;
        int puerto_servidor_replica;
        String nombre_servidor;
        String nombre_servidor_replica;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if (args.length == 3) {
            direccion_servidor = args[0];
            nombre_servidor = args[1];
            puerto_servidor = Integer.parseInt(args[2]);
            nombre_servidor_replica = "";
            puerto_servidor_replica = 0;

            try {
                Registry reg = LocateRegistry.createRegistry(puerto_servidor);
                //Registry reg = LocateRegistry.getRegistry(direccion_servidor);
                Implementacion_Servidor_Cliente servidor = new Implementacion_Servidor_Cliente(direccion_servidor, nombre_servidor, puerto_servidor, nombre_servidor_replica, puerto_servidor_replica);
                //Implementacion_Cliente stub = (Implementacion_Servidor_Cliente) UnicastRemoteObject.exportObject(servidor, 0);
                Cliente_I stubc = (Cliente_I) servidor;
                reg.bind(nombre_servidor+"_cliente", stubc);
                Servidor_I stubs = (Servidor_I) servidor;
                reg.bind(nombre_servidor+"_servidor", stubs);
                reg.rebind(nombre_servidor, servidor);
                //Naming.rebind(nombre_servidor, servidor);
                System.out.println(nombre_servidor + ": " + "Conectado");
            } catch (Exception e) {
                System.err.println(nombre_servidor + " exception:");
                e.printStackTrace();
            }

        } else if (args.length == 5) {
            direccion_servidor = args[0];
            nombre_servidor = args[1];
            puerto_servidor = Integer.parseInt(args[2]);
            nombre_servidor_replica = args[3];
            puerto_servidor_replica = Integer.parseInt(args[4]);
            
            try {
                Registry reg = LocateRegistry.createRegistry(puerto_servidor);
                
                //Registry reg = LocateRegistry.getRegistry(puerto_servidor_replica);
                //Registry reg = LocateRegistry.getRegistry(direccion_servidor);
                Implementacion_Servidor_Cliente servidor = new Implementacion_Servidor_Cliente(direccion_servidor, nombre_servidor, puerto_servidor, nombre_servidor_replica, puerto_servidor_replica);
                Cliente_I stubc = (Cliente_I) servidor;
                reg.bind(nombre_servidor+"_cliente", stubc);
                Servidor_I stubs = (Servidor_I) servidor;
                reg.bind(nombre_servidor+"_servidor", stubs);
                //Naming.rebind(nombre_servidor, servidor);
                System.out.println(nombre_servidor + ": " + "Conectado");
            } catch (Exception e) {
                System.err.println(nombre_servidor + " exception:");
                e.printStackTrace();
            }
        } else {
            System.out.println("Faltan argumentos: "
                    + "\n- Direccion del servidor"
                    + "\n- Nombre del servidor"
                    + "\n- Puerto del servidor"
                    + "\n- Nombre del servidor replica [solo si ya hay un servidor activo]"
                    + "\n- Puerto del servidor replica [solo si ya hay un servidor activo]");
            exit(0);
        }
    }

}
