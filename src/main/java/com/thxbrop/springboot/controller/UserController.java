package com.thxbrop.springboot.controller;

import com.thxbrop.springboot.Result;
import com.thxbrop.springboot.ServerException;
import com.thxbrop.springboot.annotation.TokenIgnored;
import com.thxbrop.springboot.entity.Conversation;
import com.thxbrop.springboot.entity.Token;
import com.thxbrop.springboot.entity.User;
import com.thxbrop.springboot.repository.UserRepository;
import com.thxbrop.springboot.service.TokenService;
import com.thxbrop.springboot.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserRepository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    @GetMapping("/user/{id}")
    public Result<User> getById(@PathVariable("id") int id) {
        return new Result<>(repository.findById(id).orElse(null));
    }

    @GetMapping("/user")
    public Result<Iterable<User>> getAll() {
        return new Result<>(repository.findAll());
    }

    @TokenIgnored
    @GetMapping("/user/register")
    public Result<User> register(
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

    @TokenIgnored
    @GetMapping("/user/login")
    public Result<Token> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        if (!emailHasRegister(email)) {
            return ServerException.LOGIN_EMAIL_EXIST.asResult();
        }
        Iterable<User> all = repository.findAll();
        boolean isPasswordCorrect = StreamUtils.of(all)
                .filter(user -> user.getEmail().equals(email))
                .anyMatch(user -> user.getPassword().equals(password));
        if (!isPasswordCorrect) {
            return ServerException.WRONG_PASSWORD.asResult();
        }
        User u = StreamUtils.of(all)
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        assert u != null;
        // Update the user token and old token will be marked invalidated
        return new Result<>(tokenService.put(u.getId()));
    }


    private boolean emailHasRegister(String email) {
        return ((List<User>) repository.findAll())
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
