
package Interfaz_Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Cliente_I extends Remote{
	public int Registro_Cliente (String usuario, String password) throws RemoteException;
        public int Realizar_Donacion (String usuario, float cantidad) throws RemoteException;
        public int Realizar_Donacion_Causa (String usuario, float cantidad, int causa) throws RemoteException;
        public Datos_Servidor Identificar_Usuario(String usuario, String password) throws RemoteException;
        public float Consultar_Donacion_Total()throws RemoteException;
        public float Consultar_Donacion_Total(String usuario)throws RemoteException;
        public float Consultar_Donacion_Total_Causa (String usuario, int causa)throws RemoteException;
}