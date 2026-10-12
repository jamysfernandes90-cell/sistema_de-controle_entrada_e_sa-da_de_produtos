
-- ==========================================
-- SISTEMA DE CONTROLE DE ENTRADA E SAÍDA
-- VIRTUAL IMPORTS
-- Banco de dados: MySQL
-- ==========================================

CREATE DATABASE IF NOT EXISTS virtual_imports;

USE virtual_imports;

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS produtos (
    id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL
);

-- Tabela de movimentações
CREATE TABLE IF NOT EXISTS movimentacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    quantidade INT NOT NULL,
    data_movimentacao TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (produto_id)
        REFERENCES produtos(id)
);
