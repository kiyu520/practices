package com.Frame;

import com.Entity.Supplier;
import com.Service.SupplierService;
import javax.swing.*;

public class SupListPanel extends JPanel {
    // 静态列表模型，用于存储和管理供应商数据
    static DefaultListModel<Supplier> listModel = new DefaultListModel<>();

    // 提供获取JList组件的静态方法
    public static JList<Supplier> get() {
        // 从服务层获取所有供应商并添加到列表模型
        SupplierService.findAllSupplier().forEach(listModel::addElement);
        // 返回配置好的JList组件
        return new JList<>(listModel);
    }
}