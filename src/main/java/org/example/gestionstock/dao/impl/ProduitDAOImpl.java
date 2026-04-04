package org.example.gestionstock.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.gestionstock.dao.ProduitDAO;
import org.example.gestionstock.model.Produit;
import org.example.gestionstock.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class ProduitDAOImpl implements ProduitDAO {

    @Override
    public void create(Produit p) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Produit findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Produit> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p JOIN FETCH p.categorie ORDER BY p.nom",
                            Produit.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public void update(Produit p) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
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
            Produit p = em.find(Produit.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Produit findByReference(String reference) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p WHERE p.reference = :ref", Produit.class)
                    .setParameter("ref", reference)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Produit> findByCategorie(Long categorieId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p WHERE p.categorie.id = :catId ORDER BY p.nom",
                            Produit.class)
                    .setParameter("catId", categorieId)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Produit> findByNom(String nom) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(:nom) ORDER BY p.nom",
                            Produit.class)
                    .setParameter("nom", "%" + nom + "%")
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public boolean existsByReference(String reference) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Produit p WHERE p.reference = :ref", Long.class)
                    .setParameter("ref", reference)
                    .getSingleResult();
            return count > 0;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}