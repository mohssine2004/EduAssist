package org.example.gestionstock.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.service.AuthService;

import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private AuthService authService;

    private String email;
    private String motDePasse;
    private Utilisateur utilisateurConnecte;

    /**
     * Tente la connexion et redirige selon le rôle.
     */
    public String login() {
        Utilisateur u = authService.login(email, motDePasse);

        if (u == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Email ou mot de passe incorrect.", null));
            return null;
        }

        // Stocke l'utilisateur en session
        this.utilisateurConnecte = u;

        // Stocke aussi dans la session HTTP (pour les filtres)
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("utilisateurConnecte", u);

        try {
            // Redirige selon le rôle via ExternalContext
            String contextPath = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestContextPath();

            if (u.getRole() == Role.EMPLOYE) {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect(contextPath + "/pages/stock/list.xhtml");
            } else {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect(contextPath + "/pages/dashboard.xhtml");
            }
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors de la redirection : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Déconnecte l'utilisateur et invalide la session.
     */
    public String logout() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        this.utilisateurConnecte = null;
        return "/pages/login.xhtml?faces-redirect=true";
    }

    // ── Helpers pour les pages JSF ─────────────────────────
    public boolean isConnecte() {
        return utilisateurConnecte != null;
    }

    public boolean isAdmin() {
        return utilisateurConnecte != null
                && utilisateurConnecte.getRole() == Role.ADMIN;
    }

    public boolean isManager() {
        return utilisateurConnecte != null
                && utilisateurConnecte.getRole() == Role.MANAGER;
    }

    public boolean isAdminOrManager() {
        return isAdmin() || isManager();
    }

    // ── Getters & Setters ──────────────────────────────────
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Utilisateur getUtilisateurConnecte() { return utilisateurConnecte; }
    public void setUtilisateurConnecte(Utilisateur u) { this.utilisateurConnecte = u; }
}