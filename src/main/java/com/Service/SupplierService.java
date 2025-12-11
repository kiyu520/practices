package com.Service;

import com.Entity.Supplier;
import com.Mappers.sup_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;



public class SupplierService {
    private static sup_mapper supMapper;

    public SupplierService() {
        SqlSession sqlSession = SqlUtil.getSession();
        supMapper = sqlSession.getMapper(sup_mapper.class);
    }
    public boolean addSupplier(Supplier supplier) {
        if (supplier.getExesConId() == null || supplier.getSupName().isEmpty() || supplier.getExesConId() <= 0) {
            return false;
        }
        List<Supplier> existSupplier = supMapper.select_supplier_id(supplier.getExesConId());
        if(existSupplier != null && !existSupplier.isEmpty()) {
            return false;
        }
        int result = supMapper.add_supplier_entity(supplier);
        return result > 0 ? true : false;
    }
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

    public boolean deleteSupplier(Integer supplierId) {
        if (supplierId == null || supplierId <= 0) {
            return false;
        }
        int result = supMapper.delete_supplier_id(supplierId);
        return result > 0;
    }

    public Supplier getSupplierById(Integer supplierId) {
        if (supplierId == null || supplierId <= 0) {
            return null;
        }
        List<Supplier> suppliers = supMapper.select_supplier_id(supplierId);
        // 如果查询结果不为空且有数据，返回第一个元素，否则返回null
        return (suppliers != null && !suppliers.isEmpty()) ? suppliers.get(0) : null;
    }


//    查询所有供应商
    public static List<Supplier> findAllSupplier() {

        return supMapper.select_supplier_all();
    }

    public List<Supplier> querySuppliers(Integer o, String o1, String o4, String o6) {
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            sup_mapper mapper = sqlSession.getMapper(sup_mapper.class);

            if (o != null && (Integer) o > 0) {
                return mapper.select_supplier_id((Integer) o);
            }

            if (o1 != null && !((String) o1).isEmpty()) {
                return mapper.select_supplier_name((String) o1);
           }


            if (o4 != null && !((String) o4).isEmpty()) {
                return mapper.select_supplier_address((String) o4);
            }


            if (o6 != null && !((String) o6).isEmpty()) {
                return mapper.select_supplier_relationer((String) o6);
            }



            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
