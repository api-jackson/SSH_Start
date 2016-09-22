package com.ssh.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ssh.model.Product;

/**
 * Product �� DAO ��
 * @author Jacskon
 *
 */
public class ProductDao extends HibernateDaoSupport{

	
	
	public void save(Product product)
	{
		System.out.println("DAO�е�save����ִ��...");
		this.getHibernateTemplate().save(product);
	}
}
