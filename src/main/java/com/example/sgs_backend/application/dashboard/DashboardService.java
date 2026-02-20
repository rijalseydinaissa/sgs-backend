package com.example.sgs_backend.application.dashboard;

import com.example.sgs_backend.application.dashboard.dto.DashboardKPIs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service Dashboard — calcule les KPIs
 * Résultats mis en cache (refresh toutes les heures via @Scheduled)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    @Cacheable(value = "dashboardKPIs", unless = "#result == null")
    public DashboardKPIs getKPIs() {
        log.info("Calcul KPIs dashboard (non caché)");
        
        // TODO: Requêtes optimisées sur BDD
        // Exemple de calculs réels à implémenter:
        // - SELECT SUM(total) FROM invoices WHERE DATE(created_at) = CURRENT_DATE
        // - SELECT COUNT(*) FROM products WHERE current_stock <= minimum_stock
        
        return new DashboardKPIs(
                BigDecimal.ZERO,      // todaySales
                BigDecimal.ZERO,      // weekSales
                BigDecimal.ZERO,      // monthSales
                0,                    // todayOrdersCount
                List.of(),            // topProducts
                0,                    // lowStockCount
                0,                    // outOfStockCount
                BigDecimal.ZERO,      // totalStockValue
                BigDecimal.ZERO,      // monthExpenses
                BigDecimal.ZERO,      // approvedExpenses
                BigDecimal.ZERO,      // pendingExpenses
                BigDecimal.ZERO,      // monthProfit
                0.0,                  // profitMargin
                0,                    // activeCustomers
                0,                    // pendingOrders
                0                     // overdueInvoices
        );
    }
}
