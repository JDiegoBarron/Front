package org.modelo;

public class RachaModel {

    private final int rachaActual;    // días consecutivos
    private final int mejorRacha;     // máximo histórico
    private final int monedasGanadas; // monedas de este login (0 si ya registró hoy)
    private final int monedasTotal;   // balance actual
    private final boolean loginNuevo; // true si es el primer login del día

    public RachaModel(int rachaActual, int mejorRacha,
                      int monedasGanadas, int monedasTotal, boolean loginNuevo) {
        this.rachaActual    = rachaActual;
        this.mejorRacha     = mejorRacha;
        this.monedasGanadas = monedasGanadas;
        this.monedasTotal   = monedasTotal;
        this.loginNuevo     = loginNuevo;
    }

    public int     getRachaActual()    { return rachaActual; }
    public int     getMejorRacha()     { return mejorRacha; }
    public int     getMonedasGanadas() { return monedasGanadas; }
    public int     getMonedasTotal()   { return monedasTotal; }
    public boolean isLoginNuevo()      { return loginNuevo; }
}
