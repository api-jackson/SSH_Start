package com.ssh.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.ssh.model.Product;
import com.ssh.service.ProductService;

/**
 * Product 的 action 类
 * @author Jacskon
 *
 */
public class ProductAction extends ActionSupport implements ModelDriven<Product>{

	private ProductService productService;
	
	private Product product = new Product();

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public Product getModel() {
		// TODO Auto-generated method stub
		return product;
	}
	
	/**
	 * 保存商品的执行方法:save
	 */
	public String save()
	{
		System.out.println("Action中的save方法执行...");
		productService.save(product);
		return NONE;
	}
}
