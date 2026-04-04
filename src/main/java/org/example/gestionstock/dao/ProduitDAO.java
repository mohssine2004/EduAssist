package org.example.gestionstock.dao;

import org.example.gestionstock.model.Produit;
import java.util.List;

public interface ProduitDAO extends GenericDAO<Produit, Long> {
    Produit findByReference(String reference);
    List<Produit> findByCategorie(Long categorieId);
    List<Produit> findByNom(String nom);
    boolean existsByReference(String reference);
}