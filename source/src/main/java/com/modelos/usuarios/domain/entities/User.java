package com.modelos.usuarios.domain.entities;

import java.time.LocalDate;
import java.util.List;

public record User(
        Integer id,
        String nome,
        LocalDate dtNascimento,
        Boolean status,
        List<String> telefones) {
}
