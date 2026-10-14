package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa una función cinematográfica programada.
 */
public class Funcion {

    private String codigo;
    private Pelicula thePelicula;
    private Sala theSala;
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private double precioBase;

    /**
     * Constructor de la clase Funcion.
     *
     * @param codigo código de la función
     * @param thePelicula película asociada
     * @param theSala sala donde se realizará
     * @param fecha fecha de la función
     * @param horaInicio hora de inicio
     * @param precioBase precio base de la boleta
     */
    public Funcion(String codigo,
                   Pelicula thePelicula,
                   Sala theSala,
                   LocalDate fecha,
                   LocalDateTime horaInicio,
                   double precioBase) {

        this.codigo = codigo;
        this.thePelicula = thePelicula;
        this.theSala = theSala;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.precioBase = precioBase;
    }

    /**
     * Calcula la hora de finalización de la función
     * utilizando la duración de la película.
     *
     * @return hora de finalización
     */
    public LocalDateTime calcularHoraFin() {

        return horaInicio.plusMinutes(
                thePelicula.getDuracionMinutos()
        );
    }

    /**
     * Verifica si esta función se superpone con otra función.
     *
     * @param otraFuncion función con la cual se realizará la comparación
     * @return true si existe superposición, false en caso contrario
     */
    public boolean seSuperpone(Funcion otraFuncion) {

        if (otraFuncion == null) {
            return false;
        }

        if (!fecha.equals(otraFuncion.getFecha())) {
            return false;
        }

        LocalDateTime inicio1 = horaInicio;
        LocalDateTime fin1 = calcularHoraFin();

        LocalDateTime inicio2 = otraFuncion.getHoraInicio();
        LocalDateTime fin2 = otraFuncion.calcularHoraFin();

        return inicio1.isBefore(fin2) &&
                inicio2.isBefore(fin1);
    }

    /**
     * Calcula el valor de una boleta aplicando primero
     * el recargo de la sala y posteriormente el descuento
     * correspondiente al cliente.
     *
     * @param cliente cliente que realizará la compra
     * @return valor final de la boleta
     */
    public double calcularValorBoleta(Cliente cliente) {

        double valor = precioBase *
                (1 + theSala.factorRecargo());

        if (cliente != null) {
            valor = cliente.aplicarDescuento(valor);
        }

        return valor;
    }

    /**
     * Verifica si un asiento está disponible para esta función.
     *
     * @param fila fila del asiento
     * @param numero número del asiento
     * @return true si el asiento está disponible
     */
    public boolean asientoDisponible(String fila, int numero) {

        return true;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Pelicula getThePelicula() {
        return thePelicula;
    }

    public void setThePelicula(Pelicula thePelicula) {
        this.thePelicula = thePelicula;
    }

    public Sala getTheSala() {
        return theSala;
    }

    public void setTheSala(Sala theSala) {
        this.theSala = theSala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    @Override
    public String toString() {
        return "Funcion{" +
                "codigo='" + codigo + '\'' +
                ", thePelicula=" + thePelicula +
                ", theSala=" + theSala +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", precioBase=" + precioBase +
                '}';
    }
}