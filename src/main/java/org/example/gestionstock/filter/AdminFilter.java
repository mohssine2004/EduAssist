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
 * AdminFilter — Protège les pages réservées à l'Admin uniquement.
 * Pages concernées : /pages/users/*
 */
@WebFilter("/pages/users/*")
public class AdminFilter implements Filter {

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
            // Non connecté → redirige vers login
            response.sendRedirect(request.getContextPath() + "/pages/login.xhtml");

        } else if (utilisateur.getRole() != Role.ADMIN) {
            // Connecté mais pas Admin → redirige vers dashboard
            response.sendRedirect(request.getContextPath() + "/pages/dashboard.xhtml");

        } else {
            // Admin → laisse passer
            chain.doFilter(req, res);
        }
    }
}