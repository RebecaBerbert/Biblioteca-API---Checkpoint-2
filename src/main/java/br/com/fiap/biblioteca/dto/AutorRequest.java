package br.com.fiap.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Dados recebidos no corpo das requisições POST/PUT de autor.
 */
public record AutorRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 50, message = "A nacionalidade deve ter no máximo 50 caracteres")
        String nacionalidade,

        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento
) {
}
