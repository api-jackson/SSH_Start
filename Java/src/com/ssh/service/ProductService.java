package com.ssh.service;

import org.springframework.transaction.annotation.Transactional;

import com.ssh.dao.ProductDao;
import com.ssh.model.Product;

/**
 * Product 的 service 类
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
		System.out.println("Service中的save方法执行...");
		productDao.save(product);
	}
	
}
