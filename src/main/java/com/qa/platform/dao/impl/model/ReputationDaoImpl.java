package com.qa.platform.dao.impl.model;

import com.qa.platform.dao.abstracts.model.ReputationDao;
import com.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

}
