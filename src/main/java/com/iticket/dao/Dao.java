package com.iticket.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.iticket.model.BaseObject;

public interface Dao {
	<T extends BaseObject> T getObject(Class<T> clazz, Serializable id);
	<T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue);
	<T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList);
	<T extends BaseObject> T saveObject(T entity);
	<T extends BaseObject> T removeObject(Class<T> clazz, Serializable id);
	<T extends BaseObject> T removeObject(T entity);
	<T extends BaseObject> T updateObject(T entity);
	<T extends BaseObject> void updateObjectList(Collection<T> entity);
	<T extends BaseObject> T addObject(T entity);
	<T extends BaseObject> void removeObjectList(Collection<T> entityList);
	<T extends BaseObject> void saveObjectList(Collection<T> entityList);
	<T extends BaseObject> int getObjectCount(Class<T> clazz);
	<T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id);
	<T extends BaseObject> void addObjectList(Collection<T> entityList);
	<T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname);
	<T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname, boolean isDistinct);
	<T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from, int rows);
	<T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename);
	<T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename, Collection<S> idList);
	<T extends BaseObject> void saveObjectList(T... entityList);
	/**
	 * 根据数据中的ukey(数据库中有唯一索引)获取对象（可缓存此对象）
	 * @param clazz 查询对象，如:TicketOrder.class
	 * @param ukeyName 字段名，如：tradeNo
	 * @param ukeyvalue 字段值，如：ao2umysql4HEw7Y
	 * @return
	 */
	<T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue);
	/**
	 * 批量查询对象（select * from xxxx where yyy in (....)
	 * @param clazz
	 * @param propertyName
	 * @param valueList
	 * @return
	 */
	<T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName, Collection<S> valueList);
	/**
	 * 重新读取对象，设置属性值
	 * @param entity
	 * @param property
	 * @param value
	 * @return
	 */
	<T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value);

	List findByCriteria(DetachedCriteria query);
	List findByCriteria(DetachedCriteria query, int from, int maxnum);
	List findByHql(String hql, Object... values);
}
