package com.eduassist.dao;

import com.eduassist.model.UserStats;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserStatsDAO extends GenericDAO<UserStats, Integer> {

    public UserStatsDAO() {
        super(UserStats.class);
    }

    public UserStats findByUserId(int userId) {
        try {
            TypedQuery<UserStats> query = em.createQuery(
                    "SELECT s FROM UserStats s WHERE s.user.id = :userId", UserStats.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}