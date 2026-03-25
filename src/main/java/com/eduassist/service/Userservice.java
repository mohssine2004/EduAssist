package com.eduassist.service;

import com.eduassist.dao.UserDAO;
import com.eduassist.dao.UserStatsDAO;
import com.eduassist.model.User;
import com.eduassist.model.UserStats;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserStatsDAO userStatsDAO;

    // ── Registration ──────────────────────────────────────────
    public boolean register(String nom, String email, String password) {
        if (userDAO.emailExists(email)) {
            return false; // email already taken
        }
        User user = new User(nom, email, password, "STUDENT");
        userDAO.save(user);

        // Auto-create stats for new user
        UserStats stats = new UserStats(user);
        userStatsDAO.save(stats);

        return true;
    }

    // ── Login ─────────────────────────────────────────────────
    public User login(String email, String password) {
        return userDAO.findByEmailAndPassword(email, password);
    }

    // ── CRUD ──────────────────────────────────────────────────
    public User findById(int id) {
        return userDAO.findById(id);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public void update(User user) {
        userDAO.update(user);
    }

    public void delete(int id) {
        userDAO.delete(id);
    }

    // ── XP & Gamification ────────────────────────────────────
    public void addXp(int userId, int xpAmount) {
        UserStats stats = userStatsDAO.findByUserId(userId);
        if (stats != null) {
            stats.setTotalXp(stats.getTotalXp() + xpAmount);
            // Level up every 100 XP
            int newLevel = (stats.getTotalXp() / 100) + 1;
            stats.setLevel(newLevel);
            userStatsDAO.update(stats);
        }
    }

    public void updateStreak(int userId) {
        UserStats stats = userStatsDAO.findByUserId(userId);
        if (stats != null) {
            int newStreak = stats.getCurrentStreak() + 1;
            stats.setCurrentStreak(newStreak);
            if (newStreak > stats.getLongestStreak()) {
                stats.setLongestStreak(newStreak);
            }
            userStatsDAO.update(stats);
        }
    }
}