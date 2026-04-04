package org.example.gestionstock.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.dao.UtilisateurDAO;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.util.PasswordUtil;

import java.util.List;

@Named
@ApplicationScoped
public class UtilisateurService {

    @Inject
    private UtilisateurDAO utilisateurDAO;

    /**
     * Crée un nouvel utilisateur en hashant son mot de passe.
     */
    public void creer(Utilisateur utilisateur) {
        // Validation
        if (utilisateur.getNom() == null || utilisateur.getNom().isEmpty())
            throw new IllegalArgumentException("Le nom est obligatoire.");
        if (utilisateur.getEmail() == null || utilisateur.getEmail().isEmpty())
            throw new IllegalArgumentException("L'email est obligatoire.");
        if (utilisateurDAO.existsByEmail(utilisateur.getEmail()))
            throw new IllegalArgumentException("Cet email est déjà utilisé.");

        // Validation mot de passe
        String erreurMdp = PasswordUtil.getValidationMessage(utilisateur.getMotDePasse());
        if (erreurMdp != null)
            throw new IllegalArgumentException(erreurMdp);

        // Hash du mot de passe avant stockage
        utilisateur.setEmail(utilisateur.getEmail().trim().toLowerCase());
        utilisateur.setMotDePasse(PasswordUtil.hash(utilisateur.getMotDePasse()));
        utilisateur.setActif(true);

        utilisateurDAO.create(utilisateur);
    }

    /**
     * Met à jour un utilisateur.
     * Si un nouveau mot de passe est fourni, il sera hashé.
     */
    public void modifier(Utilisateur utilisateur, String nouveauMotDePasse) {
        if (utilisateur.getId() == null)
            throw new IllegalArgumentException("Utilisateur introuvable.");

        // Si un nouveau mot de passe est fourni, on le hashe
        if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
            String erreur = PasswordUtil.getValidationMessage(nouveauMotDePasse);
            if (erreur != null) throw new IllegalArgumentException(erreur);
            utilisateur.setMotDePasse(PasswordUtil.hash(nouveauMotDePasse));
        }

        utilisateurDAO.update(utilisateur);
    }

    /**
     * Supprime un utilisateur par son id.
     */
    public void supprimer(Long id) {
        Utilisateur u = utilisateurDAO.findById(id);
        if (u == null)
            throw new IllegalArgumentException("Utilisateur introuvable.");
        utilisateurDAO.delete(id);
    }

    /**
     * Active ou désactive un compte utilisateur.
     */
    public void toggleActif(Long id) {
        Utilisateur u = utilisateurDAO.findById(id);
        if (u == null)
            throw new IllegalArgumentException("Utilisateur introuvable.");
        u.setActif(!u.isActif());
        utilisateurDAO.update(u);
    }

    public Utilisateur findById(Long id) {
        return utilisateurDAO.findById(id);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurDAO.findByEmail(email);
    }

    public List<Utilisateur> findAll() {
        return utilisateurDAO.findAll();
    }

    public List<Utilisateur> findByRole(Role role) {
        return utilisateurDAO.findByRole(role);
    }
}