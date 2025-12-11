-- Senha para todos: 123456 (ou a mesma hash que tu usas pro Joca)
INSERT INTO `user` (`id`, `name`, `last_name`, `email`, `idade`, `password`, `roles`) VALUES
(2, 'Maria', 'Silva', 'maria@teste.academy', 22, '{bcrypt}$2a$10$N.zmdr9k7uOCQb376ye.5.ZncCr4X8lrQIYn.MnNdtWkORsnl.Nee', 'USER'),
(3, 'Joao', 'Souza', 'joao@teste.academy', 30, '{bcrypt}$2a$10$N.zmdr9k7uOCQb376ye.5.ZncCr4X8lrQIYn.MnNdtWkORsnl.Nee', 'USER'),
(4, 'Ana', 'Pereira', 'ana@teste.academy', 28, '{bcrypt}$2a$10$N.zmdr9k7uOCQb376ye.5.ZncCr4X8lrQIYn.MnNdtWkORsnl.Nee', 'USER');