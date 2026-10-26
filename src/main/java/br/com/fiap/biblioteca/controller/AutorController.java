package br.com.fiap.biblioteca.controller;

import br.com.fiap.biblioteca.dto.AutorRequest;
import br.com.fiap.biblioteca.model.Autor;
import br.com.fiap.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    // GET /api/autores  ou  /api/autores?nome=machado
    @GetMapping
    public List<Autor> listar(@RequestParam(required = false) String nome) {
        return service.listar(nome);
    }

    // GET /api/autores/{id}
    @GetMapping("/{id}")
    public Autor buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/autores
    @PostMapping
    public ResponseEntity<Autor> criar(@RequestBody @Valid AutorRequest dados) {
        Autor autor = service.criar(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(autor.getId()).toUri();
        return ResponseEntity.created(uri).body(autor);
    }

    // PUT /api/autores/{id}
    @PutMapping("/{id}")
    public Autor atualizar(@PathVariable Long id, @RequestBody @Valid AutorRequest dados) {
        return service.atualizar(id, dados);
    }

    // DELETE /api/autores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
