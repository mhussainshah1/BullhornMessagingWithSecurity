package com.bullhorn.business.entities.repositories;

import com.bullhorn.business.entities.InvalidPassword;
import org.springframework.data.repository.CrudRepository;

public interface InvalidPasswordRepository extends CrudRepository<InvalidPassword, Long> {
}
