-- Clean up DB-SCHEMA-002 (missing FK indexes) and DB-SCHEMA-003 (redundant indexes).
--
-- Add 3 FK indexes that were missing:
--   * employees.user_id          (FK added via ALTER TABLE ADD COLUMN in V1)
--   * sale_items.serial_number_id (FK added via ALTER TABLE ADD COLUMN in V1)
--   * user_roles.role_id          (second PK column of user_roles, not the leading key)
CREATE INDEX IF NOT EXISTS idx_employees_user_id ON employees (user_id);
CREATE INDEX IF NOT EXISTS idx_sale_items_serial_number_id ON sale_items (serial_number_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles (role_id);

-- Drop 2 redundant indexes created in V3. Their columns are already the leading
-- prefix of existing unique indexes, so the lookups they served are covered:
--   * inventory.product_id             -> UNIQUE(product_id, variant_id, warehouse_id)
--   * notification_preferences.user_id -> UNIQUE(user_id)
DROP INDEX IF EXISTS idx_inventory_product_id;
DROP INDEX IF EXISTS idx_notification_preferences_user_id;
