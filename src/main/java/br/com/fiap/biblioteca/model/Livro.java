package br.com.fiap.biblioteca.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidade que representa a tabela TB_LIVRO no SQL Server.
 * Cada livro pertence a um autor (relacionamento N:1).
 */
@Entity
@Table(name = "TB_LIVRO")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIVRO")
    private Long id;

    @Column(name = "TITULO", nullable = false, length = 150)
    private String titulo;

    @Column(name = "ISBN", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "ANO_PUBLICACAO")
    private Integer anoPublicacao;

    @Column(name = "PRECO", precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_AUTOR", nullable = false)
    private Autor autor;

    public Livro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
