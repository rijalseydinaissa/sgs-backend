CREATE TABLE customers (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    code VARCHAR(60) NOT NULL UNIQUE, name VARCHAR(150) NOT NULL,
    customer_type VARCHAR(20) NOT NULL DEFAULT 'INDIVIDUAL',
    phone VARCHAR(30), email VARCHAR(150), tax_number VARCHAR(50),
    address_street TEXT, address_city VARCHAR(100), address_region VARCHAR(100),
    address_postal_code VARCHAR(20), address_country VARCHAR(100) DEFAULT 'Sénégal',
    account_balance DECIMAL(15,2) DEFAULT 0, balance_currency VARCHAR(3) DEFAULT 'XOF',
    credit_limit DECIMAL(15,2), active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_customers_code ON customers(code);

CREATE TABLE purchase_orders (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    reference VARCHAR(60) NOT NULL UNIQUE,
    supplier_id UUID NOT NULL REFERENCES suppliers(id),
    order_date DATE NOT NULL, expected_delivery_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    total_amount DECIMAL(15,2), currency VARCHAR(3) DEFAULT 'XOF',
    notes TEXT
);
CREATE INDEX idx_po_reference ON purchase_orders(reference);
CREATE INDEX idx_po_supplier ON purchase_orders(supplier_id);

CREATE TABLE purchase_order_lines (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    purchase_order_id UUID NOT NULL REFERENCES purchase_orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity_value DECIMAL(15,3) NOT NULL, quantity_unit VARCHAR(20) NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL, currency VARCHAR(3) NOT NULL DEFAULT 'XOF'
);

CREATE TABLE sale_orders (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    reference VARCHAR(60) NOT NULL UNIQUE,
    customer_id UUID NOT NULL REFERENCES customers(id),
    order_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'QUOTE',
    total_amount DECIMAL(15,2), currency VARCHAR(3) DEFAULT 'XOF'
);

CREATE TABLE sale_order_lines (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), updated_at TIMESTAMP,
    created_by VARCHAR(100), updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0, deleted BOOLEAN NOT NULL DEFAULT FALSE,
    sale_order_id UUID NOT NULL REFERENCES sale_orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity_value DECIMAL(15,3) NOT NULL, quantity_unit VARCHAR(20) NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL, currency VARCHAR(3) NOT NULL DEFAULT 'XOF'
);

COMMENT ON TABLE purchase_orders IS 'Commandes fournisseurs';
COMMENT ON TABLE sale_orders IS 'Commandes clients / Devis';
