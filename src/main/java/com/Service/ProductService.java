package com.Service;

import com.Entity.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
//添加产品，编号唯一
    public String addProduct(Product product) {
//        1.合法性校验
        if(product.getPro_id() <= 0 || product.getPro_name().isEmpty() || product.getPrice() < 0 || product.getQuantity() < 0){
            return "参数错误；编号/名称不能为空，价格/库存不能为负！";
        }
//        2.编号唯一性校验
        if(productDAO.findById(product.getPro_id()) != null){
            return "产品编号已存在！";
        }
//        3.调用DAO添加
        boolean success = productDAO.add(product);
        return success ? "添加成功！" : "添加失败！";
    }
//    删除产品，库存为0时
    public String deleteProduct(int pro_id) {
//        1.查询产品
        Product product = productDAO.findById(pro_id);
        if(product == null){
            return "产品不存在！";
        }
        if(product.getQuantity() != 0){
            return "库存不为0，无法删除！";
        }
//        3.调用DAO删除
        boolean success = productDAO.delete(pro_id);
        return success ? "删除成功！" : "删除失败！";
    }
//    商品进货，进货量>0
    public String stockIn(int pro_id, float outquantity) {
        if(outquantity <= 0){
            return "进货量必须大于0！";
        }
        Product product = productDAO.finfById(pro_id);
        if(product == null){
            return " 产品不存在！";
        }
        if(outquantity > product.getQuantity()){
            return "库存不足！当前库存：" + product.getQuantity();
        }
        double newStock = product.getQuantity() - outquantity;
        boolean success = productDAO.updateStock(pro_id,newStock);
        return success ? "出货成功！新库存：" + newStock : "出货失败！";
    }
//    查询所有产品
    public List<Product> findAllproducts(){
        return productDAO.findAll();
    }
}
