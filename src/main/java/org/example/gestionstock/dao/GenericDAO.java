package org.example.gestionstock.dao;

import java.util.List;

/**
 * Interface générique CRUD — toutes les DAOs héritent de celle-ci.
 * T = type de l'entité (ex: Produit)
 * ID = type de la clé primaire (ex: Long)
 */
public interface GenericDAO<T, ID> {
    void create(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(ID id);
}