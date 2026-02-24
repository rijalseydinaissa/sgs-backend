-- products : supprimer les defaults d'abord
ALTER TABLE products ALTER COLUMN current_stock_unit DROP DEFAULT;
ALTER TABLE products ALTER COLUMN maximum_stock_unit DROP DEFAULT;
ALTER TABLE products ALTER COLUMN minimum_stock_unit DROP DEFAULT;
ALTER TABLE products ALTER COLUMN reorder_point_unit DROP DEFAULT;

-- puis convertir les types
ALTER TABLE products
ALTER COLUMN current_stock_unit TYPE smallint USING current_stock_unit::smallint,
  ALTER COLUMN maximum_stock_unit TYPE smallint USING maximum_stock_unit::smallint,
  ALTER COLUMN minimum_stock_unit TYPE smallint USING minimum_stock_unit::smallint,
  ALTER COLUMN reorder_point_unit TYPE smallint USING reorder_point_unit::smallint;

-- remettre les defaults si nécessaire (adaptez les valeurs)
ALTER TABLE products ALTER COLUMN current_stock_unit SET DEFAULT 0;
ALTER TABLE products ALTER COLUMN maximum_stock_unit SET DEFAULT 0;
ALTER TABLE products ALTER COLUMN minimum_stock_unit SET DEFAULT 0;
ALTER TABLE products ALTER COLUMN reorder_point_unit SET DEFAULT 0;

-- purchase_order_lines
ALTER TABLE purchase_order_lines ALTER COLUMN quantity_unit DROP DEFAULT;
ALTER TABLE purchase_order_lines ALTER COLUMN quantity_unit TYPE smallint USING quantity_unit::smallint;

-- sale_order_lines
ALTER TABLE sale_order_lines ALTER COLUMN quantity_unit DROP DEFAULT;
ALTER TABLE sale_order_lines ALTER COLUMN quantity_unit TYPE smallint USING quantity_unit::smallint;

-- stock_movements
ALTER TABLE stock_movements ALTER COLUMN quantity_unit DROP DEFAULT;
ALTER TABLE stock_movements ALTER COLUMN quantity_unit TYPE smallint USING quantity_unit::smallint;