package com.example.sgs_backend.application.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * KPIs temps réel pour le dashboard
 */
public record DashboardKPIs(
    // Ventes
    BigDecimal todaySales,
    BigDecimal weekSales,
    BigDecimal monthSales,
    int todayOrdersCount,
    List<TopProduct> topProducts,
    
    // Stock
    int lowStockCount,
    int outOfStockCount,
    BigDecimal totalStockValue,
    
    // Dépenses
    BigDecimal monthExpenses,
    BigDecimal approvedExpenses,
    BigDecimal pendingExpenses,
    
    // Rentabilité
    BigDecimal monthProfit,
    double profitMargin,
    
    // Clients & Commandes
    int activeCustomers,
    int pendingOrders,
    int overdueInvoices
) {}
