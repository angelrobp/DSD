
package Interfaz_Cliente;


public class Datos_Servidor {
    private String direccion;
    private String nombre;
    private int puerto;

    public Datos_Servidor() {
        this.direccion = "";
        this.nombre = "";
        this.puerto = 0;
    }
    
    public Datos_Servidor(String direccion, String nombre, int puerto) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.puerto = puerto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
    
    
}
