package com.modelos.usuarios.adapters.inbound.http.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modelos.usuarios.domain.entities.User;

import java.time.LocalDate;
import java.util.List;

public record UserResponse(
        Integer id,
        String nome,
        @JsonProperty("dtNascimento") LocalDate dtNascimento,
        Boolean status,
        List<String> telefones) {

    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.id(),
                user.nome(),
                user.dtNascimento(),
                user.status(),
                List.copyOf(user.telefones()));
    }
}
