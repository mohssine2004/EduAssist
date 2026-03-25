package com.eduassist.dao;

import com.eduassist.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserDAO extends GenericDAO<User, Integer> {

    public UserDAO() {
        super(User.class);
    }

    // Find by email (used for login)
    public User findByEmail(String email) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Login — check email + password
    public User findByEmailAndPassword(String email, String password) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email AND u.password = :password",
                    User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Check if email already exists (registration)
    public boolean emailExists(String email) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    // All students only
    public List<User> findAllStudents() {
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.role = 'STUDENT'", User.class)
                .getResultList();
    }
}