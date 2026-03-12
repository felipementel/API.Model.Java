package com.modelos.usuarios.adapters.inbound.http.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modelos.usuarios.application.commands.SaveUserCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record UserRequest(
        @Positive Integer id,
        @NotBlank @Size(max = 120) String nome,
        @NotNull @JsonProperty("dtNascimento") LocalDate dtNascimento,
        @NotNull Boolean status,
        @NotNull List<String> telefones) {

    public SaveUserCommand toCommand() {
        return new SaveUserCommand(id, nome, dtNascimento, status, List.copyOf(telefones));
    }
}
