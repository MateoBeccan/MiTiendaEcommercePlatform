-- Activar todos los usuarios existentes
UPDATE users SET is_active = 1 WHERE is_active = 0;

-- Verificar el resultado
SELECT username, email, role, is_active FROM users;