package br.com.fiap.biblioteca.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Dados recebidos no corpo das requisições POST/PUT de livro.
 */
public record LivroRequest(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "O ISBN é obrigatório")
        @Size(max = 20, message = "O ISBN deve ter no máximo 20 caracteres")
        String isbn,

        @Min(value = 1000, message = "Ano de publicação inválido")
        @Max(value = 2100, message = "Ano de publicação inválido")
        Integer anoPublicacao,

        @PositiveOrZero(message = "O preço não pode ser negativo")
        BigDecimal preco,

        @NotNull(message = "O id do autor é obrigatório")
        Long autorId
) {
}
