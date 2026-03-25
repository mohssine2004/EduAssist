package com.eduassist.dao;

import com.eduassist.model.StudySession;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class StudySessionDAO extends GenericDAO<StudySession, Integer> {

    public StudySessionDAO() {
        super(StudySession.class);
    }

    public List<StudySession> findByTopicId(int topicId) {
        TypedQuery<StudySession> query = em.createQuery(
                "SELECT s FROM StudySession s WHERE s.topic.id = :topicId ORDER BY s.startTime DESC",
                StudySession.class);
        query.setParameter("topicId", topicId);
        return query.getResultList();
    }

    // Total XP earned by a user across all sessions
    public int getTotalXpByUser(int userId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT SUM(s.xpEarned) FROM StudySession s WHERE s.topic.course.user.id = :userId",
                Long.class);
        query.setParameter("userId", userId);
        Long result = query.getSingleResult();
        return result == null ? 0 : result.intValue();
    }
}