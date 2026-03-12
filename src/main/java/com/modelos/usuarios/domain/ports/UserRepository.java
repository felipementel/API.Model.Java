package com.modelos.usuarios.domain.ports;

import com.modelos.usuarios.domain.entities.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> listAll();

    User getById(Integer userId);

    void delete(Integer userId);
}
