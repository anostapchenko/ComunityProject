package com.qa.platform.service.impl.model;

import com.qa.platform.dao.abstracts.model.RoleDao;
import com.qa.platform.models.entity.user.Role;
import com.qa.platform.service.abstracts.model.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl<Role, Long> implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
        this.roleDao = roleDao;
    }

    public Optional<Role> getByName(String name) {
        return roleDao.getByName(name);
    }
}
