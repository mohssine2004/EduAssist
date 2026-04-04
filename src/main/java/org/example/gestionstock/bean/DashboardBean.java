package org.example.gestionstock.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.gestionstock.model.Stock;
import org.example.gestionstock.service.DashboardService;

import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class DashboardBean {

    @Inject
    private DashboardService dashboardService;

    private Map<String, Object> resume;
    private List<Stock> stocksFaibles;
    private List<Stock> produitsEnRupture;

    @PostConstruct
    public void init() {
        resume            = dashboardService.getResume();
        stocksFaibles     = dashboardService.getStocksFaibles();
        produitsEnRupture = dashboardService.getProduitsEnRupture();
    }

    // ── Getters ────────────────────────────────────────────
    public Map<String, Object> getResume() { return resume; }
    public List<Stock> getStocksFaibles() { return stocksFaibles; }
    public List<Stock> getProduitsEnRupture() { return produitsEnRupture; }
}