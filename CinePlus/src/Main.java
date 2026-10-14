import model.*;
import model.enumeration.Genero;
import model.enumeration.TipoSala;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {

    private static final Cine CINE =
            new Cine("CinePlus", "900123456-7", "Armenia, Quindío");

    private static final String[] OPCIONES = {
            "Registrar película",
            "Registrar cliente",
            "Programar función",
            "Vender boletas",
            "Consultar valor de una venta",
            "Consultar ocupación de una función",
            "Listar películas",
            "Registrar sala u operario",
            "Consultar ingresos de una función",
            "Listar funciones",
            "Ver asientos de una función",
            "Listar salas",
            "Listar ventas",
            "Salir"
    };

    public static void main(String[] args) {
        cargarDatosDePrueba();

        boolean salir = false;
        while (!salir) {
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "Selecciona una operación:",
                    "CinePlus",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    OPCIONES,
                    OPCIONES[0]
            );

            if (opcion == -1 || opcion == OPCIONES.length - 1) {
                salir = true;
                continue;
            }

            try {
                switch (opcion) {
                    case 0:
                        registrarPelicula();
                        break;
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        programarFuncion();
                        break;
                    case 3:
                        realizarVenta();
                        break;
                    case 4:
                        consultarValorVenta();
                        break;
                    case 5:
                        consultarOcupacion();
                        break;
                    case 6:
                        mostrarLista("Películas", CINE.getListaPeliculas());
                        break;
                    case 7:
                        registrarSalaOOperario();
                        break;
                    case 8:
                        consultarIngresos();
                        break;
                    case 9:
                        mostrarLista("Funciones", CINE.getListaFunciones());
                        break;
                    case 10:
                        mostrarMapaAsientos();
                        break;
                    case 11:
                        mostrarLista("Salas", CINE.getListaSalas());
                        break;
                    case 12:
                        mostrarLista("Ventas", CINE.getListaVentas());
                        break;
                    default:
                        break;
                }
            } catch (RuntimeException e) {
                mostrarError("No se pudo completar la operación: " + e.getMessage());
            }
        }

        JOptionPane.showMessageDialog(null, "¡Hasta pronto!");
    }

    private static void registrarPelicula() {
        String codigo = pedirTexto("Código de la película:");
        if (codigo == null) return;
        String titulo = pedirTexto("Título:");
        if (titulo == null) return;
        Integer duracion = pedirEntero("Duración en minutos:", 1, Integer.MAX_VALUE);
        if (duracion == null) return;
        Genero genero = elegirGenero();
        if (genero == null) return;
        String clasificacion = pedirTexto("Clasificación por edad (por ejemplo, +12):");
        if (clasificacion == null) return;

        boolean registrada = CINE.registrarPelicula(
                new Pelicula(codigo, titulo, duracion, genero, clasificacion)
        );
        mostrarResultado(registrada, "Película registrada.",
                "Ya existe una película con ese código.");
    }

    private static void registrarCliente() {
        String documento = pedirTexto("Documento:");
        if (documento == null) return;
        String nombre = pedirTexto("Nombre completo:");
        if (nombre == null) return;
        String correo = pedirTexto("Correo electrónico:");
        if (correo == null) return;
        String telefono = pedirTexto("Teléfono:");
        if (telefono == null) return;
        String afiliacion = pedirTexto("Código de afiliación:");
        if (afiliacion == null) return;
        Double porcentaje = pedirDecimal("Descuento porcentual (0 a 100):", 0, 100);
        if (porcentaje == null) return;

        Cliente cliente = new Cliente(
                documento, nombre, correo, telefono, afiliacion, porcentaje / 100
        );
        boolean registrado = CINE.registrarPersona(cliente);
        mostrarResultado(registrado, "Cliente registrado.",
                "Ya existe una persona con ese documento.");
    }

    private static void registrarSalaOOperario() {
        String[] opciones = {"Sala", "Operario"};
        int opcion = JOptionPane.showOptionDialog(
                null,
                "¿Qué deseas registrar?",
                "Registro",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == 0) {
            registrarSala();
        } else if (opcion == 1) {
            registrarOperario();
        }
    }

    private static void registrarSala() {
        String codigo = pedirTexto("Código de la sala:");
        if (codigo == null) return;
        String nombre = pedirTexto("Nombre de la sala:");
        if (nombre == null) return;
        Integer filas = pedirEntero("Cantidad de filas (1 a 26):", 1, 26);
        if (filas == null) return;
        Integer columnas = pedirEntero("Asientos por fila:", 1, Integer.MAX_VALUE);
        if (columnas == null) return;
        TipoSala tipo = elegirTipoSala();
        if (tipo == null) return;

        Sala sala = new Sala(
                codigo, nombre, filas * columnas, tipo, filas, columnas
        );
        boolean registrada = CINE.registrarSala(sala);
        mostrarResultado(registrada, "Sala registrada.",
                "Ya existe una sala con ese código.");
    }

    private static void registrarOperario() {
        String documento = pedirTexto("Documento:");
        if (documento == null) return;
        String nombre = pedirTexto("Nombre completo:");
        if (nombre == null) return;
        String correo = pedirTexto("Correo electrónico:");
        if (correo == null) return;
        String telefono = pedirTexto("Teléfono:");
        if (telefono == null) return;
        String codigoEmpleado = pedirTexto("Código de empleado:");
        if (codigoEmpleado == null) return;
        LocalDate contratacion = pedirFecha("Fecha de contratación (AAAA-MM-DD):");
        if (contratacion == null) return;
        Double salario = pedirDecimal("Salario básico:", 0, Double.MAX_VALUE);
        if (salario == null) return;

        Operario operario = new Operario(
                documento, nombre, correo, telefono, codigoEmpleado, contratacion, salario
        );
        boolean registrado = CINE.registrarPersona(operario);
        mostrarResultado(registrado, "Operario registrado.",
                "Ya existe una persona con ese documento.");
    }

    private static void programarFuncion() {
        if (CINE.getListaPeliculas().isEmpty() || CINE.getListaSalas().isEmpty()) {
            mostrarError("Registra al menos una película y una sala antes de programar una función.");
            return;
        }

        mostrarLista("Películas disponibles", CINE.getListaPeliculas());
        String codigoPelicula = pedirTexto("Código de la película:");
        if (codigoPelicula == null) return;
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        if (pelicula == null) {
            mostrarError("No se encontró esa película.");
            return;
        }

        mostrarLista("Salas disponibles", CINE.getListaSalas());
        String codigoSala = pedirTexto("Código de la sala:");
        if (codigoSala == null) return;
        Sala sala = buscarSala(codigoSala);
        if (sala == null) {
            mostrarError("No se encontró esa sala.");
            return;
        }

        String codigo = pedirTexto("Código de la función:");
        if (codigo == null) return;
        if (buscarFuncion(codigo) != null) {
            mostrarError("Ya existe una función con ese código.");
            return;
        }
        LocalDate fecha = pedirFecha("Fecha de la función (AAAA-MM-DD):");
        if (fecha == null) return;
        LocalTime hora = pedirHora("Hora de inicio (HH:MM):");
        if (hora == null) return;
        Double precio = pedirDecimal("Precio base de la boleta:", 0, Double.MAX_VALUE);
        if (precio == null) return;

        Funcion funcion = new Funcion(
                codigo, pelicula, sala, fecha, LocalDateTime.of(fecha, hora), precio
        );
        boolean programada = CINE.programarFuncion(funcion);
        mostrarResultado(programada, "Función programada.",
                "No se pudo programar: la sala no está disponible en ese horario.");
    }

    private static void realizarVenta() {
        if (CINE.getListaFunciones().isEmpty()) {
            mostrarError("No hay funciones programadas.");
            return;
        }
        mostrarLista("Funciones", CINE.getListaFunciones());
        String codigoFuncion = pedirTexto("Código de la función:");
        if (codigoFuncion == null) return;
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (funcion == null) {
            mostrarError("No se encontró esa función.");
            return;
        }

        List<Cliente> clientes = obtenerClientes();
        List<Operario> operarios = obtenerOperarios();
        if (clientes.isEmpty() || operarios.isEmpty()) {
            mostrarError("Se necesita al menos un cliente y un operario registrados.");
            return;
        }
        mostrarLista("Clientes", clientes);
        String documentoCliente = pedirTexto("Documento del cliente:");
        if (documentoCliente == null) return;
        Cliente cliente = buscarCliente(documentoCliente);
        if (cliente == null) {
            mostrarError("No se encontró ese cliente.");
            return;
        }
        mostrarLista("Operarios", operarios);
        String documentoOperario = pedirTexto("Documento del operario:");
        if (documentoOperario == null) return;
        Operario operario = buscarOperario(documentoOperario);
        if (operario == null) {
            mostrarError("No se encontró ese operario.");
            return;
        }

        String codigoVenta = pedirTexto("Código de la venta:");
        if (codigoVenta == null) return;
        if (buscarVenta(codigoVenta) != null) {
            mostrarError("Ya existe una venta con ese código.");
            return;
        }
        Integer cantidad = pedirEntero("Cantidad de boletas:", 1,
                funcion.getTheSala().getCapacidadMaxima());
        if (cantidad == null) return;

        String[] filas = new String[cantidad];
        int[] numeros = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            String fila = pedirTexto("Boleta " + (i + 1)
                    + " - fila (A-" + (char) ('A' + funcion.getTheSala().getFilas() - 1) + "):");
            if (fila == null) return;
            fila = fila.toUpperCase();
            if (fila.length() != 1 || fila.charAt(0) < 'A'
                    || fila.charAt(0) >= 'A' + funcion.getTheSala().getFilas()) {
                mostrarError("La fila indicada no existe en esta sala.");
                return;
            }

            Integer numero = pedirEntero("Número de asiento (1-"
                    + funcion.getTheSala().getColumnas() + "):",
                    1, funcion.getTheSala().getColumnas());
            if (numero == null) return;
            if (asientoVendido(funcion, fila, numero)) {
                mostrarError("Ese asiento ya está vendido para esta función.");
                return;
            }
            for (int j = 0; j < i; j++) {
                if (filas[j].equals(fila) && numeros[j] == numero) {
                    mostrarError("No puedes incluir el mismo asiento dos veces en una venta.");
                    return;
                }
            }
            filas[i] = fila;
            numeros[i] = numero;
        }

        Venta venta = new Venta(
                codigoVenta, LocalDate.now(), cliente, funcion, operario
        );
        for (int i = 0; i < cantidad; i++) {
            if (venta.agregarBoleta(filas[i], numeros[i]) == null) {
                mostrarError("No se pudo generar una de las boletas.");
                return;
            }
        }
        if (!CINE.registrarVenta(venta)) {
            mostrarError("No se pudo registrar la venta.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("Venta registrada.\n");
        for (Boleta boleta : venta.getListaBoletaVenta()) {
            mensaje.append(boleta).append('\n');
        }
        mensaje.append("Total: $").append(String.format("%.2f", venta.calcularTotal()));
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Venta",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static void consultarValorVenta() {
        String codigo = pedirTexto("Código de la venta:");
        if (codigo == null) return;
        Venta venta = buscarVenta(codigo);
        if (venta == null) {
            mostrarError("No se encontró esa venta.");
            return;
        }
        JOptionPane.showMessageDialog(null,
                "Valor total de la venta " + codigo + ": $"
                        + String.format("%.2f", venta.calcularTotal()));
    }

    private static void consultarOcupacion() {
        Funcion funcion = elegirFuncion();
        if (funcion == null) return;
        int vendidos = contarAsientosVendidos(funcion);
        int capacidad = funcion.getTheSala().getCapacidadMaxima();
        double porcentaje = capacidad == 0 ? 0 : vendidos * 100.0 / capacidad;
        JOptionPane.showMessageDialog(null,
                String.format("Ocupación de la función %s: %d/%d asientos (%.2f%%)",
                        funcion.getCodigo(), vendidos, capacidad, porcentaje));
    }

    private static void consultarIngresos() {
        Funcion funcion = elegirFuncion();
        if (funcion == null) return;
        double ingresos = 0;
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getTheFuncion() != null
                    && venta.getTheFuncion().getCodigo().equalsIgnoreCase(funcion.getCodigo())) {
                ingresos += venta.calcularTotal();
            }
        }
        JOptionPane.showMessageDialog(null,
                "Ingresos de la función " + funcion.getCodigo() + ": $"
                        + String.format("%.2f", ingresos));
    }

    private static void mostrarMapaAsientos() {
        Funcion funcion = elegirFuncion();
        if (funcion == null) return;
        Sala sala = funcion.getTheSala();
        StringBuilder mapa = new StringBuilder("Función: ")
                .append(funcion.getCodigo()).append(" - ")
                .append(funcion.getThePelicula().getTitulo())
                .append("\n(L = libre, X = ocupado)\n\n   ");

        for (int columna = 1; columna <= sala.getColumnas(); columna++) {
            mapa.append(String.format("%3d", columna));
        }
        mapa.append('\n');
        for (int fila = 0; fila < sala.getFilas(); fila++) {
            String letra = String.valueOf((char) ('A' + fila));
            mapa.append(' ').append(letra).append(' ');
            for (int columna = 1; columna <= sala.getColumnas(); columna++) {
                mapa.append(asientoVendido(funcion, letra, columna) ? "  X" : "  L");
            }
            mapa.append('\n');
        }
        JOptionPane.showMessageDialog(null, mapa.toString(), "Mapa de asientos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static Genero elegirGenero() {
        Genero[] generos = Genero.values();
        String[] opciones = new String[generos.length];
        for (int i = 0; i < generos.length; i++) {
            opciones[i] = generos[i].name();
        }
        Object seleccion = JOptionPane.showInputDialog(
                null, "Selecciona el género:", "Género",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
        );
        if (seleccion == null) return null;
        return Genero.valueOf(seleccion.toString());
    }

    private static TipoSala elegirTipoSala() {
        TipoSala[] tipos = TipoSala.values();
        String[] opciones = new String[tipos.length];
        for (int i = 0; i < tipos.length; i++) {
            opciones[i] = tipos[i].name();
        }
        Object seleccion = JOptionPane.showInputDialog(
                null, "Selecciona el tipo de sala:", "Tipo de sala",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
        );
        if (seleccion == null) return null;
        return TipoSala.valueOf(seleccion.toString());
    }

    private static Funcion elegirFuncion() {
        if (CINE.getListaFunciones().isEmpty()) {
            mostrarError("No hay funciones programadas.");
            return null;
        }
        mostrarLista("Funciones", CINE.getListaFunciones());
        String codigo = pedirTexto("Código de la función:");
        return codigo == null ? null : buscarFuncion(codigo);
    }

    private static void cargarDatosDePrueba() {
        Sala tradicional = new Sala("S1", "Sala Tradicional", 40,
                TipoSala.TRADICIONAL, 5, 8);
        Sala vip = new Sala("S2", "Sala VIP", 24, TipoSala.VIP, 4, 6);
        Sala sala3d = new Sala("S3", "Sala 3D", 30, TipoSala.SALA_3D, 5, 6);
        CINE.registrarSala(tradicional);
        CINE.registrarSala(vip);
        CINE.registrarSala(sala3d);

        Pelicula aventura = new Pelicula("P1", "Aventura en el Espacio", 120,
                Genero.ACCION, "+12");
        Pelicula comedia = new Pelicula("P2", "Risas en Familia", 95,
                Genero.COMEDIA, "+7");
        CINE.registrarPelicula(aventura);
        CINE.registrarPelicula(comedia);

        CINE.registrarPersona(new Operario("100", "Laura Gómez",
                "laura@cineplus.com", "3001112233", "E001",
                LocalDate.of(2024, 3, 1), 1800000));
        CINE.registrarPersona(new Cliente("200", "Carlos Pérez",
                "carlos@mail.com", "3004445566", "A001", 0.10));
        CINE.registrarPersona(new Cliente("201", "Ana Ruiz",
                "ana@mail.com", "3007778899", "A002", 0));

        LocalDate fecha = LocalDate.now().plusDays(1);
        CINE.programarFuncion(new Funcion("F1", aventura, vip, fecha,
                LocalDateTime.of(fecha, LocalTime.of(18, 0)), 20000));
        CINE.programarFuncion(new Funcion("F2", comedia, tradicional, fecha,
                LocalDateTime.of(fecha, LocalTime.of(20, 30)), 15000));
    }

    private static String pedirTexto(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(null, mensaje);
            if (texto == null) return null;
            texto = texto.trim();
            if (!texto.isEmpty()) return texto;
            mostrarError("El campo no puede quedar vacío.");
        }
    }

    private static Integer pedirEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException ignored) {
                // Se muestra el mismo mensaje para entradas no numéricas y fuera de rango.
            }
            mostrarError("Ingresa un número entero entre " + minimo + " y " + maximo + ".");
        }
    }

    private static Double pedirDecimal(String mensaje, double minimo, double maximo) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                double valor = Double.parseDouble(texto.replace(',', '.'));
                if (Double.isFinite(valor) && valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException ignored) {
                // Se muestra el mismo mensaje para entradas no numéricas y fuera de rango.
            }
            mostrarError("Ingresa un número válido entre " + minimo + " y " + maximo + ".");
        }
    }

    private static LocalDate pedirFecha(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Fecha inválida. Usa el formato AAAA-MM-DD.");
            }
        }
    }

    private static LocalTime pedirHora(String mensaje) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                return LocalTime.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Hora inválida. Usa el formato HH:MM.");
            }
        }
    }

    private static void mostrarLista(String titulo, List<?> elementos) {
        if (elementos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros.",
                    titulo, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder contenido = new StringBuilder();
        for (Object elemento : elementos) {
            contenido.append(elemento).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, contenido.toString(),
                titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarResultado(boolean correcto, String exito, String fallo) {
        JOptionPane.showMessageDialog(null, correcto ? exito : fallo,
                "CinePlus", correcto ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "CinePlus",
                JOptionPane.WARNING_MESSAGE);
    }

    private static Pelicula buscarPelicula(String codigo) {
        for (Pelicula pelicula : CINE.getListaPeliculas()) {
            if (pelicula.getCodigo().equalsIgnoreCase(codigo)) return pelicula;
        }
        return null;
    }

    private static Sala buscarSala(String codigo) {
        for (Sala sala : CINE.getListaSalas()) {
            if (sala.getCodigo().equalsIgnoreCase(codigo)) return sala;
        }
        return null;
    }

    private static Funcion buscarFuncion(String codigo) {
        for (Funcion funcion : CINE.getListaFunciones()) {
            if (funcion.getCodigo().equalsIgnoreCase(codigo)) return funcion;
        }
        return null;
    }

    private static Venta buscarVenta(String codigo) {
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getCodigo().equalsIgnoreCase(codigo)) return venta;
        }
        return null;
    }

    private static Cliente buscarCliente(String documento) {
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Cliente
                    && persona.getDocumento().equalsIgnoreCase(documento)) {
                return (Cliente) persona;
            }
        }
        return null;
    }

    private static Operario buscarOperario(String documento) {
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Operario
                    && persona.getDocumento().equalsIgnoreCase(documento)) {
                return (Operario) persona;
            }
        }
        return null;
    }

    private static List<Cliente> obtenerClientes() {
        java.util.ArrayList<Cliente> clientes = new java.util.ArrayList<>();
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Cliente) clientes.add((Cliente) persona);
        }
        return clientes;
    }

    private static List<Operario> obtenerOperarios() {
        java.util.ArrayList<Operario> operarios = new java.util.ArrayList<>();
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Operario) operarios.add((Operario) persona);
        }
        return operarios;
    }

    private static boolean asientoVendido(Funcion funcion, String fila, int numero) {
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getTheFuncion() == null
                    || !venta.getTheFuncion().getCodigo().equalsIgnoreCase(funcion.getCodigo())) {
                continue;
            }
            for (Boleta boleta : venta.getListaBoletaVenta()) {
                if (boleta.getFila().equalsIgnoreCase(fila)
                        && boleta.getNumeroAsiento() == numero) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int contarAsientosVendidos(Funcion funcion) {
        int vendidos = 0;
        for (int fila = 0; fila < funcion.getTheSala().getFilas(); fila++) {
            for (int numero = 1; numero <= funcion.getTheSala().getColumnas(); numero++) {
                if (asientoVendido(funcion, String.valueOf((char) ('A' + fila)), numero)) {
                    vendidos++;
                }
            }
        }
        return vendidos;
    }
}
