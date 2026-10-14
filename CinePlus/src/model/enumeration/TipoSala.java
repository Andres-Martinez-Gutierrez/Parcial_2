package model.enumeration;

/** Tipos de sala con su recargo sobre el precio base de la función. */
public enum TipoSala {
    TRADICIONAL(0.0),
    VIP(0.30),
    SALA_3D(0.20);

    private final double recargo;

    TipoSala(double recargo) {
        this.recargo = recargo;
    }

    /** Recargo como fracción (0.30 = 30%). */
    public double getRecargo() {
        return recargo;
    }
}
