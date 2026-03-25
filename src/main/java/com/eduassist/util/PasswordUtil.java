package com.eduassist.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitaire simple pour hacher et vérifier les mots de passe avec jBCrypt.
 */
public final class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Hash le mot de passe en utilisant bcrypt (cost 12 par défaut).
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) return null;
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Vérifie un mot de passe en clair contre un hash enregistré.
     */
    public static boolean verifyPassword(String plainPassword, String hashed) {
        if (plainPassword == null || hashed == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashed);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}

