package com.iticket.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.iticket.model.program.Program;
import com.iticket.service.ProgramService;

@Service("programService")
public class ProgramServiceImpl extends BaseServiceImpl implements ProgramService{
	@Override
	public List<Program> getProgramList(Long stadiumId) {
		DetachedCriteria query = DetachedCriteria.forClass(Program.class);
		query.add(Restrictions.eq("stadiumId", stadiumId));
		query.add(Restrictions.eq("delStatus", "N"));
		query.addOrder(Order.desc("id"));
		return baseDao.findByCriteria(query);
	}
}
