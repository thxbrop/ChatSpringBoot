package com.thxbrop.springboot.service;

import com.thxbrop.springboot.entity.Token;
import com.thxbrop.springboot.repository.TokenRepository;
import com.thxbrop.springboot.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class TokenService {
    private final TokenRepository repository;

    @Autowired
    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

    public Token pop(int userId) {
        Token res = peek(userId);
        if (res == null) return null;
        repository.delete(res);
        return res;
    }

    public Token peek(int userId) {
        return StreamUtils.of(repository.findAll())
                .filter(token -> token.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public Token put(int userId) {
        pop(userId);
        String token = UUID.randomUUID().toString().replaceAll("-", "") + "";
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(token.getBytes());
            Token t = new Token();
            t.setToken(Base64Utils.encodeToString(md5));
            t.setUserId(userId);
            return repository.save(t);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
