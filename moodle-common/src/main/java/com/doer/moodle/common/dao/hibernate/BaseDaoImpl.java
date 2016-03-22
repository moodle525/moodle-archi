package com.doer.moodle.common.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<T> implements IBaseDao<T> {

	@Autowired
	protected SessionFactory sessionFactory;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		entityClass = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getEntity(Serializable objId) {
		Session session = getSession();
		return (T) session.get(entityClass, objId);
	}

	@Override
	public void delete(T t) {
		Session session = getSession();
		session.delete(t);
	}

	@Override
	public void delete(Serializable objId) {
		Session session = getSession();
		session.delete(this.getEntity(objId));
	}

	@Override
	public void save(T t) {
		Session session = getSession();
		session.save(t);
	}

	@Override
	public void update(T t) {
		Session session = getSession();
		session.update(t);
	}

	@Override
	public void saveOrUpdate(T t) {
		Session session = getSession();
		session.saveOrUpdate(t);
	}

	@Override
	public void saveOrUpdate(Session session, T t) {
		session.saveOrUpdate(t);
	}

	@Override
	public void flush() {
		Session session = getSession();
		session.flush();
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Criteria getCriteria() {
		return getSession().createCriteria(entityClass);
	}

}
