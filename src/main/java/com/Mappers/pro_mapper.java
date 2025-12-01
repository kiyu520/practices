package com.Mappers;

import com.Entity.products;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;

public interface pro_mapper {
//    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values (#{p.pro_id},#{p.pro_name),#{d},#{d},#{d},#{d})")
//    public int add_product(@Param("p") products p);
    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values(#{pro_id},#{pro_name},#{price},#{type},#{quantity},#{sup_id})")
    public int add_product(@Param("pro_id") int pro_id, @Param("pro_name") String pro_name, @Param("price") int price, @Param("type") String type,@Param("quantity") double quantity,@Param("sup_id") int sup_id);
}
