package com.eduassist.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(name = "niveau_maitrise", nullable = false)
    private int niveauMaitrise = 0;

    @Column(name = "date_prochaine_revision")
    private LocalDate dateProchaineRevision;

    // N-1 with Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 1-1 with KnowledgeNode
    @OneToOne(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private KnowledgeNode knowledgeNode;

    // 1-N with SuggestionIA
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SuggestionIA> suggestions;

    // 1-N with Note
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Note> notes;

    // 1-N with StudySession
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudySession> studySessions;

    // ── Constructors ──────────────────────────────────────────
    public Topic() {}

    public Topic(String titre, Course course) {
        this.titre = titre;
        this.course = course;
    }

    // ── Getters & Setters ─────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public int getNiveauMaitrise() { return niveauMaitrise; }
    public void setNiveauMaitrise(int niveauMaitrise) { this.niveauMaitrise = niveauMaitrise; }

    public LocalDate getDateProchaineRevision() { return dateProchaineRevision; }
    public void setDateProchaineRevision(LocalDate dateProchaineRevision) { this.dateProchaineRevision = dateProchaineRevision; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public KnowledgeNode getKnowledgeNode() { return knowledgeNode; }
    public void setKnowledgeNode(KnowledgeNode knowledgeNode) { this.knowledgeNode = knowledgeNode; }

    public List<SuggestionIA> getSuggestions() { return suggestions; }
    public void setSuggestions(List<SuggestionIA> suggestions) { this.suggestions = suggestions; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    public List<StudySession> getStudySessions() { return studySessions; }
    public void setStudySessions(List<StudySession> studySessions) { this.studySessions = studySessions; }
}