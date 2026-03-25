package com.eduassist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "knowledge_node")
public class KnowledgeNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "concept_name", nullable = false, length = 150)
    private String conceptName;

    @Column(length = 100)
    private String category;

    // 1-1 with Topic
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false, unique = true)
    private Topic topic;

    public KnowledgeNode() {}

    public KnowledgeNode(String conceptName, String category, Topic topic) {
        this.conceptName = conceptName;
        this.category = category;
        this.topic = topic;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getConceptName() { return conceptName; }
    public void setConceptName(String conceptName) { this.conceptName = conceptName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }
}