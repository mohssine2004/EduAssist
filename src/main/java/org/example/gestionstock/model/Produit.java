package org.example.gestionstock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "reference", nullable = false, unique = true, length = 80)
    private String reference;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relation : un produit appartient à une catégorie
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    // Relation : un produit a un seul stock (inverse)
    @OneToOne(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Stock stock;

    // ── Lifecycle ──────────────────────────────────────────
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Constructors ───────────────────────────────────────
    public Produit() {}

    public Produit(String nom, String reference, Double prixUnitaire, Categorie categorie) {
        this.nom = nom;
        this.reference = reference;
        this.prixUnitaire = prixUnitaire;
        this.categorie = categorie;
    }

    // ── Méthodes métier ────────────────────────────────────
    public boolean isEnRupture() {
        return stock == null || stock.getQuantite() == 0;
    }

    public String getPrixFormate() {
        return String.format("%.2f MAD", prixUnitaire);
    }

    // ── Getters & Setters ──────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Produit{id=" + id + ", nom='" + nom + "', reference='" + reference + "'}";
    }
}