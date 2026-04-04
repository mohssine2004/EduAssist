package org.example.gestionstock.exception;

/**
 * Levée quand on essaie de retirer plus de stock que disponible.
 */
public class StockInsuffisantException extends RuntimeException {

    private final int disponible;
    private final int demande;

    public StockInsuffisantException(int disponible, int demande) {
        super("Stock insuffisant. Disponible : " + disponible + ", demandé : " + demande);
        this.disponible = disponible;
        this.demande    = demande;
    }

    public int getDisponible() { return disponible; }
    public int getDemande()    { return demande; }
}