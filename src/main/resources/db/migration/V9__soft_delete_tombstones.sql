-- V9: Add soft-delete tombstone columns (deleted_at/deleted_by) to all BaseEntity tables.
-- The is_deleted/deleted_at/deleted_by pattern replaces the previous is_deleted boolean
-- used only on sales; all 28 base tables now use null deleted_at to mean "not deleted".
-- Columns are nullable so legacy rows remain active.

ALTER TABLE categories             ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE categories             ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE brands                 ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE brands                 ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE tax_classes            ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE tax_classes            ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE products               ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE products               ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE product_variants       ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE product_variants       ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE users                  ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE users                  ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE expenses               ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE expenses               ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE expense_categories     ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE expense_categories     ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE payments               ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE payments               ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE sales                  ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE sales                  ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE sale_items             ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE sale_items             ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE suppliers              ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE suppliers              ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE purchase_orders        ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE purchase_orders        ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE purchase_order_items   ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE purchase_order_items   ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE purchase_receipts      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE purchase_receipts      ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE purchase_receipt_items ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE purchase_receipt_items ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE supplier_payments      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE supplier_payments      ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE inventory              ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE inventory              ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE batches                ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE batches                ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE serial_numbers         ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE serial_numbers         ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE stock_counts           ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE stock_counts           ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE stock_count_items      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE stock_count_items      ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE stock_adjustments      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE stock_adjustments      ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE warehouses             ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE warehouses             ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE employees              ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE employees              ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE shifts                 ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE shifts                 ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE customers              ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE customers              ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);

ALTER TABLE customer_addresses     ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE customer_addresses     ADD COLUMN IF NOT EXISTS deleted_by UUID REFERENCES users(id);
