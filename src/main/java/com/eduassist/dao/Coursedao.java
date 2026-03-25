package com.eduassist.dao;

import com.eduassist.model.Course;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CourseDAO extends GenericDAO<Course, Integer> {

    public CourseDAO() {
        super(Course.class);
    }

    // All courses belonging to a specific user
    public List<Course> findByUserId(int userId) {
        TypedQuery<Course> query = em.createQuery(
                "SELECT c FROM Course c WHERE c.user.id = :userId ORDER BY c.titre",
                Course.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // Count courses for a user
    public long countByUser(int userId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Course c WHERE c.user.id = :userId", Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }
}