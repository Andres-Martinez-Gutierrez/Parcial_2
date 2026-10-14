package model;

/**
 * Representa una boleta adquirida para una función.
 */
public class Boleta {

    private String codigo;
    private String fila;
    private int numeroAsiento;
    private double valor;

    /**
     * Constructor de la clase Boleta.
     *
     * @param codigo código de la boleta
     * @param fila fila del asiento
     * @param numeroAsiento número del asiento
     * @param valor valor de la boleta
     */
    public Boleta(String codigo,
                  String fila,
                  int numeroAsiento,
                  double valor) {

        this.codigo = codigo;
        this.fila = fila;
        this.numeroAsiento = numeroAsiento;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Boleta{" +
                "codigo='" + codigo + '\'' +
                ", fila='" + fila + '\'' +
                ", numeroAsiento=" + numeroAsiento +
                ", valor=" + valor +
                '}';
    }
}