package model;

import model.enumeration.TipoSala;

/**
 * Representa una sala cinematográfica.
 */
public class Sala {

    private String codigo;
    private String nombre;
    private int capacidadMaxima;
    private TipoSala tipo;
    private int filas;
    private int columnas;

    /**
     * Constructor de la clase Sala.
     *
     * @param codigo código de la sala
     * @param nombre nombre o número de la sala
     * @param capacidadMaxima capacidad máxima de la sala
     * @param tipo tipo de sala
     * @param filas cantidad de filas
     * @param columnas cantidad de columnas
     */
    public Sala(String codigo,
                String nombre,
                int capacidadMaxima,
                TipoSala tipo,
                int filas,
                int columnas) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.tipo = tipo;
        this.filas = filas;
        this.columnas = columnas;
    }

    /**
     * Obtiene el factor de recargo correspondiente al tipo de sala.
     *
     * @return porcentaje de recargo en formato decimal
     */
    public double factorRecargo() {

        if (tipo == TipoSala.VIP) {
            return 0.30;
        }

        if (tipo == TipoSala.SALA_3D) {
            return 0.20;
        }

        return 0.0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", tipo=" + tipo +
                ", filas=" + filas +
                ", columnas=" + columnas +
                '}';
    }
}