package com.Model;

import com.Entity.Product;
import com.Service.ProductService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private List<Product> products = new ArrayList<>();

    private final String[] COLUMN_NAMES = {
            "产品编号", "产品名称", "价格", "类别", "库存量", "供应商编号"
    };

    public ProductTableModel() {
        this.products = new ArrayList<>();
    }

    public ProductTableModel(List<Product> productList) {
    }

    // ========== 对外提供的核心方法（适配ProductView的调用） ==========
    /**
     * 获取所有产品数据（ProductView的doQuery()会调用）
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * 设置表格数据
     */
    public void setProducts(List<Product> newProducts) {
        this.products = newProducts;
        // 通知表格：数据已更新，需要刷新显示
        fireTableDataChanged();
    }

    /**
     * 添加单个产品
     */
    public void addProduct(Product product) {
        products.add(product);
        // 通知表格：新增了一行数据，刷新对应行
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }

    /**
     * 获取表格行数
     */
    @Override
    public int getRowCount() {
        return products.size();
    }

    /**
     * 获取表格列数
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * 获取列名
     */
    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    /**
     * 获取指定行/列的单元格数据
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (products.isEmpty()) {
            return null;
        }
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return product.getProd_id();      // 产品编号（对应数据库prod_id）
            case 1: return product.getProd_name();    // 产品名称（对应数据库prod_name）
            case 2: return product.getPrice();       // 价格（double类型，适配数据库）
            case 3: return product.getType();        // 类别（对应数据库type）
            case 4: return product.getQuantity();    // 库存量（double类型，适配数据库）
            case 5: return product.getSup_id();      // 供应商编号（对应数据库sup_id）
            default: return null;
        }
    }
}
