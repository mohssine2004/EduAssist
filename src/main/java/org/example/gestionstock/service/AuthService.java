package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.util.HibernateUtil;
import org.example.gestionstock.util.PasswordUtil;

import java.util.List;

@Named
@ApplicationScoped
public class AuthService {

    /**
     * Login sans injection DAO — utilise directement JPA pour fiabilité maximale.
     */
    public Utilisateur login(String email, String motDePasse) {
        System.out.println("========================================");
        System.out.println("▶ Tentative de connexion : " + email);

        if (email == null || motDePasse == null) {
            System.out.println("✗ Email ou mot de passe null");
            return null;
        }

        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();

            // Cherche l'utilisateur par email (insensible à la casse)
            List<Utilisateur> results = em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE LOWER(u.email) = LOWER(:email)",
                            Utilisateur.class)
                    .setParameter("email", email.trim())
                    .getResultList();

            System.out.println("▶ Utilisateurs trouvés avec cet email : " + results.size());

            if (results.isEmpty()) {
                System.out.println("✗ Aucun utilisateur trouvé avec email : " + email);
                return null;
            }

            Utilisateur utilisateur = results.get(0);
            System.out.println("▶ Utilisateur trouvé : " + utilisateur.getNom()
                    + " | actif=" + utilisateur.isActif()
                    + " | role=" + utilisateur.getRole());

            if (!utilisateur.isActif()) {
                System.out.println("✗ Compte désactivé");
                return null;
            }

            // Vérifie le mot de passe BCrypt
            boolean mdpOk = PasswordUtil.verify(motDePasse, utilisateur.getMotDePasse());
            System.out.println("▶ Vérification BCrypt : " + (mdpOk ? "✓ OK" : "✗ ECHEC"));

            if (!mdpOk) {
                System.out.println("✗ Mot de passe incorrect pour : " + email);
                return null;
            }

            System.out.println("✓ Connexion réussie pour : " + utilisateur.getNom());
            System.out.println("========================================");
            return utilisateur;

        } catch (Exception e) {
            System.err.println("✗ ERREUR AuthService.login : " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }

    public boolean emailExiste(String email) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u WHERE LOWER(u.email) = LOWER(:email)",
                            Long.class)
                    .setParameter("email", email.trim())
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        } finally {
            HibernateUtil.closeEntityManager(em);
        }
    }
}