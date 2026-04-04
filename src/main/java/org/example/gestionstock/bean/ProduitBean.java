package org.example.gestionstock.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.model.Categorie;
import org.example.gestionstock.model.Produit;
import org.example.gestionstock.service.CategorieService;
import org.example.gestionstock.service.ProduitService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProduitBean implements Serializable {

    @Inject private ProduitService   produitService;
    @Inject private CategorieService categorieService;

    private List<Produit>   produits;
    private List<Categorie> categories;
    private Produit         produit          = new Produit();
    private Long            categorieId;       // ← on stocke juste l'ID
    private Integer         quantiteInitiale  = 0;
    private Integer         seuilMinimum      = 5;
    private Long            idASupprimer;
    private String          recherche;

    @PostConstruct
    public void init() {
        charger();
        categories = categorieService.findAll();
    }

    private void charger() {
        produits = produitService.findAll();
    }

    // ── CRUD Actions ───────────────────────────────────────

    public void sauvegarder() {
        try {
            // Associe la catégorie choisie au produit via l'ID
            if (categorieId != null) {
                Categorie cat = new Categorie();
                cat.setId(categorieId);
                produit.setCategorie(cat);
            }

            if (produit.getId() == null) {
                produitService.creer(produit, quantiteInitiale, seuilMinimum);
                addMessage(FacesMessage.SEVERITY_INFO, "Produit créé avec succès.");
            } else {
                produitService.modifier(produit);
                addMessage(FacesMessage.SEVERITY_INFO, "Produit modifié avec succès.");
            }
            nouveau();
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void prepareModifier(Produit p) {
        this.produit     = p;
        this.categorieId = p.getCategorie() != null ? p.getCategorie().getId() : null;
    }

    public void prepareSupprimer(Long id) {
        this.idASupprimer = id;
    }

    public void confirmerSuppression() {
        try {
            produitService.supprimer(idASupprimer);
            addMessage(FacesMessage.SEVERITY_INFO, "Produit supprimé avec succès.");
            charger();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void nouveau() {
        this.produit          = new Produit();
        this.categorieId      = null;
        this.quantiteInitiale = 0;
        this.seuilMinimum     = 5;
    }

    public void rechercher() {
        produits = produitService.rechercherParNom(recherche);
    }

    public void reinitialiserRecherche() {
        recherche = null;
        charger();
    }

    // ── Helper ─────────────────────────────────────────────
    private void addMessage(FacesMessage.Severity severity, String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, msg, null));
    }

    // ── Getters & Setters ──────────────────────────────────
    public List<Produit>   getProduits()   { return produits; }
    public List<Categorie> getCategories() { return categories; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit p) { this.produit = p; }

    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long id) { this.categorieId = id; }

    public Integer getQuantiteInitiale() { return quantiteInitiale; }
    public void setQuantiteInitiale(Integer q) { this.quantiteInitiale = q; }

    public Integer getSeuilMinimum() { return seuilMinimum; }
    public void setSeuilMinimum(Integer s) { this.seuilMinimum = s; }

    public Long getIdASupprimer() { return idASupprimer; }
    public void setIdASupprimer(Long id) { this.idASupprimer = id; }

    public String getRecherche() { return recherche; }
    public void setRecherche(String r) { this.recherche = r; }
}
