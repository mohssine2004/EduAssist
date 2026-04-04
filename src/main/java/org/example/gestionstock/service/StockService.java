package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.dao.StockDAO;
import org.example.gestionstock.model.Stock;

import java.util.List;

@Named
@ApplicationScoped
public class StockService {

    @Inject
    private StockDAO stockDAO;

    /**
     * Met à jour la quantité d'un stock.
     * Refuse si la quantité est négative.
     */
    public void mettreAJour(Long stockId, Integer nouvelleQuantite) {
        if (nouvelleQuantite == null || nouvelleQuantite < 0)
            throw new IllegalArgumentException("La quantité ne peut pas être négative.");

        Stock stock = stockDAO.findById(stockId);
        if (stock == null)
            throw new IllegalArgumentException("Stock introuvable.");

        stock.setQuantite(nouvelleQuantite);
        stockDAO.update(stock);
    }

    /**
     * Met à jour le seuil minimum d'alerte d'un stock.
     */
    public void modifierSeuil(Long stockId, Integer nouveauSeuil) {
        if (nouveauSeuil == null || nouveauSeuil < 0)
            throw new IllegalArgumentException("Le seuil doit être positif.");

        Stock stock = stockDAO.findById(stockId);
        if (stock == null)
            throw new IllegalArgumentException("Stock introuvable.");

        stock.setSeuilMinimum(nouveauSeuil);
        stockDAO.update(stock);
    }

    /**
     * Ajoute une quantité au stock existant (réapprovisionnement).
     */
    public void approvisionner(Long produitId, Integer quantiteAjouter) {
        if (quantiteAjouter == null || quantiteAjouter <= 0)
            throw new IllegalArgumentException("La quantité à ajouter doit être positive.");

        Stock stock = stockDAO.findByProduit(produitId);
        if (stock == null)
            throw new IllegalArgumentException("Stock introuvable pour ce produit.");

        stock.setQuantite(stock.getQuantite() + quantiteAjouter);
        stockDAO.update(stock);
    }

    /**
     * Retire une quantité du stock (sortie de stock).
     * Refuse si la quantité disponible est insuffisante.
     */
    public void retirerStock(Long produitId, Integer quantiteRetirer) {
        if (quantiteRetirer == null || quantiteRetirer <= 0)
            throw new IllegalArgumentException("La quantité à retirer doit être positive.");

        Stock stock = stockDAO.findByProduit(produitId);
        if (stock == null)
            throw new IllegalArgumentException("Stock introuvable pour ce produit.");
        if (stock.getQuantite() < quantiteRetirer)
            throw new IllegalStateException(
                    "Stock insuffisant. Disponible : " + stock.getQuantite()
                            + ", demandé : " + quantiteRetirer);

        stock.setQuantite(stock.getQuantite() - quantiteRetirer);
        stockDAO.update(stock);
    }

    public Stock findByProduit(Long produitId) {
        return stockDAO.findByProduit(produitId);
    }

    public List<Stock> findAll() {
        return stockDAO.findAll();
    }

    public List<Stock> findStocksFaibles() {
        return stockDAO.findStocksFaibles();
    }

    public List<Stock> findEnRupture() {
        return stockDAO.findEnRupture();
    }

    /**
     * Retourne le nombre total de produits en alerte (faibles + ruptures).
     * Utilisé par le dashboard.
     */
    public int getNbAlertes() {
        return stockDAO.findStocksFaibles().size() + stockDAO.findEnRupture().size();
    }
}