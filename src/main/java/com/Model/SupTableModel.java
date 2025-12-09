// SupTableModel.java
package com.Model;

import com.Entity.Supplier;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SupTableModel extends AbstractTableModel {
    // 表格表头
    private final String[] headers = {"供应商ID", "供应商名称", "联系人", "联系电话", "地址"};
    private List<Supplier> supList; // 存储供应商数据的列表

    // 构造方法，初始化供应商数据
    public SupTableModel(List<Supplier> supList) {
        this.supList = supList;
    }

    @Override
    public int getRowCount() {
        return supList.size(); // 行数等于数据量
    }

    @Override
    public int getColumnCount() {
        return headers.length; // 列数等于表头长度
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = supList.get(rowIndex);
        // 根据列索引返回对应属性值（假设Supplier类有这些getter方法）
        return switch (columnIndex) {
            case 0 -> supplier.getExesConId();
            case 1 -> supplier.getSupName();
            case 2 -> supplier.getSupRelationer();
            case 3 -> supplier.getSupTelephone();
            case 4 -> supplier.getSupAddress();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return headers[column]; // 返回表头名称
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true; // 允许编辑所有单元格
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Supplier supplier = supList.get(rowIndex);
        // 根据列索引设置对应属性值（假设Supplier类有这些setter方法）
        switch (columnIndex) {
            case 0 -> supplier.setExesConId(Integer.parseInt((String) aValue));
            case 1 -> supplier.setSupName((String) aValue);
            case 2 -> supplier.setSupRelationer((String) aValue);
            case 3 -> supplier.setSupTelephone((String) aValue);
            case 4 -> supplier.setSupAddress((String) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex); // 通知表格刷新
    }

    // 新增供应商数据
    public void addSupplier(Supplier supplier) {
        supList.add(supplier);
        fireTableRowsInserted(supList.size() - 1, supList.size() - 1);
    }

    // 删除供应商数据
    public void removeSupplier(int rowIndex) {
        supList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}