-- ============================================
-- SCRIPT SQL - SISTEMA DE LOCAÇÃO DE VEÍCULOS
-- Banco: locadora | XAMPP MySQL
-- ============================================

CREATE DATABASE IF NOT EXISTS locadora CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE locadora;

-- Tabela Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE
);

-- Tabela Veiculo
CREATE TABLE IF NOT EXISTS veiculo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    categoria CHAR(1) NOT NULL COMMENT 'A=Hatch Compacto, B=Sedan Intermediário, C=SUV Compacto, D=Sedan Executivo, E=SUV Executivo',
    quilometragem DOUBLE NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL' COMMENT 'DISPONIVEL ou LOCADO'
);

-- Tabela Locacao
CREATE TABLE IF NOT EXISTS locacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    veiculo_id INT NOT NULL,
    data_retirada DATETIME NOT NULL,
    km_inicial DOUBLE NOT NULL,
    quantidade_diarias INT NOT NULL,
    data_devolucao DATETIME,
    km_final DOUBLE,
    valor_total DOUBLE,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (veiculo_id) REFERENCES veiculo(id)
);

-- Dados de exemplo
INSERT INTO cliente (nome, cpf) VALUES
('João Silva', '123.456.789-00'),
('Maria Oliveira', '987.654.321-00');

INSERT INTO veiculo (marca, modelo, categoria, quilometragem, status) VALUES
('Volkswagen', 'Gol', 'A', 15000, 'DISPONIVEL'),
('Toyota', 'Corolla', 'B', 8000, 'DISPONIVEL'),
('Jeep', 'Compass', 'C', 22000, 'DISPONIVEL'),
('BMW', 'Série 5', 'D', 5000, 'DISPONIVEL'),
('Land Rover', 'Discovery', 'E', 3000, 'DISPONIVEL');
