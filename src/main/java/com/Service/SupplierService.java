package com.Service;

import com.Entity.Supplier;
import com.Mappers.sup_mapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;


import static com.Tools.SqlUtil.sqlSessionFactory;

public class SupplierService {
    private sup_mapper supMapper;

    public SupplierService() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        this.supMapper = sqlSession.getMapper(sup_mapper.class);
    }
    //    添加供应商，编号唯一
    public String addSupplier(Supplier supplier) {
        if (supplier.getExesConId() == null || supplier.getSupName().isEmpty() || supplier.getExesConId() <= 0) {
            return "供应商ID/名称不能为空！";
        }
//        实际可添加编号唯一性校验
        List<Supplier> existSupplier = supMapper.select_supplier_id(supplier.getExesConId());
        if(existSupplier != null && existSupplier.isEmpty()) {}
        return "供应商编号已存在！";
    }

    private Supplier supplier;
    //    调用mapper新增
    int result = supMapper.add_supplier_entity(supplier);
//    查询所有供应商
    public List<Supplier> findAllSupplier() {
        return supMapper.select_supplier_all();
    }
}
