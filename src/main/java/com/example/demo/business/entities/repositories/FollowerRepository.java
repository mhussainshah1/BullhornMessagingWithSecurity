package com.example.demo.business.entities.repositories;

import com.example.demo.business.entities.Follower;
import com.example.demo.business.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface FollowerRepository extends CrudRepository<Follower, Long> {

//    Set<Follower> findAllByFollowers(User user);
}
