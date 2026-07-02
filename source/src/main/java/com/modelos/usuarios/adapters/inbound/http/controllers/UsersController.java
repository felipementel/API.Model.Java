package com.modelos.usuarios.adapters.inbound.http.controllers;

import com.modelos.usuarios.adapters.inbound.http.contracts.ErrorResponse;
import com.modelos.usuarios.adapters.inbound.http.contracts.UserRequest;
import com.modelos.usuarios.adapters.inbound.http.contracts.UserResponse;
import com.modelos.usuarios.application.services.UserService;
import com.modelos.usuarios.domain.errors.UserAlreadyExistsException;
import com.modelos.usuarios.domain.errors.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserResponse.fromDomain(userService.createUser(request.toCommand())));
        } catch (UserAlreadyExistsException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(error.getMessage()));
        }
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.listUsers().stream()
                .map(UserResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> get(@PathVariable Integer usuarioId) {
        try {
            return ResponseEntity.ok(UserResponse.fromDomain(userService.getUser(usuarioId)));
        } catch (UserNotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(error.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<?> update(@PathVariable Integer usuarioId, @Valid @RequestBody UserRequest request) {
        try {
            return ResponseEntity.ok(UserResponse.fromDomain(userService.updateUser(usuarioId, request.toCommand())));
        } catch (UserNotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(error.getMessage()));
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> delete(@PathVariable Integer usuarioId) {
        try {
            userService.deleteUser(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(error.getMessage()));
        }
    }
}
