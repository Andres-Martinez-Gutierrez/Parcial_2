import model.*;
import model.enumeration.Genero;
import model.enumeration.TipoSala;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Cine CINE =
            new Cine("CinePlus", "900123456-7", "Armenia, Quindío");

    public static void main(String[] args) {
        cargarDatosDePrueba();

        boolean salir = false;
        while (!salir) {
            String opcion = mostrarMenu();
            if (opcion == null || opcion.equals("Salir")) {
                salir = true;
                continue;
            }

            try {
                switch (opcion) {
                    case "Registrar película": registrarPelicula(); break;
                    case "Registrar cliente": registrarCliente(); break;
                    case "Programar función": programarFuncion(); break;
                    case "Realizar venta de boletas": realizarVenta(); break;
                    case "Consultar total de una venta": calcularTotalVenta(); break;
                    case "Consultar ocupación de una función": consultarOcupacion(); break;
                    default: mostrarError("Opción no válida.");
                }
            } catch (RuntimeException e) {
                mostrarError("No se pudo completar la operación.");
            }
        }

        JOptionPane.showMessageDialog(null, "Gracias por utilizar CinePlus.");
    }

    /** Muestra el menú principal. */
    private static String mostrarMenu() {
        String[] opciones = {
                "Registrar película",
                "Registrar cliente",
                "Programar función",
                "Realizar venta de boletas",
                "Consultar total de una venta",
                "Consultar ocupación de una función",
                "Salir"
        };

        return (String) JOptionPane.showInputDialog(
                null, "Seleccione una operación:", "CinePlus - Menú principal",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
    }

    /** Registra una película. */
    private static void registrarPelicula() {
        String codigo = pedirTexto("Código de la película:");
        if (codigo == null) return;
        String titulo = pedirTexto("Título:");
        if (titulo == null) return;
        Integer duracion = pedirEntero("Duración en minutos:", Integer.MAX_VALUE);
        if (duracion == null) return;
        Genero genero = elegirGenero();
        if (genero == null) return;
        String clasificacion = pedirTexto("Clasificación por edad (ejemplo: +12):");
        if (clasificacion == null) return;

        boolean registrada = CINE.registrarPelicula(
                new Pelicula(codigo, titulo, duracion, genero, clasificacion));

        mostrarResultado(registrada, "Película registrada correctamente.",
                "Ya existe una película con ese código.");
    }

    /** Registra un cliente. */
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
                documento, nombre, correo, telefono, afiliacion, porcentaje / 100);

        boolean registrado = CINE.registrarPersona(cliente);
        mostrarResultado(registrado, "Cliente registrado correctamente.",
                "Ya existe una persona con ese documento.");
    }

    /** Programa una función asociando película y sala. */
    private static void programarFuncion() {
        if (CINE.getListaPeliculas().isEmpty()) {
            mostrarError("No hay películas registradas.");
            return;
        }
        if (CINE.getListaSalas().isEmpty()) {
            mostrarError("No hay salas registradas.");
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

        LocalDate fecha = pedirFecha();
        if (fecha == null) return;
        LocalTime hora = pedirHora();
        if (hora == null) return;
        Double precio = pedirDecimal("Precio base de la boleta:", 0.01, Double.MAX_VALUE);
        if (precio == null) return;

        Funcion funcion = new Funcion(
                codigo, pelicula, sala, fecha, LocalDateTime.of(fecha, hora), precio);

        boolean programada = CINE.programarFuncion(funcion);
        mostrarResultado(programada, "Función programada correctamente.",
                "No se pudo programar la función. La sala puede estar ocupada en ese horario.");
    }

    /** Realiza una venta de una o varias boletas para una misma función. */
    private static void realizarVenta() {
        if (CINE.getListaFunciones().isEmpty()) {
            mostrarError("No hay funciones programadas.");
            return;
        }

        List<Cliente> clientes = obtenerClientes();
        List<Operario> operarios = obtenerOperarios();
        if (clientes.isEmpty()) {
            mostrarError("No hay clientes registrados.");
            return;
        }
        if (operarios.isEmpty()) {
            mostrarError("No hay operarios registrados.");
            return;
        }

        mostrarLista("Funciones disponibles", CINE.getListaFunciones());
        String codigoFuncion = pedirTexto("Código de la función:");
        if (codigoFuncion == null) return;
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (funcion == null) {
            mostrarError("No se encontró esa función.");
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

        int capacidad = funcion.getTheSala().getCapacidadMaxima();
        int vendidos = contarAsientosVendidos(funcion);
        int disponibles = capacidad - vendidos;
        if (disponibles <= 0) {
            mostrarError("No hay asientos disponibles para esta función.");
            return;
        }

        Integer cantidad = pedirEntero(
                "Asientos disponibles: " + disponibles +
                        "\n¿Cuántas boletas desea comprar?", disponibles);
        if (cantidad == null) return;

        Venta venta = new Venta(codigoVenta, LocalDate.now(), cliente, funcion, operario);
        int agregadas = 0;

        while (agregadas < cantidad) {
            String fila = pedirTexto(
                    "Boleta " + (agregadas + 1) + " - fila (A-" +
                            (char) ('A' + funcion.getTheSala().getFilas() - 1) + "):"
            );
            if (fila == null) return;
            fila = fila.toUpperCase();

            if (!filaValida(funcion.getTheSala(), fila)) {
                mostrarError("La fila indicada no existe en esta sala.");
                continue;
            }

            Integer numero = pedirEntero(
                    "Número de asiento (1-" + funcion.getTheSala().getColumnas() + "):",
                    funcion.getTheSala().getColumnas());
            if (numero == null) return;

            if (asientoVendido(funcion, fila, numero)) {
                mostrarError("Ese asiento ya está vendido para esta función.");
                continue;
            }
            if (asientoYaAgregado(venta, fila, numero)) {
                mostrarError("Ese asiento ya fue agregado a esta venta.");
                continue;
            }

            Boleta boleta = venta.agregarBoleta(fila, numero);
            if (boleta == null) {
                mostrarError("No se pudo agregar la boleta.");
                continue;
            }
            agregadas++;
        }

        if (!CINE.registrarVenta(venta)) {
            mostrarError("No se pudo registrar la venta.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("Venta registrada correctamente.\n\n");
        for (Boleta boleta : venta.getListaBoletaVenta()) {
            mensaje.append(boleta).append("\n");
        }
        mensaje.append("\nTotal de la venta: $")
                .append(String.format("%.2f", venta.calcularTotal()));

        JOptionPane.showMessageDialog(null, mensaje.toString(), "Venta registrada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Consulta el valor total de una venta mediante su código. */
    private static void calcularTotalVenta() {
        String codigo = pedirTexto("Código de la venta:");
        if (codigo == null) return;
        Venta venta = buscarVenta(codigo);
        if (venta == null) {
            mostrarError("No se encontró esa venta.");
            return;
        }

        JOptionPane.showMessageDialog(null,
                "Venta: " + venta.getCodigo() + "\nTotal: $" +
                        String.format("%.2f", venta.calcularTotal()),
                "Total de la venta", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Calcula el porcentaje de ocupación de una función. */
    private static void consultarOcupacion() {
        if (CINE.getListaFunciones().isEmpty()) {
            mostrarError("No hay funciones programadas.");
            return;
        }

        mostrarLista("Funciones disponibles", CINE.getListaFunciones());
        String codigo = pedirTexto("Código de la función:");
        if (codigo == null) return;
        Funcion funcion = buscarFuncion(codigo);
        if (funcion == null) {
            mostrarError("No se encontró esa función.");
            return;
        }

        int vendidos = contarAsientosVendidos(funcion);
        int capacidad = funcion.getTheSala().getCapacidadMaxima();
        double porcentaje = vendidos * 100.0 / capacidad;

        JOptionPane.showMessageDialog(null,
                String.format("Función: %s\nAsientos vendidos: %d\nCapacidad: %d\nOcupación: %.2f%%",
                        funcion.getCodigo(), vendidos, capacidad, porcentaje),
                "Ocupación de la función", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Permite seleccionar el género. */
    private static Genero elegirGenero() {
        Genero[] generos = Genero.values();
        String[] opciones = new String[generos.length];
        for (int i = 0; i < generos.length; i++) opciones[i] = generos[i].name();

        Object seleccion = JOptionPane.showInputDialog(null, "Seleccione el género:", "Género",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) return null;
        return Genero.valueOf(seleccion.toString());
    }

    /** Carga los datos de prueba usados para demostrar el sistema. */
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

    /** Solicita un texto obligatorio. */
    private static String pedirTexto(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(null, mensaje);
            if (texto == null) return null;
            texto = texto.trim();
            if (!texto.isEmpty()) return texto;
            mostrarError("El campo no puede quedar vacío.");
        }
    }

    /** Solicita un entero dentro de un rango. */
    private static Integer pedirEntero(String mensaje, int maximo) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= 1 && valor <= maximo) return valor;
            } catch (NumberFormatException ignored) {
            }
            mostrarError("Ingresa un número entero entre " + 1 + " y " + maximo + ".");
        }
    }

    /** Solicita un decimal dentro de un rango. */
    private static Double pedirDecimal(String mensaje, double minimo, double maximo) {
        while (true) {
            String texto = pedirTexto(mensaje);
            if (texto == null) return null;
            try {
                double valor = Double.parseDouble(texto.replace(',', '.'));
                if (Double.isFinite(valor) && valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException ignored) {
            }
            mostrarError("Ingresa un número válido entre " + minimo + " y " + maximo + ".");
        }
    }

    /** Solicita una fecha. */
    private static LocalDate pedirFecha() {
        while (true) {
            String texto = pedirTexto("Fecha de la función (AAAA-MM-DD):");
            if (texto == null) return null;
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Fecha inválida. Usa el formato AAAA-MM-DD.");
            }
        }
    }

    /** Solicita una hora. */
    private static LocalTime pedirHora() {
        while (true) {
            String texto = pedirTexto("Hora de inicio (HH:MM):");
            if (texto == null) return null;
            try {
                return LocalTime.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Hora inválida. Usa el formato HH:MM.");
            }
        }
    }

    /** Muestra una lista de objetos. */
    private static void mostrarLista(String titulo, List<?> elementos) {
        if (elementos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros.", titulo,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder contenido = new StringBuilder();
        for (Object elemento : elementos) {
            contenido.append(formatearElemento(elemento)).append('\n');
        }

        JOptionPane.showMessageDialog(null, contenido.toString(), titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Formatea los objetos principales para mostrarlos. */
    private static String formatearElemento(Object elemento) {
        if (elemento instanceof Pelicula pelicula) {
            return pelicula.getCodigo() + " | " + pelicula.getTitulo() + " | "
                    + pelicula.getDuracionMinutos() + " min | " + pelicula.getGenero()
                    + " | " + pelicula.getClasificacionEdad();
        }
        if (elemento instanceof Sala sala) {
            return sala.getCodigo() + " | " + sala.getNombre() + " | "
                    + sala.getTipo() + " | Capacidad: " + sala.getCapacidadMaxima();
        }
        if (elemento instanceof Funcion funcion) {
            return funcion.getCodigo() + " | " + funcion.getThePelicula().getTitulo()
                    + " | " + funcion.getFecha() + " " + funcion.getHoraInicio().toLocalTime()
                    + " | Sala: " + funcion.getTheSala().getNombre();
        }
        if (elemento instanceof Cliente cliente) {
            return cliente.getDocumento() + " | " + cliente.getNombreCompleto();
        }
        if (elemento instanceof Operario operario) {
            return operario.getDocumento() + " | " + operario.getNombreCompleto();
        }
        if (elemento instanceof Venta venta) {
            return venta.getCodigo() + " | " + venta.getFecha() + " | Boletas: "
                    + venta.getListaBoletaVenta().size() + " | Total: $"
                    + String.format("%.2f", venta.calcularTotal());
        }
        return String.valueOf(elemento);
    }

    /** Muestra el resultado de una operación. */
    private static void mostrarResultado(boolean correcto, String exito, String fallo) {
        JOptionPane.showMessageDialog(null, correcto ? exito : fallo, "CinePlus",
                correcto ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    /** Muestra un mensaje de error. */
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "CinePlus", JOptionPane.WARNING_MESSAGE);
    }

    /** Busca una película por código. */
    private static Pelicula buscarPelicula(String codigo) {
        for (Pelicula pelicula : CINE.getListaPeliculas()) {
            if (pelicula.getCodigo().equalsIgnoreCase(codigo)) return pelicula;
        }
        return null;
    }

    /** Busca una sala por código. */
    private static Sala buscarSala(String codigo) {
        for (Sala sala : CINE.getListaSalas()) {
            if (sala.getCodigo().equalsIgnoreCase(codigo)) return sala;
        }
        return null;
    }

    /** Busca una función por código. */
    private static Funcion buscarFuncion(String codigo) {
        for (Funcion funcion : CINE.getListaFunciones()) {
            if (funcion.getCodigo().equalsIgnoreCase(codigo)) return funcion;
        }
        return null;
    }

    /** Busca una venta por código. */
    private static Venta buscarVenta(String codigo) {
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getCodigo().equalsIgnoreCase(codigo)) return venta;
        }
        return null;
    }

    /** Busca un cliente por documento. */
    private static Cliente buscarCliente(String documento) {
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Cliente
                    && persona.getDocumento().equalsIgnoreCase(documento)) {
                return (Cliente) persona;
            }
        }
        return null;
    }

    /** Busca un operario por documento. */
    private static Operario buscarOperario(String documento) {
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Operario
                    && persona.getDocumento().equalsIgnoreCase(documento)) {
                return (Operario) persona;
            }
        }
        return null;
    }

    /** Obtiene todos los clientes registrados. */
    private static List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Cliente) clientes.add((Cliente) persona);
        }
        return clientes;
    }

    /** Obtiene todos los operarios registrados. */
    private static List<Operario> obtenerOperarios() {
        List<Operario> operarios = new ArrayList<>();
        for (Persona persona : CINE.getListaPersona()) {
            if (persona instanceof Operario) operarios.add((Operario) persona);
        }
        return operarios;
    }

    /** Verifica si una fila existe en una sala. */
    private static boolean filaValida(Sala sala, String fila) {
        if (fila == null || fila.length() != 1) return false;
        char letra = fila.charAt(0);
        return letra >= 'A' && letra < 'A' + sala.getFilas();
    }

    /** Verifica si un asiento ya fue vendido para una función. */
    private static boolean asientoVendido(Funcion funcion, String fila, int numero) {
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getTheFuncion() == null
                    || !venta.getTheFuncion().getCodigo()
                    .equalsIgnoreCase(funcion.getCodigo())) {
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

    /** Verifica si un asiento ya fue agregado a la venta actual. */
    private static boolean asientoYaAgregado(Venta venta, String fila, int numero) {
        for (Boleta boleta : venta.getListaBoletaVenta()) {
            if (boleta.getFila().equalsIgnoreCase(fila)
                    && boleta.getNumeroAsiento() == numero) {
                return true;
            }
        }
        return false;
    }

    /** Cuenta los asientos vendidos para una función. */
    private static int contarAsientosVendidos(Funcion funcion) {
        int vendidos = 0;
        for (Venta venta : CINE.getListaVentas()) {
            if (venta.getTheFuncion() != null
                    && venta.getTheFuncion().getCodigo()
                    .equalsIgnoreCase(funcion.getCodigo())) {
                vendidos += venta.getListaBoletaVenta().size();
            }
        }
        return vendidos;
    }
}
