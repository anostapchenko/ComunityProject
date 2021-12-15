package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
@EnableTransactionManagement
public class TestServiceNumberOfUsers {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int getNumberOfUsers(){
        Query query = entityManager.createQuery("select u from User u", User.class);
        System.out.println("testService" + query.getResultList());
        return query.getResultList().size();
    }
}
