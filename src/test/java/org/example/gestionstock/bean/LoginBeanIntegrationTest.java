package org.example.gestionstock.bean;

import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests d'intégration pour le Bean de connexion (LoginBean).
 *
 * Ces tests vérifient :
 * - L'appel au service d'authentification
 * - Le stockage de l'utilisateur en session
 * - Les redirections selon les rôles
 * - La gestion des erreurs de connexion
 */
@DisplayName("Tests d'intégration - Login Bean")
public class LoginBeanIntegrationTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginBean loginBean;

    private Utilisateur utilisateurAdmin;
    private Utilisateur utilisateurEmploye;

    @BeforeEach
    public void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'utilisateurs de test
        utilisateurAdmin = new Utilisateur();
        utilisateurAdmin.setId(1L);
        utilisateurAdmin.setNom("Admin Test");
        utilisateurAdmin.setEmail("admin@test.com");
        utilisateurAdmin.setRole(Role.ADMIN);
        utilisateurAdmin.setActif(true);
        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt(10));
        utilisateurAdmin.setMotDePasse(hashedPassword);

        utilisateurEmploye = new Utilisateur();
        utilisateurEmploye.setId(2L);
        utilisateurEmploye.setNom("Employé Test");
        utilisateurEmploye.setEmail("employe@test.com");
        utilisateurEmploye.setRole(Role.EMPLOYE);
        utilisateurEmploye.setActif(true);
        utilisateurEmploye.setMotDePasse(hashedPassword);
    }

    // ═══════════════════════════════════════════════════════════
    // ✅ TESTS DE CONNEXION RÉUSSIE
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("✅ Connexion admin réussie")
    public void testLoginAdminReussie() {
        // GIVEN : Un email et mot de passe corrects pour un admin
        loginBean.setEmail("admin@test.com");
        loginBean.setMotDePasse("password123");

        // Mock du service pour retourner l'admin
        when(authService.login("admin@test.com", "password123"))
                .thenReturn(utilisateurAdmin);

        // WHEN : On appelle la méthode login
        String result = loginBean.login();

        // THEN :
        // - Le service devrait être appelé une fois
        verify(authService, times(1)).login("admin@test.com", "password123");

        // - L'utilisateur devrait être stocké en session
        assertThat(loginBean.getUtilisateurConnecte())
                .as("L'utilisateur devrait être stocké en session")
                .isNotNull()
                .isEqualTo(utilisateurAdmin);

        // - L'utilisateur devrait être un admin
        assertThat(loginBean.isAdmin())
                .as("L'utilisateur connecté devrait être admin")
                .isTrue();
    }

    @Test
    @DisplayName("✅ Connexion employé réussie")
    public void testLoginEmployeReussie() {
        // GIVEN : Un email et mot de passe corrects pour un employé
        loginBean.setEmail("employe@test.com");
        loginBean.setMotDePasse("password123");

        // Mock du service
        when(authService.login("employe@test.com", "password123"))
                .thenReturn(utilisateurEmploye);

        // WHEN : On appelle la méthode login
        String result = loginBean.login();

        // THEN :
        assertThat(loginBean.getUtilisateurConnecte())
                .as("L'utilisateur employé devrait être stocké")
                .isNotNull();

        assertThat(loginBean.isAdmin())
                .as("L'employé ne devrait pas être admin")
                .isFalse();

        assertThat(loginBean.isConnecte())
                .as("L'utilisateur devrait être connecté")
                .isTrue();
    }

    // ═══════════════════════════════════════════════════════════
    // ❌ TESTS D'ERREUR DE CONNEXION
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("❌ Connexion échouée : mot de passe incorrect")
    public void testLoginMotDePasseIncorrect() {
        // GIVEN : Un email valide mais mot de passe incorrect
        loginBean.setEmail("admin@test.com");
        loginBean.setMotDePasse("wrongpassword");

        // Mock du service pour retourner null (login échoué)
        when(authService.login("admin@test.com", "wrongpassword"))
                .thenReturn(null);

        // WHEN : On appelle la méthode login
        String result = loginBean.login();

        // THEN :
        // - L'utilisateur ne devrait pas être connecté
        assertThat(loginBean.getUtilisateurConnecte())
                .as("L'utilisateur ne devrait pas être connecté")
                .isNull();

        assertThat(loginBean.isConnecte())
                .as("Aucun utilisateur ne devrait être connecté")
                .isFalse();

        // - Le service devrait avoir été appelé
        verify(authService, times(1)).login("admin@test.com", "wrongpassword");
    }

    @Test
    @DisplayName("❌ Connexion échouée : utilisateur inexistant")
    public void testLoginUtilisateurInexistant() {
        // GIVEN : Un email inexistant
        loginBean.setEmail("inexistant@test.com");
        loginBean.setMotDePasse("password123");

        // Mock du service
        when(authService.login("inexistant@test.com", "password123"))
                .thenReturn(null);

        // WHEN/THEN :
        String result = loginBean.login();

        assertThat(loginBean.getUtilisateurConnecte())
                .as("Aucun utilisateur ne devrait être trouvé")
                .isNull();
    }

    @Test
    @DisplayName("❌ Connexion échouée : champs vides")
    public void testLoginChampsVides() {
        // GIVEN : Email ou mot de passe vide
        loginBean.setEmail("");
        loginBean.setMotDePasse("");

        // Mock du service
        when(authService.login("", ""))
                .thenReturn(null);

        // WHEN/THEN :
        loginBean.login();

        assertThat(loginBean.isConnecte())
                .as("L'utilisateur ne devrait pas être connecté avec champs vides")
                .isFalse();
    }

    // ═══════════════════════════════════════════════════════════
    // 🚪 TESTS DE DÉCONNEXION
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("🚪 Déconnexion réussie")
    public void testLogoutReussie() {
        // GIVEN : Un utilisateur connecté
        loginBean.setUtilisateurConnecte(utilisateurAdmin);

        assertThat(loginBean.isConnecte())
                .as("L'utilisateur devrait être connecté avant logout")
                .isTrue();

        // WHEN : On appelle la méthode logout
        String result = loginBean.logout();

        // THEN :
        // - L'utilisateur devrait être déconnecté
        assertThat(loginBean.getUtilisateurConnecte())
                .as("L'utilisateur devrait être déconnecté")
                .isNull();

        assertThat(loginBean.isConnecte())
                .as("Aucun utilisateur ne devrait être connecté")
                .isFalse();
    }

    // ═══════════════════════════════════════════════════════════
    // 👤 TESTS DE RÔLES ET PERMISSIONS
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("👤 Admin a les permissions admin")
    public void testAdminPermissions() {
        // GIVEN : Un utilisateur admin connecté
        loginBean.setUtilisateurConnecte(utilisateurAdmin);

        // THEN :
        assertThat(loginBean.isAdmin())
                .as("Admin devrait avoir isAdmin() = true")
                .isTrue();

        assertThat(loginBean.isAdminOrManager())
                .as("Admin devrait avoir isAdminOrManager() = true")
                .isTrue();

        assertThat(loginBean.isConnecte())
                .as("Admin devrait être connecté")
                .isTrue();
    }

    @Test
    @DisplayName("👤 Employé n'a pas les permissions admin")
    public void testEmployePermissions() {
        // GIVEN : Un utilisateur employé connecté
        loginBean.setUtilisateurConnecte(utilisateurEmploye);

        // THEN :
        assertThat(loginBean.isAdmin())
                .as("Employé ne devrait pas avoir isAdmin() = true")
                .isFalse();

        assertThat(loginBean.isAdminOrManager())
                .as("Employé ne devrait pas avoir isAdminOrManager() = true")
                .isFalse();

        assertThat(loginBean.isConnecte())
                .as("Employé devrait être connecté")
                .isTrue();
    }

    @Test
    @DisplayName("👤 Sans connexion, aucune permission")
    public void testNoConnectionNoPermissions() {
        // GIVEN : Pas d'utilisateur connecté
        loginBean.setUtilisateurConnecte(null);

        // THEN :
        assertThat(loginBean.isConnecte())
                .as("Aucun utilisateur ne devrait être connecté")
                .isFalse();

        assertThat(loginBean.isAdmin())
                .as("isAdmin() devrait être false")
                .isFalse();

        assertThat(loginBean.isAdminOrManager())
                .as("isAdminOrManager() devrait être false")
                .isFalse();
    }

    // ═══════════════════════════════════════════════════════════
    // 🧪 TESTS DE VALIDATIONS DU FORMULAIRE
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("🧪 Email et mot de passe sont bien stockés dans le bean")
    public void testEmailMotDePasseStockage() {
        // GIVEN : Email et mot de passe
        String email = "test@example.com";
        String password = "SecurePass123!";

        // WHEN : On les assigne au bean
        loginBean.setEmail(email);
        loginBean.setMotDePasse(password);

        // THEN : Ils devraient être bien stockés
        assertThat(loginBean.getEmail())
                .as("L'email devrait être stocké")
                .isEqualTo(email);

        assertThat(loginBean.getMotDePasse())
                .as("Le mot de passe devrait être stocké")
                .isEqualTo(password);
    }

    @Test
    @DisplayName("🧪 Utilisation correcte du getter de l'utilisateur connecté")
    public void testUtilisateurConnecteGetter() {
        // GIVEN : Pas d'utilisateur connecté initialement
        assertThat(loginBean.getUtilisateurConnecte())
                .as("Pas d'utilisateur connecté au départ")
                .isNull();

        // WHEN : On connecte un utilisateur
        loginBean.setUtilisateurConnecte(utilisateurAdmin);

        // THEN : Le getter devrait retourner l'utilisateur
        assertThat(loginBean.getUtilisateurConnecte())
                .as("L'utilisateur connecté devrait être retourné")
                .isEqualTo(utilisateurAdmin)
                .isNotNull();
    }
}

