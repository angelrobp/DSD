package Implementacion_Servidor_Cliente_RMI_DSD;

import Interfaz_Cliente.Cliente_I;
import Interfaz_Cliente.Datos_Servidor;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Interfaz_Servidor.Servidor_I;

public class Implementacion_Servidor_Cliente extends UnicastRemoteObject implements Cliente_I, Servidor_I {

    private ArrayList<Usuario> clientes;
    private Servidor_I servidor_replicado;
    private String nombre_servidor, nombre_servidor_replica;
    private String direccion_servidor;
    private int puerto_servidor, puerto_servidor_replica;
    private int donacion_total, donacion_causa_1_total, donacion_causa_2_total;

    public Implementacion_Servidor_Cliente(String direccion_servidor, String nombre_servidor, int puerto_servidor, String nombre_servidor_replica, int puerto_servidor_replica) throws RemoteException {
        super();
        clientes = new ArrayList<>();
        this.nombre_servidor = nombre_servidor;
        this.nombre_servidor_replica = nombre_servidor_replica;
        this.direccion_servidor = direccion_servidor;
        this.puerto_servidor = puerto_servidor;
        this.puerto_servidor_replica = puerto_servidor_replica;
        this.servidor_replicado = null;
        try {
            if (!nombre_servidor_replica.equals("")) {
                Registry registry = LocateRegistry.getRegistry(puerto_servidor_replica);
                servidor_replicado = (Servidor_I) registry.lookup(nombre_servidor_replica + "_servidor");
                servidor_replicado.Conectar_Servidor(direccion_servidor, nombre_servidor, puerto_servidor);
                System.out.println(nombre_servidor + ": Servidores enlazados");
            }

        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(Implementacion_Servidor_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Conectar_Servidor(String direccion_servidor, String nombre_replica, int puerto_servidor_replica) throws RemoteException {
        System.out.println("ELEMENTOS RECIBIDOS: " + direccion_servidor + ", " + nombre_replica + ", " + puerto_servidor_replica);
        this.nombre_servidor_replica = nombre_replica;
        this.puerto_servidor_replica = puerto_servidor_replica;

        Registry registry = LocateRegistry.getRegistry(puerto_servidor_replica);
        try {
            this.servidor_replicado = (Servidor_I) registry.lookup(nombre_replica + "_servidor");
        } catch (NotBoundException ex) {
            Logger.getLogger(Implementacion_Servidor_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(Implementacion_Servidor_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.nombre_servidor + ": Servidores enlazados");
    }

    @Override
    public int get_Numero_Usuarios() throws RemoteException {
        return clientes.size();
    }

    @Override
    public void add_Usuario(String usuario, String password) throws RemoteException {
        this.clientes.add(new Usuario(usuario, password));
    }

    @Override
    public int Buscar_Usuario(String usuario, String password) throws RemoteException {
        int estado = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario) && user.getPassword().equals(password)) {
                estado = 1;
            }
        }
        return estado;
    }

    @Override
    public float get_Total_Donaciones() throws RemoteException {
        float donacion_total = 0;
        for (Usuario user : clientes) {
            donacion_total += user.getDonacion();
        }
        return donacion_total;
    }

    @Override
    public float get_Total_Donaciones(String usuario) throws RemoteException {
        float donacion_total = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                donacion_total += user.getDonacion();
            }
        }
        return donacion_total;
    }

    @Override
    public float get_Total_Donaciones_causa(int causa) throws RemoteException {
        float donacion_total = 0;
        for (Usuario user : clientes) {
            donacion_total += user.getDonacion_causa(causa);
        }
        donacion_total += servidor_replicado.get_Total_Donaciones_causa(causa);
        return donacion_total;
    }

    @Override
    public float get_Total_Donaciones_causa(String usuario, int causa) throws RemoteException {
        float donacion_total = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                donacion_total += user.getDonacion_causa(causa);
            }
        }
        return donacion_total;
    }

    @Override
    public int Comprobar_Usuario(String usuario) throws RemoteException {
        int estado = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                estado = 1;
            }
        }
        return estado;
    }

    @Override
    public int Registro_Cliente(String usuario, String password) throws RemoteException {
        int estado = 0;
        if (this.Comprobar_Usuario(usuario) == 1) {
            System.out.println(nombre_servidor + ": " + "Ya existe el usuario");
        } else if (servidor_replicado.Comprobar_Usuario(usuario) == 1) {
            System.out.println(nombre_servidor + ": " + "Ya existe el usuario");
        } else {
            estado = 1;
            int numero_clientes_local = this.get_Numero_Usuarios();
            int numero_clientes_replica = servidor_replicado.get_Numero_Usuarios();

            if (numero_clientes_local <= numero_clientes_replica) {
                add_Usuario(usuario, password);
            } else {
                servidor_replicado.add_Usuario(usuario, password);
            }
        }

        return estado;
    }

    @Override
    public int Realizar_Donacion(String usuario, float cantidad) throws RemoteException {
        int estado = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                estado = 1;
                this.donacion_total += cantidad;
                user.setDonacion(user.getDonacion() + cantidad);
            }
        }
        return estado;
    }

    @Override
    public Datos_Servidor Identificar_Usuario(String usuario, String password) throws RemoteException {
        Datos_Servidor datos_servidor = new Datos_Servidor();
        if (this.Buscar_Usuario(usuario, password) == 1) {
            datos_servidor = new Datos_Servidor(this.direccion_servidor, this.nombre_servidor, this.puerto_servidor);
        } else if (servidor_replicado.Buscar_Usuario(usuario, password) == 1) {
            datos_servidor = new Datos_Servidor(this.direccion_servidor, this.nombre_servidor_replica, this.puerto_servidor_replica);
        }

        return datos_servidor;
    }

    @Override
    public float Consultar_Donacion_Total() throws RemoteException {
        float donacion_total = this.get_Total_Donaciones();
        donacion_total += servidor_replicado.get_Total_Donaciones();
        return donacion_total;
    }

    @Override
    public float Consultar_Donacion_Total(String usuario) throws RemoteException {
        float donacion_total = this.get_Total_Donaciones(usuario);
        return donacion_total;
    }

    @Override
    public int Realizar_Donacion_Causa(String usuario, float cantidad, int causa) throws RemoteException {
        int estado = 0;
        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                estado = 1;
                user.setDonado(true);
                if (causa == 1) {
                    this.donacion_causa_1_total += cantidad;
                } else {
                    this.donacion_causa_2_total += cantidad;
                }
                user.setDonacion_causa(causa, user.getDonacion_causa(causa) + cantidad);
            }
        }
        return estado;
    }

    @Override
    public float Consultar_Donacion_Total_Causa(String usuario, int causa) throws RemoteException {

        float total = 0;

        for (Usuario user : clientes) {
            if (user.getUsuario().equals(usuario)) {
                if (user.isDonado()) {
                    switch (causa) {
                        case 1:
                            total = this.get_Total_Donaciones_causa(usuario, 1);
                            break;
                        case 2:
                            total = this.get_Total_Donaciones_causa(usuario, 2);
                            break;
                        case 3:
                            total = this.get_Total_Donaciones_causa(usuario, 1);
                            total += this.get_Total_Donaciones_causa(usuario, 2);
                            break;
                        case 4:
                            total = this.get_Total_Donaciones_causa(1);
                            break;
                        case 5:
                            total = this.get_Total_Donaciones_causa(2);
                            break;
                        case 6:
                            total = this.get_Total_Donaciones_causa(1);
                            total += this.get_Total_Donaciones_causa(2);
                            break;
                    }
                } else {
                    total = -1;
                }

            }
        }

        return total;
    }

    private class Usuario {

        private String usuario, password;
        private float donacion, donacion_causa_1, donacion_causa_2;
        private boolean donado;

        public Usuario(String usuario, String password) {
            this.usuario = usuario;
            this.password = password;
            this.donacion = 0;
            this.donacion_causa_1 = 0;
            this.donacion_causa_2 = 0;
            this.donado = false;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public float getDonacion() {
            return donacion;
        }

        public float getDonacion_causa(int causa) {
            if (causa == 1) {
                return this.donacion_causa_1;
            } else {
                return this.donacion_causa_2;
            }
        }

        public void setDonacion_causa(int causa, float donacion_causa) {
            if (causa == 1) {
                this.donacion_causa_1 = donacion_causa;
            } else {
                this.donacion_causa_2 = donacion_causa;
            }
        }

        public void setDonacion(float donacion) {
            this.donacion = donacion;
        }

        public boolean isDonado() {
            return donado;
        }

        public void setDonado(boolean donado) {
            this.donado = donado;
        }

    }
}
