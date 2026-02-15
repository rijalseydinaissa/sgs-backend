package com.example.sgs_backend.application.common;


import com.example.sgs_backend.domain.common.BaseEntity;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service abstrait générique qui implémente le CRUD standard une seule fois.
 *
 * Les services spécifiques (ProductService, ExpenseService...) étendent cette classe
 * et n'implémentent que la logique propre à leur domaine.
 *
 * Design Pattern : Template Method
 *
 * @param <T>   Entité domaine (extends BaseEntity)
 * @param <ID>  Type identifiant
 * @param <REQ> DTO de requête (création/modification)
 * @param <RES> DTO de réponse
 */
@Slf4j
public abstract class BaseService<T extends BaseEntity, ID, REQ, RES> {

    // ── Méthodes abstraites à implémenter par les sous-classes ────────────────

    protected abstract BaseRepository<T, ID> getRepository();

    protected abstract T toEntity(REQ request);

    protected abstract RES toResponse(T entity);

    protected abstract void updateEntity(T entity, REQ request);

    /** Nom de l'entité pour les messages d'erreur */
    protected abstract String getEntityName();

    // ── CRUD générique ────────────────────────────────────────────────────────

    @Transactional
    public RES create(REQ request) {
        log.info("Création de {} en cours...", getEntityName());
        T entity = toEntity(request);
        T saved = getRepository().save(entity);
        log.info("{} créé avec id={}", getEntityName(), saved.getId());
        return toResponse(saved);
    }

    /**
     * Lecture seule — readOnly=true active les optimisations Hibernate :
     * pas de dirty checking, mode flush NEVER → performances meilleures.
     */
    @Transactional(readOnly = true)
    public RES findById(ID id) {
        return getRepository()
                .findByIdAndDeletedFalse(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id));
    }

    @Transactional(readOnly = true)
    public PageResponse<RES> findAll(Pageable pageable) {
        Page<RES> page = getRepository()
                .findAllByDeletedFalse(pageable)
                .map(this::toResponse);
        return PageResponse.of(page);
    }

    @Transactional
    public RES update(ID id, REQ request) {
        log.info("Mise à jour de {} id={}", getEntityName(), id);
        T entity = getRepository()
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id));
        updateEntity(entity, request);
        T saved = getRepository().save(entity);
        return toResponse(saved);
    }

    /**
     * Soft delete — marque l'entité comme supprimée sans DELETE SQL.
     * Les données restent en base pour l'audit et la traçabilité.
     */
    @Transactional
    public void delete(ID id) {
        log.info("Suppression (soft) de {} id={}", getEntityName(), id);
        if (!getRepository().existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException(getEntityName(), id);
        }
        getRepository().softDelete(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return getRepository().countByDeletedFalse();
    }
}
