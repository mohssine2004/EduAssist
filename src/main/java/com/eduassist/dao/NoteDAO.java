package com.eduassist.dao;

import com.eduassist.model.Note;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class NoteDAO extends GenericDAO<Note, Integer> {

    public NoteDAO() {
        super(Note.class);
    }

    public List<Note> findByTopicId(int topicId) {
        TypedQuery<Note> query = em.createQuery(
                "SELECT n FROM Note n WHERE n.topic.id = :topicId ORDER BY n.createdAt DESC",
                Note.class);
        query.setParameter("topicId", topicId);
        return query.getResultList();
    }
}