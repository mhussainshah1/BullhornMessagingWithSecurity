package com.bullhorn.business.entities.repositories;

import com.bullhorn.business.entities.InvalidPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidPasswordRepository extends CrudRepository<InvalidPassword, Long> {
}
