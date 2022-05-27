package com.thxbrop.springboot.repository;

import com.thxbrop.springboot.entity.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Integer> {

}
