package com.ssh.service;

import org.springframework.transaction.annotation.Transactional;

import com.ssh.dao.ProductDao;
import com.ssh.model.Product;

/**
 * Product �� service ��
 * @author Jacskon
 *
 */
@Transactional
public class ProductService {
	
	private ProductDao productDao;

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	public void save(Product product)
	{
		System.out.println("Service�е�save����ִ��...");
		productDao.save(product);
	}
	
}
