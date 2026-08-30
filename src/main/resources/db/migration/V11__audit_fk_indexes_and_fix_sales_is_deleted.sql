-- V11: Add supporting indexes on the audited FK columns (created_by/updated_by/
-- deleted_by) added by V8, and fix the sales.is_deleted nullability.
--
-- Hibernate/JPA scan (DB-HIB-003 + DB-SCHEMA-002) reported 79 foreign key
-- columns lacking a usable leading index. PostgreSQL does not auto-create an
-- index for FK columns, so joins / cascading on users(id) can force a full
-- table scan on each child table. Each entry uses IF NOT EXISTS so the
-- migration is idempotent and safe to re-run.

CREATE INDEX IF NOT EXISTS idx_batches_created_by             ON batches             (created_by);
CREATE INDEX IF NOT EXISTS idx_batches_updated_by             ON batches             (updated_by);
CREATE INDEX IF NOT EXISTS idx_batches_deleted_by             ON batches             (deleted_by);
CREATE INDEX IF NOT EXISTS idx_brands_created_by              ON brands              (created_by);
CREATE INDEX IF NOT EXISTS idx_brands_updated_by              ON brands              (updated_by);
CREATE INDEX IF NOT EXISTS idx_brands_deleted_by              ON brands              (deleted_by);
CREATE INDEX IF NOT EXISTS idx_categories_created_by          ON categories          (created_by);
CREATE INDEX IF NOT EXISTS idx_categories_updated_by          ON categories          (updated_by);
CREATE INDEX IF NOT EXISTS idx_categories_deleted_by          ON categories          (deleted_by);
CREATE INDEX IF NOT EXISTS idx_customer_addresses_created_by  ON customer_addresses  (created_by);
CREATE INDEX IF NOT EXISTS idx_customer_addresses_updated_by  ON customer_addresses  (updated_by);
CREATE INDEX IF NOT EXISTS idx_customer_addresses_deleted_by  ON customer_addresses  (deleted_by);
CREATE INDEX IF NOT EXISTS idx_customers_created_by           ON customers           (created_by);
CREATE INDEX IF NOT EXISTS idx_customers_updated_by           ON customers           (updated_by);
CREATE INDEX IF NOT EXISTS idx_customers_deleted_by           ON customers           (deleted_by);
CREATE INDEX IF NOT EXISTS idx_employees_created_by           ON employees           (created_by);
CREATE INDEX IF NOT EXISTS idx_employees_updated_by           ON employees           (updated_by);
CREATE INDEX IF NOT EXISTS idx_employees_deleted_by           ON employees           (deleted_by);
CREATE INDEX IF NOT EXISTS idx_expense_categories_created_by  ON expense_categories  (created_by);
CREATE INDEX IF NOT EXISTS idx_expense_categories_updated_by  ON expense_categories  (updated_by);
CREATE INDEX IF NOT EXISTS idx_expense_categories_deleted_by  ON expense_categories  (deleted_by);
CREATE INDEX IF NOT EXISTS idx_expenses_updated_by            ON expenses            (updated_by);
CREATE INDEX IF NOT EXISTS idx_expenses_deleted_by            ON expenses            (deleted_by);
CREATE INDEX IF NOT EXISTS idx_inventory_created_by           ON inventory           (created_by);
CREATE INDEX IF NOT EXISTS idx_inventory_updated_by           ON inventory           (updated_by);
CREATE INDEX IF NOT EXISTS idx_inventory_deleted_by           ON inventory           (deleted_by);
CREATE INDEX IF NOT EXISTS idx_payments_created_by            ON payments            (created_by);
CREATE INDEX IF NOT EXISTS idx_payments_updated_by            ON payments            (updated_by);
CREATE INDEX IF NOT EXISTS idx_payments_deleted_by            ON payments            (deleted_by);
CREATE INDEX IF NOT EXISTS idx_product_variants_created_by    ON product_variants    (created_by);
CREATE INDEX IF NOT EXISTS idx_product_variants_updated_by    ON product_variants    (updated_by);
CREATE INDEX IF NOT EXISTS idx_product_variants_deleted_by    ON product_variants    (deleted_by);
CREATE INDEX IF NOT EXISTS idx_products_created_by            ON products            (created_by);
CREATE INDEX IF NOT EXISTS idx_products_updated_by            ON products            (updated_by);
CREATE INDEX IF NOT EXISTS idx_products_deleted_by            ON products            (deleted_by);
CREATE INDEX IF NOT EXISTS idx_purchase_order_items_created_by ON purchase_order_items (created_by);
CREATE INDEX IF NOT EXISTS idx_purchase_order_items_updated_by ON purchase_order_items (updated_by);
CREATE INDEX IF NOT EXISTS idx_purchase_order_items_deleted_by ON purchase_order_items (deleted_by);
CREATE INDEX IF NOT EXISTS idx_purchase_orders_updated_by     ON purchase_orders     (updated_by);
CREATE INDEX IF NOT EXISTS idx_purchase_orders_deleted_by     ON purchase_orders     (deleted_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipt_items_created_by ON purchase_receipt_items (created_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipt_items_updated_by ON purchase_receipt_items (updated_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipt_items_deleted_by ON purchase_receipt_items (deleted_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipts_created_by   ON purchase_receipts   (created_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipts_updated_by   ON purchase_receipts   (updated_by);
CREATE INDEX IF NOT EXISTS idx_purchase_receipts_deleted_by   ON purchase_receipts   (deleted_by);
CREATE INDEX IF NOT EXISTS idx_sale_items_created_by          ON sale_items          (created_by);
CREATE INDEX IF NOT EXISTS idx_sale_items_updated_by          ON sale_items          (updated_by);
CREATE INDEX IF NOT EXISTS idx_sale_items_deleted_by          ON sale_items          (deleted_by);
CREATE INDEX IF NOT EXISTS idx_sales_created_by               ON sales               (created_by);
CREATE INDEX IF NOT EXISTS idx_sales_updated_by               ON sales               (updated_by);
CREATE INDEX IF NOT EXISTS idx_serial_numbers_created_by      ON serial_numbers      (created_by);
CREATE INDEX IF NOT EXISTS idx_serial_numbers_updated_by      ON serial_numbers      (updated_by);
CREATE INDEX IF NOT EXISTS idx_serial_numbers_deleted_by      ON serial_numbers      (deleted_by);
CREATE INDEX IF NOT EXISTS idx_shifts_created_by              ON shifts              (created_by);
CREATE INDEX IF NOT EXISTS idx_shifts_updated_by              ON shifts              (updated_by);
CREATE INDEX IF NOT EXISTS idx_shifts_deleted_by              ON shifts              (deleted_by);
CREATE INDEX IF NOT EXISTS idx_stock_adjustments_updated_by   ON stock_adjustments   (updated_by);
CREATE INDEX IF NOT EXISTS idx_stock_adjustments_deleted_by   ON stock_adjustments   (deleted_by);
CREATE INDEX IF NOT EXISTS idx_stock_count_items_created_by   ON stock_count_items   (created_by);
CREATE INDEX IF NOT EXISTS idx_stock_count_items_updated_by   ON stock_count_items   (updated_by);
CREATE INDEX IF NOT EXISTS idx_stock_count_items_deleted_by   ON stock_count_items   (deleted_by);
CREATE INDEX IF NOT EXISTS idx_stock_counts_created_by        ON stock_counts        (created_by);
CREATE INDEX IF NOT EXISTS idx_stock_counts_updated_by        ON stock_counts        (updated_by);
CREATE INDEX IF NOT EXISTS idx_stock_counts_deleted_by        ON stock_counts        (deleted_by);
CREATE INDEX IF NOT EXISTS idx_supplier_payments_updated_by   ON supplier_payments   (updated_by);
CREATE INDEX IF NOT EXISTS idx_supplier_payments_deleted_by   ON supplier_payments   (deleted_by);
CREATE INDEX IF NOT EXISTS idx_suppliers_created_by           ON suppliers           (created_by);
CREATE INDEX IF NOT EXISTS idx_suppliers_updated_by           ON suppliers           (updated_by);
CREATE INDEX IF NOT EXISTS idx_suppliers_deleted_by           ON suppliers           (deleted_by);
CREATE INDEX IF NOT EXISTS idx_tax_classes_created_by         ON tax_classes         (created_by);
CREATE INDEX IF NOT EXISTS idx_tax_classes_updated_by         ON tax_classes         (updated_by);
CREATE INDEX IF NOT EXISTS idx_tax_classes_deleted_by         ON tax_classes         (deleted_by);
CREATE INDEX IF NOT EXISTS idx_users_created_by               ON users               (created_by);
CREATE INDEX IF NOT EXISTS idx_users_updated_by               ON users               (updated_by);
CREATE INDEX IF NOT EXISTS idx_users_deleted_by               ON users               (deleted_by);
CREATE INDEX IF NOT EXISTS idx_warehouses_created_by          ON warehouses          (created_by);
CREATE INDEX IF NOT EXISTS idx_warehouses_updated_by          ON warehouses          (updated_by);
CREATE INDEX IF NOT EXISTS idx_warehouses_deleted_by          ON warehouses          (deleted_by);

-- The sales.is_deleted column predates V10 (it already had a DEFAULT false),
-- so V10's ADD COLUMN IF NOT EXISTS ... NOT NULL was a no-op and left it
-- nullable. Align it with the other BaseEntity tables and the @Column(nullable = false)
-- mapping on BaseEntity#isDeleted.
ALTER TABLE sales ALTER COLUMN is_deleted SET DEFAULT FALSE;
ALTER TABLE sales ALTER COLUMN is_deleted SET NOT NULL;
