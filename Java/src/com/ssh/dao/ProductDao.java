package com.ssh.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ssh.model.Product;

/**
 * Product 的 DAO 类
 * @author Jacskon
 *
 */
public class ProductDao extends HibernateDaoSupport{

	
	
	public void save(Product product)
	{
		System.out.println("DAO中的save方法执行...");
		this.getHibernateTemplate().save(product);
	}
}
