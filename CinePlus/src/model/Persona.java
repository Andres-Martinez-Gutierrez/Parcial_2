package model;

/**
 * Clase abstracta que representa la información común
 * de los diferentes tipos de personas del sistema.
 */
public abstract class Persona {

    private String documento;
    private String nombreCompleto;
    private String correo;
    private String telefono;

    /**
     * Constructor de la clase Persona.
     *
     * @param documento documento de identificación
     * @param nombreCompleto nombre completo de la persona
     * @param correo correo electrónico
     * @param telefono número telefónico
     */
    public Persona(String documento,
                   String nombreCompleto,
                   String correo,
                   String telefono) {

        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "documento='" + documento + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}