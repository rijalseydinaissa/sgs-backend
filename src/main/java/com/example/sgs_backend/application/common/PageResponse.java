package com.example.sgs_backend.application.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Réponse paginée générique.
 * Enveloppe les résultats avec les métadonnées de pagination.
 *
 * @param <T> Type de contenu de la page
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    /** Construit un PageResponse depuis un Page Spring Data */
    public static <T> PageResponse<T> of(Page<T> springPage) {
        return new PageResponse<>(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.isFirst(),
                springPage.isLast()
        );
    }
}
