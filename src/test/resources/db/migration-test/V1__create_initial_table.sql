-- Criação da tabela de Perfis
CREATE TABLE profile (
    id BIGINT AUTO_INCREMENT NOT NULL,
    description VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Criação da tabela de Usuários
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    idade INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Criação da tabela de Ligação (User <-> Profile)
CREATE TABLE user_profile (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Adicionando Constraints (Regras)

-- Garante que não existam dois perfis com mesmo nome
ALTER TABLE profile
    ADD CONSTRAINT uk_profile_name UNIQUE (name);

-- Garante que não existam dois usuários com mesmo email
ALTER TABLE user
    ADD CONSTRAINT uk_user_email UNIQUE (email);

-- Adiciona a Chave Estrangeira ligando User_Profile ao Profile
ALTER TABLE user_profile
    ADD CONSTRAINT fk_user_profile_profile
    FOREIGN KEY (profile_id) REFERENCES profile (id);

-- Adiciona a Chave Estrangeira ligando User_Profile ao User
ALTER TABLE user_profile
    ADD CONSTRAINT fk_user_profile_user
    FOREIGN KEY (user_id) REFERENCES user (id);