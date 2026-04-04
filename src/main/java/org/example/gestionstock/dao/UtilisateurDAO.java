package org.example.gestionstock.dao;

import org.example.gestionstock.model.Utilisateur;
import org.example.gestionstock.model.Role;
import java.util.List;

public interface UtilisateurDAO extends GenericDAO<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
    boolean existsByEmail(String email);
}