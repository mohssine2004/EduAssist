package org.example.gestionstock.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.model.Categorie;
import org.example.gestionstock.service.CategorieService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CategorieBean implements Serializable {

    @Inject
    private CategorieService categorieService;

    private List<Categorie> categories;
    private Categorie       categorie = new Categorie();
    private Long            idASupprimer;

    @PostConstruct
    public void init() {
        charger();
    }

    private void charger() {
        categories = categorieService.findAll();
    }

    // ── CRUD Actions ───────────────────────────────────────

    /** Sauvegarde : crée ou modifie selon si l'id est null */
    public void sauvegarder() {
        try {
            if (categorie.getId() == null) {
                categorieService.creer(categorie);
                addMessage(FacesMessage.SEVERITY_INFO,
                        "Catégorie créée avec succès.");
            } else {
                categorieService.modifier(categorie);
                addMessage(FacesMessage.SEVERITY_INFO,
                        "Catégorie modifiée avec succès.");
            }
            categorie = new Categorie();
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /** Prépare le formulaire pour la modification */
    public void prepareModifier(Categorie c) {
        this.categorie = c;
    }

    /** Prépare la confirmation de suppression */
    public void prepareSupprimer(Long id) {
        this.idASupprimer = id;
    }

    /** Confirme la suppression */
    public void confirmerSuppression() {
        try {
            categorieService.supprimer(idASupprimer);
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Catégorie supprimée avec succès.");
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    /** Réinitialise le formulaire */
    public void nouveau() {
        this.categorie = new Categorie();
    }

    // ── Helper ─────────────────────────────────────────────
    private void addMessage(FacesMessage.Severity severity, String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, msg, null));
    }

    // ── Getters & Setters ──────────────────────────────────
    public List<Categorie> getCategories() { return categories; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public Long getIdASupprimer() { return idASupprimer; }
    public void setIdASupprimer(Long idASupprimer) { this.idASupprimer = idASupprimer; }
}