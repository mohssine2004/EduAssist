package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.example.gestionstock.model.Categorie;
import org.example.gestionstock.model.Produit;
import org.example.gestionstock.model.Stock;
import org.example.gestionstock.util.HibernateUtil;

import java.util.List;

@Named
@ApplicationScoped
public class ProduitService {

    /**
     * Crée un nouveau produit et initialise son stock dans la même transaction.
     */
    public void creer(Produit produit, Integer quantiteInitiale, Integer seuilMinimum) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // Validations
            if (produit.getNom() == null || produit.getNom().isEmpty())
                throw new IllegalArgumentException("Le nom du produit est obligatoire.");
            if (produit.getReference() == null || produit.getReference().isEmpty())
                throw new IllegalArgumentException("La référence est obligatoire.");
            if (produit.getPrixUnitaire() == null || produit.getPrixUnitaire() < 0)
                throw new IllegalArgumentException("Le prix doit être positif.");
            if (produit.getCategorie() == null || produit.getCategorie().getId() == null)
                throw new IllegalArgumentException("La catégorie est obligatoire.");

            // Vérifie que la référence n'existe pas déjà
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Produit p WHERE p.reference = :ref", Long.class)
                    .setParameter("ref", produit.getReference())
                    .getSingleResult();
            if (count > 0)
                throw new IllegalArgumentException("Cette référence existe déjà.");

            em.getTransaction().begin();

            // Reattache la catégorie dans ce contexte de persistance
            Categorie categorie = em.find(Categorie.class, produit.getCategorie().getId());
            if (categorie == null)
                throw new IllegalArgumentException("Catégorie introuvable.");
            produit.setCategorie(categorie);

            // Sauvegarde le produit
            em.persist(produit);
            em.flush(); // Force l'insertion pour récupérer l'ID généré

            // Crée le stock associé dans la même transaction
            Stock stock = new Stock();
            stock.setProduit(produit);
            stock.setQuantite(quantiteInitiale != null ? quantiteInitiale : 0);
            stock.setSeuilMinimum(seuilMinimum != null ? seuilMinimum : 5);
            em.persist(stock);

            em.getTransaction().commit();
            System.out.println("✓ Produit créé : " + produit.getNom());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("✗ Erreur création produit : " + e.getMessage());
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    /**
     * Modifie un produit existant.
     */
    public void modifier(Produit produit) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            if (produit.getId() == null)
                throw new IllegalArgumentException("Produit introuvable.");
            if (produit.getNom() == null || produit.getNom().isEmpty())
                throw new IllegalArgumentException("Le nom est obligatoire.");
            if (produit.getPrixUnitaire() == null || produit.getPrixUnitaire() < 0)
                throw new IllegalArgumentException("Le prix doit être positif.");

            em.getTransaction().begin();

            // Reattache la catégorie
            if (produit.getCategorie() != null && produit.getCategorie().getId() != null) {
                Categorie categorie = em.find(Categorie.class, produit.getCategorie().getId());
                produit.setCategorie(categorie);
            }

            em.merge(produit);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    /**
     * Supprime un produit (son stock sera supprimé en cascade).
     */
    public void supprimer(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Produit produit = em.find(Produit.class, id);
            if (produit == null)
                throw new IllegalArgumentException("Produit introuvable.");
            em.remove(produit);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    public Produit findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

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

    public List<Produit> rechercherParNom(String nom) {
        if (nom == null || nom.isEmpty()) return findAll();
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
}
