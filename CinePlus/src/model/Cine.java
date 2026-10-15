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
     * Registra una sala verificando que no exista
     * otra con el mismo código.
     *
     * @param sala sala que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarSala(Sala sala) {

        if (sala == null || sala.getCodigo() == null) {
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
     * Registra una película verificando que no exista
     * otra con el mismo código.
     *
     * @param pelicula película que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarPelicula(Pelicula pelicula) {

        if (pelicula == null || pelicula.getCodigo() == null) {
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
     * Programa una función verificando:
     * - que los datos sean válidos;
     * - que no exista otra función con el mismo código;
     * - que la película esté registrada;
     * - que la sala esté registrada;
     * - que no exista conflicto de horario.
     *
     * @param funcion función que será programada
     * @return true si fue programada correctamente
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

        // Verifica que el código de la función sea único.
        for (Funcion existente : listaFunciones) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(funcion.getCodigo())) {

                return false;
            }
        }

        // Verifica que la película esté registrada.
        if (!peliculaRegistrada(funcion.getThePelicula())) {
            return false;
        }

        // Verifica que la sala esté registrada.
        if (!salaRegistrada(funcion.getTheSala())) {
            return false;
        }

        // Verifica disponibilidad teniendo en cuenta la duración.
        if (!salaDisponible(
                funcion.getTheSala(),
                funcion.getFecha(),
                funcion.getHoraInicio(),
                funcion.getThePelicula().getDuracionMinutos())) {

            return false;
        }

        // IMPORTANTE:
        // La función debe quedar almacenada en el cine.
        listaFunciones.add(funcion);

        return true;
    }

    /**
     * Verifica si una película está registrada.
     *
     * @param pelicula película que se desea verificar
     * @return true si está registrada
     */
    private boolean peliculaRegistrada(Pelicula pelicula) {

        for (Pelicula existente : listaPeliculas) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(pelicula.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Verifica si una sala está registrada.
     *
     * @param sala sala que se desea verificar
     * @return true si está registrada
     */
    private boolean salaRegistrada(Sala sala) {

        for (Sala existente : listaSalas) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(sala.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Registra una persona verificando que su documento
     * no esté registrado anteriormente.
     *
     * @param persona persona que será registrada
     * @return true si fue registrada correctamente
     */
    public boolean registrarPersona(Persona persona) {

        if (persona == null || persona.getDocumento() == null) {
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
     * Registra una venta verificando que:
     * - tenga cliente, función y operario;
     * - la venta tenga al menos una boleta;
     * - el código sea único;
     * - cliente y operario estén registrados;
     * - la función esté registrada;
     * - los asientos sean válidos;
     * - no existan asientos repetidos.
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

        // Verifica código único de venta.
        for (Venta existente : listaVentas) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(venta.getCodigo())) {

                return false;
            }
        }

        // Verifica que cliente, operario y función estén registrados.
        if (!personaRegistrada(venta.getTheCliente(), Cliente.class)
                || !personaRegistrada(venta.getTheOperario(), Operario.class)
                || !funcionRegistrada(venta.getTheFuncion())) {

            return false;
        }

        Sala sala = venta.getTheFuncion().getTheSala();

        List<Boleta> boletasNuevas =
                venta.getListaBoletaVenta();

        // Verifica cada boleta.
        for (int i = 0; i < boletasNuevas.size(); i++) {

            Boleta boleta = boletasNuevas.get(i);

            if (boleta == null || !asientoValido(sala, boleta)) {
                return false;
            }

            // No permite repetir asiento dentro de la misma venta.
            for (int j = 0; j < i; j++) {

                Boleta anterior = boletasNuevas.get(j);

                if (mismoAsiento(anterior, boleta)) {
                    return false;
                }
            }

            // No permite vender un asiento ya vendido
            // para esta misma función.
            for (Venta ventaExistente : listaVentas) {

                if (ventaExistente.getTheFuncion() == null
                        || !ventaExistente.getTheFuncion()
                        .getCodigo()
                        .equalsIgnoreCase(
                                venta.getTheFuncion().getCodigo())) {

                    continue;
                }

                for (Boleta boletaExistente
                        : ventaExistente.getListaBoletaVenta()) {

                    if (mismoAsiento(boletaExistente, boleta)) {
                        return false;
                    }
                }
            }
        }

        listaVentas.add(venta);

        return true;
    }

    /**
     * Verifica si una persona está registrada y pertenece
     * al tipo indicado.
     *
     * @param persona persona que se desea buscar
     * @param tipo clase esperada
     * @return true si está registrada
     */
    private boolean personaRegistrada(
            Persona persona,
            Class<?> tipo) {

        for (Persona registrada : listaPersona) {

            if (tipo.isInstance(registrada)
                    && registrada.getDocumento()
                    .equalsIgnoreCase(persona.getDocumento())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Verifica si una función está registrada.
     *
     * @param funcion función que se desea verificar
     * @return true si está registrada
     */
    private boolean funcionRegistrada(Funcion funcion) {

        for (Funcion registrada : listaFunciones) {

            if (registrada.getCodigo()
                    .equalsIgnoreCase(funcion.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Verifica que el asiento exista físicamente en la sala.
     *
     * @param sala sala correspondiente
     * @param boleta boleta que contiene el asiento
     * @return true si el asiento es válido
     */
    private boolean asientoValido(Sala sala, Boleta boleta) {

        String fila = boleta.getFila();

        if (fila == null || fila.length() != 1) {
            return false;
        }

        char letraFila =
                Character.toUpperCase(fila.charAt(0));

        return letraFila >= 'A'
                && letraFila < 'A' + sala.getFilas()
                && boleta.getNumeroAsiento() >= 1
                && boleta.getNumeroAsiento() <= sala.getColumnas();
    }

    /**
     * Compara dos asientos.
     *
     * @param primera primera boleta
     * @param segunda segunda boleta
     * @return true si representan el mismo asiento
     */
    private boolean mismoAsiento(
            Boleta primera,
            Boleta segunda) {

        return primera.getFila()
                .equalsIgnoreCase(segunda.getFila())
                && primera.getNumeroAsiento()
                == segunda.getNumeroAsiento();
    }

    /**
     * Busca todas las funciones asociadas a una película.
     *
     * @param codigo código de la película
     * @return lista de funciones
     */
    public List<Funcion> funcionesDePelicula(String codigo) {

        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : listaFunciones) {

            if (funcion.getThePelicula() != null
                    && funcion.getThePelicula()
                    .getCodigo()
                    .equalsIgnoreCase(codigo)) {

                resultado.add(funcion);
            }
        }

        return resultado;
    }

    /**
     * Verifica si una sala está disponible para programar
     * una función teniendo en cuenta la duración de la película.
     *
     * @param sala sala que se desea utilizar
     * @param fecha fecha de la función
     * @param horaInicio hora de inicio
     * @param duracionNueva duración de la nueva función
     * @return true si la sala está disponible
     */
    public boolean salaDisponible(
            Sala sala,
            LocalDate fecha,
            LocalDateTime horaInicio,
            int duracionNueva) {

        if (sala == null
                || fecha == null
                || horaInicio == null
                || duracionNueva <= 0) {

            return false;
        }

        LocalDateTime finNueva =
                horaInicio.plusMinutes(duracionNueva);

        for (Funcion existente : listaFunciones) {

            if (existente.getTheSala() != null
                    && existente.getTheSala()
                    .getCodigo()
                    .equalsIgnoreCase(sala.getCodigo())
                    && existente.getFecha().equals(fecha)) {

                LocalDateTime finExistente =
                        existente.calcularHoraFin();

                boolean hayCruce =
                        horaInicio.isBefore(finExistente)
                                && existente.getHoraInicio()
                                .isBefore(finNueva);

                if (hayCruce) {
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