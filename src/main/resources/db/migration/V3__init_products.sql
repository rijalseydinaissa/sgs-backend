-- ══════════════════════════════════════════════════════════════════
--  SGS — V3 CORRIGÉ : Produits utilisent Money VO et Quantity VO
-- ══════════════════════════════════════════════════════════════════

CREATE TABLE product_categories (
    id          UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(300),
    icon_url    VARCHAR(255),
    parent_id   UUID REFERENCES product_categories(id),
    active      BOOLEAN      NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_categories_code      ON product_categories(code);
CREATE INDEX idx_categories_parent_id ON product_categories(parent_id);

CREATE TABLE suppliers (
    id                  UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT       NOT NULL DEFAULT 0,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    code                VARCHAR(50)  NOT NULL UNIQUE,
    name                VARCHAR(150) NOT NULL,
    phone               VARCHAR(30),
    email               VARCHAR(150),
    contact_person      VARCHAR(100),
    tax_number          VARCHAR(50),
    address_street      TEXT,
    address_city        VARCHAR(100),
    address_region      VARCHAR(100),
    address_postal_code VARCHAR(20),
    address_country     VARCHAR(100) DEFAULT 'Sénégal',
    payment_terms_days  INT          DEFAULT 30,
    average_rating      DECIMAL(3,2),
    active              BOOLEAN      NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_suppliers_code  ON suppliers(code);
CREATE INDEX idx_suppliers_email ON suppliers(email);

-- ✅ CORRIGÉ : colonnes Money VO (amount + currency) et Quantity VO (value + unit)
CREATE TABLE products (
    id                UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    version           BIGINT       NOT NULL DEFAULT 0,
    deleted           BOOLEAN      NOT NULL DEFAULT FALSE,

    reference         VARCHAR(60)  NOT NULL UNIQUE,
    name              VARCHAR(200) NOT NULL,
    description       TEXT,
    barcode           VARCHAR(50)  UNIQUE,
    image_url         VARCHAR(255),

    category_id       UUID         NOT NULL REFERENCES product_categories(id),
    main_supplier_id  UUID         REFERENCES suppliers(id),
    site_id           UUID         REFERENCES sites(id),

    -- Money VO @Embedded
    purchase_price     DECIMAL(15,2),
    purchase_currency  VARCHAR(3),
    selling_price      DECIMAL(15,2) NOT NULL,
    selling_currency   VARCHAR(3)    NOT NULL DEFAULT 'XOF',

    -- Quantity VO @Embedded (supporte décimales : 2.5 kg, 1.8 L...)
    current_stock_value DECIMAL(15,3) NOT NULL DEFAULT 0,
    current_stock_unit  VARCHAR(20)   NOT NULL DEFAULT 'PIECE',
    minimum_stock_value DECIMAL(15,3) NOT NULL DEFAULT 0,
    minimum_stock_unit  VARCHAR(20)   NOT NULL DEFAULT 'PIECE',
    maximum_stock_value DECIMAL(15,3),
    maximum_stock_unit  VARCHAR(20),
    reorder_point_value DECIMAL(15,3),
    reorder_point_unit  VARCHAR(20),

    evaluation_method VARCHAR(20)  NOT NULL DEFAULT 'WEIGHTED_AVERAGE',
    has_expiry_date   BOOLEAN      NOT NULL DEFAULT FALSE,
    expiry_alert_days INT          DEFAULT 30,
    status            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT chk_positive_selling CHECK (selling_price > 0),
    CONSTRAINT chk_nonneg_stock     CHECK (current_stock_value >= 0),
    CONSTRAINT chk_nonneg_min       CHECK (minimum_stock_value >= 0)
);
CREATE INDEX idx_products_reference ON products(reference);
CREATE INDEX idx_products_barcode   ON products(barcode);
CREATE INDEX idx_products_category  ON products(category_id);
CREATE INDEX idx_products_status    ON products(status);
CREATE INDEX idx_products_site      ON products(site_id);

CREATE TABLE product_variants (
    id               UUID        NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT      NOT NULL DEFAULT 0,
    deleted          BOOLEAN     NOT NULL DEFAULT FALSE,
    product_id       UUID        NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    sku              VARCHAR(80) NOT NULL UNIQUE,
    attribute_name   VARCHAR(50),
    attribute_value  VARCHAR(100),
    price_adjustment DECIMAL(15,2) DEFAULT 0,
    current_stock    INT         NOT NULL DEFAULT 0,
    barcode          VARCHAR(50) UNIQUE,
    active           BOOLEAN     NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_variants_product ON product_variants(product_id);
CREATE INDEX idx_variants_sku     ON product_variants(sku);

CREATE TABLE product_suppliers (
    id                 UUID        NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at         TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    created_by         VARCHAR(100),
    updated_by         VARCHAR(100),
    version            BIGINT      NOT NULL DEFAULT 0,
    deleted            BOOLEAN     NOT NULL DEFAULT FALSE,
    product_id         UUID        NOT NULL REFERENCES products(id),
    supplier_id        UUID        NOT NULL REFERENCES suppliers(id),
    supplier_reference VARCHAR(80),
    unit_cost          DECIMAL(15,2),
    min_order_qty      INT         DEFAULT 1,
    lead_time_days     INT,
    is_preferred       BOOLEAN     NOT NULL DEFAULT FALSE,
    UNIQUE (product_id, supplier_id)
);
CREATE INDEX idx_ps_product  ON product_suppliers(product_id);
CREATE INDEX idx_ps_supplier ON product_suppliers(supplier_id);

CREATE TABLE barcodes (
    id            UUID        NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at    TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP,
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT      NOT NULL DEFAULT 0,
    deleted       BOOLEAN     NOT NULL DEFAULT FALSE,
    code          VARCHAR(50) NOT NULL UNIQUE,
    barcode_type  VARCHAR(20) NOT NULL,
    product_id    UUID        REFERENCES products(id),
    label_printed BOOLEAN     NOT NULL DEFAULT FALSE,
    print_count   INT         NOT NULL DEFAULT 0
);

-- Seed catégories
INSERT INTO product_categories (code, name, description, created_by) VALUES
('ALIM',       'Alimentation',       'Produits alimentaires',      'SYSTEM'),
('ALIM-BOISS', 'Boissons',           'Eau, jus, sodas',            'SYSTEM'),
('ALIM-EPIC',  'Épicerie',           'Huile, riz, sucre...',       'SYSTEM'),
('HYGIENE',    'Hygiène & Beauté',   'Savon, shampoing...',        'SYSTEM'),
('MEUBLES',    'Meubles',            'Mobilier',                   'SYSTEM'),
('ELECT',      'Électronique',       'Téléphones, PC...',          'SYSTEM'),
('TEXTILE',    'Textile',            'Vêtements',                  'SYSTEM'),
('PHARMA',     'Pharmacie',          'Médicaments non prescrits',  'SYSTEM'),
('BUREAU',     'Fournitures bureau', 'Papeterie',                  'SYSTEM'),
('DIVERS',     'Divers',             'Autres',                     'SYSTEM');

UPDATE product_categories SET parent_id = (SELECT id FROM product_categories WHERE code = 'ALIM')
WHERE code IN ('ALIM-BOISS', 'ALIM-EPIC');
