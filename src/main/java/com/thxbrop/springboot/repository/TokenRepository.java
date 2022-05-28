package com.thxbrop.springboot.repository;

import com.thxbrop.springboot.entity.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Token i SET i.token = :token WHERE i.userId = :userId")
    void updateToken(@Param("userId") int userId, @Param("token") String token);
}