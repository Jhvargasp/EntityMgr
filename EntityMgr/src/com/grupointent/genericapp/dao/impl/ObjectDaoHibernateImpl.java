/**
 * 
 */
package com.grupointent.genericapp.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.grupointent.genericapp.dao.ObjectDao;

/**
 * @author Jorge
 * 
 */
public class ObjectDaoHibernateImpl extends HibernateDaoSupport implements
		ObjectDao {
	private static Logger log = Logger.getLogger(ObjectDaoHibernateImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.grupointent.correspondence.domain.dao.EmployeeDao#get(java.lang.String
	 * )
	 */
	public Object get(String id, String className) {
		log.debug("Getting  " + className + " id " + id);

		ClassMetadata entityMetaData = getHibernateTemplate()
				.getSessionFactory().getClassMetadata(className);
		String colName = entityMetaData.getIdentifierPropertyName();
		Type t = entityMetaData.getPropertyType(colName);
		Object idK = id;
		if (t.getName().equalsIgnoreCase("integer")) {
			idK = Integer.parseInt(id);
		} else if (t.getName().equalsIgnoreCase("date")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				idK = (sdf.parse(id));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Object object = getHibernateTemplate().get(className,
				(Serializable) idK);
		return object;

	}

	public void delete(Object entity) {
		log.debug("Deleting  " + entity);
		getHibernateTemplate().delete(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.grupointent.correspondence.domain.dao.EmployeeDao#saveOrUpdate(com
	 * .grupointent.correspondence.domain.Employee)
	 */
	public boolean saveOrUpdate(Object object) {
		log.debug("Saving object ");
		getHibernateTemplate().saveOrUpdate(object);
		return false;
	}

	public List<Object> query(String query,boolean fullResults) {
		log.debug("query:" + query);

		Session session = null;
		try {

			session = getSessionFactory().getCurrentSession();
			log.debug("Query to:"
					+ session.connection().getMetaData()
							.getDatabaseProductName());

		} catch (Exception e) {
			// e.printStackTrace();
			try {
				session = getSessionFactory().openSession();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		try {
			log.debug("Query to:"
					+ session.connection().getMetaData()
							.getDatabaseProductName());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query query2 = session.createQuery(query);
		if (!fullResults) {
			query2.setMaxResults(50);
		}
		List l = null;
		try {
			l = query2.setCacheable(true).list();

		} catch (Exception e) {
			l = query2.list();
		}

		session.close();
		return l;

	}

	public List<Object> querySQL(String query) {
		log.debug("query:" + query);

		Session session = null;
		try {

			session = getSessionFactory().getCurrentSession();
			log.debug("Query to:"
					+ session.connection().getMetaData()
							.getDatabaseProductName());

		} catch (Exception e) {
			// e.printStackTrace();
			try {
				session = getSessionFactory().openSession();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		try {
			log.debug("Query to:"
					+ session.connection().getMetaData()
							.getDatabaseProductName());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query query2 = session.createSQLQuery(query);
		// query2.setMaxResults(50);
		List l = null;
		try {
			l = query2.list();

		} catch (Exception e) {

		}

		session.close();
		return l;

	}

}
