package com.Service;

import com.Entity.Product;
import com.Mappers.pro_mapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collections;
import java.util.List;

public class ProductService {
    private SqlSessionFactory sqlSessionFactory;
    private pro_mapper proMapper;

    public ProductService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        this.proMapper = sqlSession.getMapper(pro_mapper.class);
    }
//添加产品，编号唯一
    public boolean addProduct(Product product) {
//        1.合法性校验
        if(product.getPro_id() <= 0 || product.getPro_name().isEmpty() || product.getPrice() < 0 || product.getQuantity() < 0){
            return false;
        }
//        2.编号唯一性校验(用Mapper按id查询)
        List<Product> existProduct = proMapper.select_product_id(product.getPro_id());
        if(existProduct != null && !existProduct.isEmpty()){
            return false;
        }
//        3.调用Mapper添加
        int result = proMapper.add_product_entity(product);
        return result > 0;
    }
//    删除产品，库存为0时
    public boolean deleteProduct(int pro_id) {
//        1.查询产品
        List<Product> existProduct = proMapper.select_product_id(pro_id);
        if(existProduct == null || existProduct.isEmpty()){
            return false;
        }
        Product product = existProduct.get(0);//按id查询结果唯一，取第一个
//        2.检验库存是否为0
        if(product.getQuantity() != 0){
            return false;
        }
//        3.调用Mapper删除
        int result = proMapper.delete_product_id(pro_id);
        return result > 0;
    }
//    商品进货，进货量>0
    public boolean stockIn(int pro_id, float inquantity) {
        if(inquantity <= 0){
            return false;
        }
//        查询商品是否存在
        List<Product> existProduct = proMapper.select_product_id(pro_id);
        if(existProduct == null){
            return false;
        }
        Product product = existProduct.get(0);
//        计算库存并更新
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
    public List<Product> findAllproducts(){
        return proMapper.select_product_all();
    }

    public Product getProductById(int prodId) {
        // 调用Mapper按ID查询（返回List，需提取唯一结果）
        List<Product> productList = proMapper.select_product_id(prodId);
        // 处理空结果：无数据返回null，有数据返回第一个（ID唯一）
        return productList != null && !productList.isEmpty() ? productList.get(0) : null;
    }

    // 多条件查询产品（适配pro_mapper的多维度查询）
    // 入参约定（按顺序）：prodId(INT)、prodName(STRING)、priceBegin(DOUBLE)、priceEnd(DOUBLE)、type(STRING)、supId(INT)
    public List<Product> queryProducts(Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        // 1. 优先按非空参数匹配查询维度（按需扩展其他条件）
        // 场景1：按产品ID查询
        if (o != null && o instanceof Integer && (Integer) o > 0) {
            return proMapper.select_product_id((Integer) o);
        }
        // 场景2：按产品名称查询
        if (o1 != null && o1 instanceof String && !((String) o1).isEmpty()) {
            return proMapper.select_product_name((String) o1);
        }
        // 场景3：按价格区间查询
        if (o2 != null && o3 != null && o2 instanceof Double && o3 instanceof Double) {
            Double begin = (Double) o2;
            Double end = (Double) o3;
            return proMapper.select_product_price(begin, end);
        }
        // 场景4：按产品类型查询
        if (o4 != null && o4 instanceof String && !((String) o4).isEmpty()) {
            return proMapper.select_product_type((String) o4);
        }
        // 场景5：按供应商ID查询
        if (o5 != null && o5 instanceof Integer && (Integer) o5 > 0) {
            return proMapper.select_product_supid((Integer) o5);
        }
        // 无有效查询条件：返回空列表（避免null）
        return Collections.emptyList();
    }
}
