package com.example.demo.business.entities.repositories;

import com.example.demo.business.entities.Message;
import com.example.demo.business.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findAllByUser(User user);

    Iterable<Message> findAllByOrderByPostedDateTimeDesc();
}
