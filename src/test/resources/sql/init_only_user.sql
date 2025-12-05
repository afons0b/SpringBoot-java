
-- 1. Limpa qualquer usuário que já exista (para evitar conflito de ID)
DELETE FROM `user_profile`; -- Apaga ligações primeiro (FK)
DELETE FROM `profile`;
DELETE FROM `user`;         -- Apaga usuários depois

-- Insere APENAS o usuário para permitir o Login
-- (Não insere nada na tabela 'profile' nem 'user_profile')
INSERT INTO `user` (`id`, `name`, `last_name`, `email`, `idade`, `password`, `roles`)
VALUES (1, 'Joca', 'Da Silva', 'joca@teste.academy', 25, '{bcrypt}$2a$10$wW8dPq4TPwRWfxY5LKGOvu39PTbGi53Cg.BNswLVdteH57pJhPiwC', 'USER');