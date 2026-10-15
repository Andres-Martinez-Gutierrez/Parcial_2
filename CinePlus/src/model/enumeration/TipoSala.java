package model.enumeration;

public enum TipoSala {
    TRADICIONAL(0.0),
    VIP(0.30),
    SALA_3D(0.20);

    private final double recargo;

    TipoSala(double recargo) {
        this.recargo = recargo;
    }

    public double getRecargo() {
        return recargo;
    }
}
