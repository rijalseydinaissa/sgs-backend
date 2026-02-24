-- inventory_lines
ALTER TABLE inventory_lines
ALTER COLUMN counted_qty_unit TYPE SMALLINT
  USING counted_qty_unit::SMALLINT;

ALTER TABLE inventory_lines
ALTER COLUMN theoretical_qty_unit TYPE SMALLINT
  USING theoretical_qty_unit::SMALLINT;

-- invoice_lines
ALTER TABLE invoice_lines
ALTER COLUMN quantity_unit TYPE SMALLINT
  USING quantity_unit::SMALLINT;
