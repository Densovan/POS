-- Grant the manager role read access to every catalog reference module.
--
-- V2 seeded the manager role with products:read/create/update/export only.
-- Because every catalog controller carries a class-level guard requiring the
-- module's :read authority, managers could not list categories, brands,
-- units, or tax classes at all — awkward for anyone supervising day-to-day
-- store operations.
--
-- This grants the four catalog read permissions. Mutations (create/update/
-- delete) on reference data remain owner/admin-only; adjust via
-- PUT /roles/{id}/permissions if policy changes.

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p
  ON p.module IN ('categories', 'brands', 'units', 'taxes')
 AND p.action = 'read'
WHERE r.name = 'manager'
ON CONFLICT DO NOTHING;
