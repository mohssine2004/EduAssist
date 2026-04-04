package org.example.gestionstock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorie")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, unique = true, length = 100)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relation : une catégorie contient plusieurs produits
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Produit> produits = new ArrayList<>();

    // ── Lifecycle ──────────────────────────────────────────
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Constructors ───────────────────────────────────────
    public Categorie() {}

    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // ── Méthode métier ─────────────────────────────────────
    public int getNbProduits() {
        return produits != null ? produits.size() : 0;
    }

    // ── Getters & Setters ──────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Produit> getProduits() { return produits; }
    public void setProduits(List<Produit> produits) { this.produits = produits; }

    @Override
    public String toString() {
        return "Categorie{id=" + id + ", nom='" + nom + "'}";
    }
}