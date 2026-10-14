package model;

/**
 * Representa un cliente del cine.
 * Hereda los datos generales de Persona.
 */
public class Cliente extends Persona {

    private String codigoAfiliacion;
    private double porcentajeDescuento;

    /**
     * Constructor de la clase Cliente.
     *
     * @param documento documento de identificación
     * @param nombreCompleto nombre completo del cliente
     * @param correo correo electrónico
     * @param telefono número telefónico
     * @param codigoAfiliacion código de afiliación
     * @param porcentajeDescuento porcentaje de descuento
     */
    public Cliente(String documento,
                   String nombreCompleto,
                   String correo,
                   String telefono,
                   String codigoAfiliacion,
                   double porcentajeDescuento) {

        super(documento, nombreCompleto, correo, telefono);

        this.codigoAfiliacion = codigoAfiliacion;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Aplica el porcentaje de descuento al valor recibido.
     *
     * @param valor valor al cual se aplicará el descuento
     * @return valor final después del descuento
     */
    public double aplicarDescuento(double valor) {

        return valor * (1 - porcentajeDescuento);
    }

    public String getCodigoAfiliacion() {
        return codigoAfiliacion;
    }

    public void setCodigoAfiliacion(String codigoAfiliacion) {
        this.codigoAfiliacion = codigoAfiliacion;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "documento='" + getDocumento() + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", codigoAfiliacion='" + codigoAfiliacion + '\'' +
                ", porcentajeDescuento=" + porcentajeDescuento +
                '}';
    }
}