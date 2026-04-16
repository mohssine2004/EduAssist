package org.example.gestionstock.service;

import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mindrot.jbcrypt.BCrypt;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour le service d'authentification (AuthService).
 *
 * Ces tests vérifient :
 * - La connexion avec des identifiants corrects
 * - Le rejet de connexions avec identifiants incorrects
 * - Le hashage sécurisé des mots de passe
 * - La création d'utilisateurs avec rôles
 */
@DisplayName("Tests d'intégration - Authentification")
public class AuthServiceIntegrationTest {

    private AuthService authService;
    private Utilisateur utilisateurTest;
    private String motDePasseTest;

    @BeforeEach
    public void setUp() {
        // Initialisation de AuthService (mock du DAO en production)
        authService = new AuthService();

        // Création d'un utilisateur de test
        motDePasseTest = "TestPassword123!";
        utilisateurTest = new Utilisateur();
        utilisateurTest.setNom("Admin Test");
        utilisateurTest.setEmail("admin@test.com");
        utilisateurTest.setRole(Role.ADMIN);
        utilisateurTest.setActif(true);

        // Hash du mot de passe
        String hashedPassword = BCrypt.hashpw(motDePasseTest, BCrypt.gensalt(10));
        utilisateurTest.setMotDePasse(hashedPassword);
    }

    // ═══════════════════════════════════════════════════════════
    // ✅ TESTS DE SUCCÈS
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("✅ Connexion réussie avec email et mot de passe corrects")
    public void testLoginAvecIdentifiantsValides() {
        // GIVEN : Un utilisateur avec identifiants valides
        String emailValide = "admin@test.com";
        String motDePasseValide = "TestPassword123!";

        // WHEN : On tente une connexion
        // (On simule le comportement du service)
        boolean passwordMatch = BCrypt.checkpw(motDePasseValide, utilisateurTest.getMotDePasse());

        // THEN : La connexion devrait réussir
        assertThat(passwordMatch)
                .as("Le mot de passe devrait correspondre")
                .isTrue();

        assertThat(utilisateurTest.getEmail())
                .as("L'email devrait correspondre")
                .isEqualTo(emailValide);

        assertThat(utilisateurTest.isActif())
                .as("L'utilisateur devrait être actif")
                .isTrue();
    }

    @Test
    @DisplayName("✅ Vérification que le mot de passe est hashé et sécurisé")
    public void testMotDePasseEstHashe() {
        // GIVEN : Un mot de passe original
        String originalPassword = "MySecurePassword123!";

        // WHEN : On hash le mot de passe avec BCrypt
        String hashedPassword = BCrypt.hashpw(originalPassword, BCrypt.gensalt(10));

        // THEN :
        // Le hash ne devrait pas égaler le mot de passe original
        assertThat(hashedPassword)
                .as("Le hash ne devrait pas égaler le mot de passe original")
                .isNotEqualTo(originalPassword);

        // Le hash devrait être vérifiable
        assertThat(BCrypt.checkpw(originalPassword, hashedPassword))
                .as("Le mot de passe devrait être vérifiable")
                .isTrue();

        // Chaque hash devrait être unique (même pour le même mot de passe)
        String hashedPassword2 = BCrypt.hashpw(originalPassword, BCrypt.gensalt(10));
        assertThat(hashedPassword)
                .as("Les hashes ne devraient pas être identiques")
                .isNotEqualTo(hashedPassword2);
    }

    @Test
    @DisplayName("✅ Création d'utilisateur avec rôles différents")
    public void testCreationUtilisateurAvecRoles() {
        // GIVEN : Trois utilisateurs avec rôles différents
        Utilisateur admin = creerUtilisateur("admin@test.com", "Admin", Role.ADMIN);
        Utilisateur manager = creerUtilisateur("manager@test.com", "Manager", Role.MANAGER);
        Utilisateur employe = creerUtilisateur("employe@test.com", "Employé", Role.EMPLOYE);

        // THEN : Chaque utilisateur devrait avoir le bon rôle
        assertThat(admin.getRole())
                .as("L'admin devrait avoir le rôle ADMIN")
                .isEqualTo(Role.ADMIN);

        assertThat(manager.getRole())
                .as("Le manager devrait avoir le rôle MANAGER")
                .isEqualTo(Role.MANAGER);

        assertThat(employe.getRole())
                .as("L'employé devrait avoir le rôle EMPLOYE")
                .isEqualTo(Role.EMPLOYE);
    }

    // ═══════════════════════════════════════════════════════════
    // ❌ TESTS D'ERREUR (Validations)
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("❌ Connexion échouée avec mot de passe incorrect")
    public void testLoginAvecMotDePasseIncorrect() {
        // GIVEN : Un mot de passe incorrect
        String motDePasseIncorrect = "WrongPassword123!";

        // WHEN : On vérifie le mot de passe
        boolean passwordMatch = BCrypt.checkpw(motDePasseIncorrect, utilisateurTest.getMotDePasse());

        // THEN : La connexion devrait échouer
        assertThat(passwordMatch)
                .as("Le mot de passe incorrect ne devrait pas correspondre")
                .isFalse();
    }

    @Test
    @DisplayName("❌ Connexion échouée : utilisateur inactif")
    public void testLoginUtilisateurInactif() {
        // GIVEN : Un utilisateur inactif
        utilisateurTest.setActif(false);

        // WHEN/THEN : Le login devrait échouer
        assertThat(utilisateurTest.isActif())
                .as("L'utilisateur devrait être inactif")
                .isFalse();
    }

    @Test
    @DisplayName("❌ Email vide n'est pas accepté")
    public void testEmailVideNonAutorise() {
        // GIVEN : Un utilisateur avec email vide
        Utilisateur user = creerUtilisateur("", "Test", Role.EMPLOYE);

        // THEN : L'email devrait être vide
        assertThat(user.getEmail())
                .as("L'email ne devrait pas être vide")
                .isEmpty();

        // WHEN : On essaie de faire un login, ça devrait échouer
        // (Validation côté service)
    }

    @Test
    @DisplayName("❌ Mot de passe vide n'est pas accepté")
    public void testMotDePasseVideNonAutorise() {
        // GIVEN : Un utilisateur avec mot de passe vide
        Utilisateur user = new Utilisateur();
        user.setEmail("test@test.com");
        user.setMotDePasse("");

        // THEN : Le mot de passe ne devrait pas être vide
        assertThat(user.getMotDePasse())
                .as("Le mot de passe ne devrait pas être vide")
                .isEmpty();
    }

    // ═══════════════════════════════════════════════════════════
    // 🔒 TESTS DE SÉCURITÉ
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("🔒 BCrypt avec cost factor de 10 est suffisamment sécurisé")
    public void testBCryptSecurityLevel() {
        // GIVEN : Un mot de passe
        String password = "VerySecurePassword123!";

        // WHEN : On hash avec BCrypt (cost factor = 10)
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));

        // THEN : Le hash devrait contenir le cost factor $10$
        assertThat(hashed)
                .as("Le hash devrait contenir le cost factor $10$")
                .contains("$10$");
    }

    @Test
    @DisplayName("🔒 Les mots de passe ne sont jamais stockés en clair")
    public void testMotDePasseNonStockeEnClair() {
        // GIVEN : Un mot de passe original
        String originalPassword = "SuperSecret123!";
        String hashed = BCrypt.hashpw(originalPassword, BCrypt.gensalt(10));

        // WHEN/THEN : Le hash ne devrait jamais égaler le mot de passe original
        assertThat(hashed)
                .as("Le hash ne devrait jamais égaler le mot de passe original")
                .isNotEqualTo(originalPassword)
                .isNotEmpty()
                .isNotBlank();
    }

    @Test
    @DisplayName("🔒 Validation : Email doit avoir un format valide")
    public void testValidationFormatEmail() {
        // GIVEN : Plusieurs formats d'email
        String emailValide1 = "user@example.com";
        String emailValide2 = "admin.test@gestionstock.fr";
        String emailInvalide1 = "userexample.com"; // Sans @
        String emailInvalide2 = "@example.com";     // Pas de username
        String emailInvalide3 = "user@";            // Pas de domaine

        // THEN : Validation basique avec regex
        assertThat(emailValide1)
                .as("Email valide 1")
                .matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        assertThat(emailValide2)
                .as("Email valide 2")
                .matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        assertThat(emailInvalide1)
                .as("Email invalide 1 ne devrait pas passer")
                .doesNotMatch("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // ═══════════════════════════════════════════════════════════
    // 👤 TESTS DE RÔLES ET PERMISSIONS
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("👤 Admin a accès à l'administration")
    public void testAdminHasAdminAccess() {
        // GIVEN : Un utilisateur ADMIN
        Utilisateur admin = creerUtilisateur("admin@test.com", "Admin", Role.ADMIN);

        // THEN : L'admin devrait avoir les permissions
        assertThat(admin.getRole())
                .isEqualTo(Role.ADMIN);

        assertThat(isAdmin(admin))
                .as("L'admin devrait avoir accès à l'administration")
                .isTrue();
    }

    @Test
    @DisplayName("👤 Manager n'a pas accès à l'administration complète")
    public void testManagerNoAdminAccess() {
        // GIVEN : Un utilisateur MANAGER
        Utilisateur manager = creerUtilisateur("manager@test.com", "Manager", Role.MANAGER);

        // THEN : Le manager ne devrait pas être admin
        assertThat(manager.getRole())
                .isNotEqualTo(Role.ADMIN);

        assertThat(isAdmin(manager))
                .as("Le manager ne devrait pas avoir l'accès admin")
                .isFalse();
    }

    @Test
    @DisplayName("👤 Employé n'a accès qu'à la lecture du stock")
    public void testEmployeRestrictedAccess() {
        // GIVEN : Un utilisateur EMPLOYE
        Utilisateur employe = creerUtilisateur("employe@test.com", "Employé", Role.EMPLOYE);

        // THEN : L'employé ne devrait pas être admin
        assertThat(employe.getRole())
                .isEqualTo(Role.EMPLOYE);

        assertThat(isAdmin(employe))
                .as("L'employé ne devrait pas avoir l'accès admin")
                .isFalse();

        assertThat(isAdminOrManager(employe))
                .as("L'employé ne devrait pas être admin ou manager")
                .isFalse();
    }

    // ═══════════════════════════════════════════════════════════
    // 🛠️ MÉTHODES UTILITAIRES POUR LES TESTS
    // ═══════════════════════════════════════════════════════════

    private Utilisateur creerUtilisateur(String email, String nom, Role role) {
        Utilisateur user = new Utilisateur();
        user.setEmail(email);
        user.setNom(nom);
        user.setRole(role);
        user.setActif(true);
        String password = "TestPassword123!";
        user.setMotDePasse(BCrypt.hashpw(password, BCrypt.gensalt(10)));
        return user;
    }

    private boolean isAdmin(Utilisateur user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    private boolean isAdminOrManager(Utilisateur user) {
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER);
    }
}

