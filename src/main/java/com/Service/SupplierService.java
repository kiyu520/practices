package com.Service;

import com.Entity.Supplier;
import com.Mappers.sup_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;


import static com.Tools.SqlUtil.sqlSessionFactory;

public class SupplierService {
    private sup_mapper supMapper;

    public SupplierService() {
        SqlSession sqlSession = SqlUtil.getSession();
        this.supMapper = sqlSession.getMapper(sup_mapper.class);
    }
    //    添加供应商，编号唯一
    public String addSupplier(Supplier supplier) {
        if (supplier.getExesConId() == null || supplier.getSupName().isEmpty() || supplier.getExesConId() <= 0) {
            return "供应商ID/名称不能为空！";
        }
//        实际可添加编号唯一性校验
        List<Supplier> existSupplier = supMapper.select_supplier_id(supplier.getExesConId());
        if(existSupplier != null && existSupplier.isEmpty()) {
            return "供应商编号已存在！";
        }
        //    调用mapper新增
        int result = supMapper.add_supplier_entity(supplier);
        return result > 0 ? "success" : "供应商添加失败！";
    }
    /**
     * 根据ID查询供应商（适配Frame层修改/删除时的存在性校验）
     * @param supplierId 供应商ID
     * @return 供应商列表（无数据返回空列表）
     */
    public List<Supplier> findSupplierById(Integer supplierId) {
        if (supplierId == null || supplierId <= 0) {
            return null;
        }
        return supMapper.select_supplier_id(supplierId);
    }
    /**
     * 修改供应商信息
     * @param supplier 供应商实体（需包含ID和要修改的字段）
     * @return 修改成功返回true，失败返回false
     */
    public boolean updateSupplier(Supplier supplier) {
        // 1. 参数校验
        if (supplier.getExesConId() == null || supplier.getExesConId() <= 0) {
            return false;
        }
        // 2. 调用Mapper执行修改
        int result = supMapper.update_supplier_id(supplier);
        // 3. 受影响行数>0则修改成功
        return result > 0;
    }
    /**
     * 删除指定ID的供应商
     * @param supplierId 供应商ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteSupplier(Integer supplierId) {
        // 1. 参数校验
        if (supplierId == null || supplierId <= 0) {
            return false;
        }
        // 2. 调用Mapper执行删除
        int result = supMapper.delete_supplier_id(supplierId);
        return result > 0;
    }


//    查询所有供应商
    public List<Supplier> findAllSupplier() {
        return supMapper.select_supplier_all();
    }
}
