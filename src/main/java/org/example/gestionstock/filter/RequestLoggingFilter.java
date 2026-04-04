package org.example.gestionstock.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Petit filtre de debug : logge toutes les requêtes entrantes (méthode + URI + remoteAddr)
 * Permet de savoir si la requête atteint l'application ou est bloquée en amont.
 */
@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            System.out.println("[RequestLoggingFilter] " + req.getMethod() + " " + req.getRequestURI() + " from " + request.getRemoteAddr());
        } catch (Exception ex) {
            System.out.println("[RequestLoggingFilter] erreur lors du logging: " + ex.getMessage());
        }
        chain.doFilter(request, response);
    }
}

