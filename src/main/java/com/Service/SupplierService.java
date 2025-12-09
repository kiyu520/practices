package com.Service;

import com.Entity.Supplier;
import com.Mappers.sup_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;


import static com.Tools.SqlUtil.sqlSessionFactory;

public class SupplierService {
    private static sup_mapper supMapper;

    static{
        SqlSession sqlSession = SqlUtil.getSession();
        supMapper = sqlSession.getMapper(sup_mapper.class);
    }
    public SupplierService() {
        SqlSession sqlSession = SqlUtil.getSession();
        supMapper = sqlSession.getMapper(sup_mapper.class);
    }
    //    添加供应商，编号唯一
    public String addSupplier(Supplier supplier) {
        if (supplier.getExesConId() == null || supplier.getSupName().isEmpty() || supplier.getExesConId() <= 0) {
            return "供应商ID/名称不能为空！";
        }
//        实际可添加编号唯一性校验
        List<Supplier> existSupplier = supMapper.select_supplier_id(supplier.getExesConId());
        if(existSupplier != null && !existSupplier.isEmpty()) {
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
    /**
     * 根据ID查询单个供应商
     * @param supplierId 供应商ID
     * @return 供应商实体（无数据返回null）
     */
    public Supplier getSupplierById(Integer supplierId) {
        if (supplierId == null || supplierId <= 0) {
            return null;
        }
        List<Supplier> suppliers = supMapper.select_supplier_id(supplierId);
        // 如果查询结果不为空且有数据，返回第一个元素，否则返回null
        return (suppliers != null && !suppliers.isEmpty()) ? suppliers.get(0) : null;
    }

//    /**
//     * 校验供应商是否关联产品
//     * @param supplierId 供应商ID
//     * @return 关联返回true，否则返回false
//     */
//    public boolean isSupplierRelatedProduct(Integer supplierId) {
//        if (supplierId == null || supplierId <= 0) {
//            return false;
//        }
//        // 假设存在产品相关的mapper方法，实际需根据项目情况实现
//        // 这里需要你根据实际的产品表关联关系补充查询逻辑
//        int relatedCount = supMapper.countRelatedProducts(supplierId);
//        return relatedCount > 0;
//    }

//    查询所有供应商
    public static List<Supplier> findAllSupplier() {
        return supMapper.select_supplier_all();
    }

    public static List<Integer> findAllSupplierId() {
        return supMapper.select_exesConId();
    }

    public List<Supplier> querySuppliers(Object o, Object o1, Object o2, Object o3,
                                         Object o4, Object o5, Object o6, Object o7) {
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            sup_mapper mapper = sqlSession.getMapper(sup_mapper.class);

            // 1. 条件1：按供应商ID查询（Integer类型 + 大于0）
            if (o instanceof Integer && (Integer) o > 0) {
                return mapper.select_supplier_id((Integer) o);
            }

            // 2. 条件2：按供应商名称查询（String类型 + 非空）
            if (o1 instanceof String && !((String) o1).isEmpty()) {
                // 模糊查询（若需精确匹配，直接用 = #{name}，此处按常用场景用LIKE）
                return mapper.select_supplier_name("%" + ((String) o1) + "%");
            }

            // 3. 条件3：按地址查询（String类型 + 非空）
            if (o2 instanceof String && !((String) o2).isEmpty()) {
                return mapper.select_supplier_address("%" + ((String) o2) + "%");
            }

            // 4. 条件4：按邮编查询（String类型 + 非空）
            if (o3 instanceof String && !((String) o3).isEmpty()) {
                return mapper.select_supplier_postcode((String) o3); // 邮编一般精确匹配
            }

            // 5. 条件5：按电话查询（String类型 + 非空）
            if (o4 instanceof String && !((String) o4).isEmpty()) {
                return mapper.select_supplier_Telephone((String) o4); // 电话精确匹配
            }

            // 6. 条件6：按联系人查询（String类型 + 非空）
            if (o5 instanceof String && !((String) o5).isEmpty()) {
                return mapper.select_supplier_relationer("%" + ((String) o5) + "%"); // 模糊查询
            }

            // 7. 条件7：按邮箱查询（String类型 + 非空）
            if (o6 instanceof String && !((String) o6).isEmpty()) {
                return mapper.select_supplier_email((String) o6); // 邮箱精确匹配
            }

            // 8. 条件8：按传真查询（String类型 + 非空）
            if (o7 instanceof String && !((String) o7).isEmpty()) {
                return mapper.select_supplier_Fex((String) o7); // 传真精确匹配
            }

            // 无有效查询条件：返回空列表（避免null，与 queryProducts 逻辑一致）
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            // 异常时也返回空列表（避免Frame层空指针）
            return Collections.emptyList();
        }
    }
}
