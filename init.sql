-- Criação do banco de dados se não existir
CREATE DATABASE IF NOT EXISTS bd_contas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_contas;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de contas
CREATE TABLE IF NOT EXISTS conta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(30) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    limite_cheque_especial DECIMAL(15,2) NOT NULL DEFAULT 0.00,  -- Limite de cheque especial
    tipo ENUM('corrente', 'poupanca') NOT NULL,
    proprietario_id INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (proprietario_id) REFERENCES cliente(id)
);
