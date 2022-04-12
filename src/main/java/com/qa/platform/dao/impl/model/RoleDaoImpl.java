package com.qa.platform.dao.impl.model;

import com.qa.platform.dao.abstracts.model.RoleDao;
import com.qa.platform.dao.util.SingleResultUtil;
import com.qa.platform.models.entity.user.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class RoleDaoImpl extends ReadWriteDaoImpl<Role, Long> implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Role> getByName(String name) {
        String hql = "select r as name from Role r where r.name = :name";
        TypedQuery<Role> query = (TypedQuery<Role>) entityManager.createQuery(hql).setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}