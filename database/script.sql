-- =====================================================================
-- Script do banco de dados da Biblioteca API (SQL Server)
-- Execute no SSMS, Azure Data Studio ou sqlcmd.
-- Obs.: a aplicação também cria as tabelas sozinha (ddl-auto=update);
--       este script serve para criar o banco e inserir dados de exemplo.
-- =====================================================================

IF DB_ID('biblioteca') IS NULL
    CREATE DATABASE biblioteca;
GO

USE biblioteca;
GO

IF OBJECT_ID('TB_AUTOR', 'U') IS NULL
CREATE TABLE TB_AUTOR (
    ID_AUTOR        BIGINT IDENTITY(1,1) PRIMARY KEY,
    NOME            VARCHAR(100) NOT NULL,
    NACIONALIDADE   VARCHAR(50),
    DATA_NASCIMENTO DATE
);
GO

IF OBJECT_ID('TB_LIVRO', 'U') IS NULL
CREATE TABLE TB_LIVRO (
    ID_LIVRO        BIGINT IDENTITY(1,1) PRIMARY KEY,
    TITULO          VARCHAR(150) NOT NULL,
    ISBN            VARCHAR(20)  NOT NULL UNIQUE,
    ANO_PUBLICACAO  INT,
    PRECO           NUMERIC(10,2),
    ID_AUTOR        BIGINT NOT NULL,
    CONSTRAINT FK_LIVRO_AUTOR FOREIGN KEY (ID_AUTOR) REFERENCES TB_AUTOR (ID_AUTOR)
);
GO

-- Dados de exemplo (só insere se as tabelas estiverem vazias)
IF NOT EXISTS (SELECT 1 FROM TB_AUTOR)
BEGIN
    INSERT INTO TB_AUTOR (NOME, NACIONALIDADE, DATA_NASCIMENTO) VALUES
        ('Machado de Assis', 'Brasileira', '1839-06-21'),
        ('Clarice Lispector', 'Brasileira', '1920-12-10'),
        ('George Orwell',     'Britânica',  '1903-06-25');

    INSERT INTO TB_LIVRO (TITULO, ISBN, ANO_PUBLICACAO, PRECO, ID_AUTOR) VALUES
        ('Dom Casmurro',                    '978-8535910663', 1899, 29.90, 1),
        ('Memórias Póstumas de Brás Cubas', '978-8535910664', 1881, 34.90, 1),
        ('A Hora da Estrela',               '978-8532508126', 1977, 39.90, 2),
        ('1984',                            '978-8535914849', 1949, 44.90, 3);
END
GO

SELECT * FROM TB_AUTOR;
SELECT * FROM TB_LIVRO;
