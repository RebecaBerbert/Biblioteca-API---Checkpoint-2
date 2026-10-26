package br.com.fiap.biblioteca.controller;

import br.com.fiap.biblioteca.dto.LivroRequest;
import br.com.fiap.biblioteca.model.Livro;
import br.com.fiap.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    // GET /api/livros  |  /api/livros?titulo=dom  |  /api/livros?autorId=1
    @GetMapping
    public List<Livro> listar(@RequestParam(required = false) String titulo,
                              @RequestParam(required = false) Long autorId) {
        return service.listar(titulo, autorId);
    }

    // GET /api/livros/{id}
    @GetMapping("/{id}")
    public Livro buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/livros
    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody @Valid LivroRequest dados) {
        Livro livro = service.criar(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(livro.getId()).toUri();
        return ResponseEntity.created(uri).body(livro);
    }

    // PUT /api/livros/{id}
    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody @Valid LivroRequest dados) {
        return service.atualizar(id, dados);
    }

    // DELETE /api/livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
