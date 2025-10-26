-- Agregar columna is_active a la tabla users
-- Si ya existe, este comando dará error pero no afectará los datos
ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT TRUE;

-- Actualizar usuarios existentes para que estén activos
UPDATE users SET is_active = TRUE WHERE is_active IS NULL;

-- Hacer la columna NOT NULL después de actualizar
ALTER TABLE users MODIFY COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;