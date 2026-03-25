package com.eduassist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "suggestion_ia")
public class SuggestionIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 50)
    private String type;

    @Column(length = 500)
    private String url;

    // N-1 with Topic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    public SuggestionIA() {}

    public SuggestionIA(String titre, String type, String url, Topic topic) {
        this.titre = titre;
        this.type = type;
        this.url = url;
        this.topic = topic;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }
}