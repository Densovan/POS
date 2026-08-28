-- Additional business indexes declared via @Table(indexes = ...) on the entities
-- that are not pure foreign-key lookup indexes (those live in V3).

-- Users are looked up by email (login) and filtered by is_active.
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users (is_active);
