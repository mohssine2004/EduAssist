package org.example.gestionstock.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.example.gestionstock.model.Categorie;
import org.example.gestionstock.service.CategorieService;

@FacesConverter(value = "categorieConverter", managed = true)
public class CategorieConverter implements Converter<Categorie> {

    @Inject
    private CategorieService categorieService;

    @Override
    public Categorie getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return categorieService.findById(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Categorie categorie) {
        if (categorie == null || categorie.getId() == null) return "";
        return String.valueOf(categorie.getId());
    }
}