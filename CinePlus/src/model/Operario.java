package model;

import java.time.LocalDate;

/**
 * Representa a un operario encargado de registrar las ventas.
 * Hereda los datos generales de Persona.
 */
public class Operario extends Persona {

    private String codigoEmpleado;
    private LocalDate fechaContratacion;
    private double salarioBasico;

    /**
     * Constructor de la clase Operario.
     *
     * @param documento documento de identificación
     * @param nombreCompleto nombre completo
     * @param correo correo electrónico
     * @param telefono número telefónico
     * @param codigoEmpleado código del empleado
     * @param fechaContratacion fecha de contratación
     * @param salarioBasico salario básico
     */
    public Operario(String documento,
                    String nombreCompleto,
                    String correo,
                    String telefono,
                    String codigoEmpleado,
                    LocalDate fechaContratacion,
                    double salarioBasico) {

        super(documento, nombreCompleto, correo, telefono);

        this.codigoEmpleado = codigoEmpleado;
        this.fechaContratacion = fechaContratacion;
        this.salarioBasico = salarioBasico;
    }

    /**
     * Registra una venta asociada al operario.
     *
     * @param venta venta que será registrada
     */
    public void registrarVenta(Venta venta) {

        if (venta != null) {
            System.out.println("Venta registrada: " + venta.getCodigo());
        }
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getSalarioBasico() {
        return salarioBasico;
    }

    public void setSalarioBasico(double salarioBasico) {
        this.salarioBasico = salarioBasico;
    }

    @Override
    public String toString() {
        return "Operario{" +
                "documento='" + getDocumento() + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                ", salarioBasico=" + salarioBasico +
                '}';
    }
}