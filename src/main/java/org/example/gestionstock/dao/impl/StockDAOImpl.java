package org.example.gestionstock.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.gestionstock.dao.StockDAO;
import org.example.gestionstock.model.Stock;
import org.example.gestionstock.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class StockDAOImpl implements StockDAO {

    @Override
    public void create(Stock s) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Stock findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Stock> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Stock s JOIN FETCH s.produit ORDER BY s.produit.nom",
                            Stock.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public void update(Stock s) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(s);
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
            Stock s = em.find(Stock.class, id);
            if (s != null) em.remove(s);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Stock findByProduit(Long produitId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Stock s WHERE s.produit.id = :produitId", Stock.class)
                    .setParameter("produitId", produitId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Stock> findStocksFaibles() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Stock s WHERE s.quantite > 0 AND s.quantite < s.seuilMinimum",
                            Stock.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Stock> findEnRupture() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Stock s WHERE s.quantite = 0",
                            Stock.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}