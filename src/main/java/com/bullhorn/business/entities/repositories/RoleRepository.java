package com.bullhorn.business.entities.repositories;

import com.bullhorn.business.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
