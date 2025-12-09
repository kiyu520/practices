// SupTablePanel.java
package com.Frame;

import com.Entity.Supplier;
import com.Model.SupTableModel;
import com.Service.SupplierService;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.List;

public class SupTablePanel extends JPanel {
    // 表格模型实例
    private static SupTableModel tableModel;
    // 表格组件
    private static JTable table;

    // 获取配置好的表格组件
    public static JTable get() {
        // 从服务层获取所有供应商数据
        List<Supplier> suppliers = SupplierService.findAllSupplier();
        // 初始化表格模型
        tableModel = new SupTableModel(suppliers);
        // 创建表格并应用模型
        table = new JTable(tableModel);
        return table;
    }

    // 提供获取表格模型的方法（便于外部操作数据）
    public static SupTableModel getTableModel() {
        return tableModel;
    }
}