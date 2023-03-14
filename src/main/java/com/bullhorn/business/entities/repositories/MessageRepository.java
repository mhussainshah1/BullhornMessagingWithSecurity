package com.bullhorn.business.entities.repositories;

import com.bullhorn.business.entities.Message;
import com.bullhorn.business.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findAllByUser(User user);

    Iterable<Message> findAllByOrderByPostedDateTimeDesc();
}
