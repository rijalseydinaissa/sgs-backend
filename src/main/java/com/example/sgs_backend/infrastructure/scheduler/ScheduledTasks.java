package com.example.sgs_backend.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tâches planifiées
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final CacheManager cacheManager;

    /**
     * Calcul KPIs dashboard - toutes les heures
     */
    @Scheduled(cron = "0 0 * * * *")
    public void refreshDashboardKPIs() {
        log.info("=== Refresh KPIs dashboard ===");
        cacheManager.getCache("dashboardKPIs").clear();
    }

    /**
     * Alerte stock faible - tous les jours à 8h
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkLowStock() {
        log.info("=== Vérification stock faible ===");
        // TODO: 
        // 1. Requête produits avec stock <= seuil
        // 2. Générer email alerte pour managers
        // 3. Créer StockAlert en BDD
    }

    /**
     * Rappel factures impayées - tous les jours à 9h
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueInvoices() {
        log.info("=== Vérification factures impayées ===");
        // TODO:
        // 1. Requête invoices avec dueDate < NOW et status != PAID
        // 2. Envoyer email/SMS rappel clients
        // 3. Logger les rappels envoyés
    }

    /**
     * Inventaire automatique mensuel - 1er du mois à 6h
     */
    @Scheduled(cron = "0 0 6 1 * *")
    public void monthlyInventory() {
        log.info("=== Inventaire automatique mensuel ===");
        // TODO:
        // 1. Créer Inventory pour chaque site
        // 2. Générer InventoryLine pour tous les produits
        // 3. Envoyer notification aux responsables
    }

    /**
     * Backup BDD - tous les dimanches à 2h
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    public void backupDatabase() {
        log.info("=== Backup base de données ===");
        // TODO:
        // 1. pg_dump vers fichier
        // 2. Upload vers S3/backup serveur
        // 3. Notification admins si échec
    }
}
