package com.modelos.usuarios.domain.errors;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer userId) {
        super("Usuario com id " + userId + " nao foi encontrado.");
    }
}
