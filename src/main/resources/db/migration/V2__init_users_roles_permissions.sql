-- ══════════════════════════════════════════════════════════════════
--  SGS — V2 : Users, Roles, Permissions
--  IMPORTANT : User/Role/Permission étendent BaseEntity
--  → les colonnes audit (created_at, created_by, etc.) sont incluses
-- ══════════════════════════════════════════════════════════════════

CREATE TABLE permissions (
    id          UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    name        VARCHAR(60)  NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE roles (
    id          UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    name        VARCHAR(40)  NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE role_permissions (
    role_id       UUID NOT NULL REFERENCES roles(id)       ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id                    UUID         NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at            TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP,
    created_by            VARCHAR(100),
    updated_by            VARCHAR(100),
    version               BIGINT       NOT NULL DEFAULT 0,
    deleted               BOOLEAN      NOT NULL DEFAULT FALSE,
    username              VARCHAR(60)  NOT NULL UNIQUE,
    email                 VARCHAR(150) NOT NULL UNIQUE,
    password              VARCHAR(255) NOT NULL,
    first_name            VARCHAR(80),
    last_name             VARCHAR(80),
    phone                 VARCHAR(30),
    status                VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    two_factor_enabled    BOOLEAN      NOT NULL DEFAULT FALSE,
    two_factor_secret     VARCHAR(255),
    failed_login_attempts INT          NOT NULL DEFAULT 0,
    locked_until          TIMESTAMP,
    last_login_at         TIMESTAMP,
    last_login_ip         VARCHAR(45),
    password_changed_at   TIMESTAMP,
    site_id               UUID REFERENCES sites(id)
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email    ON users(email);
CREATE INDEX idx_users_status   ON users(status);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ── Seed permissions ──────────────────────────────────────────────
INSERT INTO permissions (name, description) VALUES
('PRODUCT_READ','Consulter les produits'),('PRODUCT_CREATE','Créer'),
('PRODUCT_UPDATE','Modifier'),('PRODUCT_DELETE','Supprimer'),
('PRODUCT_IMPORT','Importer CSV'),('PRODUCT_EXPORT','Exporter'),
('STOCK_READ','Consulter stocks'),('STOCK_ENTRY','Entrée stock'),
('STOCK_EXIT','Sortie stock'),('STOCK_TRANSFER','Transfert inter-sites'),
('STOCK_ADJUST','Ajustement'),('STOCK_INVENTORY','Inventaire'),
('EXPENSE_READ','Consulter dépenses'),('EXPENSE_CREATE','Créer dépense'),
('EXPENSE_UPDATE','Modifier dépense'),('EXPENSE_APPROVE','Approuver'),
('EXPENSE_REJECT','Rejeter'),('EXPENSE_EXPORT','Exporter'),
('PURCHASE_ORDER_READ','Consulter commandes frs'),('PURCHASE_ORDER_CREATE','Créer commande frs'),
('PURCHASE_ORDER_RECEIVE','Réceptionner'),('PURCHASE_ORDER_CANCEL','Annuler'),
('SALE_ORDER_READ','Consulter ventes'),('SALE_ORDER_CREATE','Créer vente'),
('INVOICE_READ','Consulter factures'),('INVOICE_CREATE','Créer facture'),
('INVOICE_SEND','Envoyer facture'),
('CUSTOMER_READ','Consulter clients'),('CUSTOMER_CREATE','Créer client'),('CUSTOMER_UPDATE','Modifier client'),
('REPORT_STOCK','Rapport stock'),('REPORT_FINANCIAL','Rapport financier'),
('REPORT_EXPENSE','Rapport dépenses'),('REPORT_SALES','Rapport ventes'),
('USER_READ','Consulter users'),('USER_CREATE','Créer user'),
('USER_UPDATE','Modifier user'),('USER_DELETE','Supprimer user'),
('ROLE_MANAGE','Gérer rôles'),('SITE_MANAGE','Gérer sites'),
('CONFIG_MANAGE','Configuration'),('AUDIT_LOG_READ','Journaux audit');

-- ── Seed rôles ────────────────────────────────────────────────────
INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN',            'Administrateur — accès total'),
('ROLE_STOCK_MANAGER',    'Responsable stock'),
('ROLE_PURCHASE_MANAGER', 'Responsable achats'),
('ROLE_SALES',            'Commercial'),
('ROLE_ACCOUNTANT',       'Comptable'),
('ROLE_AUDITOR',          'Auditeur — lecture seule');

-- ADMIN → toutes permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p WHERE r.name = 'ROLE_ADMIN';

-- STOCK_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN
('PRODUCT_READ','STOCK_READ','STOCK_ENTRY','STOCK_EXIT','STOCK_TRANSFER','STOCK_ADJUST','STOCK_INVENTORY','REPORT_STOCK')
WHERE r.name = 'ROLE_STOCK_MANAGER';

-- PURCHASE_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN
('PRODUCT_READ','STOCK_READ','STOCK_ENTRY','PURCHASE_ORDER_READ','PURCHASE_ORDER_CREATE','PURCHASE_ORDER_RECEIVE','PURCHASE_ORDER_CANCEL','REPORT_STOCK')
WHERE r.name = 'ROLE_PURCHASE_MANAGER';

-- SALES
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN
('PRODUCT_READ','STOCK_READ','STOCK_EXIT','SALE_ORDER_READ','SALE_ORDER_CREATE','INVOICE_READ','INVOICE_CREATE','INVOICE_SEND','CUSTOMER_READ','CUSTOMER_CREATE','CUSTOMER_UPDATE','REPORT_SALES')
WHERE r.name = 'ROLE_SALES';

-- ACCOUNTANT
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN
('EXPENSE_READ','EXPENSE_CREATE','EXPENSE_UPDATE','EXPENSE_APPROVE','EXPENSE_REJECT','EXPENSE_EXPORT','INVOICE_READ','REPORT_FINANCIAL','REPORT_EXPENSE','REPORT_SALES')
WHERE r.name = 'ROLE_ACCOUNTANT';

-- AUDITOR
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN
('PRODUCT_READ','STOCK_READ','EXPENSE_READ','PURCHASE_ORDER_READ','SALE_ORDER_READ','INVOICE_READ','CUSTOMER_READ','REPORT_STOCK','REPORT_FINANCIAL','REPORT_EXPENSE','REPORT_SALES','AUDIT_LOG_READ')
WHERE r.name = 'ROLE_AUDITOR';

-- ── Admin par défaut : admin / Admin@1234 ─────────────────────────
INSERT INTO users (username, email, password, first_name, last_name, status, created_by)
-- ✅ PAR ÇA (hash BCrypt de "Admin@1234")
VALUES ('admin','admin@sgs.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','Super','Admin','ACTIVE','SYSTEM');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u CROSS JOIN roles r WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';
