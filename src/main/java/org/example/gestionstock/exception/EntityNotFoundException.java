package org.example.gestionstock.exception;

/**
 * Levée quand une entité demandée n'existe pas en base de données.
 */
public class EntityNotFoundException extends RuntimeException {

    private final String entite;
    private final Object id;

    public EntityNotFoundException(String entite, Object id) {
        super(entite + " avec l'id " + id + " introuvable.");
        this.entite = entite;
        this.id     = id;
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.entite = null;
        this.id     = null;
    }

    public String getEntite() { return entite; }
    public Object getId()     { return id; }
}