/**
 * 
 */
package com.grupointent.genericapp.dao;

import java.util.List;

import org.hibernate.SessionFactory;

/**
 * @author Jorge
 * 
 */
public interface ObjectDao {

	public Object get(String id, String className);

	public boolean saveOrUpdate(Object object);

	public void delete(Object entity);

	public List<Object> query(String query);

	public List<Object> querySQL(String query);

	public SessionFactory getSessionFactory();

}
