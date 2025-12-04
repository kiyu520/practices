package com.Model;

import com.Entity.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProTableModel extends AbstractTableModel {
    private final String[] headers = {"ID","名称","类型","价格","库存","供应商ID"};
    private List<Product> ProList; // 直接存储 List 数据
    public ProTableModel(List<Product> ProList) {
        this.ProList = ProList;
    }
    @Override
    public int getRowCount() {
        return ProList.size(); // 行数 = List 大小
    }

    @Override
    public int getColumnCount() {
        return headers.length; // 列数 = 表头长度
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // 根据行号和列号，返回对应实体类的属性
        Product pro = ProList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pro.getProd_id();
            case 1 -> pro.getProd_name();
            case 2 -> pro.getType();
            case 3 -> pro.getPrice();
            case 4 -> pro.getQuantity();
            case 5 -> pro.getSup_id();
            default -> null;
        };
    }
    @Override
    public String getColumnName(int column) {
        return headers[column];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true; // 允许编辑所有单元格
    }

    // 可选：编辑表格时，同步修改 List 中的实体类
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Product pro = ProList.get(rowIndex);
        switch (columnIndex) {
            case 0: pro.setProd_id(Integer.parseInt((String) aValue) ); break;
            case 1: pro.setProd_name((String) aValue); break;
            case 2: pro.setType((String) aValue); break;
            case 3: pro.setPrice(Double.parseDouble((String) aValue)); break;
            case 4: pro.setQuantity(Integer.parseInt((String) aValue)); break;
            case 5: pro.setSup_id(Integer.parseInt((String) aValue)); break;
        }
        fireTableCellUpdated(rowIndex, columnIndex); // 通知表格刷新单元格
    }

    // 自定义方法：新增数据（修改 List 后通知表格）
    public void addStudent(Product pro) {
        ProList.add(pro);
        fireTableRowsInserted(ProList.size() - 1, ProList.size() - 1); // 通知表格新增行
    }

    // 自定义方法：删除数据
    public void removeStudent(int rowIndex) {
        ProList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex); // 通知表格删除行
    }
}
