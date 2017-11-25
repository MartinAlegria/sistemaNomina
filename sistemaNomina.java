/*
* Martín Alegría
* Diego Moreno
* Luis García
*
* Programa de listado de nómina para una microempresa con máximo de 10 empleados.
* En el programa puedes dar de alta y baja a un empleado, introducir sus asginaciones y deducciones, al igual que modificar sus datos.
* Despues de hacer todos estos, el programa dará la ficha de información de cada usuario al igual que el listado de nómina de la empresa para ese mes.
* 
* */

import java.util.*;
import javax.swing.*;
import java.io.*;

public class sistemaNomina {

    //Objetos
    static Empleados[] emp = new Empleados[10];
    static BufferedWriter bw;
    static BufferedReader br;
    static File f = new File("init.csv");
    static Scanner lectura = new Scanner(System.in);
    static int[] diasTrabajados = new int[10];
    static int[][] asignaciones = new int[4][10];
    /*
    * index 0 -> BONOS
    * index 1 -> FERIADOS
    * index 2 -> HORAS EXTRAS
    * index 3 -> OTROS
    * */
    static int[][] deducciones = new int[4][10];
    /*
    * index 0 -> IVA
    * index 1 -> ISR
    * index 2 -> PRESTAMOS
    * index 3 -> OTROS
    * */
    static boolean[] extrasAgregados = new boolean[10];
    static int[] sueldoNeto = new int[10];


    //Variables
    static int numeroDeNomina;
    static int opcionMenu;
    static int indexEmpleado;
    static String datos;
    static boolean encontrado = false;
    static boolean valido = false;


    public static void main (String[]args){


        //EMPLEADOS - SOLO 5 PARA PODER DARLE AL USUARIO ESPACIO DE ELIMINAR O AÑADIR MÁS

        emp[0] = new Empleados("Martin", "Alegria", "CEO", 50000, "11/02/2016", 101);
        emp[1] = new Empleados("Diego", "Moreno", "Intendente", 2000, "11/02/2016", 102);
        emp[2] = new Empleados("Luis", "García", "Programador", 10000, "11/02/2016", 103);
        emp[3] = new Empleados("Chava", "Iglesias", "Presidente", 50000, "11/02/2016", 104);
        emp[4] = new Empleados("Hugo", "Sanchez", "Asistente", 3000, "11/02/2016", 105);

        //RELLENA LOS DEMAS ESPACIOS CON DATOS DEFAULT EN UN ARREGLO DE OBJETOS
        for(int i = 5; i<10; i++){
            emp[i] = new Empleados();
        }
        //RELLENA LOS EXTRAS AGREGADOS COMO FALSE -- NOS VA A AYUDAR PARA DECIRLE AL USUARIO A CUALES EMPLEADOS NO LES HA AGREGADO SUS DEDUCIONES, DIAS TRABAJADOS, ASIGNACIONES, ETC.
        for(int i = 0; i<10; i++){
            extrasAgregados[i] = false;
        }

        
        if(f.exists()){ // SI EL ARCHIVO INIT YA EXISTE SOLO LO LEE
            readInit();
        }else{
            initFiles(); //SI NO EXISTE CREA UNO Y LO LLENA
        }

        System.out.println("\t BIENVENIDO AL SISTEMA");

        do {
            menu();

            switch (opcionMenu) {

                case 1: // DAR DE ALTA A UN EMPLEADO
                    alta();
                    initFiles();
                    break;

                case 2: //DAR DE BAJA A UN EMPLEADO
                    baja();
                    initFiles();
                    break;

                case 3: //MODIFICAR DATOS DE UN EMPLEADO
                    mod();
                    inputUsuario();
                    initFiles();
                    break;

                case 4: //OTRO USUARIO
                    buscaEmpleado(); //METODO QUE BUSCA EMPLADOS POR SU NUMERO DE CUENTA
                    datos = emp[indexEmpleado].imprimirDatos();
                    System.out.println("\t----------------------------");
                    System.out.println("\t" + datos);
                    System.out.println("\t----------------------------\n");
                    inputUsuario();;
                    initFiles();
                    break;

                case 5: //SEGUIR ADELANTE
                    seguirAdelante();
                    break;
                int nominaEmpresa = 0;
                default: //NINGUNO DE ESOS
                    System.out.println("\tLa opcion no es valida, intentalo otra vez");
                    break;

            }//switch

        }while (opcionMenu != 5); //LOOP MIENTRAS EL USUARIO NO QUIERA SEGUIR ADELANTE

        reciboPago();
        fichaInfo();
        listadoNomina();



    }//main

    public static void buscaEmpleado(){

        String lista = ""; //String en el cual se va a hacer la lista de nombres y numeros de cuenta

        do {

            System.out.println("\t*INGRESA EL # NOMINA* [Ej. 101 al 110] ");

            for (int i = 0; i<10; i++){ // CREA UNA LISTA CON TODOS LOS NOMBRES Y NUMEROS DE CUENTA DE LOS EMPLEADOS

            String temp = emp[i].getNombre(); //Variable temporal para la comparación de abajo

            if(!temp.equalsIgnoreCase("x")){ //Si el nombre del usuario es igual a una x, no se imprime, ya que el nombre default de los empleados sin inicializar es x
                lista += "\t" + (i+1) + ".- " + emp[i].getNombre() + " \t\tNum de Cuenta: \t" + emp[i].getNumCuenta() + "\n";
            }//if
        }

            System.out.println(lista);

            numeroDeNomina = lectura.nextInt();

            for (int i = 0; i <10; i++) {

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

    }//busca empleado


    public static void inputUsuario(){

        double ivatl = 0.0;
        int temp = (int) emp[indexEmpleado].getSueldoBase(); //Variable temporal para el calculo del iva

        System.out.println("\tIngrese los dias trabajados de " + emp[indexEmpleado].getNombre());
        diasTrabajados[indexEmpleado] = lectura.nextInt();

        System.out.println("\tIngrese sus asignaciones"); //SE INGRESAN ASIGNACIONES Y SE GUARDAN EN UNA VARIABLE
        System.out.println("\t\tBonos:");
            asignaciones[0][indexEmpleado] = lectura.nextInt();
        System.out.println("\t\tFeriados");
            asignaciones[1][indexEmpleado] += lectura.nextInt();
        System.out.println("\t\tHoras Extras");
            asignaciones[2][indexEmpleado] += lectura.nextInt();
        System.out.println("\t\tOtros");
            asignaciones[3][indexEmpleado] += lectura.nextInt();

        System.out.println("\tIngrese sus deducciones (IVA, ISR, Prestamos)"); //SE INGRESAN DEDUCCIONES Y SE GUARDAN EN UNA VARIABLE
            ivatl =  temp * 0.16;
            deducciones[0][indexEmpleado] = (int) ivatl;
        System.out.println("\t\tISR");
            deducciones[1][indexEmpleado] += lectura.nextInt();
        System.out.println("\t\tPrestamos");
            deducciones[2][indexEmpleado] += lectura.nextInt();
        System.out.println("\t\tOtros");
            deducciones[3][indexEmpleado] += lectura.nextInt();

        //PONE TRUE EN EL ARRAY DE EXTRAS AGREGADOS
        extrasAgregados[indexEmpleado] = true;

    }//input usuario

    public static void menu() {


            //MENU PARA DAR DE ALTA, BAJA O MODIFIAR DATOS DE UN EMPLEADO
            System.out.println("\n\tQUE OPCION DESEA SELECCIONAR (SELECCIONE EL NUMERO DE LA OPCION)");
            System.out.println("\t ---------    ---------    --------------------    ----------------------    --------------------");
            System.out.println("\t| 1. ALTA |  | 2. BAJA |  | 3. MODIFICAR DATOS |  | 4. CONSULTA EMPLEADO |  | 5. SEGUIR ADELANTE |");
            System.out.println("\t ---------    ---------    --------------------    ----------------------    --------------------");

        do {

            opcionMenu = lectura.nextInt();

                if(opcionMenu >=1 || opcionMenu <=4){ //SE SELECCIONA UNA OPCION DENTRO DEL ARANGO DE OPCIONES
                valido = true;
                }
                if(opcionMenu<0 || opcionMenu >5){ //SE SELECCIONA OTRA OPCION QUE NO ESTA DENTRO DEL RANGO DE OPCIONES
                System.out.println("\tLA OPCION NO ES VALIDA, INTENTA DE NUEVO");
                valido = false;
                }

        } while (!valido);//do while

    }//menu

    public static void alta(){

        boolean emptySpace = false; //Variable para ver si hay un espacio vacío
        int emptySpaceIndex = -1; //Variable donde va a ir el index del espacio vacío

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
            int numC = 0;

            System.out.println("\tINGRESA EL NOMBRE");
            String nom = lectura.next();
            System.out.println("\tINGRESA EL APELLIDO");
            String ape = lectura.next();
            System.out.println("\tINGRESA EL CARGO");
            String cargo = lectura.next();
            System.out.println("\tINGRESA EL SUELDO");
            long sueldo = lectura.nextLong();
            System.out.println("\tINGRESA LA FECHA DE INGRESO (XX/XX/XX)");
            String fecha = lectura.next();

            if(emptySpaceIndex == 9){

                System.out.println("\tEL NUMERO DE CUENTA DEL USUARIO ES: 110");
                numC = 110;

            }else{

                System.out.println("\tEL NUMERO DE CUENTA DEL USUARIO ES: 10" + (emptySpaceIndex +1));
                numC = 100 + (emptySpaceIndex+1);

            }

            emp[emptySpaceIndex].actualizaDatos(nom,ape,cargo,sueldo,fecha,numC);

        }

    }//alta

    public static void baja(){

        String lista = "";
        boolean correcto = true;
        int numDeCuenta = 0;
        int indexDelete = -1;

        for (int i = 0; i<10; i++){ // CREA UNA LISTA CON TODOS LOS NOMBRES Y NUMEROS DE CUENTA DE LOS EMPLEADOS
            String temp = emp[i].getNombre();
            if(!temp.equalsIgnoreCase("x")){
                lista += "\t" + (i+1) + ".- " + emp[i].getNombre() + " \t\tNum de Cuenta: \t" + emp[i].getNumCuenta() + "\n";
            }//if
        }

        System.out.println("\tA QUE EMPLEADO QUIERES DAR DE BAJA ? (INGRESA SU NÚMERO DE CUENTA):");
        System.out.println(lista);

        do {

            numDeCuenta = lectura.nextInt();

            for(int i = 0; i<10; i++){ //ENCUENTRA EL NUMERO DE CUENTA QUE COINCIDE CON EL EMPLEADO

                int temp = emp[i].getNumCuenta();

                if (temp == numDeCuenta){
                    indexDelete = i;
                    correcto = true;
                    break;
                }//if
                else{
                    correcto = false;
                }

            }//for

            if(!correcto){
                System.out.print("\tEL NUMERO DE CUENTA NO COINCIDE CON NINGUNO DE LOS EMPLEADOS, INTETNA DE NUEVO \n");
            }

        }while(!correcto);

        if (correcto) {
            System.out.println("\t" + emp[indexDelete].getNombre() + " ha sido dado de BAJA");
            emp[indexDelete] = new Empleados(); //CAMBIA LOS DATOS DEL EMPLEADO EN indexDelete A LOS DATOS DEFAULT "ELIMINANDOLO"
        }

    }// baja

    public static void mod(){

        String lista = "";
        boolean correcto = true;
        int numDeCuenta = 0;
        int indexMod = -1;

        for (int i = 0; i<10; i++){ // CREA UNA LISTA CON TODOS LOS NOMBRES Y NUMEROS DE CUENTA DE LOS EMPLEADOS
            String temp = emp[i].getNombre();
            if(!temp.equalsIgnoreCase("x")){
                lista += "\t" + (i+1) + ".- " + emp[i].getNombre() + " \t\tNum de Cuenta: \t" + emp[i].getNumCuenta() + "\n";
            }//if
        }

        System.out.println("\tA QUE EMPLEADO LE QUIERES MODIFICAR LOS DATOS ? (INGRESA SU NÚMERO DE CUENTA):");
        System.out.println(lista);

        do {

            numDeCuenta = lectura.nextInt();

            for(int i = 0; i<10; i++){ //ENCUENTRA EL NUMERO DE CUENTA QUE COINCIDE CON EL EMPLEADO

                int temp = emp[i].getNumCuenta();

                if (temp == numDeCuenta){
                    indexMod = i;
                    correcto = true;
                    break;
                }//if
                else{
                    correcto = false;
                }

            }//for

            if(!correcto){
                System.out.print("\tEL NUMERO DE CUENTA NO COINCIDE CON NINGUNO DE LOS EMPLEADOS, INTETNA DE NUEVO \n");
            }

        }while(!correcto);

        if (correcto){

            int numC;

            System.out.println("\tINGRESA EL NOMBRE");
            String nom = lectura.next();
            System.out.println("\tINGRESA EL APELLIDO");
            String ape = lectura.next();
            System.out.println("\tINGRESA EL CARGO");
            String cargo = lectura.next();
            System.out.println("\tINGRESA EL SUELDO");
            long sueldo = lectura.nextLong();
            System.out.println("\tINGRESA LA FECHA DE INGRESO (XX/XX/XX)");
            String fecha = lectura.next();

            if(indexMod == 9){

                System.out.println("\tEL NUMERO DE CUENTA DEL USUARIO ES: 110");
                numC = 110;

            }else{

                System.out.println("\tEL NUMERO DE CUENTA DEL USUARIO ES: 10" + (indexMod +1));
                numC = 100 + (indexMod+1);

            }

            emp[indexMod].actualizaDatos(nom,ape,cargo,sueldo,fecha,numC);

        }

        indexEmpleado = indexMod;

    }//mod

    public static void initFiles(){ //CREA LOS FILES DE INICIALIZACION PARA CADA EMPLEADO

        String nombres = "";
        String apellidos = "";
        String cargos = "";
        String sueldos = "";
        String fechas = "";
        String numeros = "";
        
        try {
            
            f = new File("init.csv");
            bw = new BufferedWriter(new FileWriter("init.csv")); //CREAMOS EL ARCHIVO DE INICIALIZACIÓN
            
            for (int i = 0; i < 10; i++) {

                nombres += "" + emp[i].getNombre() + ",";
                apellidos += "" + emp[i].getApellido() + ",";
                cargos += "" + emp[i].getCargo() + ",";
                sueldos += "" + emp[i].getSueldoBase() + ",";
                fechas += "" + emp[i].getFechaIngreso() + ",";
                numeros += "" + emp[i].getNumCuenta() + ",";

            }//for
            
            //CREAMOS UN ARCHIVO DONDE CADA LINEA ES INFORMACIÓN SOBRE LOS EMPLEADOS
            bw.write(nombres); //PRIMERA LINEA -> NOMBRE
            bw.newLine();
            bw.write(apellidos); //SEGUNDA LINA -> APELLIDO
            bw.newLine();
            bw.write(cargos); //TERCERA LIENA -> CARGOS
            bw.newLine();
            bw.write(sueldos); //CUARTA LINEA -> SUELDOS
            bw.newLine();
            bw.write(fechas); //QUINTA LINEA -> FECHAS DE INGRESO
            bw.newLine();
            bw.write(numeros); //SEXTA LINEA -> NUMEROS DE CUENTA
            
            bw.flush();
            bw.close();
            

        } catch (IOException e){
            e.printStackTrace();
        }
    }//init files
    
    public static void readInit(){
        
        String[] nombresInit = new String[10];
        String[] apellidosInit = new String[10];
        String[] cargosInit = new String[10];
        String[] s = new String[10];
        long[] sueldosInit = new long[10];
        String[] fechasInit = new String[10];
        String[] n = new String[10];
        int[] numerosInit = new int[10];
        
        try {
    
        
        br = new BufferedReader(new FileReader(f));
            
        String nombres = br.readLine();
        String apellidos = br.readLine();
        String cargos = br.readLine();
        String sueldos = br.readLine();
        String fechas = br.readLine();
        String numeros = br.readLine();
        
        nombresInit = nombres.split(",");
        apellidosInit = apellidos.split(",");
        cargosInit = cargos.split(",");
        s = sueldos.split(",");
        fechasInit = fechas.split(",");
        n = numeros.split(",");
        
            
            for(int i = 0; i<10; i++){ //FOR PARA PONER LOS NUMEROS DE CUENTAS Y SALDOS EN UN ARREGLO DE NUMEROS Y NO STRINGS
                
                sueldosInit[i] = Long.parseLong(s[i]);
                numerosInit[i] = Integer.parseInt(n[i]);
                
            }
            
            for(int i = 0; i<10; i++){ //FOR PARA ACTUALIZAR LOS DATOS DE LOS EMPLEADOS A PARTIR DEL ARCHIVO
                
                emp[i].actualizaDatos(nombresInit[i], apellidosInit[i], cargosInit[i], sueldosInit[i], fechasInit[i], numerosInit[i]);
                
            }
    
        }catch(IOException e){
        
            e.printStackTrace();
        }
    
    }//readInit

    public static void seguirAdelante(){

        boolean alguno = false; //Pa saber si hay alguno que falta de deducciones, asignaciones, dias trabajados
        String empleados = "";

        for(int i = 0; i<10; i++){

            if(!extrasAgregados[i]){
                alguno = true;

                String temp = emp[i].getNombre();
                if(!temp.equalsIgnoreCase("x")){
                empleados += "\t -> " + emp[i].getNombre() + " " + emp[i].getApellido() + "\n";
                }
            }

        }//for

        if(alguno){
            System.out.println("\tTODAVIA NO INGRESAS ASIGNACIONES, DEDUCCIONES, DIAS TRABJADOS DE LOS SIGUIENTES EMPLEADOS: ");
            System.out.println(empleados);
        }//if

        System.out.println("\tESTAS SEGURO QUE QUIERES SEGUIR ADELANTE ? [Y/N]");
        String dec = lectura.next();
        if(dec.equalsIgnoreCase("n")){
            opcionMenu = 0;
        }


    }

    public static void reciboPago(){


        for(int i = 0; i<10; i++){

            String temp = emp[i].getNombre();

            if(!temp.equalsIgnoreCase("X")) {

                int asignacionesTotales = 0;
                int deduccionesTotales = 0;
                long sueldotl = emp[i].getSueldoBase();

                System.out.println("\t * RECIBO DE PAGO *");
                System.out.println("\t----------------------------\n");
                System.out.println("\t" + emp[i].getNombre() + " " + emp[i].getApellido());
                System.out.println("\t SUELDO BRUTO: $" + emp[i].getSueldoBase());

                //ASIGNACIONES

                System.out.println("\t ASIGNACIONES: ");
                System.out.println("\t - BONOS: $" + asignaciones[0][i]);
                System.out.println("\t - FERIADOS: $" + asignaciones[1][i]);
                System.out.println("\t - HORAS EXTRAS: $" + asignaciones[2][i]);
                System.out.println("\t - OTROS: $" + asignaciones[3][i]);

                for (int j = 0; j< 4; j++) {
                    asignacionesTotales += asignaciones[j][i];
                }

                System.out.println("\t ASIGNACIONES TOTALES: $" + asignacionesTotales);
                System.out.println("\t----------------------------\n");

                //DEDUCCIONES

                System.out.println("\t DEDUCCIONES: ");
                System.out.println("\t - IVA: $" + deducciones[0][i]);
                System.out.println("\t - ISR: $" + deducciones[1][i]);
                System.out.println("\t - PRESTAMOS: $" + deducciones[2][i]);
                System.out.println("\t - OTROS: $" + deducciones[3][i]);

                for (int j = 0; j < 4; j++) {
                    deduccionesTotales += deducciones[j][i];
                }

                System.out.println("\t DEDUCCIONES TOTALES: $" + deduccionesTotales);
                System.out.println("\t----------------------------\n");

                sueldoNeto[i] += (int) sueldotl + asignacionesTotales - deduccionesTotales;

                System.out.println("\t El Sueldo Neto de " + emp[i].getNombre() + " es $" + sueldoNeto[i]);

                System.out.println("\t----------------------------\n");

            }//IF


        }//for

    }//recibo de pago

    public static void fichaInfo(){

        for(int i=0; i<10; i++){
            String temp = emp[i].getNombre();

            if(!temp.equalsIgnoreCase("X")) {
                System.out.println(emp[i].imprimirDatos());
                System.out.println("\tEl sueldo de "+emp[i].getNombre() + " es $" + sueldoNeto[i]+"\n");
             }//if

        }//for

    }//fichaInfo

    public static void listadoNomina(){

        int nominaEmpresa = 0;

        for(int i = 0; i<10; i++){

            String temp = emp[i].getNombre();

            if(!temp.equalsIgnoreCase("X")){

                System.out.println("\t" + temp + ":  $" + sueldoNeto[i] );
                System.out.println("\t----------------------------\n");
                nominaEmpresa += sueldoNeto[i];

            }//if

        }

        System.out.println("\tListado total de nómina para la empresa: \n\t $" + nominaEmpresa);

    }//listadoNomina

}//class