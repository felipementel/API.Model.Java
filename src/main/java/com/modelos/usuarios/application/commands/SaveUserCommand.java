package com.modelos.usuarios.application.commands;

import java.time.LocalDate;
import java.util.List;

public record SaveUserCommand(
        Integer id,
        String nome,
        LocalDate dtNascimento,
        Boolean status,
        List<String> telefones) {
}
