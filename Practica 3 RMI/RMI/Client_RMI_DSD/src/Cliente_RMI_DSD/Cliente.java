package Cliente_RMI_DSD;

import Interfaz_Cliente.Cliente_I;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.System.exit;
import java.util.Scanner;
import Interfaz_Cliente.Datos_Servidor;

public class Cliente implements Runnable {

    public String usuario, password;
    public Datos_Servidor datos_servidor;

    public Cliente(String server, String nombre_objeto_remoto, int puerto) {
        datos_servidor = new Datos_Servidor(server, nombre_objeto_remoto, puerto);
    }

    public void run() {
        System.out.println("Conectando...");
        System.out.println("VALORES: " + this.datos_servidor.getDireccion() + ", " + this.datos_servidor.getNombre() + ", " + this.datos_servidor.getPuerto());
        try {
            Registry registry = LocateRegistry.getRegistry(this.datos_servidor.getDireccion(), this.datos_servidor.getPuerto());
            Cliente_I instancia_local = (Cliente_I) registry.lookup(this.datos_servidor.getNombre() + "_cliente");
            boolean identificado = false;
            boolean estado = true;
            String opcion = "";
            Scanner scan = new Scanner(System.in);

            while (estado) {

                System.out.println("\n------------------- Menú de login -------------------\n"
                        + "R: Registrarse\n"
                        + "I: Identificarse\n"
                        + "S: Salir\n");

                System.out.println("Elija una opción: ");
                opcion = scan.nextLine();
                opcion = opcion.toLowerCase();

                switch (opcion) {

                    case "r":
                        System.out.println("Registro:");
                        System.out.println("Introduzca el nombre de usuario: ");
                        usuario = scan.nextLine();
                        System.out.println("Introduzca la contraseña: ");
                        password = scan.nextLine();
                        int respuesta_servidor = instancia_local.Registro_Cliente(usuario, password);

                        if (respuesta_servidor == 1) {
                            System.out.println("Registrado correctamente.");
                        } else {
                            System.out.println("Error en el registro. Usuario ya existente.");
                        }

                        break;
                    case "i":
                        System.out.println("Identificacion:");
                        System.out.println("Introduzca el nombre de usuario: ");
                        usuario = scan.nextLine();
                        System.out.println("Introduzca su contraseña: ");
                        password = scan.nextLine();

                        Datos_Servidor datos_aux = instancia_local.Identificar_Usuario(usuario, password);

                        if (!datos_aux.getNombre().equals("")) {
                            this.datos_servidor = datos_aux;
                            registry = LocateRegistry.getRegistry(this.datos_servidor.getDireccion(), this.datos_servidor.getPuerto());
                            instancia_local = (Cliente_I) registry.lookup(this.datos_servidor.getNombre() + "_cliente");
                            identificado = true;
                        } else {
                            System.out.println("Nombre de usuario y/o contraseña incorrectos");
                        }
                        break;
                    case "s":
                        estado = false;
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("La opción no existe.");
                        break;
                }

                while (identificado) {

                    System.out.println("\n------------------- Menú de donaciones -------------------\n"
                            + "D: Donar\n"
                            + "C: Donar a una causa\n"
                            + "R: Consultar total donado a una causa\n"
                            + "T: Consultar total donado\n"
                            + "P: Consultar mi total donado\n"
                            + "S: Cerrar sesión\n");

                    System.out.println("Elija una opción: ");
                    opcion = scan.nextLine();
                    opcion = opcion.toLowerCase();

                    switch (opcion) {
                        case "c":
                            float cantidad = 0;
                            while (cantidad <= 0) {
                                System.out.println("Introduzca la cantidad que desea donar: ");
                                cantidad = Float.parseFloat(scan.nextLine());
                            }
                            int causa = 0;
                            while (causa < 1 || causa > 2) {
                                System.out.println("Introduzca la causa a la que desea donar: ");
                                causa = Integer.parseInt(scan.nextLine());
                            }
                            int respuesta_servidor = instancia_local.Realizar_Donacion_Causa(usuario, cantidad, causa);

                            if (respuesta_servidor == 1) {
                                System.out.println("Donación realizada con exito.");
                            } else {
                                System.out.println("Error al ingresar la donacion.");
                            }
                            break;
                        case "d":
                            cantidad = 0;
                            while (cantidad <= 0) {
                                System.out.println("Introduzca la cantidad que desea donar: ");
                                cantidad = Float.parseFloat(scan.nextLine());
                            }
                            respuesta_servidor = instancia_local.Realizar_Donacion(usuario, cantidad);

                            if (respuesta_servidor == 1) {
                                System.out.println("Donación realizada con exito.");
                            } else {
                                System.out.println("Error al ingresar la donacion.");
                            }
                            break;
                        case "r":
                            causa = 0;
                            while (causa < 1 || causa > 6) {
                                System.out.println("\n------------------- Que causa quiere consultar -------------------\n"
                                        + "1: Mi donacion total Causa 1\n"
                                        + "2: Mi donacion total Causa 2\n"
                                        + "3: Mi donacion total en causas\n"
                                        + "4: Donaciones totales en causa 1\n"
                                        + "5: Donaciones totales en causa 2\n"
                                        + "6: Donaciones totales en causas\n");
                                causa = Integer.parseInt(scan.nextLine());
                            }
                            float total = instancia_local.Consultar_Donacion_Total_Causa(usuario, causa);

                            if (total >= 0) {

                                switch (causa) {
                                    case 1:
                                        System.out.println("Su total de donaciones en la causa 1 alcanza: " + total + "€");
                                        break;
                                    case 2:
                                        System.out.println("Su total de donaciones en la causa 2 alcanza: " + total + "€");
                                        break;
                                    case 3:
                                        System.out.println("Su total de donaciones en todas las causas alcanza: " + total + "€");
                                        break;
                                    case 4:
                                        System.out.println("El total de donaciones en el sistema en la causa 1 alcanza: " + total + "€");
                                        break;
                                    case 5:
                                        System.out.println("El total de donaciones en el sistema en la causa 2 alcanza: " + total + "€");
                                        break;
                                    case 6:
                                        System.out.println("El total de donaciones en el sistema en todas las causas alcanza: " + total + "€");
                                        break;
                                }
                            } else {
                                System.out.println("Debes donar para poder consultar cualquier cantidad donada a la causa");
                            }

                            break;
                        case "t":
                            total = instancia_local.Consultar_Donacion_Total();

                            System.out.println("Las donaciones totales alcanzan: " + total + "€");
                            break;
                        case "p":
                            System.out.println("Su total de donaciones alcanza: " + instancia_local.Consultar_Donacion_Total(usuario) + "€");
                            break;
                        case "s":
                            identificado = false;
                            usuario = "";
                            password = "";
                            System.out.println("Has cerrado sesión.");
                            break;
                        default:
                            System.out.println("La opción no existe.");
                            break;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Cliente exception:");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        if (args.length < 3) {
            System.out.println("Faltan parametros: "
                    + "\n- Direccion del servidor"
                    + "\n- Nombre del servidor"
                    + "\n- Puerto del servidor");
            exit(0);
        }
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Cliente cliente = new Cliente(args[0], args[1], Integer.parseInt(args[2]));
        Thread thr = new Thread(cliente, "Cliente_0");
        thr.start();
    }
}
