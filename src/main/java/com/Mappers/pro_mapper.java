package com.Mappers;

import com.Entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface pro_mapper {
    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values (#{p.pro_id},#{p.pro_name},#{p.price},#{p.type},#{p.quantity},#{p.sup_id})")
    public int add_product_entity(@Param("p") Product p);
    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values(#{pro_id},#{pro_name},#{price},#{type},#{quantity},#{sup_id})")
    public int add_product_args(@Param("pro_id") int pro_id, @Param("pro_name") String pro_name, @Param("price") int price, @Param("type") String type,@Param("quantity") double quantity,@Param("sup_id") int sup_id);
}
