package org.example.gestionstock.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.service.UtilisateurService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UtilisateurBean implements Serializable {

    @Inject
    private UtilisateurService utilisateurService;

    private List<Utilisateur> utilisateurs;
    private Utilisateur       utilisateur      = new Utilisateur();
    private String            nouveauMotDePasse;
    private String            confirmMotDePasse;
    private Long              idASupprimer;

    @PostConstruct
    public void init() {
        charger();
    }

    private void charger() {
        utilisateurs = utilisateurService.findAll();
    }

    // ── CRUD Actions ───────────────────────────────────────

    public void sauvegarder() {
        try {
            // Vérifie que les deux mots de passe correspondent
            if (utilisateur.getId() == null) {
                if (nouveauMotDePasse == null || nouveauMotDePasse.isEmpty()) {
                    addMessage(FacesMessage.SEVERITY_ERROR,
                            "Le mot de passe est obligatoire.");
                    return;
                }
                if (!nouveauMotDePasse.equals(confirmMotDePasse)) {
                    addMessage(FacesMessage.SEVERITY_ERROR,
                            "Les mots de passe ne correspondent pas.");
                    return;
                }
                utilisateur.setMotDePasse(nouveauMotDePasse);
                utilisateurService.creer(utilisateur);
                addMessage(FacesMessage.SEVERITY_INFO,
                        "Utilisateur créé avec succès.");
            } else {
                if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
                    if (!nouveauMotDePasse.equals(confirmMotDePasse)) {
                        addMessage(FacesMessage.SEVERITY_ERROR,
                                "Les mots de passe ne correspondent pas.");
                        return;
                    }
                }
                utilisateurService.modifier(utilisateur, nouveauMotDePasse);
                addMessage(FacesMessage.SEVERITY_INFO,
                        "Utilisateur modifié avec succès.");
            }
            nouveau();
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void prepareModifier(Utilisateur u) {
        this.utilisateur       = u;
        this.nouveauMotDePasse = null;
        this.confirmMotDePasse = null;
    }

    public void prepareSupprimer(Long id) {
        this.idASupprimer = id;
    }

    public void confirmerSuppression() {
        try {
            utilisateurService.supprimer(idASupprimer);
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Utilisateur supprimé avec succès.");
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void toggleActif(Long id) {
        try {
            utilisateurService.toggleActif(id);
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void nouveau() {
        this.utilisateur       = new Utilisateur();
        this.nouveauMotDePasse = null;
        this.confirmMotDePasse = null;
    }

    /** Liste des rôles pour le dropdown JSF */
    public Role[] getRoles() {
        return Role.values();
    }

    // ── Helper ─────────────────────────────────────────────
    private void addMessage(FacesMessage.Severity severity, String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, msg, null));
    }

    // ── Getters & Setters ──────────────────────────────────
    public List<Utilisateur> getUtilisateurs() { return utilisateurs; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur u) { this.utilisateur = u; }

    public String getNouveauMotDePasse() { return nouveauMotDePasse; }
    public void setNouveauMotDePasse(String mdp) { this.nouveauMotDePasse = mdp; }

    public String getConfirmMotDePasse() { return confirmMotDePasse; }
    public void setConfirmMotDePasse(String mdp) { this.confirmMotDePasse = mdp; }

    public Long getIdASupprimer() { return idASupprimer; }
    public void setIdASupprimer(Long id) { this.idASupprimer = id; }
}