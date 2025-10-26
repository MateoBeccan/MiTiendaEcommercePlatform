-- Ejecutar estos comandos uno por uno en MySQL

-- 1. Agregar columna (si da error "Duplicate column name", ignorar)
ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT TRUE;

-- 2. Actualizar usuarios existentes
UPDATE users SET is_active = TRUE WHERE is_active IS NULL;

-- 3. Verificar que todos los usuarios tengan is_active
SELECT username, email, role, is_active FROM users;

-- 4. Si todo se ve bien, hacer la columna NOT NULL
ALTER TABLE users MODIFY COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;