package com.doer.moodle.common.dao.hibernate;

import java.io.Serializable;

import org.hibernate.Session;

public interface IBaseDao<T> {

	/**
	 * 根据主键获取对象
	 * 
	 * @param objId
	 * @return
	 */
	public T getEntity(Serializable objId);

	/**
	 * 删除某个对象
	 * 
	 * @param t
	 */
	public void delete(T t);

	/**
	 * 删除某个对象
	 * 
	 * @param objId
	 */
	public void delete(Serializable objId);

	/**
	 * 保存某个对象
	 * 
	 * @param t
	 */
	public void save(T t);

	/**
	 * 更新某个对象
	 * 
	 * @param t
	 */
	public void update(T t);

	/**
	 * 保存或者更新某个对象
	 * 
	 * @param t
	 */
	public void saveOrUpdate(T t);

	/**
	 * 保存或者更新某个对象
	 * 
	 * @param session
	 * @param t
	 */
	public void saveOrUpdate(Session session, T t);

	/**
	 * 刷新缓存
	 */
	public void flush();
	

}
