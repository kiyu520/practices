package com.Model;

import com.Entity.Supplier;
import javax.swing.table.AbstractTableModel; // 改用 AbstractTableModel（与 ProTableModel 一致）
import java.util.List;

public class SupplierTableModel extends AbstractTableModel {
    private final String[] headers = {"供应商ID", "供应商名称", "地址", "邮编", "电话", "传真", "联系人", "邮箱"};
    private List<Supplier> supplierList;

    public SupplierTableModel(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public SupplierTableModel() {
        this(null);
    }

    @Override
    public int getRowCount() {
        return supplierList == null ? 0 : supplierList.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = supplierList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> supplier.getExesConId(); // 供应商ID
            case 1 -> supplier.getSupName();   // 供应商名称
            case 2 -> supplier.getSupAddress();   // 地址
            case 3 -> supplier.getPostcode();   // 邮编
            case 4 -> supplier.getSupTelephone();     // 电话
            case 5 -> supplier.getSupFax();       // 传真
            case 6 -> supplier.getSupRelationer();   // 联系人
            case 7 -> supplier.getSupEmail();     // 邮箱
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // 获取指定行索引的供应商对象
        Supplier supplier = supplierList.get(rowIndex);
        // 检查输入值是否为空或仅包含空白字符
        if (aValue == null || aValue.toString().trim().isEmpty()) {
            return;
        }
        try {
            // 根据列索引设置供应商的不同属性
            switch (columnIndex) {
                case 0: // 供应商ID（整数）
                    // 设置供应商ID，将输入值转换为整数
                    supplier.setExesConId(Integer.parseInt(aValue.toString().trim()));
                    break;
                case 1: // 名称（字符串）
                    // 设置供应商名称
                    supplier.setSupName(aValue.toString().trim());
                    break;
                case 2: // 地址（字符串）
                    // 设置供应商地址
                    supplier.setSupAddress(aValue.toString().trim());
                    break;
                case 3: // 邮编（字符串）
                    // 设置供应商邮编
                    supplier.setPostcode(aValue.toString().trim());
                    break;
                case 4: // 电话（字符串）
                    // 设置供应商电话
                    supplier.setSupTelephone(aValue.toString().trim());
                    break;
                case 5: // 传真（字符串）
                    // 设置供应商传真
                    supplier.setSupFax(aValue.toString().trim());
                    break;
                case 6: // 联系人（字符串）
                    // 设置供应商联系人
                    supplier.setSupRelationer(aValue.toString().trim());
                    break;
                case 7: // 邮箱（字符串）
                    // 设置供应商邮箱
                    supplier.setSupEmail(aValue.toString().trim());
                    break;
            }
            // 通知表格单元格已更新
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (NumberFormatException e) {
            // 处理数字格式异常，提示用户供应商ID必须为整数
            throw new NumberFormatException("供应商ID必须为整数！");
        }
    }

    public void setSuppliers(List<Supplier> newSupplierList) {
        this.supplierList = newSupplierList;
        fireTableDataChanged();
    }

    public void addSupplier(Supplier supplier) {
        if (supplierList == null) {
            return;
        }
        supplierList.add(supplier);
        fireTableRowsInserted(supplierList.size() - 1, supplierList.size() - 1);
    }

    public void removeSupplier(int rowIndex) {
        if (supplierList == null || rowIndex < 0 || rowIndex >= supplierList.size()) {
            return;
        }
        supplierList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public List<Supplier> getSuppliers() {
        return supplierList;
    }
}