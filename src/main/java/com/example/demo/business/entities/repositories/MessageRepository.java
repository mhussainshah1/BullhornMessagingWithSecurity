package com.example.demo.business.entities.repositories;

import com.example.demo.business.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

}
