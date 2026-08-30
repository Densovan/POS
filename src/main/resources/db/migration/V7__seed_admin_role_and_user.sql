-- Seed a full permission catalogue across all POS modules and grant the admin
-- role every permission so an admin login can handle everything in the system.
-- Also seeds an initial admin user account.
--
-- Default admin credentials (change the password after first login):
--   email:    admin@pos.local
--   password: Admin@123
--
-- The password_hash below is a BCrypt hash of "Admin@123".

-- 1) Permissions: every module x standard action.
INSERT INTO permissions (module, action, description) VALUES
    ('pos',       'read',   'View POS records'),
    ('pos',       'create', 'Create POS records'),
    ('pos',       'update', 'Update POS records'),
    ('pos',       'delete', 'Delete POS records'),
    ('pos',       'export', 'Export POS records'),
    ('products',  'read',   'View products'),
    ('products',  'create', 'Create products'),
    ('products',  'update', 'Update products'),
    ('products',  'delete', 'Delete products'),
    ('products',  'export', 'Export products'),
    ('inventory', 'read',   'View inventory'),
    ('inventory', 'create', 'Create inventory movements'),
    ('inventory', 'update', 'Update inventory'),
    ('inventory', 'delete', 'Delete inventory records'),
    ('inventory', 'export', 'Export inventory'),
    ('inventory', 'count',  'Perform stock counts'),
    ('inventory', 'adjust', 'Approve stock adjustments'),
    ('customers', 'read',   'View customers'),
    ('customers', 'create', 'Create customers'),
    ('customers', 'update', 'Update customers'),
    ('customers', 'delete', 'Delete customers'),
    ('customers', 'export', 'Export customers'),
    ('suppliers', 'read',   'View suppliers'),
    ('suppliers', 'create', 'Create suppliers'),
    ('suppliers', 'update', 'Update suppliers'),
    ('suppliers', 'delete', 'Delete suppliers'),
    ('suppliers', 'export', 'Export suppliers'),
    ('purchases', 'read',   'View purchase orders and receipts'),
    ('purchases', 'create', 'Create purchase orders'),
    ('purchases', 'update', 'Update purchase orders'),
    ('purchases', 'delete', 'Delete purchase orders'),
    ('purchases', 'approve','Approve purchase orders'),
    ('purchases', 'export', 'Export purchase records'),
    ('expenses',  'read',   'View expenses'),
    ('expenses',  'create', 'Create expenses'),
    ('expenses',  'update', 'Update expenses'),
    ('expenses',  'delete', 'Delete expenses'),
    ('expenses',  'approve','Approve expenses'),
    ('expenses',  'export', 'Export expenses'),
    ('employees', 'read',   'View employees'),
    ('employees', 'create', 'Create employees'),
    ('employees', 'update', 'Update employees'),
    ('employees', 'delete', 'Delete employees'),
    ('attendance','read',   'View attendance'),
    ('attendance','create', 'Create attendance records'),
    ('attendance','update', 'Update attendance'),
    ('attendance','delete', 'Delete attendance'),
    ('shifts',    'read',   'View shifts'),
    ('shifts',    'create', 'Open shifts'),
    ('shifts',    'update', 'Close and update shifts'),
    ('shifts',    'delete', 'Delete shifts'),
    ('warehouses','read',   'View warehouses'),
    ('warehouses','create', 'Create warehouses'),
    ('warehouses','update', 'Update warehouses'),
    ('warehouses','delete', 'Delete warehouses'),
    ('categories','read',   'View categories'),
    ('categories','create', 'Create categories'),
    ('categories','update', 'Update categories'),
    ('categories','delete', 'Delete categories'),
    ('brands',    'read',   'View brands'),
    ('brands',    'create', 'Create brands'),
    ('brands',    'update', 'Update brands'),
    ('brands',    'delete', 'Delete brands'),
    ('units',     'read',   'View units'),
    ('units',     'create', 'Create units'),
    ('units',     'update', 'Update units'),
    ('units',     'delete', 'Delete units'),
    ('taxes',     'read',   'View tax classes'),
    ('taxes',     'create', 'Create tax classes'),
    ('taxes',     'update', 'Update tax classes'),
    ('taxes',     'delete', 'Delete tax classes'),
    ('reports',   'read',   'View reports'),
    ('reports',   'export', 'Export reports'),
    ('settings',  'read',   'View system settings'),
    ('settings',  'update', 'Update system settings'),
    ('audit',     'read',   'View audit logs'),
    ('audit',     'export', 'Export audit logs'),
    ('backups',   'read',   'View backups'),
    ('backups',   'create', 'Create backups'),
    ('backups',   'restore','Restore backups'),
    ('backups',   'delete', 'Delete backups'),
    ('roles',     'read',   'View roles'),
    ('roles',     'manage', 'Manage roles and permissions'),
    ('users',     'read',   'View users'),
    ('users',     'create', 'Create users'),
    ('users',     'update', 'Update users'),
    ('users',     'delete', 'Delete users'),
    ('users',     'manage', 'Manage user roles'),
    ('loyalty',   'read',   'View loyalty records'),
    ('loyalty',   'update', 'Update loyalty points'),
    ('refunds',   'read',   'View refunds'),
    ('refunds',   'create', 'Process refunds'),
    ('refunds',   'update', 'Update refunds'),
    ('exchanges', 'read',   'View exchanges'),
    ('exchanges', 'create', 'Process exchanges'),
    ('exchanges', 'update', 'Update exchanges')
ON CONFLICT (module, action) DO NOTHING;

-- 2) Grant the admin role every permission in the catalogue.
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'admin'
ON CONFLICT DO NOTHING;

-- 3) Seed the initial admin user (email + password above).
INSERT INTO users (email, password_hash, is_active, login_attempts)
VALUES ('admin@pos.local', '$2a$10$jJl4.P/srXvjuFo8ENav9eZdoZ0hgqAGPU9iva4l5vsIrGc6A6MBu', true, 0)
ON CONFLICT (email) DO NOTHING;

-- 4) Assign the admin role to the seeded admin user.
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE u.email = 'admin@pos.local' AND r.name = 'admin'
ON CONFLICT DO NOTHING;
