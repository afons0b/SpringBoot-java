-- 1. Primeiro apaga a tabela de ligação (por causa da Chave Estrangeira/FK)
DELETE FROM `user_profile`;

-- 2. Depois apaga a tabela de perfis
DELETE FROM `profile`;