package org.example.gestionstock.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil — Utilitaire pour hasher et vérifier les mots de passe avec BCrypt.
 *
 * Utilisation :
 *   // Hasher un mot de passe avant de le stocker en BD
 *   String hash = PasswordUtil.hash("monMotDePasse");
 *
 *   // Vérifier un mot de passe lors de la connexion
 *   boolean ok = PasswordUtil.verify("monMotDePasse", hashStockeEnBD);
 */
public class PasswordUtil {

    // Plus le chiffre est élevé, plus le hash est sécurisé (mais lent)
    // 12 est un bon équilibre sécurité / performance
    private static final int BCRYPT_ROUNDS = 12;

    // Constructeur privé — classe utilitaire, pas d'instanciation
    private PasswordUtil() {}

    /**
     * Hashe un mot de passe en clair avec BCrypt.
     * À appeler avant de sauvegarder l'utilisateur en base de données.
     *
     * @param plainPassword le mot de passe en clair
     * @return le hash BCrypt à stocker en BD
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Vérifie qu'un mot de passe en clair correspond au hash stocké en BD.
     * À appeler lors de la connexion (login).
     *
     * @param plainPassword le mot de passe saisi par l'utilisateur
     * @param hashedPassword le hash stocké en base de données
     * @return true si le mot de passe est correct, false sinon
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vérifie si un mot de passe respecte les règles minimales de sécurité.
     * - Au moins 8 caractères
     * - Au moins une lettre majuscule
     * - Au moins une lettre minuscule
     * - Au moins un chiffre
     *
     * @param password le mot de passe à valider
     * @return true si le mot de passe est valide
     */
    public static boolean isStrong(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower   = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Retourne un message d'erreur si le mot de passe est trop faible,
     * ou null s'il est valide. Pratique pour l'affichage dans JSF.
     *
     * @param password le mot de passe à valider
     * @return message d'erreur ou null
     */
    public static String getValidationMessage(String password) {
        if (password == null || password.isEmpty())
            return "Le mot de passe est obligatoire.";
        if (password.length() < 8)
            return "Le mot de passe doit contenir au moins 8 caractères.";
        if (password.chars().noneMatch(Character::isUpperCase))
            return "Le mot de passe doit contenir au moins une majuscule.";
        if (password.chars().noneMatch(Character::isLowerCase))
            return "Le mot de passe doit contenir au moins une minuscule.";
        if (password.chars().noneMatch(Character::isDigit))
            return "Le mot de passe doit contenir au moins un chiffre.";
        return null;
    }
}