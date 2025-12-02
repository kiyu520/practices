package com.Service;

import com.Entity.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
//添加产品，编号唯一
    public boolean addProduct(Product product) {
//        1.合法性校验
        if(product.getPro_id() <= 0 || product.getPro_name().isEmpty() || product.getPrice() < 0 || product.getQuantity() < 0){
            return false;
        }
//        2.编号唯一性校验
        if(productDAO.findById(product.getPro_id()) != null){
            return false;
        }
//        3.调用DAO添加
        return productDAO.add(product);
    }
//    删除产品，库存为0时
    public boolean deleteProduct(int pro_id) {
//        1.查询产品
        Product product = productDAO.findById(pro_id);
        if(product == null){
            return false;
        }
        if(product.getQuantity() != 0){
            return false;
        }
//        3.调用DAO删除
        return productDAO.delete(pro_id);
    }
//    商品进货，进货量>0
    public boolean stockIn(int pro_id, float inquantity) {
        if(inquantity <= 0){
            return false;
        }
//        查询商品是否存在
        Product product = productDAO.finfById(pro_id);
        if(product == null){
            return false;
        }
//        计算库存并更新
        double newStock = product.getQuantity() + inquantity;
        return productDAO.updateStock(pro_id,newStock);

    }
    public boolean stockOut(int pro_id, float outquantity) {
        if(outquantity <= 0) {
            return false;
        }
        Product product = productDAO.findById(pro_id);
        if(product == null){
            return false;
        }
        if(outquantity >= product.getQuantity()){
            return false;
        }
        double newStock = product.getQuantity() - outquantity;
        return productDAO.updateStock(pro_id,newStock);
    }
//    查询所有产品
    public List<Product> findAllproducts(){
        return productDAO.findAll();
    }
}
