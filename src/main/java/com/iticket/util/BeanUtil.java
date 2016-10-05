package com.iticket.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class BeanUtil {
	private static final transient GewaLogger dbLogger = LoggerUtils.getLogger(BeanUtil.class);
	public static void copyProperties(Object dest, Object orig) {
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			// ignore
		}
	}
	/**
	 * array,collection,map
	 * @param list
	 * @return <code>true</code> if the List/array/map is null or empty.
	 */
	public final static boolean isEmptyContainer(Object container) {
		if (container == null)
			return true;
		if (container.getClass().isArray()) {
			// Thanks to Eric Fixler for this refactor.
			return Array.getLength(container) == 0;
		}
		if (container instanceof Collection) {
			return ((Collection) container).size() == 0;
		}
		if(container instanceof Map){
			return ((Map) container).size() == 0;
		}
		return false;
	}


	public static Object get(Object object, String property) {
		try {
			return PropertyUtils.getProperty(object, property);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
		}
		return null;
	}

	public static void set(Object object, String property, Object newproperty) {
		try {
			PropertyUtils.setProperty(object, property, newproperty);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (Exception e) {
		}
	}

	public static List<Long> getNotNullId(final List<Long> idList) {
		List<Long> result = new ArrayList<Long>();
		if (idList == null || idList.isEmpty())
			return result;
		for (Long id : idList)
			if (id != null)
				result.add(id);
		return result;
	}

	public static <T> Map beanListToMap(final Collection<T> beanList, String keyproperty, String valueproperty, boolean ignoreNull) {
		Map result = new HashMap();
		for (Object bean : beanList) {
			try {
				Object key = PropertyUtils.getNestedProperty(bean, keyproperty);
				Object value = PropertyUtils.getNestedProperty(bean, valueproperty);
				if (key == null)
					continue;
				if (value != null)
					result.put(key, value);
				else if (!ignoreNull)
					result.put(key, value);
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * @param beanList
	 * @param property
	 * @return
	 */
	public static Map groupBeanList(final Collection beanList, String property) {
		return groupBeanList(beanList, property, null);
	}

	/**
	 * @param beanList
	 * @param property
	 * @param nullKey
	 * @return
	 */
	public static Map groupBeanList(final Collection beanList, String property, Object nullKey) {
		Map<Object, List> result = new LinkedHashMap<Object, List>();
		for (Object bean : beanList) {
			try {
				Object keyvalue = PropertyUtils.getNestedProperty(bean, property);
				if (keyvalue == null)
					keyvalue = nullKey;
				if (keyvalue != null) {
					List tmpList = result.get(keyvalue);
					if (tmpList == null) {
						tmpList = new ArrayList();
						result.put(keyvalue, tmpList);
					}
					tmpList.add(bean);
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static Map beanListToMap(final Collection beanList, String keyproperty) {
		Map result = new HashMap();
		for (Object bean : beanList) {
			try {
				Object key = PropertyUtils.getNestedProperty(bean, keyproperty);
				if (key != null)
					result.put(key, bean);
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static List<Long> getIdList(final String idListStr, String spliter) {
		List<Long> result = new ArrayList<Long>();
		if (StringUtils.isBlank(idListStr))
			return result;
		String[] idList = idListStr.split(spliter);
		for (String idStr : idList) {
			try {
				long id = Long.parseLong(idStr.trim());
				result.add(id);
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static List<Integer> getIntgerList(final String idListStr, String spliter) {
		List<Integer> result = new ArrayList<Integer>();
		if (StringUtils.isBlank(idListStr))
			return result;
		String[] idList = idListStr.split(spliter);
		for (String idStr : idList) {
			try {
				int id = Integer.parseInt(idStr.trim());
				result.add(id);
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static <T> List<T> getBeanPropertyList(final Collection beanList, Class<T> clazz, String propertyname, boolean unique) {
		return getBeanPropertyList(beanList, propertyname, unique);
	}

	public static <T> List<T> getBeanPropertyList(final Collection beanList, String propertyname, boolean unique) {
		List<T> result = new ArrayList<T>();
		for (Object bean : beanList) {
			try {
				T pv = (T) PropertyUtils.getProperty(bean, propertyname);
				if (pv != null && (!unique || !result.contains(pv)))
					result.add(pv);
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 
	 * @param beanList
	 * @param properties
	 * @return
	 */
	public static List<Map> getBeanMapList(final Collection beanList, String... properties) {
		List<Map> result = new ArrayList<Map>();
		Iterator it = beanList.iterator();
		Object bean = null;
		while (it.hasNext()) {
			bean = it.next();
			Map beanMap = new HashMap();
			boolean hasProperty = false;
			for (String property : properties) {
				try {
					Object pv = null;
					String keyname = property;
					if (property.indexOf(".") > 0) {
						pv = PropertyUtils.getNestedProperty(bean, property);
						keyname = keyname.replace('.', '_');
					} else {
						pv = PropertyUtils.getProperty(bean, property);
					}
					if (pv != null) {
						hasProperty = true;
						beanMap.put(keyname, pv);
					}
				} catch (Exception e) {
				}
			}
			if (hasProperty)
				result.add(beanMap);
		}
		return result;
	}

	public static Map getBeanMap(final Object bean) {
		return getBeanMap(bean, true);
	}

	/**
	 * 
	 * @param bean
	 * @param nested
	 * @return
	 */
	public static Map getBeanMap(final Object bean, boolean nested) {
		return getBeanMap(bean, nested, false);
	}

	public static Map<String, Object> getBeanMap(final Object bean, boolean nested, boolean ignoreNull) {
		if (bean == null)
			return null;
		Assert.isTrue(!isSimpleProperty(bean.getClass()), "bean can't be simple!");
		Map beanMap = null;
		try {
			if (bean instanceof Map) {
				beanMap = new LinkedHashMap((Map) bean);
			} else {
				beanMap = PropertyUtils.describe(bean);
			}
			Object pv = null;
			for (Object key : new ArrayList(beanMap.keySet())) {
				pv = beanMap.get(key);
				if (pv == null) {
					if (ignoreNull)
						beanMap.remove(key);
				} else if (!isSimpleProperty(pv.getClass())) {
					if (!nested) {
						beanMap.remove(key);
					} else {
						if (pv instanceof Collection)
							beanMap.put(key, getBeanMapList((Collection) pv, false));
						else if (pv.getClass().isArray())
							beanMap.put(key, getBeanMapList(Arrays.asList((Object[]) pv), false));
						else
							beanMap.put(key, getBeanMap(pv, false));
					}
				}
			}
			beanMap.remove("class");
		} catch (Exception e) {
			dbLogger.warn(e, 20);
			return null;
		}
		return beanMap;
	}

	public static Map getBeanMapWithKey(final Object bean, String... propertyList) {
		return getBeanMapWithKey(bean, false, propertyList);
	}

	public static Map getBeanMapWithKey(final Object bean, boolean nested, String... keys) {
		return getBeanMapWithKey(bean, nested, false, keys);
	}

	public static Map getBeanMapWithKey(final Object bean, boolean nested, boolean ignoreNull, String... keys) {
		if (keys == null || keys.length == 0)
			return getBeanMap(bean, nested, ignoreNull);
		Map result = new HashMap();
		Object pv = null;
		for (String key : keys) {
			try {
				pv = PropertyUtils.getNestedProperty(bean, key);
			} catch (Exception e) {
			}
			if (pv == null) {
				if (ignoreNull)
					continue;
				else
					result.put(key, null);
			} else if (!isSimpleProperty(pv.getClass())) {
				if (nested) {
					if (pv instanceof Collection)
						result.put(key, getBeanMapList((Collection) pv, false));
					else
						result.put(key, getBeanMap(pv, false));
				}
			} else {
				result.put(key, pv);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param beanList
	 * @param containNest
	 * @return
	 */
	public static List getBeanMapList(final Collection beanList, boolean nested) {
		List result = new ArrayList<Map>();
		for (Object bean : beanList) {
			if (bean == null || isSimpleProperty(bean.getClass()))
				result.add(bean);
			else
				result.add(getBeanMap(bean, nested));
		}
		return result;
	}

	public static List<Map> getBeanMapList(Collection beanList, boolean nested, String... keys) {
		if (keys == null || keys.length == 0)
			return getBeanMapList(beanList, nested);
		List<Map> result = new ArrayList<Map>();
		for (Object bean : beanList) {
			result.add(getBeanMapWithKey(bean, nested, keys));
		}
		return result;
	}

	public static String buildString(final Object bean, boolean nested) {
		if (bean == null)
			return null;
		Map map = getBeanMap(bean, nested, true);
		return buildString(map);
	}

	private static String buildString(Map map) {
		StringBuilder result = new StringBuilder();
		for (Object key : map.keySet()) {
			result.append("," + key + "=");
			if (map.get(key) instanceof Collection) {
				result.append("[");
				Collection vlist = (Collection) map.get(key);
				for (Object el : vlist) {
					if (el == null)
						continue;
					if (isSimpleProperty(el.getClass()))
						result.append(el);
					else if (el instanceof Map)
						result.append(buildString((Map) el));
					result.append(",");
				}
				if (vlist.size() > 0)
					result.deleteCharAt(result.length() - 1);
				result.append("]");
			} else if (map.get(key) instanceof Map) {
				result.append("{" + buildString((Map) map.get(key)) + "}");
			} else {
				result.append(map.get(key));
			}
		}
		if (result.length() > 0)
			result.deleteCharAt(0);
		return result.toString();
	}

	/**
	 * 
	 * @param beanList
	 * @param keyProperty
	 * @param valueProperty
	 * @return
	 */
	public static Map getKeyValuePairMap(List beanList, String keyProperty, String valueProperty) {
		Map result = new HashMap();
		for (Object bean : beanList) {
			try {
				Object keyvalue = PropertyUtils.getProperty(bean, keyProperty);
				if (keyvalue != null) {
					result.put(keyvalue, PropertyUtils.getProperty(bean, valueProperty));
				}
			} catch (Exception e) {// ignore
			}
		}
		return result;
	}

	public static Map<Object, List> groupBeanProperty(List beanList, String keyname, String valuename) {
		Map<Object, List> result = new HashMap<Object, List>();
		for (Object bean : beanList) {
			try {
				Object keyvalue = PropertyUtils.getProperty(bean, keyname);
				if (keyvalue != null) {
					List list = result.get(keyvalue);
					if (list == null) {
						list = new ArrayList();
						result.put(keyvalue, list);
					}
					list.add(PropertyUtils.getProperty(bean, valuename));
				}
			} catch (Exception e) {// ignore
			}
		}
		return result;
	}

	public static <T> List<T> getSubList(List<T> list, int from, int maxnum) {
		if (list == null || list.size() <= from)
			return new ArrayList<T>();
		return new ArrayList(list.subList(from, Math.min(from + maxnum, list.size())));
	}

	/**
	 * 
	 * @param longList
	 * @param length
	 * @return
	 */
	public static <T> List<List<T>> partition(List<T> longList, int length) {
		List<List<T>> result = new ArrayList<List<T>>();
		if (longList == null || longList.isEmpty())
			return result;
		if (longList.size() <= length) {
			result.add(longList);
		} else {
			int groups = (longList.size() - 1) / length + 1;
			for (int i = 0; i < groups - 1; i++) {
				result.add(new ArrayList(longList.subList(length * i, length * (i + 1))));
			}
			result.add(new ArrayList(longList.subList(length * (groups - 1), longList.size())));
		}
		return result;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~copy from spring for reduce
	// dependency~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Check if the given type represents a "simple" property: a primitive, a
	 * String or other CharSequence, a Number, a Date, a URI, a URL, a Locale, a
	 * Class, or a corresponding array.
	 * <p>
	 * Used to determine properties to check for a "simple" dependency-check.
	 * 
	 * @param clazz
	 *            the type to check
	 * @return whether the given type represents a "simple" property
	 * @see org.springframework.beans.factory.support.RootBeanDefinition#DEPENDENCY_CHECK_SIMPLE
	 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies
	 */
	public static boolean isSimpleProperty(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * Check if the given type represents a "simple" value type: a primitive, a
	 * String or other CharSequence, a Number, a Date, a URI, a URL, a Locale or
	 * a Class.
	 * 
	 * @param clazz
	 *            the type to check
	 * @return whether the given type represents a "simple" value type
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz)
				|| clazz.equals(URI.class) || clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~for hbase~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public static Map<String, String> getSimpleStringMap(final Object bean) {
		if (bean == null)
			return null;
		if(bean instanceof Map){
			return toSimpleStringMap((Map) bean);
		}
		if (BeanUtil.isSimpleProperty(bean.getClass())) {
			throw new IllegalArgumentException("bean can't be simple!");
		}
		Map beanMap = null;
		try {
			beanMap = PropertyUtils.describe(bean);
			Object pv = null;
			for (Object key : new ArrayList(beanMap.keySet())) {
				pv = beanMap.get(key);
				if (pv == null) {
					beanMap.remove(key);
					continue;
				}
				if (!BeanUtil.isSimpleProperty(pv.getClass()))
					beanMap.remove(key);
			}
			beanMap.remove("class");
		} catch (Exception e) {
			
			return null;
		}
		return toSimpleStringMap(beanMap);
	}

	public static Map<String, String> getSimpleStringMapWithKey(final Object bean, String... keys) {
		Map<String, String> result = new HashMap<String, String>();
		Object pv;
		for (String key : keys) {
			try {
				if (ValidateUtil.isVariable(key, 1, 50)) {
					pv = PropertyUtils.getNestedProperty(bean, key);
					if (pv != null && BeanUtil.isSimpleProperty(pv.getClass())) {
						result.put(key, getStringValue(pv));
					}
				}
			} catch (Exception e) {// ignore
			}
		}
		return result;
	}

	public static Map<String, String> toSimpleStringMap(Map map) {
		Map<String, String> result = new HashMap<String, String>();
		for (Object key : map.keySet()) {
			String tmpKey = getStringValue(key);
			// if(ValidateUtil.isVariable(tmpKey, 1, 50)){
			result.put(tmpKey, getStringValue(map.get(key)));
			// }
		}
		return result;
	}

	public static String getStringValue(Object value) {
		if (value == null)
			return null;
		if (value instanceof String)
			return (String) value;
		if (value instanceof Timestamp) {
			return DateUtil.formatTimestamp((Timestamp) value);
		} else if (value instanceof Date) {
			return DateUtil.formatDate((Date) value);
		} else {// ignore URL... etc,
			return value.toString();
		}
	}

	public static String getIdentityHashCode(Object object) {
		return "0x" + Integer.toHexString(System.identityHashCode(object));
	}

	/**
	 * Sorts map by values in ascending order.
	 * 
	 * @param <K>
	 *            map keys type
	 * @param <V>
	 *            map values type
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<V>> LinkedHashMap<K, V> sortMapByValue(Map<K, V> map, boolean asc) {
		List<Entry<K, V>> sortedEntries = sortEntriesByValue(map.entrySet(), asc);
		LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K, V>(map.size());
		for (Entry<K, V> entry : sortedEntries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * Sorts map entries by value in ascending order.
	 * 
	 * @param <K>
	 *            map keys type
	 * @param <V>
	 *            map values type
	 * @param entries
	 * @return
	 */
	private static <K, V extends Comparable<V>> List<Entry<K, V>> sortEntriesByValue(Set<Entry<K, V>> entries, boolean asc) {
		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(entries);
		Collections.sort(sortedEntries, new ValueComparator<V>(asc));
		return sortedEntries;
	}

	/**
	 * Komparator podla hodnot v polozkach mapy.
	 * 
	 * @param <V>
	 *            typ hodnot
	 */
	private static class ValueComparator<V extends Comparable<V>> implements Comparator<Entry<?, V>> {
		private boolean asc;
		public ValueComparator(boolean asc){
			this.asc = asc;
		}
		@Override
		public int compare(Entry<?, V> entry1, Entry<?, V> entry2) {
			int result = entry1.getValue().compareTo(entry2.getValue());
			return asc?result:-result;
		}
	}
	public static <T> List<T> copyListByField(List sourceList, String...copyFields){
		return copyListByField(sourceList, null, copyFields);
	}
	
	public static <T> List<T> copyListByField(List sourceList, Class targetClass, String...copyFields){
		List newList = new ArrayList();
		try{
			for(Object bean : sourceList){
				Object newBean;
				if(targetClass!=null){
					newBean = targetClass.newInstance();
				}else{
					newBean = bean.getClass().newInstance();
				}
				for(String name:copyFields){
					Object value = getProperty(bean, name);
					setProperty(newBean, name, value);
				}
				newList.add(newBean);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return newList;
	}
	static void setProperty(final Object bean, final String property, final Object value){
		try {
			PropertyUtils.setProperty(bean, property, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static Object getProperty(Object bean, String property){
		try {
			Object pv = null;
			if(Map.class.isAssignableFrom(bean.getClass())){
				pv = ((Map)bean).get(property);
			}else{
				pv = PropertyUtils.getProperty(bean, property);
			}
			return pv;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static <T> List<Map<String, Object>> copyToListMap(List<T> resList){
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		if(CollectionUtils.isEmpty(resList)){
			return resMapList;
		}
		for(T t : resList){
			resMapList.add(getBeanMap(t));
		}
		return resMapList;
	}
}

