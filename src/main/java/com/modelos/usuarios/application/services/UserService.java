package com.modelos.usuarios.application.services;

import com.modelos.usuarios.application.commands.SaveUserCommand;
import com.modelos.usuarios.domain.entities.User;
import com.modelos.usuarios.domain.errors.UserAlreadyExistsException;
import com.modelos.usuarios.domain.errors.UserNotFoundException;
import com.modelos.usuarios.domain.ports.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(SaveUserCommand command) {
        if (repository.getById(command.id()) != null) {
            throw new UserAlreadyExistsException(command.id());
        }

        return repository.save(toUser(command, command.id()));
    }

    public List<User> listUsers() {
        return repository.listAll();
    }

    public User getUser(Integer userId) {
        User user = repository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        return user;
    }

    public User updateUser(Integer userId, SaveUserCommand command) {
        getUser(userId);
        return repository.save(toUser(command, userId));
    }

    public void deleteUser(Integer userId) {
        getUser(userId);
        repository.delete(userId);
    }

    private User toUser(SaveUserCommand command, Integer userId) {
        return new User(
                userId,
                command.nome(),
                command.dtNascimento(),
                command.status(),
                List.copyOf(command.telefones()));
    }
}
