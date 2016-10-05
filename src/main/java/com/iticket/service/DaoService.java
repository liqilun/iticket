package com.iticket.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.iticket.model.BaseObject;

public interface DaoService {
	<T extends BaseObject> T saveObject(T o);
	<T extends BaseObject> T getObject(Class<T> clazz, Serializable id);
	<T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue);
	<T extends BaseObject> T removeObject(T o);
	<T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id);
	<T extends BaseObject> int getObjectCount(Class<T> clazz);
	<T extends BaseObject> void removeObjectList(Collection<T> entityList);
	<T extends BaseObject> void saveObjectList(Collection<T> entityList);
	<T extends BaseObject> void addObjectList(Collection<T> entityList);
	<T extends BaseObject> void saveObjectList(T ... entityList);
	<T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from, int rows);
	<T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList);
	<T extends BaseObject, S extends Serializable> Map<S, T> getObjectMap(Class<T> clazz, Collection<S> idList);
	<T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename);
	<T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename, Collection<S> idList);
	<T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname, boolean isDistinct);
	<T extends BaseObject> T updateObject(T entity);
	<T extends BaseObject> T addObject(T entity);
	/**
	 * 根据数据中的ukey获取对象（可缓存此对象）,主键必须是id
	 * @param clazz 查询对象，如:TicketOrder.class
	 * @param ukeyName 字段名，如：tradeNo
	 * @param ukeyvalue 字段值，如：1091023141727057
	 * @return
	 * TODO:cache
	 */
	<T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue);
	
	List queryByRowsRange(final String hql, final int from, final int maxnum, final Object... params);
	
	<T extends BaseObject> T addPropertyNum(Class<T> clazz, Serializable id, String property, Integer num);
	<T extends BaseObject> void addPropertyNum(T entity, String property, Integer num);
	<T extends BaseObject> void addPropertiesNum(T entity, String properties, Integer... nums);
	<T extends BaseObject> T addPropertiesNum(Class<T> clazz, Serializable id, String properties, Integer... nums);
	
	<T extends BaseObject> T updateProperties(Class<T> clazz, Serializable id, String properties, Serializable... values);
	<T extends BaseObject> void updateProperties(T entity, String properties, Serializable... values);
	<T extends BaseObject, S extends Serializable> S getObjectPropertyByUkey(Class<T> clazz, String ukeyname, Serializable ukeyvalue, String propertyname);
	<T extends BaseObject> Object getObjectProperty(Class<T> clazz, Serializable id, String propertyname);
	/**
	 * 批量查询对象（select * from xxxx where yyy in (....)
	 * @param clazz
	 * @param propertyName
	 * @param valueList
	 * @return
	 */
	<T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName, Collection<S> valueList);
	List<Map<String, Object>> queryMapBySQL(String sql, int from, int maxnum, Object... params);
	List findByCriteria(DetachedCriteria query);
	List findByCriteria(DetachedCriteria query, int from, int maxnum);
	List findByHql(String hql, Object... values);
	/**
	 * 重新读取对象，设置属性值
	 * @param entity
	 * @param property
	 * @param value
	 * @return
	 */
	<T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value);
}
