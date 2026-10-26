package br.com.fiap.biblioteca.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint para comprovar que a API está conectada ao SQL Server.
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final JdbcTemplate jdbcTemplate;

    public StatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // GET /api/status
    @GetMapping
    public Map<String, Object> status() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("api", "online");
        info.put("banco", jdbcTemplate.queryForObject("SELECT DB_NAME()", String.class));
        info.put("servidor", jdbcTemplate.queryForObject("SELECT @@SERVERNAME", String.class));
        info.put("versaoSqlServer", jdbcTemplate.queryForObject(
                "SELECT CAST(SERVERPROPERTY('ProductVersion') AS VARCHAR(50))", String.class));
        info.put("totalAutores", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TB_AUTOR", Long.class));
        info.put("totalLivros", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TB_LIVRO", Long.class));
        return info;
    }
}
