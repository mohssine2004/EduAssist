package com.eduassist.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Generic DAO — all other DAOs extend this.
 * T  = entity type (e.g. User)
 * ID = primary key type (e.g. Integer)
 */
public abstract class GenericDAO<T, ID> {

    // WildFly injects the EntityManager from persistence.xml
    @PersistenceContext(unitName = "EduAssistPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // ── CREATE ────────────────────────────────────────────────
    public void save(T entity) {
        em.persist(entity);
    }

    // ── READ ──────────────────────────────────────────────────
    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
        return query.getResultList();
    }

    // ── UPDATE ────────────────────────────────────────────────
    public T update(T entity) {
        return em.merge(entity);
    }

    // ── DELETE ────────────────────────────────────────────────
    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public void deleteEntity(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}