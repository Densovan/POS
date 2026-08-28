CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO roles (name, description, is_system_role) VALUES
    ('owner',   'Shop owner with full access',        true),
    ('admin',   'Administrator',                       true),
    ('manager', 'Store manager',                       true),
    ('cashier', 'Point of sale operator',              true)
ON CONFLICT (name) DO NOTHING;

INSERT INTO permissions (module, action, description) VALUES
    ('products', 'read',   'View products'),
    ('products', 'create', 'Create products'),
    ('products', 'update', 'Update products'),
    ('products', 'delete', 'Delete products'),
    ('products', 'export', 'Export products')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name IN ('owner', 'admin') AND p.module = 'products'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
    ON p.module = 'products' AND p.action IN ('read', 'create', 'update', 'export')
WHERE r.name = 'manager'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
    ON p.module = 'products' AND p.action = 'read'
WHERE r.name = 'cashier'
ON CONFLICT DO NOTHING;
