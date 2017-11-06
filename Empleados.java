
public class Empleados {

    //Variables de Instancia

    private String nombre;
    private String apellido;
    private String cargo;
    private long sueldoBase;
    private String fechaIngreso;
    private int numCuenta;

    //Constructor por Omision

    Empleados(){
        this("X", "X", "X", 0, "XX/XX/XX", 0);
    }

    Empleados (String nombre, String apellido, String cargo, long sueldoBase, String fechaIngreso, int numCuenta){

        setNombre(nombre);
        setApellido(apellido);
        setCargo(cargo);
        setSueldo(sueldoBase);
        setFecha(fechaIngreso);
        setCuenta(numCuenta);
    }

    //SETERS

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void setApellido(String apellido) {
        this.apellido = apellido;
    }

    private void setCargo(String cargo){
        this.cargo = cargo;
    }

    private void setSueldo(long sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    private void setFecha(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    private void setCuenta(int numCuenta){
        this.numCuenta = numCuenta;
    }

    //GETERS

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public long getSueldoBase() {
        return sueldoBase;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    //METODO PARA ACTUALIZAR DATOS

    public void actualizaDatos(String nombre, String apellido, String cargo, long sueldoBase, String fechaIngreso, int numCuenta){
        setNombre(nombre);
        setApellido(apellido);
        setCargo(cargo);
        setSueldo(sueldoBase);
        setFecha(fechaIngreso);
        setCuenta(numCuenta);
    }

    //METODO PARA IMPRIMIR DATOS

    public String imprimirDatos(){
        String temp = "";
        temp += "\t" + getNombre() + " " + getApellido() + "\n";
        temp += "\t" + getCargo() + "  Sueldo: $" + getSueldoBase() + "\n";
        temp += "\tFecha de Ingreso:" + getFechaIngreso() + "\n";
        temp += "\tNumero de Cuenta: " + getNumCuenta();
        return temp;
    }



}//class
