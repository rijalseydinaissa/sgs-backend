-- ═══════════════════════════════════════════════════════════════════
--  SGS — V1 : Schéma de base — extensions et commentaires
--  Sprint 1 — Squelette initial
-- ═══════════════════════════════════════════════════════════════════

-- Extension UUID pour PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";   -- Recherche full-text optimisée

-- ── Table sites (nécessaire comme FK dans les autres tables) ──────
CREATE TABLE sites (
    id          UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,

    name        VARCHAR(200) NOT NULL,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    type        VARCHAR(30)  NOT NULL DEFAULT 'STORE',   -- STORE | WAREHOUSE | DEPOT
    address     TEXT,
    city        VARCHAR(100),
    country     VARCHAR(100) DEFAULT 'Sénégal',
    phone       VARCHAR(30),
    email       VARCHAR(150),
    active      BOOLEAN      NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE sites IS 'Sites/magasins/entrepôts de l''entreprise';
COMMENT ON COLUMN sites.code IS 'Code unique du site (ex: DAKAR-01)';
COMMENT ON COLUMN sites.type IS 'Type : STORE=Magasin, WAREHOUSE=Entrepôt, DEPOT=Dépôt';

-- Insérer le site par défaut
INSERT INTO sites (id, name, code, type, city, active, created_by)
VALUES (uuid_generate_v4(), 'Siège Principal', 'SIEGE-01', 'STORE', 'Dakar', true, 'SYSTEM');
