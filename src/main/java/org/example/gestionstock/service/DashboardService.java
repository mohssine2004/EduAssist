package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.dao.CategorieDAO;
import org.example.gestionstock.dao.ProduitDAO;
import org.example.gestionstock.dao.StockDAO;
import org.example.gestionstock.dao.UtilisateurDAO;
import org.example.gestionstock.model.Stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ApplicationScoped
public class DashboardService {

    @Inject private ProduitDAO    produitDAO;
    @Inject private CategorieDAO  categorieDAO;
    @Inject private StockDAO      stockDAO;
    @Inject private UtilisateurDAO utilisateurDAO;

    /** Nombre total de produits */
    public long getNbProduits() {
        return produitDAO.findAll().size();
    }

    /** Nombre total de catégories */
    public long getNbCategories() {
        return categorieDAO.findAll().size();
    }

    /** Nombre total d'utilisateurs */
    public long getNbUtilisateurs() {
        return utilisateurDAO.findAll().size();
    }

    /** Nombre de produits en rupture de stock */
    public long getNbRuptures() {
        return stockDAO.findEnRupture().size();
    }

    /** Nombre de produits en stock faible */
    public long getNbStocksFaibles() {
        return stockDAO.findStocksFaibles().size();
    }

    /** Liste des produits en rupture (pour affichage dashboard) */
    public List<Stock> getProduitsEnRupture() {
        return stockDAO.findEnRupture();
    }

    /** Liste des produits en stock faible (pour affichage dashboard) */
    public List<Stock> getStocksFaibles() {
        return stockDAO.findStocksFaibles();
    }

    /**
     * Résumé complet pour le dashboard.
     * Retourne une Map avec toutes les statistiques.
     */
    public Map<String, Object> getResume() {
        Map<String, Object> resume = new HashMap<>();
        resume.put("nbProduits",      getNbProduits());
        resume.put("nbCategories",    getNbCategories());
        resume.put("nbUtilisateurs",  getNbUtilisateurs());
        resume.put("nbRuptures",      getNbRuptures());
        resume.put("nbStocksFaibles", getNbStocksFaibles());
        resume.put("nbAlertes",       getNbRuptures() + getNbStocksFaibles());
        return resume;
    }
}