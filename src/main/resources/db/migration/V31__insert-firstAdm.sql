-- Criar roles se não existirem
INSERT INTO role (id, name)
SELECT gen_random_uuid(), 'AdminSystem'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'AdminSystem');


-- Criar usuário admin se não existir
INSERT INTO auth (id, name, username_key, password_key)
SELECT gen_random_uuid(), 'Administrador do Sistema', 'adminsystem@gmail.com', 'Basket123@@'  -- senha já criptografada com BCrypt
WHERE NOT EXISTS (SELECT 1 FROM auth WHERE username_key = 'adminsystem@gmail.com');

-- Vincular admin à role ADMIN
INSERT INTO user_role (id_user, id_role)
SELECT a.id, r.id
FROM auth a, role r
WHERE a.username_key = 'adminsystem@gmail.com'
  AND r.name = 'AdminSystem'
  AND NOT EXISTS (
      SELECT 1 FROM user_role ar
      WHERE ar.id_user = a.id AND ar.id_role = r.id
  );
