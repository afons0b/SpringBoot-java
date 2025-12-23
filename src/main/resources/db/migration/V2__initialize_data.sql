-- 1. Inserindo Perfis (Profile)
-- Vamos criar IDs fixos para facilitar a ligação depois
INSERT INTO profile (id, name, description) VALUES (1, 'ADMIN', 'Perfil de Administrador');
INSERT INTO profile (id, name, description) VALUES (2, 'REGULAR', 'Perfil de Usuário Comum');

-- 2. Inserindo Usuários (User)
-- Senha para todos é "test": {bcrypt}$2a$10$wW8dPq4TPwRWfxY5LKGOvu39PTbGi53Cg.BNswLVdteH57pJhPiwC

-- Usuário Admin
INSERT INTO user (id, name, last_name, idade, email, password, roles)
VALUES
(1, 'Big Boss', 'Manager', 35, 'admin@teste.academy', '{bcrypt}$2a$10$wW8dPq4TPwRWfxY5LKGOvu39PTbGi53Cg.BNswLVdteH57pJhPiwC', 'ADMIN,USER');

-- Usuário Comum (O nosso querido Joca)
INSERT INTO user (id, name, last_name, idade, email, password, roles)
VALUES
(2, 'Joca', 'Silva', 25, 'joca@teste.academy', '{bcrypt}$2a$10$wW8dPq4TPwRWfxY5LKGOvu39PTbGi53Cg.BNswLVdteH57pJhPiwC', 'REGULAR');

-- 3. Ligando Usuários aos Perfis (Tabela User_Profile)

-- Liga o Big Boss (id 1) ao perfil ADMIN (id 1)
INSERT INTO user_profile (user_id, profile_id) VALUES (1, 1);

-- Liga o Joca (id 2) ao perfil REGULAR (id 2)
INSERT INTO user_profile (user_id, profile_id) VALUES (2, 2);