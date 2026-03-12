package com.modelos.usuarios.domain.errors;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(Integer userId) {
        super("Usuario com id " + userId + " ja existe.");
    }
}
