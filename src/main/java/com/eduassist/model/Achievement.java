package com.eduassist.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icone_url", length = 255)
    private String iconeUrl;

    // N-N with User (inverse side)
    @ManyToMany(mappedBy = "achievements", fetch = FetchType.LAZY)
    private List<User> users;

    // ── Constructors ──────────────────────────────────────────
    public Achievement() {}

    public Achievement(String titre, String description, String iconeUrl) {
        this.titre = titre;
        this.description = description;
        this.iconeUrl = iconeUrl;
    }

    // ── Getters & Setters ─────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconeUrl() { return iconeUrl; }
    public void setIconeUrl(String iconeUrl) { this.iconeUrl = iconeUrl; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}