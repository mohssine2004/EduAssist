package org.example.gestionstock.dao.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.gestionstock.dao.UtilisateurDAO;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public void create(Utilisateur u) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Utilisateur findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Utilisateur.class, id);
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Utilisateur u ORDER BY u.nom", Utilisateur.class)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public void update(Utilisateur u) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(u);
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
            Utilisateur u = em.find(Utilisateur.class, id);
            if (u != null) em.remove(u);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public Utilisateur findByEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public List<Utilisateur> findByRole(Role role) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.role = :role ORDER BY u.nom", Utilisateur.class)
                    .setParameter("role", role)
                    .getResultList();
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}