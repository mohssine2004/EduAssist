package org.example.gestionstock.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.gestionstock.dao.CategorieDAO;
import org.example.gestionstock.model.Categorie;
import org.example.gestionstock.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class CategorieDAOImpl implements CategorieDAO {

    @Override
    public void create(Categorie c) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Categorie findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Categorie> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categorie c ORDER BY c.nom", Categorie.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public void update(Categorie c) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Categorie c = em.find(Categorie.class, id);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Categorie findByNom(String nom) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Categorie c WHERE c.nom = :nom", Categorie.class)
                    .setParameter("nom", nom)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public boolean existsByNom(String nom) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Categorie c WHERE c.nom = :nom", Long.class)
                    .setParameter("nom", nom)
                    .getSingleResult();
            return count > 0;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}