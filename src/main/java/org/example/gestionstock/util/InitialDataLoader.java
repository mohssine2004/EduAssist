package org.example.gestionstock.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.model.Utilisateur;

/**
 * InitialDataLoader — Se déclenche au démarrage de l'application CDI.
 * Utilise @Observes au lieu de @PostConstruct pour compatibilité WildFly.
 */
@ApplicationScoped
public class InitialDataLoader {

    /**
     * Se déclenche automatiquement quand le contexte ApplicationScoped est initialisé.
     * Plus fiable que @PostConstruct sur WildFly.
     */
    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object event) {
        EntityManager em = null;
        try {
            System.out.println("========================================");
            System.out.println("▶ InitialDataLoader démarré via @Observes");

            em = HibernateUtil.getEntityManager();

            // Vérifie combien d'utilisateurs existent
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u", Long.class)
                    .getSingleResult();

            System.out.println("▶ Utilisateurs existants : " + count);

            // Ne crée les comptes QUE si la table est vide
            if (count > 0) {
                System.out.println("✓ Comptes déjà présents — pas d'initialisation nécessaire.");
                System.out.println("========================================");
                return;
            }

            System.out.println("▶ Table vide — création des comptes de test...");

            em.getTransaction().begin();

            // ── Admin ──
            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setEmail("admin@test.com");
            admin.setMotDePasse(PasswordUtil.hash("Test@1234"));
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            em.persist(admin);

            // ── Manager ──
            Utilisateur manager = new Utilisateur();
            manager.setNom("Gestionnaire");
            manager.setEmail("manager@test.com");
            manager.setMotDePasse(PasswordUtil.hash("Test@1234"));
            manager.setRole(Role.MANAGER);
            manager.setActif(true);
            em.persist(manager);

            // ── Employé ──
            Utilisateur employe = new Utilisateur();
            employe.setNom("Employé");
            employe.setEmail("employe@test.com");
            employe.setMotDePasse(PasswordUtil.hash("Test@1234"));
            employe.setRole(Role.EMPLOYE);
            employe.setActif(true);
            em.persist(employe);

            em.getTransaction().commit();

            // Vérification finale
            Long newCount = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u", Long.class)
                    .getSingleResult();

            System.out.println("✓ " + newCount + " comptes créés avec succès !");
            System.out.println("✓ admin@test.com     / Test@1234");
            System.out.println("✓ manager@test.com   / Test@1234");
            System.out.println("✓ employe@test.com   / Test@1234");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("✗ ERREUR InitialDataLoader : " + e.getMessage());
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}