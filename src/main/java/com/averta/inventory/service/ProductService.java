package com.averta.inventory.service;

import com.averta.inventory.entity.Product;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.averta.inventory.bo.ProcurmentProductBo;
import com.averta.inventory.bo.Response;

public interface ProductService {

	public void addProduct(Product product) throws Exception;

	public Product updateProduct(Product product, Long productId) throws Exception;

	public Product getProductById(Long productId) throws Exception;

	public Response getProducts(Integer pageNum, Integer numPerPage, String sortBy, String sortOrder, String searchBy)
			throws Exception;

	public void setProductStatus(Long productId) throws Exception;

	void importProducts(MultipartFile importFile) throws Exception;

	public List<ProcurmentProductBo> productList() throws Exception;

}
