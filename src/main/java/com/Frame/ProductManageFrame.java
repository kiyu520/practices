package com.Frame;

import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductManageFrame extends JFrame {
    private JTextField prodIdField, prodNameField, priceField, typeField, quantityField;
    private JComboBox<Integer> supIdCombo;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    public ProductManageFrame() {
        setTitle("商品信息管理");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // 表单区域（添加/修改产品）
        JLabel prodIdLabel = new JLabel("产品ID:");
        prodIdLabel.setBounds(30, 30, 60, 25);
        prodIdField = new JTextField();
        prodIdField.setBounds(100, 30, 120, 25);

        JLabel prodNameLabel = new JLabel("名称:");
        prodNameLabel.setBounds(250, 30, 60, 25);
        prodNameField = new JTextField();
        prodNameField.setBounds(310, 30, 120, 25);

        JLabel priceLabel = new JLabel("价格:");
        priceLabel.setBounds(460, 30, 60, 25);
        priceField = new JTextField();
        priceField.setBounds(520, 30, 120, 25);

        JLabel typeLabel = new JLabel("种类:");
        typeLabel.setBounds(30, 80, 60, 25);
        typeField = new JTextField();
        typeField.setBounds(100, 80, 120, 25);

        JLabel quantityLabel = new JLabel("库存:");
        quantityLabel.setBounds(250, 80, 60, 25);
        quantityField = new JTextField();
        quantityField.setBounds(310, 80, 120, 25);

        JLabel supIdLabel = new JLabel("供应商:");
        supIdLabel.setBounds(460, 80, 60, 25);
        supIdCombo = new JComboBox<>();
        supIdCombo.setBounds(520, 80, 120, 25);
        // 加载供应商ID到下拉框
        loadSupplierIds();

        // 按钮区域
        JButton addBtn = new JButton("添加");
        addBtn.setBounds(100, 130, 80, 30);
        JButton deleteBtn = new JButton("删除");
        deleteBtn.setBounds(200, 130, 80, 30);
        JButton resetBtn = new JButton("重置");
        resetBtn.setBounds(300, 130, 80, 30);

        // 表格区域（显示产品列表）
        String[] columnNames = {"产品ID", "名称", "价格", "种类", "库存", "供应商ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(30, 180, 720, 350);

        // 添加组件
        add(prodIdLabel);
        add(prodIdField);
        add(prodNameLabel);
        add(prodNameField);
        add(priceLabel);
        add(priceField);
        add(typeLabel);
        add(typeField);
        add(quantityLabel);
        add(quantityField);
        add(supIdLabel);
        add(supIdCombo);
        add(addBtn);
        add(deleteBtn);
        add(resetBtn);
        add(scrollPane);

        // 加载产品列表
        loadProductTable();

        // 按钮事件
        addBtn.addActionListener(e -> addProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        resetBtn.addActionListener(e -> resetFields());
    }

    // 加载供应商ID到下拉框
    private void loadSupplierIds() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            supIdCombo.addItem(supplier.getExesConId());
        }
    }

    // 加载产品列表到表格
    private void loadProductTable() {
        // 清空表格
        tableModel.setRowCount(0);
        // 查询所有产品（无查询条件）
        List<Product> products = productService.queryProducts(null, null, null, null, null, null);
        for (Product product : products) {
            Object[] rowData = {
                    product.getPro_id(),
                    product.getPro_name(),
                    product.getPrice(),
                    product.getType(),
                    product.getQuantity(),
                    product.getSup_id()
            };
            tableModel.addRow(rowData);
        }
    }

    // 添加产品
    private void addProduct() {
        try {
            int prodId = Integer.parseInt(prodIdField.getText().trim());
            String prodName = prodNameField.getText().trim();
            float price = Float.parseFloat(priceField.getText().trim());
            String type = typeField.getText().trim();
            float quantity = Float.parseFloat(quantityField.getText().trim());
            int supId = (Integer) supIdCombo.getSelectedItem();

            Product product = new Product(prodId, prodName, price, type, quantity, supId);
            boolean success = productService.addProduct(product);
            if (success) {
                JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadProductTable(); // 刷新表格
                resetFields();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败（数据不合法或产品已存在）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入合法的数字！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 删除产品
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的产品！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int prodId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该产品吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = productService.deleteProduct(prodId);
            if (success) {
                JOptionPane.showMessageDialog(this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadProductTable(); // 刷新表格
            } else {
                JOptionPane.showMessageDialog(this, "删除失败（产品不存在或库存不为0）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 重置表单
    private void resetFields() {
        prodIdField.setText("");
        prodNameField.setText("");
        priceField.setText("");
        typeField.setText("");
        quantityField.setText("");
        supIdCombo.setSelectedIndex(0);
    }
}