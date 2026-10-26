package br.com.fiap.biblioteca.service;

import br.com.fiap.biblioteca.dto.LivroRequest;
import br.com.fiap.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.fiap.biblioteca.exception.RegraNegocioException;
import br.com.fiap.biblioteca.model.Autor;
import br.com.fiap.biblioteca.model.Livro;
import br.com.fiap.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorService autorService;

    public LivroService(LivroRepository livroRepository, AutorService autorService) {
        this.livroRepository = livroRepository;
        this.autorService = autorService;
    }

    public List<Livro> listar(String titulo, Long autorId) {
        if (autorId != null) {
            return livroRepository.findByAutorId(autorId);
        }
        if (titulo != null && !titulo.isBlank()) {
            return livroRepository.findByTituloContainingIgnoreCase(titulo);
        }
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: id " + id));
    }

    @Transactional
    public Livro criar(LivroRequest dados) {
        if (livroRepository.existsByIsbn(dados.isbn())) {
            throw new RegraNegocioException("Já existe um livro com o ISBN " + dados.isbn());
        }
        Livro livro = new Livro();
        preencher(livro, dados);
        return livroRepository.save(livro);
    }

    @Transactional
    public Livro atualizar(Long id, LivroRequest dados) {
        Livro livro = buscarPorId(id);
        if (livroRepository.existsByIsbnAndIdNot(dados.isbn(), id)) {
            throw new RegraNegocioException("Já existe outro livro com o ISBN " + dados.isbn());
        }
        preencher(livro, dados);
        return livroRepository.save(livro);
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }

    private void preencher(Livro livro, LivroRequest dados) {
        Autor autor = autorService.buscarPorId(dados.autorId());
        livro.setTitulo(dados.titulo());
        livro.setIsbn(dados.isbn());
        livro.setAnoPublicacao(dados.anoPublicacao());
        livro.setPreco(dados.preco());
        livro.setAutor(autor);
    }
}
