package com.Model;

import com.Entity.Product;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// 自定义表格模型：关联Product实体和JTable
public class ProductTableModel extends AbstractTableModel {
    // 1. 核心数据源：存储表格要显示的所有产品数据
    private List<Product> products = new ArrayList<>();

    // 2. 表格列名（和你的查询页面列对应）
    private final String[] COLUMN_NAMES = {
            "产品编号", "产品名称", "价格", "类别", "库存量", "供应商编号"
    };

    // 3. 空构造方法（初始化空数据）
    public ProductTableModel() {
        this.products = new ArrayList<>();
    }

    // ========== 对外提供的核心方法（适配ProductView的调用） ==========
    /**
     * 获取所有产品数据（ProductView的doQuery()会调用）
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * 设置表格数据（ProductView的doQuery()过滤后会调用）
     */
    public void setProducts(List<Product> newProducts) {
        this.products = newProducts;
        // 通知表格：数据已更新，需要刷新显示
        fireTableDataChanged();
    }

    /**
     * 添加单个产品（ProductView的importData()导入时会调用）
     */
    public void addProduct(Product product) {
        products.add(product);
        // 通知表格：新增了一行数据，刷新对应行
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }

    /**
     * 加载初始数据（ProductView的resetQuery()会调用）
     * 注：暂为空实现，后续可对接MyBatis查询数据库
     */
    public void loadProducts() {
        // 如需从数据库加载，替换为：
        // products = proMapper.select_product_all();
        this.products = new ArrayList<>(); // 初始化空列表，避免空指针
    }

    // ========== 必须重写的抽象方法（Swing表格核心） ==========
    /**
     * 获取表格行数（数据源有多少产品，表格就有多少行）
     */
    @Override
    public int getRowCount() {
        return products.size();
    }

    /**
     * 获取表格列数（和COLUMN_NAMES长度一致）
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * 获取列名（表格顶部的表头文字）
     */
    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    /**
     * 获取指定行/列的单元格数据（表格显示的核心逻辑）
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // 空数据时返回null，避免报错
        if (products.isEmpty()) {
            return null;
        }
        // 获取当前行对应的Product对象
        Product product = products.get(rowIndex);
        // 根据列索引返回对应字段（适配你的Product实体和数据库字段）
        switch (columnIndex) {
            case 0: return product.getPro_id();      // 产品编号（对应数据库prod_id）
            case 1: return product.getPro_name();    // 产品名称（对应数据库prod_name）
            case 2: return product.getPrice();       // 价格（double类型，适配数据库）
            case 3: return product.getType();        // 类别（对应数据库type）
            case 4: return product.getQuantity();    // 库存量（double类型，适配数据库）
            case 5: return product.getSup_id();      // 供应商编号（对应数据库sup_id）
            default: return null;
        }
    }
}
