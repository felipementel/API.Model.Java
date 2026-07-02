package com.modelos.usuarios.adapters.outbound.repositories;

import com.modelos.usuarios.domain.entities.User;
import com.modelos.usuarios.domain.ports.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Integer, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        User storedUser = cloneUser(user);
        storage.put(user.id(), storedUser);
        return cloneUser(storedUser);
    }

    @Override
    public List<User> listAll() {
        return storage.values().stream().map(this::cloneUser).toList();
    }

    @Override
    public User getById(Integer userId) {
        User user = storage.get(userId);
        return user == null ? null : cloneUser(user);
    }

    @Override
    public void delete(Integer userId) {
        storage.remove(userId);
    }

    private User cloneUser(User user) {
        return new User(
                user.id(),
                user.nome(),
                user.dtNascimento(),
                user.status(),
                List.copyOf(user.telefones()));
    }
}
