package com.Mappers;

import com.Entity.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface sup_mapper {
    // 新增（实体类）：插入时MyBatis通过Setter设值，字段名直接对应SQL的下划线字段
    @Insert("insert into practice.suppliers(exesConId, sup_name, sup_address, postcode, sup_telephone, sup_fax, sup_relationer, sup_email) " +
            "values(#{s.exesConId}, #{s.supName}, #{s.supAddress}, #{s.postcode}, #{s.supTelephone}, #{s.supFax}, #{s.supRelationer}, #{s.supEmail})")
    public int add_supplier_entity(@Param("s") Supplier s);

    // 新增（单个参数）：直接对应数据库下划线字段
    @Insert("insert into practice.suppliers(exesConId, sup_name, sup_address, postcode, sup_telephone, sup_fax, sup_relationer, sup_email) " +
            "values(#{exesConId}, #{supName}, #{supAddress}, #{postcode}, #{supTelephone}, #{supFax}, #{supRelationer}, #{supEmail})")
    public int add_supplier_args(@Param("exesConId") Integer exesConId,
                                 @Param("supName") String supName,
                                 @Param("supAddress") String supAddress,
                                 @Param("postcode") String postcode,
                                 @Param("supTelephone") String supTelephone,
                                 @Param("supFax") String supFax,
                                 @Param("supRelationer") String supRelationer,
                                 @Param("supEmail") String supEmail);

    // 字段映射模板：数据库下划线字段 → 实体类小驼峰属性
    @Results(id = "supplierMap", value = {
            @Result(column = "exesConId", property = "exesConId"),
            @Result(column = "sup_name", property = "supName"),
            @Result(column = "sup_address", property = "supAddress"),
            @Result(column = "postcode", property = "postcode"),
            @Result(column = "sup_telephone", property = "supTelephone"),
            @Result(column = "sup_fax", property = "supFax"),
            @Result(column = "sup_relationer", property = "supRelationer"),
            @Result(column = "sup_email", property = "supEmail")
    })
    @Select("select * from practice.suppliers")
    public List<Supplier> select_supplier_all();

    // 复用上面的supplierMap映射
    @ResultMap("supplierMap")
    @Select("select * from practice.suppliers where exesConId=#{exesConId}")
    public List<Supplier> select_supplier_id(Integer exesConId);

    @ResultMap("supplierMap")
    @Select("select * from practice.suppliers where sup_name=#{supName}")
    public List<Supplier> select_supplier_name(String supName);

    @ResultMap("supplierMap")
    @Select("select * from practice.suppliers where sup_address=#{supAddress}")
    public List<Supplier> select_supplier_address(String supAddress);

    @ResultMap("supplierMap")
    @Select("select * from practice.suppliers where sup_relationer=#{supRelationer}")
    public List<Supplier> select_supplier_relationer(String supRelationer);

    @ResultMap("supplierMap")
    @Select("select * from practice.suppliers where sup_email=#{supEmail}")
    public List<Supplier> select_supplier_email(String supEmail);

    // 修改（按ID更新）：SQL用数据库下划线字段，MyBatis通过实体类Getter取值
    @Update("update practice.suppliers set sup_name=#{s.supName}, sup_address=#{s.supAddress}, postcode=#{s.postcode}, " +
            "sup_telephone=#{s.supTelephone}, sup_fax=#{s.supFax}, sup_relationer=#{s.supRelationer}, sup_email=#{s.supEmail} " +
            "where exesConId=#{s.exesConId}")
    public int update_supplier_id(@Param("s") Supplier s);

    // 修改（按名称更新）
    @Update("update practice.suppliers set exesConId=#{s.exesConId}, sup_address=#{s.supAddress}, postcode=#{s.postcode}, " +
            "sup_telephone=#{s.supTelephone}, sup_fax=#{s.supFax}, sup_relationer=#{s.supRelationer}, sup_email=#{s.supEmail} " +
            "where sup_name=#{s.supName}")
    public int update_supplier_name(@Param("s") Supplier s);

    // 删除（按ID）
    @Delete("delete from practice.suppliers where exesConId=#{exesConId}")
    public int delete_supplier_id(Integer exesConId);

    // 删除（按名称）
    @Delete("delete from practice.suppliers where sup_name=#{supName}")
    public int delete_supplier_name(String supName);

    @Select("select exesConId from practice.suppliers")
    public List<Integer> select_exesConId();
}