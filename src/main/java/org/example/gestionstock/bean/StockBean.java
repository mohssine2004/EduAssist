package org.example.gestionstock.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.model.Stock;
import org.example.gestionstock.service.StockService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class StockBean implements Serializable {

    @Inject
    private StockService stockService;

    private List<Stock> stocks;
    private Stock       stockSelectionne;
    private Integer     nouvelleQuantite;
    private Integer     quantiteOperation;

    @PostConstruct
    public void init() {
        charger();
    }

    private void charger() {
        stocks = stockService.findAll();
    }

    // ── Actions ────────────────────────────────────────────

    /** Prépare la mise à jour directe du stock */
    public void prepareMiseAJour(Stock s) {
        this.stockSelectionne  = s;
        this.nouvelleQuantite  = s.getQuantite();
    }

    /** Met à jour directement la quantité */
    public void mettreAJour() {
        try {
            stockService.mettreAJour(stockSelectionne.getId(), nouvelleQuantite);
            addMessage(FacesMessage.SEVERITY_INFO, "Stock mis à jour avec succès.");
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /** Approvisionne (ajoute une quantité) */
    public void approvisionner(Long produitId) {
        try {
            stockService.approvisionner(produitId, quantiteOperation);
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Approvisionnement effectué : +" + quantiteOperation + " unités.");
            quantiteOperation = null;
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /** Retire une quantité du stock */
    public void retirerStock(Long produitId) {
        try {
            stockService.retirerStock(produitId, quantiteOperation);
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Sortie de stock effectuée : -" + quantiteOperation + " unités.");
            quantiteOperation = null;
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /** Stocks en alerte (faibles + ruptures) pour le badge */
    public int getNbAlertes() {
        return stockService.getNbAlertes();
    }

    // ── Helper ─────────────────────────────────────────────
    private void addMessage(FacesMessage.Severity severity, String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, msg, null));
    }

    // ── Getters & Setters ──────────────────────────────────
    public List<Stock> getStocks() { return stocks; }

    public Stock getStockSelectionne() { return stockSelectionne; }
    public void setStockSelectionne(Stock s) { this.stockSelectionne = s; }

    public Integer getNouvelleQuantite() { return nouvelleQuantite; }
    public void setNouvelleQuantite(Integer q) { this.nouvelleQuantite = q; }

    public Integer getQuantiteOperation() { return quantiteOperation; }
    public void setQuantiteOperation(Integer q) { this.quantiteOperation = q; }
}