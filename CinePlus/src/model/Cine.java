package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el cine y administra las películas, salas,
 * funciones, personas y ventas del sistema.
 */
public class Cine {

    private String nombreComercial;
    private String nit;
    private String direccion;

    private List<Pelicula> listaPeliculas;
    private List<Sala> listaSalas;
    private List<Funcion> listaFunciones;
    private List<Persona> listaPersona;
    private List<Venta> listaVentas;

    /**
     * Constructor de la clase Cine.
     *
     * @param nombreComercial nombre comercial del cine
     * @param nit NIT del cine
     * @param direccion dirección del cine
     */
    public Cine(String nombreComercial, String nit, String direccion) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;

        this.listaPeliculas = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaPersona = new ArrayList<>();
        this.listaVentas = new ArrayList<>();
    }

    /**
     * Registra una sala en el cine.
     *
     * @param sala sala que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarSala(Sala sala) {

        if (sala == null) {
            return false;
        }

        for (Sala salaExistente : listaSalas) {

            if (salaExistente.getCodigo()
                    .equalsIgnoreCase(sala.getCodigo())) {

                return false;
            }
        }

        listaSalas.add(sala);
        return true;
    }

    /**
     * Registra una película en el catálogo verificando
     * que no exista otra con el mismo código.
     *
     * @param pelicula película que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarPelicula(Pelicula pelicula) {

        if (pelicula == null) {
            return false;
        }

        for (Pelicula peliculaExistente : listaPeliculas) {

            if (peliculaExistente.getCodigo()
                    .equalsIgnoreCase(pelicula.getCodigo())) {

                return false;
            }
        }

        listaPeliculas.add(pelicula);
        return true;
    }

    /**
     * Programa una función cinematográfica verificando que
     * no exista conflicto de horario en la misma sala.
     *
     * @param funcion función que será programada
     * @return true si la función fue programada correctamente
     */
    public boolean programarFuncion(Funcion funcion) {
        if (funcion == null
                || funcion.getCodigo() == null
                || funcion.getFecha() == null
                || funcion.getHoraInicio() == null
                || funcion.getThePelicula() == null
                || funcion.getTheSala() == null) {
            return false;
        }

        for (Funcion existente : listaFunciones) {
            if (existente.getCodigo().equalsIgnoreCase(funcion.getCodigo())) {
                return false;
            }
        }

        boolean peliculaRegistrada = false;
        for (Pelicula pelicula : listaPeliculas) {
            if (pelicula.getCodigo().equalsIgnoreCase(
                    funcion.getThePelicula().getCodigo())) {
                peliculaRegistrada = true;
                break;
            }
        }

        boolean salaRegistrada = false;
        for (Sala sala : listaSalas) {
            if (sala.getCodigo().equalsIgnoreCase(
                    funcion.getTheSala().getCodigo())) {
                salaRegistrada = true;
                break;
            }
        }

        if (!peliculaRegistrada || !salaRegistrada) {
            return false;
        }

        return salaDisponible(
                funcion.getTheSala(),
                funcion.getFecha(),
                funcion.getHoraInicio(),
                funcion.getThePelicula().getDuracionMinutos()
        );
    }

    /**
     * Registra una persona en el sistema.
     *
     * @param persona persona que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarPersona(Persona persona) {

        if (persona == null) {
            return false;
        }

        for (Persona personaExistente : listaPersona) {

            if (personaExistente.getDocumento()
                    .equalsIgnoreCase(persona.getDocumento())) {

                return false;
            }
        }

        listaPersona.add(persona);
        return true;
    }

    /**
     * Registra una venta verificando que la venta no sea nula.
     *
     * @param venta venta que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarVenta(Venta venta) {
        if (venta == null
                || venta.getCodigo() == null
                || venta.getTheCliente() == null
                || venta.getTheOperario() == null
                || venta.getTheFuncion() == null
                || venta.getTheFuncion().getTheSala() == null
                || venta.getListaBoletaVenta() == null
                || venta.getListaBoletaVenta().isEmpty()) {
            return false;
        }

        for (Venta existente : listaVentas) {
            if (existente.getCodigo().equalsIgnoreCase(venta.getCodigo())) {
                return false;
            }
        }

        if (!personaRegistrada(venta.getTheCliente(), Cliente.class)
                || !personaRegistrada(venta.getTheOperario(), Operario.class)
                || !funcionRegistrada(venta.getTheFuncion())) {
            return false;
        }

        Sala sala = venta.getTheFuncion().getTheSala();
        List<Boleta> boletasNuevas = venta.getListaBoletaVenta();

        for (int i = 0; i < boletasNuevas.size(); i++) {
            Boleta boleta = boletasNuevas.get(i);

            if (boleta == null || !asientoValido(sala, boleta)) {
                return false;
            }

            // Evita repetir un asiento dentro de esta misma venta.
            for (int j = 0; j < i; j++) {
                Boleta anterior = boletasNuevas.get(j);
                if (mismoAsiento(anterior, boleta)) {
                    return false;
                }
            }

            // Evita vender un asiento que ya aparece en otra venta de esta función.
            for (Venta ventaExistente : listaVentas) {
                if (ventaExistente.getTheFuncion() == null
                        || !ventaExistente.getTheFuncion().getCodigo()
                        .equalsIgnoreCase(venta.getTheFuncion().getCodigo())) {
                    continue;
                }

                for (Boleta boletaExistente : ventaExistente.getListaBoletaVenta()) {
                    if (mismoAsiento(boletaExistente, boleta)) {
                        return false;
                    }
                }
            }
        }

        listaVentas.add(venta);
        return true;
    }

    private boolean personaRegistrada(Persona persona, Class<?> tipo) {
        for (Persona registrada : listaPersona) {
            if (tipo.isInstance(registrada)
                    && registrada.getDocumento().equalsIgnoreCase(persona.getDocumento())) {
                return true;
            }
        }
        return false;
    }

    private boolean funcionRegistrada(Funcion funcion) {
        for (Funcion registrada : listaFunciones) {
            if (registrada.getCodigo().equalsIgnoreCase(funcion.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    private boolean asientoValido(Sala sala, Boleta boleta) {
        String fila = boleta.getFila();

        if (fila == null || fila.length() != 1) {
            return false;
        }

        char letraFila = Character.toUpperCase(fila.charAt(0));

        return letraFila >= 'A'
                && letraFila < 'A' + sala.getFilas()
                && boleta.getNumeroAsiento() >= 1
                && boleta.getNumeroAsiento() <= sala.getColumnas();
    }

    private boolean mismoAsiento(Boleta primera, Boleta segunda) {
        return primera.getFila().equalsIgnoreCase(segunda.getFila())
                && primera.getNumeroAsiento() == segunda.getNumeroAsiento();
    }

    /**
     * Busca todas las funciones asociadas a una película.
     *
     * @param codigo codigo de la película
     * @return lista de funciones de la película
     */
    public List<Funcion> funcionesDePelicula(String codigo) {

        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : listaFunciones) {

            if (funcion.getThePelicula() != null &&
                    funcion.getThePelicula()
                            .getCodigo()
                            .equalsIgnoreCase(codigo)) {

                resultado.add(funcion);
            }
        }

        return resultado;
    }

    /**
     * Verifica si una sala está disponible para programar
     * una función en una fecha y hora determinadas.
     *
     * @param sala sala que se desea utilizar
     * @param fecha fecha de la función
     * @param horaInicio hora de inicio
     * @return true si la sala está disponible
     */
        public boolean salaDisponible(
                Sala sala,
                LocalDate fecha,
                LocalDateTime horaInicio,
        int duracionNueva) {

            if (sala == null || fecha == null || horaInicio == null || duracionNueva <= 0) {
                return false;
            }

            LocalDateTime finNueva = horaInicio.plusMinutes(duracionNueva);

            for (Funcion existente : listaFunciones) {
                if (existente.getTheSala() != null
                        && existente.getTheSala().getCodigo().equalsIgnoreCase(sala.getCodigo())
                        && existente.getFecha().equals(fecha)) {

                    LocalDateTime finExistente = existente.calcularHoraFin();

                    if (horaInicio.isBefore(finExistente)
                            && existente.getHoraInicio().isBefore(finNueva)) {
                        return false;
                    }
                }
            }

            return true;
        }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public List<Sala> getListaSalas() {
        return listaSalas;
    }

    public void setListaSalas(List<Sala> listaSalas) {
        this.listaSalas = listaSalas;
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public List<Persona> getListaPersona() {
        return listaPersona;
    }

    public void setListaPersona(List<Persona> listaPersona) {
        this.listaPersona = listaPersona;
    }

    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    @Override
    public String toString() {
        return "Cine{" +
                "nombreComercial='" + nombreComercial + '\'' +
                ", nit='" + nit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", listaPeliculas=" + listaPeliculas +
                ", listaSalas=" + listaSalas +
                ", listaFunciones=" + listaFunciones +
                ", listaPersona=" + listaPersona +
                ", listaVentas=" + listaVentas +
                '}';
    }
}

