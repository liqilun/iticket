package com.iticket.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iticket.Config;
import com.iticket.dao.Dao;
import com.iticket.service.BaseService;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;

public class BaseServiceImpl implements BaseService{
	protected final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	@Autowired@Qualifier("baseDao")
	protected Dao baseDao;
	@Autowired@Qualifier("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	public void setHibernateTemplate(HibernateTemplate hbt) {
		hibernateTemplate = hbt;
	}
	
	@Autowired@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate template){
		jdbcTemplate = template;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setBaseDao(Dao baseDao) {
		this.baseDao = baseDao;
	}
	@Autowired@Qualifier("config")
	protected Config config;
	
	@Override
	public List queryByRowsRange(final String hql, final int from, final int maxnum, final Object... params){
		return hibernateTemplate.execute(new HibernateCallback<List>(){
			@Override
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setFirstResult(from).setMaxResults(maxnum);
				if(params != null)
					for (int i = 0,length=params.length; i < length; i++) {
						query.setParameter(i, params[i]);
					}
				return query.list();
			}
		});
	}
	@Override
	public List queryByNameParams(final String hql, final int from, final int maxnum, final String paramnames, final Object... params){
		return hibernateTemplate.execute(new HibernateCallback<List>(){
			@Override
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				query.setFirstResult(from).setMaxResults(maxnum);
				String[] names = StringUtils.split(paramnames, ",");
				for (int i=0,length=names.length; i < length; i++) {
					if(params[i] instanceof Collection){
						query.setParameterList(names[i], (Collection)params[i]);
					}else{
						query.setParameter(names[i], params[i]);
					}
				}
				return query.list();
			}
		});
	}
}
