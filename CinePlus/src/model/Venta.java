package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta realizada por un cliente y registrada
 * por un operario.
 */
public class Venta {

    private String codigo;
    private LocalDate fecha;

    private Cliente theCliente;
    private Funcion theFuncion;
    private Operario theOperario;

    private List<Boleta> listaBoletaVenta;

    /**
     * Constructor de la clase Venta.
     *
     * @param codigo código de la venta
     * @param fecha fecha de la venta
     * @param theCliente cliente que realiza la compra
     * @param theFuncion función seleccionada
     * @param theOperario operario que registra la venta
     */
    public Venta(String codigo,
                 LocalDate fecha,
                 Cliente theCliente,
                 Funcion theFuncion,
                 Operario theOperario) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.theCliente = theCliente;
        this.theFuncion = theFuncion;
        this.theOperario = theOperario;

        this.listaBoletaVenta = new ArrayList<>();
    }

    /**
     * Agrega una boleta a la venta verificando previamente
     * que el asiento esté disponible para la función.
     *
     * @param fila fila del asiento
     * @param numero número del asiento
     * @return boleta creada o null si el asiento no está disponible
     */
    public Boleta agregarBoleta(String fila, int numero) {

        if (!theFuncion.asientoDisponible(fila, numero)) {
            return null;
        }

        double valor = theFuncion.calcularValorBoleta(theCliente);

        Boleta boleta = new Boleta(
                codigo + "-B" + (listaBoletaVenta.size() + 1),
                fila,
                numero,
                valor
        );

        listaBoletaVenta.add(boleta);

        return boleta;
    }

    /**
     * Calcula el valor total de la venta sumando
     * los valores individuales de todas sus boletas.
     *
     * @return valor total de la venta
     */
    public double calcularTotal() {

        double total = 0;

        for (Boleta boleta : listaBoletaVenta) {
            total += boleta.getValor();
        }

        return total;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getTheCliente() {
        return theCliente;
    }

    public void setTheCliente(Cliente theCliente) {
        this.theCliente = theCliente;
    }

    public Funcion getTheFuncion() {
        return theFuncion;
    }

    public void setTheFuncion(Funcion theFuncion) {
        this.theFuncion = theFuncion;
    }

    public Operario getTheOperario() {
        return theOperario;
    }

    public void setTheOperario(Operario theOperario) {
        this.theOperario = theOperario;
    }

    public List<Boleta> getListaBoletaVenta() {
        return listaBoletaVenta;
    }

    public void setListaBoletaVenta(List<Boleta> listaBoletaVenta) {
        this.listaBoletaVenta = listaBoletaVenta;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "codigo='" + codigo + '\'' +
                ", fecha=" + fecha +
                ", theCliente=" + theCliente +
                ", theFuncion=" + theFuncion +
                ", theOperario=" + theOperario +
                ", listaBoletaVenta=" + listaBoletaVenta +
                '}';
    }
}