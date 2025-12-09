package com.Model;

import com.Entity.Supplier;
import javax.swing.table.AbstractTableModel; // 改用 AbstractTableModel（与 ProTableModel 一致）
import java.util.List;

public class SupplierTableModel extends AbstractTableModel {
    // 表头：对应供应商字段（与查询面板字段一致）
    private final String[] headers = {"供应商ID", "供应商名称", "地址", "邮编", "电话", "传真", "联系人", "邮箱"};
    private List<Supplier> supplierList; // 存储供应商数据（对应 ProTableModel 的 Pro                              List）

    // 构造方法：传入初始供应商列表
    public SupplierTableModel(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    // 无参构造：适配默认初始化
    public SupplierTableModel() {
        this(null);
    }

    // ========== 重写 AbstractTableModel 核心方法（与 ProTableModel 结构一致） ==========
    @Override
    public int getRowCount() {
        // 行数 = 列表大小（空列表返回 0，避免空指针）
        return supplierList == null ? 0 : supplierList.size();
    }

    @Override
    public int getColumnCount() {
        // 列数 = 表头长度
        return headers.length;
    }

    @Override
    public String getColumnName(int column) {
        // 返回对应列的表头名称
        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // 行号 → 供应商实体 → 列号 → 对应属性（与表头顺序一致）
        Supplier supplier = supplierList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> supplier.getExesConId(); // 供应商ID（对应数据库 exes_con_id）
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
        // 允许编辑所有单元格（与 ProTableModel 一致）
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // 编辑表格时，同步修改 supplierList 中的实体（核心：与 ProTableModel 的 setValueAt 逻辑一致）
        Supplier supplier = supplierList.get(rowIndex);
        if (aValue == null || aValue.toString().trim().isEmpty()) {
            return; // 空值不处理
        }
        try {
            switch (columnIndex) {
                case 0: // 供应商ID（整数）
                    supplier.setExesConId(Integer.parseInt(aValue.toString().trim()));
                    break;
                case 1: // 名称（字符串）
                    supplier.setSupName(aValue.toString().trim());
                    break;
                case 2: // 地址（字符串）
                    supplier.setSupAddress(aValue.toString().trim());
                    break;
                case 3: // 邮编（字符串）
                    supplier.setPostcode(aValue.toString().trim());
                    break;
                case 4: // 电话（字符串）
                    supplier.setSupTelephone(aValue.toString().trim());
                    break;
                case 5: // 传真（字符串）
                    supplier.setSupFax(aValue.toString().trim());
                    break;
                case 6: // 联系人（字符串）
                    supplier.setSupRelationer(aValue.toString().trim());
                    break;
                case 7: // 邮箱（字符串）
                    supplier.setSupEmail(aValue.toString().trim());
                    break;
            }
            // 通知表格：该单元格已更新，触发界面刷新（必须调用）
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (NumberFormatException e) {
            // 供应商ID格式错误提示（与 ProTableModel 的数字格式处理逻辑一致）
            throw new NumberFormatException("供应商ID必须为整数！");
        }
    }

    // ========== 自定义方法（参考 ProTableModel 的 addStudent/removeStudent） ==========
    /**
     * 设置/替换所有供应商数据（对应你要的 setSuppliers 方法）
     * 功能：直接覆盖原有列表，刷新整个表格
     */
    public void setSuppliers(List<Supplier> newSupplierList) {
        this.supplierList = newSupplierList;
        // 通知表格：所有数据已变更，刷新整个表格（与 ProTableModel 的 fire 方法风格一致）
        fireTableDataChanged();
    }

    /**
     * 新增单个供应商（参考 ProTableModel 的 addStudent）
     */
    public void addSupplier(Supplier supplier) {
        if (supplierList == null) {
            return;
        }
        supplierList.add(supplier);
        // 通知表格：新增行（指定行号，避免全表刷新，性能更好）
        fireTableRowsInserted(supplierList.size() - 1, supplierList.size() - 1);
    }

    /**
     * 删除指定行的供应商（参考 ProTableModel 的 removeStudent）
     */
    public void removeSupplier(int rowIndex) {
        if (supplierList == null || rowIndex < 0 || rowIndex >= supplierList.size()) {
            return;
        }
        supplierList.remove(rowIndex);
        // 通知表格：删除指定行
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * 获取当前所有供应商数据（供导出/导入使用）
     */
    public List<Supplier> getSuppliers() {
        return supplierList;
    }
}