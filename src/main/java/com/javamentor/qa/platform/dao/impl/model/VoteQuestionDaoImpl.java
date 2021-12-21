package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion,Long> implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isUserVoteByQuestionIdAndUserId(Long id, Long userId){
        Query queryValidateUserVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.user.id in :userId) and (v.question.id in : id )  ", VoteQuestion.class);
        queryValidateUserVote.setParameter("userId",userId);
        queryValidateUserVote.setParameter("id",id);
        return queryValidateUserVote.getResultList().size() == 0;
    }

    @Override
    public int getVote(Long id){
        Query queryDownVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.question.id in :id) and (v.vote = 'DOWN_VOTE')  ", VoteQuestion.class);
        queryDownVote.setParameter("id",id);
        int downVote = queryDownVote.getResultList().size() * -1;
        Query queryUpVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.question.id in :id) and (v.vote = 'UP_VOTE')  ", VoteQuestion.class);
        queryUpVote.setParameter("id",id);
        int upVote = queryUpVote.getResultList().size();
        return downVote+upVote;
    }
}
