package com.Service;

import com.Entity.Supplier;

import java.util.List;

public class SupplierService {
    public String supplierDAO = new SupplierDAO();
//    添加供应商，编号唯一
    public String addSupplier(Supplier supplier) {
        if (supplier.getExesConId() <= 0 || supplier.getSupName().isEmpty()) {
            return "供应商ID/名称不能为空！";
        }
//        实际可添加编号唯一性校验
        boolean success = supplierDAO.add(supplier);
        return success ? "添加成功！" : "添加失败！";
    }
//    查询所有供应商
    public List<Supplier> findAllSuppliers() {
        return supplierDAO.findAll();
    }
}
