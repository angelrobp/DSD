/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz_Servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servidor_I extends Remote{
        public void Conectar_Servidor (String direccion_servidor, String nombre_servidor_replica, int puerto_servidor_replica) throws RemoteException;
	public int Comprobar_Usuario (String usuario) throws RemoteException;
        public int get_Numero_Usuarios () throws RemoteException;
        public void add_Usuario (String usuario, String password) throws RemoteException;
        public int Buscar_Usuario (String usuario, String password) throws RemoteException;
        public float get_Total_Donaciones () throws RemoteException;
        public float get_Total_Donaciones (String usuario) throws RemoteException;
        public float get_Total_Donaciones_causa(int causa) throws RemoteException;
        public float get_Total_Donaciones_causa(String usuario, int causa) throws RemoteException;
}
