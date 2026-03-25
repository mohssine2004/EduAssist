package com.eduassist.dao;

import com.eduassist.model.Topic;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class TopicDAO extends GenericDAO<Topic, Integer> {

    public TopicDAO() {
        super(Topic.class);
    }

    // All topics in a course
    public List<Topic> findByCourseId(int courseId) {
        TypedQuery<Topic> query = em.createQuery(
                "SELECT t FROM Topic t WHERE t.course.id = :courseId ORDER BY t.titre",
                Topic.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    // Topics due for revision today or earlier
    public List<Topic> findTopicsDueForRevision(int userId) {
        TypedQuery<Topic> query = em.createQuery(
                "SELECT t FROM Topic t WHERE t.course.user.id = :userId " +
                        "AND t.dateProchaineRevision <= :today ORDER BY t.dateProchaineRevision",
                Topic.class);
        query.setParameter("userId", userId);
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }

    // Count topics per course
    public long countByCourse(int courseId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Topic t WHERE t.course.id = :courseId", Long.class);
        query.setParameter("courseId", courseId);
        return query.getSingleResult();
    }
}