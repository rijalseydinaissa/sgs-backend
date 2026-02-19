package com.example.sgs_backend.domain.product;

public enum StockEvaluationMethod {
    FIFO,             // Premier entré, premier sorti
    LIFO,             // Dernier entré, premier sorti
    WEIGHTED_AVERAGE  // Prix moyen pondéré (PMP)
}
