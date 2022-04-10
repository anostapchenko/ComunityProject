package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.user.Role;

import java.util.Optional;

public interface RoleService extends ReadWriteService<Role, Long> {

    Optional<Role> getByName(String name);

}
