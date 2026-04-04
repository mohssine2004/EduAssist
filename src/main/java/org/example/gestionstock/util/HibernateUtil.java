package org.example.gestionstock.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * HibernateUtil — Singleton gérant l'EntityManagerFactory.
 *
 * Utilisation :
 *   EntityManager em = HibernateUtil.getEntityManager();
 *   try {
 *       em.getTransaction().begin();
 *       // ... opérations ...
 *       em.getTransaction().commit();
 *   } catch (Exception e) {
 *       em.getTransaction().rollback();
 *       throw e;
 *   } finally {
 *       HibernateUtil.closeEntityManager(em);
 *   }
 */
public class HibernateUtil {

    // Nom de la persistence-unit dans persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "gestionStockPU";

    // Une seule instance de l'EntityManagerFactory (coûteuse à créer)
    private static EntityManagerFactory entityManagerFactory;

    // Bloc statique : crée l'EMF au chargement de la classe
    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("✅ EntityManagerFactory créée avec succès.");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création de l'EntityManagerFactory : " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    // Constructeur privé — empêche l'instanciation
    private HibernateUtil() {}

    /**
     * Retourne l'EntityManagerFactory (singleton).
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Crée et retourne un nouvel EntityManager.
     * Chaque opération (DAO) doit avoir son propre EM.
     */
    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Ferme l'EntityManager proprement après utilisation.
     */
    public static void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Ferme l'EntityManagerFactory (appeler à l'arrêt de l'application).
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("🔒 EntityManagerFactory fermée.");
        }
    }
}