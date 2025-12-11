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
        return supList.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
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
        return headers[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Supplier supplier = supList.get(rowIndex);
        switch (columnIndex) {
            case 0 -> supplier.setExesConId(Integer.parseInt((String) aValue));
            case 1 -> supplier.setSupName((String) aValue);
            case 2 -> supplier.setSupRelationer((String) aValue);
            case 3 -> supplier.setSupTelephone((String) aValue);
            case 4 -> supplier.setSupAddress((String) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addSupplier(Supplier supplier) {
        supList.add(supplier);
        fireTableRowsInserted(supList.size() - 1, supList.size() - 1);
    }

    public void removeSupplier(int rowIndex) {
        supList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}