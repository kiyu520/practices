package com.Service;

import com.Entity.Product;
import com.Mappers.pro_mapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collections;
import java.util.List;

public class ProductService {
    private static pro_mapper proMapper;

    public ProductService() {
        // 从工具类获取SqlSessionFactory
        SqlSessionFactory factory = com.Tools.SqlUtil.sqlSessionFactory;
        SqlSession sqlSession = factory.openSession(true);
        this.proMapper = sqlSession.getMapper(pro_mapper.class);
    }
    public boolean addProduct(Product product) {
        if(product.getProd_id() <= 0 || product.getProd_name().isEmpty() || product.getPrice() < 0 || product.getQuantity() < 0){
            return false;
        }
        List<Product> existProduct = proMapper.select_product_id(product.getProd_id());
        if(existProduct != null && !existProduct.isEmpty()){
            return false;
        }
        int result = proMapper.add_product_entity(product);
        return result > 0;
    }
    public boolean deleteProduct(int pro_id) {

        List<Product> existProduct = proMapper.select_product_id(pro_id);
        if(existProduct == null || existProduct.isEmpty()){
            return false;
        }
        Product product = existProduct.get(0);
        if(product.getQuantity() != 0){
            return false;
        }
        int result = proMapper.delete_product_id(pro_id);
        return result > 0;
    }
    public boolean stockIn(int pro_id, float inquantity) {
        if(inquantity <= 0){
            return false;
        }
        List<Product> existProduct = proMapper.select_product_id(pro_id);
        if(existProduct == null){
            return false;
        }
        Product product = existProduct.get(0);
        double newStock = product.getQuantity() + inquantity;
        product.setQuantity(newStock);
        int result = proMapper.update_product_id(product);
        return result > 0;

    }
    public boolean stockOut(int pro_id, float outquantity) {
        if(outquantity <= 0) {
            return false;
        }
        List<Product> existProduct = proMapper.select_product_id(pro_id);
        if(existProduct == null){
            return false;
        }
        Product product = existProduct.get(0);

        if(outquantity > product.getQuantity()){
            return false;
        }
        double newStock = product.getQuantity() - outquantity;
        product.setQuantity(newStock);
        int result = proMapper.update_product_id(product);
        return result > 0;
    }
//    查询所有产品
    public static List<Product> findAllproducts(){
        return proMapper.select_product_all();
    }

    public Product getProductById(int prodId) {
        // 调用Mapper按ID查询（返回List，需提取唯一结果）
        List<Product> productList = proMapper.select_product_id(prodId);
        // 处理空结果：无数据返回null，有数据返回第一个（ID唯一）
        return productList != null && !productList.isEmpty() ? productList.get(0) : null;
    }

    public List<Product> queryProducts(Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {

        if (o != null && (Integer) o > 0) {
            return proMapper.select_product_id((Integer) o);
        }
        if (o1 != null && !((String) o1).isEmpty()) {
            return proMapper.select_product_name((String) o1);
        }
        if (o2 != null) {
            return proMapper.select_product_price((Double) o2);
        }
        if (o3 != null && !((String) o3).isEmpty()) {
            return proMapper.select_product_type((String) o3);
        }
        if(o4 != null && o4 instanceof Double){
            return proMapper.select_product_quantity((Double) o4);
        }
        if (o5 != null && (Integer) o5 > 0) {
            return proMapper.select_product_supid((Integer) o5);
        }
        return Collections.emptyList();
    }

    public boolean isProductExist(int prodId) {
        return getProductById(prodId) != null;
    }

    public double getStockById(int prodId) {
        Product product = getProductById(prodId);
        return product != null ? product.getQuantity() : 0;
    }

    public Integer getSupplierIdByProdId(int prodId) {
        Product product = getProductById(prodId);
        return product != null ? product.getSup_id() : null;
    }
}
