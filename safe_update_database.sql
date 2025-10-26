-- Script seguro para actualizar la base de datos
-- Ejecutar línea por línea si es necesario

-- 1. Intentar agregar la columna (ignorar error si ya existe)
SET @sql = 'ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT TRUE';
SET @sql_check = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                  WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'is_active' AND TABLE_SCHEMA = DATABASE());

-- Solo ejecutar si la columna no existe
SET @sql = IF(@sql_check = 0, @sql, 'SELECT "Column already exists" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. Actualizar usuarios existentes para que estén activos
UPDATE users SET is_active = TRUE WHERE is_active IS NULL;

-- 3. Hacer la columna NOT NULL después de actualizar
ALTER TABLE users MODIFY COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;