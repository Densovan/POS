-- V8: Add audited created_by/updated_by columns to all BaseEntity tables.
-- Columns are nullable: public endpoints (e.g. /auth/register) persist without an
-- authenticated user, and legacy rows predate this change. Values are populated by
-- Spring Data JPA auditing (see JpaAuditingConfig / AppUserPrincipal).

ALTER TABLE categories            ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE categories            ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE brands                ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE brands                ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE tax_classes           ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE tax_classes           ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE products              ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE products              ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE product_variants      ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE product_variants      ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE users                 ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE users                 ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE expenses              ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE expenses              ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE expense_categories    ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE expense_categories    ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE payments              ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE payments              ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE sales                 ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE sales                 ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE sale_items            ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE sale_items            ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE suppliers             ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE suppliers             ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE purchase_orders       ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE purchase_orders       ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE purchase_order_items  ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE purchase_order_items  ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE purchase_receipts     ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE purchase_receipts     ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE purchase_receipt_items ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE purchase_receipt_items ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE supplier_payments     ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE supplier_payments     ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE inventory             ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE inventory             ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE batches               ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE batches               ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE serial_numbers        ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE serial_numbers        ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE stock_counts          ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE stock_counts          ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE stock_count_items     ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE stock_count_items     ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE stock_adjustments     ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE stock_adjustments     ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE warehouses            ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE warehouses            ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE employees             ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE employees             ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE shifts                ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE shifts                ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE customers             ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE customers             ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);

ALTER TABLE customer_addresses    ADD COLUMN IF NOT EXISTS created_by UUID REFERENCES users(id);
ALTER TABLE customer_addresses    ADD COLUMN IF NOT EXISTS updated_by UUID REFERENCES users(id);
