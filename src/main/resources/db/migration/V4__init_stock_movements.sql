-- ══════════════════════════════════════════════════════════════════
--  SGS — V4 : Mouvements de stock, inventaires, alertes
--  Utilise Quantity VO (value + unit)
-- ══════════════════════════════════════════════════════════════════

CREATE TABLE stock_movements (
    id            UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP,
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT       NOT NULL DEFAULT 0,
    deleted       BOOLEAN      NOT NULL DEFAULT FALSE,

    product_id       UUID         NOT NULL REFERENCES products(id),
    movement_type    VARCHAR(20)  NOT NULL,
    quantity_value   DECIMAL(15,3) NOT NULL,
    quantity_unit    VARCHAR(20)   NOT NULL,
    movement_date    TIMESTAMP    NOT NULL DEFAULT NOW(),
    reference        VARCHAR(80),
    notes            TEXT,
    from_site_id     UUID         REFERENCES sites(id),
    to_site_id       UUID         REFERENCES sites(id),
    user_id          UUID         REFERENCES users(id),
    stock_before     DECIMAL(15,3),
    stock_after      DECIMAL(15,3)
);
CREATE INDEX idx_movements_product ON stock_movements(product_id);
CREATE INDEX idx_movements_date    ON stock_movements(movement_date);
CREATE INDEX idx_movements_type    ON stock_movements(movement_type);

CREATE TABLE inventories (
    id                  UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT       NOT NULL DEFAULT 0,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    reference           VARCHAR(60)  NOT NULL UNIQUE,
    site_id             UUID         NOT NULL REFERENCES sites(id),
    inventory_date      DATE         NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    responsible_user_id UUID         REFERENCES users(id),
    notes               TEXT
);
CREATE INDEX idx_inventories_site   ON inventories(site_id);
CREATE INDEX idx_inventories_date   ON inventories(inventory_date);
CREATE INDEX idx_inventories_status ON inventories(status);

CREATE TABLE inventory_lines (
    id                     UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at             TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP,
    created_by             VARCHAR(100),
    updated_by             VARCHAR(100),
    version                BIGINT       NOT NULL DEFAULT 0,
    deleted                BOOLEAN      NOT NULL DEFAULT FALSE,
    inventory_id           UUID         NOT NULL REFERENCES inventories(id) ON DELETE CASCADE,
    product_id             UUID         NOT NULL REFERENCES products(id),
    theoretical_qty_value  DECIMAL(15,3),
    theoretical_qty_unit   VARCHAR(20),
    counted_qty_value      DECIMAL(15,3),
    counted_qty_unit       VARCHAR(20),
    notes                  VARCHAR(200)
);

CREATE TABLE stock_alerts (
    id                  UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT       NOT NULL DEFAULT 0,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    product_id          UUID         NOT NULL REFERENCES products(id),
    alert_type          VARCHAR(30)  NOT NULL,
    message             VARCHAR(255) NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    triggered_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    resolved_at         TIMESTAMP,
    resolved_by_user_id UUID         REFERENCES users(id)
);
CREATE INDEX idx_alerts_product ON stock_alerts(product_id);
CREATE INDEX idx_alerts_type    ON stock_alerts(alert_type);
CREATE INDEX idx_alerts_status  ON stock_alerts(status);

COMMENT ON TABLE stock_movements IS 'Tous les mouvements de stock (entrées/sorties/transferts)';
COMMENT ON TABLE inventories IS 'Sessions d''inventaire physique';
COMMENT ON TABLE stock_alerts IS 'Alertes stock générées automatiquement';
