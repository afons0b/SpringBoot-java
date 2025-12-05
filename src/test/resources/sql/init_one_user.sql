-- 1. Criar o Perfil 'USER' (ID 1)
INSERT INTO `profile` (`id`, `name`, `description`)
VALUES (1, 'USER', 'Perfil de usuário comum');

-- 2. Criar o Usuário 'Joca' (ID 1)
-- A senha abaixo é o hash para: "123456"
INSERT INTO `user` (`id`, `name`, `last_name`, `email`, `idade`, `password`, `roles`)
VALUES (1, 'Joca', 'Da Silva', 'joca@teste.academy', 25, '{bcrypt}$2a$10$wW8dPq4TPwRWfxY5LKGOvu39PTbGi53Cg.BNswLVdteH57pJhPiwC', 'USER');

-- 3. Criar a Ligação na Tabela 'user_profile'
-- Liga o Usuário 1 ao Perfil 1
INSERT INTO `user_profile` (`id`, `user_id`, `profile_id`)
VALUES (1, 1, 1);