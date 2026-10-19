-- Grant the owner role the full permission catalogue.
--
-- V2 only seeded products:* for the owner role, so the first registered user
-- (auto-assigned owner at /auth/register) could view and manage products but
-- could not create categories, brands, units, or tax classes — and had no
-- roles:manage / users:manage, so they could not fix it themselves.
--
-- The owner runs the business, so they get every permission, like admin.
-- Existing owner + products rows are kept; ON CONFLICT DO NOTHING makes this
-- migration safe to run against any state.

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'owner'
ON CONFLICT DO NOTHING;
