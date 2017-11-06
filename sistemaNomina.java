
import java.util.*;
import javax.swing.*;
import java.io.*;

public class sistemaNomina {

    //Objetos
    static Empleados[] emp = new Empleados[10];
    static BufferedWriter bw;
    static BufferedReader br;
    static File f;
    static Scanner lectura = new Scanner(System.in);


    //Variables
    static int numeroDeNomina;
    static int diasTrabajados;
    static int asignaciones;
    static int deducciones;
    static int opcionMenu;
    static int indexEmpleado;
    static String datos;
    static boolean encontrado = false;
    static boolean valido = false;


    public static void main (String[]args){


        //EMPLEADOS - SOLO 5 PARA PODER DARLE AL USUARIO ESPACIO DE ELIMINAR O AÑADIR MÁS

        emp[0] = new Empleados("Martin", "Alegria", "CEO", 10000000, "11/02/2016", 101);
        emp[1] = new Empleados("Diego", "Moreno", "Intendente", 1000, "11/02/2016", 102);
        emp[2] = new Empleados("Luis", "García", "Programador", 50000, "11/02/2016", 103);
        emp[3] = new Empleados("Chava", "Iglesias", "Presidente", 2000000, "11/02/2016", 104);
        emp[4] = new Empleados("Hugo", "Sanchez", "Asistente", 3000, "11/02/2016", 105);

        //RELLENA LOS DEMAS ESPACIOS CON DATOS DEFAULT
        for(int i = 5; i<10; i++){
            emp[i] = new Empleados();
        }

        buscaEmpleado(); //METODO QUE BUSCA EMPLADOS POR SU NUMERO DE
        datos = emp[indexEmpleado].imprimirDatos();
        System.out.println("\t" + datos);
        inputUsuario();

        do {
            menu();

            switch (opcionMenu) {

                case 1: // DAR DE ALTA A UN EMPLEADO
                    alta();
                    break;

                case 2: //DAR DE BAJA A UN EMPLEADO
                    break;

                case 3: //MODIFICAR DATOS DE UN EMPLEADO
                    break;

                case 4: //SEGUIR ADELANTE
                    break;

                default: //NINGUNO DE ESOS
                    System.out.println("\tLa opcion no es valida, intentalo otra vez");
                    break;

            }//switch

        }while (opcionMenu != 4); //LOOP MIENTRAS EL USUARIO NO QUIERA SEGUIR ADELANTE



    }//main

    public static void buscaEmpleado(){

        do {

            System.out.println("\t*INGRESA EL # NOMINA* [Ej. 001 al 010] ");
            numeroDeNomina = lectura.nextInt();

            for (int i = 0; i <5; i++) {

                int temp = emp[i].getNumCuenta();

                if (temp == numeroDeNomina){ //SE BUSCA SI EL NUMERO DE NOMINA MATCHEA CON ALGUNO EL SISTEMA
                    indexEmpleado = i;
                    encontrado = true;
                    break;
                }
                else{
                    encontrado = false;
                }

            }

             if (!encontrado){ //EL NUMERO NO COINCIDE
                System.out.println("\tEL NUMERO DE NOMINA INGRESADO NO MATCHEA CON NINGUNO DEL SISTEMA");
                System.out.println("\tINTENTA DE NUEVO\n");
            }

        }while(!encontrado); //WHILE PARA BUSCAR EL NUMERO DE NOMINA, NO SALE HASTA QUE SE HAYA ENCONTRADO UNO QUE COINCIDA CON ALGUNO DEL SISTEMA

    }


    public static void inputUsuario(){

        System.out.println("\tIngrese los dias trabajados de " + emp[indexEmpleado].getNombre());
        diasTrabajados = lectura.nextInt();

        System.out.println("\tIngrese sus asignaciones"); //SE INGRESAN ASIGNACIONES Y SE GUARDAN EN UNA VARIABLE
        System.out.println("\t\tBonos:");
        asignaciones = lectura.nextInt();
        System.out.println("\t\tFeriados");
        asignaciones += lectura.nextInt();
        System.out.println("\t\tHoras Extras");
        asignaciones += lectura.nextInt();
        System.out.println("\t\tOtros");
        asignaciones += lectura.nextInt();

        System.out.println("\tIngrese sus deducciones (IVA, ISR, Prestamos)"); //SE INGRESAN DEDUCCIONES Y SE GUARDAN EN UNA VARIABLE
        System.out.println("\t\tIVA:");
        deducciones = lectura.nextInt();
        System.out.println("\t\tISR");
        deducciones += lectura.nextInt();
        System.out.println("\t\tPrestamos");
        deducciones += lectura.nextInt();
        System.out.println("\t\tOtros");
        deducciones += lectura.nextInt();

    }

    public static void menu() {


            //MENU PARA DAR DE ALTA, BAJA O MODIFIAR DATOS DE UN EMPLEADO
            System.out.println("\n\tQUE OPCION DESEA SELECCIONAR (SELECCIONE EL NUMERO DE LA OPCION)");
            System.out.println("\t ---------    ---------    --------------------    --------------------");
            System.out.println("\t| 1. ALTA |  | 2. BAJA |  | 3. MODIFICAR DATOS |  | 4. SEGUIR ADELANTE |");
            System.out.println("\t ---------    ---------    --------------------    --------------------");

        do {

            opcionMenu = lectura.nextInt();

            if(opcionMenu >=1 || opcionMenu <=4){ //SE SELECCIONA UNA OPCION DENTRO DEL ARANGO DE OPCIONES
                valido = true;
            }
            if(opcionMenu<0 || opcionMenu >4){ //SE SELECCIONA OTRA OPCION QUE NO ESTA DENTRO DEL RANGO DE OPCIONES
                System.out.println("\tLA OPCION NO ES VALIDA, INTENTA DE NUEVO");
                valido = false;
            }

        } while (!valido);

    }

    public static void alta(){

        boolean emptySpace = false;
        int emptySpaceIndex = -1;

        for(int i = 0; i<emp.length; i++){

            if (emp[i].getNombre().equals("X") && emp[i].getNumCuenta() == 0){ //PARA VER SI ALGUN ESPACIO ESTA VACIO (TIENE DATOS DEFAULT)
                emptySpace = true;
                emptySpaceIndex = i;
                break;
            }//if
            else{
                emptySpace = false;
            }

        }//for

        if(!emptySpace){ //HAY UN MAXIMO DE 10 ESPACIOS PARA EMPLEADOS, SI YA ESTA LLENO SE PIDE BORRAR UNO
            System.out.println("\tNO HAY ESPACIO PARA AGREGAR OTRO EMPLEADO");
            System.out.println("\tDA DE BAJA A UN USUARIO PARA AGREGAR A OTRO ");
        }
        else{ //SI HAY LUGAR

            //VARIABLES PARA ACTUALIZAR DATOS DEL ESPACIO LIBRE

            System.out.println("\tINGRESA EL NOMBRE");
            String nom = lectura.nextLine();
            System.out.println("\tINGRESA EL APELLIDO");
            String ape = lectura.nextLine();
            System.out.println("\tINGRESA EL CARGO");
            String cargo = lectura.nextLine();
            System.out.println("\tINGRESA EL SUELDO");
            long sueldo = lectura.nextLong();
            System.out.println("\tINGRESA LA FECHA DE INGRESO (XX/XX/XX)");
            String fecha = lectura.nextLine();
            System.out.println("\tINGRESA EL NUMERO DE CUENTA (TRES DIGITOS EMPEZANDO CON 1 ej. 107)");
            int numC = lectura.nextInt();

            emp[emptySpaceIndex].actualizaDatos(nom,ape,cargo,sueldo,fecha,numC);

        }

    }

    public static void baja(){}

    public static void mod(){}

}//class

