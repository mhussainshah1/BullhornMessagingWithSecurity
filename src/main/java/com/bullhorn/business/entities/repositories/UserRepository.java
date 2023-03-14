package com.bullhorn.business.entities.repositories;

import com.bullhorn.business.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Long countByEmail(String email);

    Long countByUsername(String username);

    Set<User> findAllByFollowers(User user);

    Set<User> findAllByFollowings(User user);
}
