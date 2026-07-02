package com.modelos.usuarios.application;

import com.modelos.usuarios.adapters.outbound.repositories.InMemoryUserRepository;
import com.modelos.usuarios.application.commands.SaveUserCommand;
import com.modelos.usuarios.application.services.UserService;
import com.modelos.usuarios.domain.entities.User;
import com.modelos.usuarios.domain.errors.UserAlreadyExistsException;
import com.modelos.usuarios.domain.errors.UserNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    @Test
    void shouldCreateUserAndListUsers() {
        UserService service = new UserService(new InMemoryUserRepository());

        User createdUser = service.createUser(makeCommand(1, "Maria"));
        List<User> users = service.listUsers();

        assertThat(createdUser.id()).isEqualTo(1);
        assertThat(createdUser.nome()).isEqualTo("Maria");
        assertThat(users).hasSize(1);
        assertThat(users.getFirst().telefones()).containsExactly("11999990000", "1133334444");
    }

    @Test
    void shouldThrowWhenCreatingUserWithExistingId() {
        UserService service = new UserService(new InMemoryUserRepository());
        SaveUserCommand command = makeCommand(1, "Maria");
        service.createUser(command);

        assertThrows(UserAlreadyExistsException.class, () -> service.createUser(command));
    }

    @Test
    void shouldUpdateUserKeepingRouteId() {
        UserService service = new UserService(new InMemoryUserRepository());
        service.createUser(makeCommand(1, "Maria"));

        User updatedUser = service.updateUser(1, new SaveUserCommand(
                99,
                "Ana",
                LocalDate.of(1988, 5, 20),
                false,
                List.of("11888887777")));

        assertThat(updatedUser.id()).isEqualTo(1);
        assertThat(updatedUser.nome()).isEqualTo("Ana");
        assertThat(updatedUser.status()).isFalse();
        assertThat(updatedUser.telefones()).containsExactly("11888887777");
    }

    @Test
    void shouldThrowWhenDeletingUnknownUser() {
        UserService service = new UserService(new InMemoryUserRepository());

        assertThrows(UserNotFoundException.class, () -> service.deleteUser(999));
    }

    private SaveUserCommand makeCommand(Integer id, String nome) {
        return new SaveUserCommand(
                id,
                nome,
                LocalDate.of(1990, 1, 10),
                true,
                List.of("11999990000", "1133334444"));
    }
}
