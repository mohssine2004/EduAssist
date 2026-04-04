package org.example.gestionstock.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.gestionstock.model.Role;
import org.example.gestionstock.model.Utilisateur;

import java.io.IOException;

/**
 * ManagerFilter — Protège les pages réservées à Admin + Manager.
 * Pages concernées : /pages/produits/* et /pages/categories/*
 * Un Employé qui essaie d'accéder à ces pages sera redirigé.
 */
@WebFilter({"/pages/produits/*", "/pages/categories/*"})
public class ManagerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession         session  = request.getSession(false);

        Utilisateur utilisateur = (session != null)
                ? (Utilisateur) session.getAttribute("utilisateurConnecte")
                : null;

        if (utilisateur == null) {
            // Non connecté → login
            response.sendRedirect(request.getContextPath() + "/pages/login.xhtml");

        } else if (utilisateur.getRole() == Role.EMPLOYE) {
            // Employé → redirige vers la page stock (lecture seule)
            response.sendRedirect(request.getContextPath() + "/pages/stock/list.xhtml");

        } else {
            // Admin ou Manager → laisse passer
            chain.doFilter(req, res);
        }
    }
}