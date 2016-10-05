package com.iticket.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iticket.dao.Dao;
import com.iticket.model.BaseObject;
import com.iticket.util.BeanUtil;

public class BaseDaoHibernate implements Dao {
	
	@Autowired@Qualifier("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	@Override
	public <T extends BaseObject> T saveObject(T entity) {
		if(entity!=null) {
			hibernateTemplate.saveOrUpdate(entity);
			
		}
		return entity;
	}
	@Override
	public <T extends BaseObject> T getObject(Class<T> clazz, Serializable id) {
		if(id==null||clazz==null) return null;
		T o = hibernateTemplate.get(clazz, id);
		return o;
	}
	
	@Override
	public <T extends BaseObject> T removeObject(Class<T> clazz, Serializable id) {
		if(id==null||clazz==null) return null;
		T o = getObject(clazz, id);
		if( o!=null ) hibernateTemplate.delete(o);
		return o;
	}
	@Override
	public <T extends BaseObject> T removeObject(T object){
		if(object != null) hibernateTemplate.delete(object);
		return object;
	}
	@Override
	public <T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList) {
		if(idList == null || idList.isEmpty()) return new ArrayList<T>(0);
		List<T> result = new ArrayList<T>();
		T obj = null;
		for (S id : idList) {
			obj = getObject(clazz, id);
			if (obj != null) {
				result.add(obj);
			}
		}
		return result;
	}
	
	@Override
	public <T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName, Collection<S> valueList) {
		List<S> vlist = null;
		if(valueList instanceof List) vlist = (List)valueList;
		else vlist = new ArrayList<S>(valueList);
		List<List<S>> groupList = BeanUtil.partition(vlist, 500);
		List<T> result = new ArrayList<T>();
		for(List<S> group: groupList){
			DetachedCriteria query = DetachedCriteria.forClass(clazz);
			query.add(Restrictions.in(propertyName, group));
			List rows = hibernateTemplate.findByCriteria(query);
			result.addAll(rows);
		}
		return result;
	}

	@Override
	public <T extends BaseObject> void removeObjectList(Collection<T> entityList) {
		for(T entity: entityList){
			if(entity != null) removeObject(entity);
		}
	}
	@Override
	public <T extends BaseObject> void saveObjectList(Collection<T> entityList) {
		for(T entity: entityList){
			if(entity != null) saveObject(entity);
		}
	}
	@Override
	public <T extends BaseObject> void updateObjectList(Collection<T> entityList) {
		for(T entity: entityList){
			if(entity != null) {
				hibernateTemplate.update(entity);
			}
		}
	}
	@Override
	public <T extends BaseObject> int getObjectCount(Class<T> clazz){
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		query.setProjection(Projections.rowCount());
		List result = hibernateTemplate.findByCriteria(query);
		if(result.isEmpty()) return 0;
		return Integer.parseInt("" + result.get(0));
	}
	@Override
	public <T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id) {
		T entity = getObject(clazz, id);
		if(entity == null) return null;
		removeObject(entity);
		return entity;
	}
	@Override
	public <T extends BaseObject> T updateObject(T entity) {
		hibernateTemplate.update(entity);
		return entity;
	}
	@Override
	public <T extends BaseObject> T addObject(T entity) {
		hibernateTemplate.save(entity);
		return entity;
	}
	@Override
	public <T extends BaseObject> void addObjectList(Collection<T> entityList) {
		for(T entity: entityList){
			if(entity != null) addObject(entity);
		}
	}
	@Override
	public <T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from, int rows) {
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		if(asc) query.addOrder(Order.asc(orderField));
		else query.addOrder(Order.desc(orderField));
		List result = hibernateTemplate.findByCriteria(query, from, rows);
		return result;
	}
	@Override
	public <T extends BaseObject> void saveObjectList(T... entityList) {
		for(T entity: entityList){
			if(entity!= null) saveObject(entity);
		}
	}
	@Override
	public <T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename){
		Map result = new HashMap();
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		query.setProjection(Projections.projectionList()
				.add(Projections.property(keyname), keyname)
				.add(Projections.property(valuename), valuename));
		query.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List entryList = hibernateTemplate.findByCriteria(query);
		for(Object entry: entryList) result.put(((Map)entry).get(keyname), ((Map)entry).get(valuename));
		return result;
	}
	@Override
	public <T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename, Collection<S> idList){
		Map result = new HashMap();
		//TODO: 判断对象是否是cache，无cache使用上面方法！hibernateTemplate.getSessionFactory().;
		for(S id:idList){
			T entity = getObject(clazz, id);
			if(entity != null) result.put(BeanUtil.get(entity, keyname), BeanUtil.get(entity, valuename));
		}
		return result;
	}
	
	@Override
	public <T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue){
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		query.add(Restrictions.eq(fieldname, fieldvalue));
		List result = hibernateTemplate.findByCriteria(query);
		return result;
	}
	@Override
	public <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname) {
		return getObjectPropertyList(clazz, propertyname, false);
	}
	@Override
	public <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname, boolean isDistinct) {
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		if(isDistinct) query.setProjection(Projections.distinct(Projections.property(propertyname)));
		else query.setProjection(Projections.property(propertyname));
		List result = hibernateTemplate.findByCriteria(query);
		return result;
	}
	
	@Override
	public <T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		query.add(Restrictions.eq(ukeyName, ukeyValue));
		List result = hibernateTemplate.findByCriteria(query);
		if(result.isEmpty()) return null;
		if(result.size() > 1) throw new IllegalStateException("查询出多个记录：" + clazz.getName() + ", ukey=" + ukeyName + ", value=" + ukeyValue);
		return (T)result.get(0);
	}
	
	@Override
	public <T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value) {
		hibernateTemplate.refresh(entity);
		BeanUtil.set(entity, property, value);
		hibernateTemplate.update(entity);
	}
	
	
	@Override
	public List findByCriteria(DetachedCriteria query) {
		return hibernateTemplate.findByCriteria(query);
	}
	@Override
	public List findByCriteria(DetachedCriteria query, int from, int maxnum) {
		return hibernateTemplate.findByCriteria(query, from, maxnum);
	}
	@Override
	public List findByHql(String hql, Object... values) {
		return hibernateTemplate.find(hql, values);
	}
	
}
