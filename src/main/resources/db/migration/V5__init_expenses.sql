CREATE TABLE expense_categories (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    code VARCHAR(50) NOT NULL UNIQUE, name VARCHAR(150) NOT NULL,
    description VARCHAR(300), parent_id UUID REFERENCES expense_categories(id),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE expenses (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    reference VARCHAR(60) NOT NULL UNIQUE, description VARCHAR(200) NOT NULL,
    amount DECIMAL(15,2) NOT NULL, currency VARCHAR(3) NOT NULL DEFAULT 'XOF',
    expense_date DATE NOT NULL,
    category_id UUID NOT NULL REFERENCES expense_categories(id),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    payment_method VARCHAR(20), payment_date DATE,
    supplier_id UUID REFERENCES suppliers(id),
    submitted_by_user_id UUID REFERENCES users(id),
    approved_by_user_id UUID REFERENCES users(id),
    approved_at DATE, rejection_reason VARCHAR(500),
    notes TEXT, receipt_url VARCHAR(255), site_id UUID REFERENCES sites(id)
);
CREATE INDEX idx_expenses_reference ON expenses(reference);
CREATE INDEX idx_expenses_category ON expenses(category_id);
CREATE INDEX idx_expenses_status ON expenses(status);

CREATE TABLE budgets (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    name VARCHAR(60) NOT NULL, category_id UUID NOT NULL REFERENCES expense_categories(id),
    period_type VARCHAR(20) NOT NULL, period_start DATE NOT NULL, period_end DATE NOT NULL,
    allocated_amount DECIMAL(15,2) NOT NULL, currency VARCHAR(3) NOT NULL DEFAULT 'XOF',
    site_id UUID, notes TEXT
);

INSERT INTO expense_categories (code, name, description, created_by) VALUES
('LOYER',    'Loyer',               'Loyer du local', 'SYSTEM'),
('SALAIRES', 'Salaires',            'Paie du personnel', 'SYSTEM'),
('ACHATS',   'Achats marchandises', 'Approvisionnement produits', 'SYSTEM'),
('TRANSPORT','Transport',           'Déplacements, livraisons', 'SYSTEM'),
('EAU_ELEC', 'Eau & Électricité',   'Factures eau et électricité', 'SYSTEM'),
('MARKETING','Marketing',           'Publicité, communication', 'SYSTEM'),
('FOURNITURE','Fournitures bureau', 'Papeterie, matériel', 'SYSTEM'),
('ENTRETIEN','Entretien',           'Réparations, maintenance', 'SYSTEM'),
('IMPOTS',   'Impôts & Taxes',      'Impôts, taxes diverses', 'SYSTEM'),
('DIVERS',   'Divers',              'Autres dépenses', 'SYSTEM');
