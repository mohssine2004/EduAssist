package com.eduassist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_stats")
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "current_streak", nullable = false)
    private int currentStreak = 0;

    @Column(name = "longest_streak", nullable = false)
    private int longestStreak = 0;

    @Column(name = "total_xp", nullable = false)
    private int totalXp = 0;

    @Column(nullable = false)
    private int level = 1;

    // 1-1 with User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ── Constructors ──────────────────────────────────────────
    public UserStats() {}

    public UserStats(User user) {
        this.user = user;
    }

    // ── Getters & Setters ─────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }

    public int getTotalXp() { return totalXp; }
    public void setTotalXp(int totalXp) { this.totalXp = totalXp; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}