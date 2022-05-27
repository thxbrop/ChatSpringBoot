package com.thxbrop.springboot.controller;

import com.thxbrop.springboot.Result;
import com.thxbrop.springboot.ServerException;
import com.thxbrop.springboot.entity.User;
import com.thxbrop.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{id}")
    public Result<User> getById(@PathVariable("id") int id) {
        return new Result<>(repository.findById(id).orElse(null));
    }

    @GetMapping("/user")
    public Result<Iterable<User>> getAll() {
        return new Result<>(repository.findAll());
    }

    @GetMapping(value = "/user/save")
    public Result<User> save(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password
    ) {
        if (emailHasRegister(email)) {
            return ServerException.REGISTER_EMAIL_EXIST.asResult();
        }
        User user = new User(email, username, password);
        return new Result<>(repository.save(user));
    }

    private boolean emailHasRegister(String email) {
        return ((List<User>) repository.findAll())
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
