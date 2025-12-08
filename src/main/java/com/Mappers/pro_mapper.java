package com.Mappers;

import com.Entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface pro_mapper {
    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values (#{p.pro_id},#{p.pro_name},#{p.price},#{p.type},#{p.quantity},#{p.sup_id})")
    public int add_product_entity(@Param("p") Product p);
    @Insert("insert into practice.products(prod_id, prod_name, price, `type`, quantity, sup_id) values(#{pro_id},#{pro_name},#{price},#{type},#{quantity},#{sup_id})")
    public int add_product_args(@Param("pro_id") int pro_id, @Param("pro_name") String pro_name, @Param("price") double price, @Param("type") String type,@Param("quantity") double quantity,@Param("sup_id") int sup_id);

    @Select("select * from practice.products")
    public List<Product> select_product_all();

    @Select("select * from practice.products where prod_name=#{pro_name}")
    public List<Product> select_product_name(String pro_name);

    @Select("select * from practice.products where prod_id=#{pro_id}")
    public List<Product> select_product_id(int pro_id);

    @Select("select * from practice.products where price>=#{begin} and price<=#{end}")
    public List<Product> select_product_price(@Param("begin") double pri_begin,@Param("end") double pri_end);

    @Select("select * from practice.products where sup_id=#{sup_id}")
    public List<Product> select_product_supid(int sup_id);

    @Select("select * from practice.products where `type`=#{type}")
    public List<Product> select_product_type(String type);

    @Select("select * from practice.products where quantity>= #{begin} and quantity <= #{end}")
    public List<Product> select_product_quantity(@Param("begin") double pri_begin, @Param("end") double pri_end);

    @Update("update practice.products set prod_id=#{p.prod_id}, prod_name=#{p.prod_name}, price=#{p.price}, `type`=#{p.type}, quantity=#{p.quantity}, sup_id=#{p.sup_id} where prod_name=#{p.prod_name}")
    public int update_product_id(@Param("p") Product p);

    @Update("update practice.products set prod_id=#{p.pro_id}, prod_name=#{p.pro_name}, price=#{p.price}, `type`=#{p.type}, quantity=#{p.quantity}, sup_id=#{p.sup_id} where prod_id=#{p.pro_id}")
    public int update_product_name(@Param("p") Product p);

    @Delete("delete from practice.products where prod_id=#{pro_id}")
    public int delete_product_id(int pro_id);

    @Delete("delete from practice.products where prod_name=#{pro_name}")
    public int delete_product_name(String pro_name);

    @Delete("delete from practice.products where sup_id=#{sup_id}")
    public int delete_product_supid(int sup_id);
}
