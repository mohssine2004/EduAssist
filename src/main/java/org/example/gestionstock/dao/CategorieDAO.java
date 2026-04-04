package org.example.gestionstock.dao;

import org.example.gestionstock.model.Categorie;
import java.util.List;

public interface CategorieDAO extends GenericDAO<Categorie, Long> {
    Categorie findByNom(String nom);
    boolean existsByNom(String nom);
}