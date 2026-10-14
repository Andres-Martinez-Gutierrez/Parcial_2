package model;

import  model.enumeration.Genero;

/**
 * Representa una película disponible en el catálogo del cine.
 */
public class Pelicula {

    private String codigo;
    private String titulo;
    private int duracionMinutos;
    private Genero genero;
    private String clasificacionEdad;

    /**
     * Constructor de la clase Pelicula.
     *
     * @param codigo código único de la película
     * @param titulo título de la película
     * @param duracionMinutos duración de la película en minutos
     * @param genero género de la película
     * @param clasificacionEdad clasificación por edad
     */
    public Pelicula(String codigo,
                    String titulo,
                    int duracionMinutos,
                    Genero genero,
                    String clasificacionEdad) {

        this.codigo = codigo;
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.clasificacionEdad = clasificacionEdad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getClasificacionEdad() {
        return clasificacionEdad;
    }

    public void setClasificacionEdad(String clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "codigo='" + codigo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                ", genero=" + genero +
                ", clasificacionEdad='" + clasificacionEdad + '\'' +
                '}';
    }
}