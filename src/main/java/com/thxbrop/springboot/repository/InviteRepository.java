package com.thxbrop.springboot.repository;

import com.thxbrop.springboot.entity.Invite;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InviteRepository extends CrudRepository<Invite, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Invite i SET i.state = :state WHERE i.id = :id")
    void updateState(@Param("id") int id, @Param("state") int state);
}