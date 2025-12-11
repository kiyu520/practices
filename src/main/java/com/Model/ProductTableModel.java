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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> newProducts) {
        this.products = newProducts;
        // 通知表格：数据已更新，需要刷新显示
        fireTableDataChanged();
    }

    public void addProduct(Product product) {
        products.add(product);
        // 通知表格：新增了一行数据，刷新对应行
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }

    @Override
    public int getRowCount() {
        return products.size();
    }
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (products.isEmpty()) {
            return null;
        }
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return product.getProd_id();
            case 1: return product.getProd_name();
            case 2: return product.getPrice();
            case 3: return product.getType();
            case 4: return product.getQuantity();
            case 5: return product.getSup_id();
            default: return null;
        }
    }
}
