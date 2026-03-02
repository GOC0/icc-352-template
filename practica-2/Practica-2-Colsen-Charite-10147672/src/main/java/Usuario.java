public class Usuario {

    private String nombre;
    private String contrasena;
    private boolean esAdmin;

    public Usuario(String nombre, String contrasena, boolean esAdmin) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.esAdmin = esAdmin;
    }

    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public boolean isAdmin() { return esAdmin; }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
