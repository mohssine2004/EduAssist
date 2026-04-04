package org.example.gestionstock.dao;

import org.example.gestionstock.model.Stock;
import java.util.List;

public interface StockDAO extends GenericDAO<Stock, Long> {
    Stock findByProduit(Long produitId);
    List<Stock> findStocksFaibles();
    List<Stock> findEnRupture();
}