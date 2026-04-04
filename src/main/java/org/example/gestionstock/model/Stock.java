package org.example.gestionstock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantite", nullable = false)
    private Integer quantite = 0;

    @Column(name = "seuil_minimum", nullable = false)
    private Integer seuilMinimum = 5;

    @Column(name = "date_maj")
    private LocalDateTime dateMaj;

    // Relation : un stock appartient à un seul produit (propriétaire)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false, unique = true)
    private Produit produit;

    // ── Lifecycle ──────────────────────────────────────────
    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.dateMaj = LocalDateTime.now();
    }

    // ── Constructors ───────────────────────────────────────
    public Stock() {}

    public Stock(Produit produit, Integer quantite, Integer seuilMinimum) {
        this.produit = produit;
        this.quantite = quantite;
        this.seuilMinimum = seuilMinimum;
    }

    // ── Méthodes métier ────────────────────────────────────

    // Vérifie si le stock est en dessous du seuil minimum
    public boolean isStockFaible() {
        return quantite > 0 && quantite < seuilMinimum;
    }

    // Vérifie si le produit est en rupture totale
    public boolean isEnRupture() {
        return quantite == 0;
    }

    // Retourne l'état du stock sous forme de texte
    public String getEtatStock() {
        if (isEnRupture())  return "RUPTURE";
        if (isStockFaible()) return "FAIBLE";
        return "OK";
    }

    // ── Getters & Setters ──────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Integer getSeuilMinimum() { return seuilMinimum; }
    public void setSeuilMinimum(Integer seuilMinimum) { this.seuilMinimum = seuilMinimum; }

    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    @Override
    public String toString() {
        return "Stock{id=" + id + ", quantite=" + quantite + ", seuil=" + seuilMinimum + ", etat=" + getEtatStock() + "}";
    }
}