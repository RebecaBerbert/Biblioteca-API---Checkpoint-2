package br.com.fiap.biblioteca.service;

import br.com.fiap.biblioteca.dto.AutorRequest;
import br.com.fiap.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.fiap.biblioteca.exception.RegraNegocioException;
import br.com.fiap.biblioteca.model.Autor;
import br.com.fiap.biblioteca.repository.AutorRepository;
import br.com.fiap.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    public List<Autor> listar(String nome) {
        if (nome != null && !nome.isBlank()) {
            return autorRepository.findByNomeContainingIgnoreCase(nome);
        }
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado: id " + id));
    }

    @Transactional
    public Autor criar(AutorRequest dados) {
        Autor autor = new Autor(dados.nome(), dados.nacionalidade(), dados.dataNascimento());
        return autorRepository.save(autor);
    }

    @Transactional
    public Autor atualizar(Long id, AutorRequest dados) {
        Autor autor = buscarPorId(id);
        autor.setNome(dados.nome());
        autor.setNacionalidade(dados.nacionalidade());
        autor.setDataNascimento(dados.dataNascimento());
        return autorRepository.save(autor);
    }

    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarPorId(id);
        if (livroRepository.existsByAutorId(id)) {
            throw new RegraNegocioException("Não é possível excluir: o autor possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }
}
