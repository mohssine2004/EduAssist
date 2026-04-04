package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.dao.CategorieDAO;
import org.example.gestionstock.dao.ProduitDAO;
import org.example.gestionstock.model.Categorie;

import java.util.List;

@Named
@ApplicationScoped
public class CategorieService {

    @Inject
    private CategorieDAO categorieDAO;

    @Inject
    private ProduitDAO produitDAO;

    /**
     * Crée une nouvelle catégorie.
     */
    public void creer(Categorie categorie) {
        if (categorie.getNom() == null || categorie.getNom().isEmpty())
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire.");
        if (categorieDAO.existsByNom(categorie.getNom().trim()))
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà.");

        categorie.setNom(categorie.getNom().trim());
        categorieDAO.create(categorie);
    }

    /**
     * Modifie une catégorie existante.
     */
    public void modifier(Categorie categorie) {
        if (categorie.getId() == null)
            throw new IllegalArgumentException("Catégorie introuvable.");
        if (categorie.getNom() == null || categorie.getNom().isEmpty())
            throw new IllegalArgumentException("Le nom est obligatoire.");

        categorie.setNom(categorie.getNom().trim());
        categorieDAO.update(categorie);
    }

    /**
     * Supprime une catégorie — refuse si elle contient des produits.
     */
    public void supprimer(Long id) {
        Categorie categorie = categorieDAO.findById(id);
        if (categorie == null)
            throw new IllegalArgumentException("Catégorie introuvable.");

        // Vérifie qu'aucun produit n'est lié à cette catégorie
        if (!produitDAO.findByCategorie(id).isEmpty())
            throw new IllegalStateException(
                    "Impossible de supprimer : cette catégorie contient des produits.");

        categorieDAO.delete(id);
    }

    public Categorie findById(Long id) {
        return categorieDAO.findById(id);
    }

    public Categorie findByNom(String nom) {
        return categorieDAO.findByNom(nom);
    }

    public List<Categorie> findAll() {
        return categorieDAO.findAll();
    }
}