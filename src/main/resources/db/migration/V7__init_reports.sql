CREATE TABLE reports (
    id UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    file_name VARCHAR(100) NOT NULL UNIQUE,
    report_type VARCHAR(30) NOT NULL,
    report_format VARCHAR(20) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    generated_by_user_id UUID REFERENCES users(id),
    generated_at TIMESTAMP NOT NULL,
    parameters TEXT
);

CREATE INDEX idx_reports_type ON reports(report_type);
CREATE INDEX idx_reports_generated_at ON reports(generated_at);

COMMENT ON TABLE reports IS 'Historique des rapports générés (PDF, Excel, CSV)';
