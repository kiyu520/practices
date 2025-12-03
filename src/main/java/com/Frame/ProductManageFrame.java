package com.Frame;

import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductManageFrame extends JFrame {
    // 表单组件
    private JTextField prodIdField, prodNameField, priceField, typeField, quantityField;
    private JComboBox<Integer> supIdCombo; // 供应商下拉选择框
    // 表格组件（显示产品列表）
    private JTable productTable;
    private DefaultTableModel tableModel;
    // 业务层对象
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    public ProductManageFrame() {
        // 窗口基础配置
        setTitle("商品信息管理");
        setSize(800, 600);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 关闭窗口不退出程序
        setLayout(null);

        // 1. 表单区域（添加产品）
        // 产品ID
        JLabel prodIdLabel = new JLabel("产品ID:");
        prodIdLabel.setBounds(30, 30, 60, 25);
        prodIdField = new JTextField();
        prodIdField.setBounds(100, 30, 120, 25);
        // 产品名称
        JLabel prodNameLabel = new JLabel("名称:");
        prodNameLabel.setBounds(250, 30, 60, 25);
        prodNameField = new JTextField();
        prodNameField.setBounds(310, 30, 120, 25);
        // 价格
        JLabel priceLabel = new JLabel("价格:");
        priceLabel.setBounds(460, 30, 60, 25);
        priceField = new JTextField();
        priceField.setBounds(520, 30, 120, 25);
        // 种类
        JLabel typeLabel = new JLabel("种类:");
        typeLabel.setBounds(30, 80, 60, 25);
        typeField = new JTextField();
        typeField.setBounds(100, 80, 120, 25);
        // 库存
        JLabel quantityLabel = new JLabel("库存:");
        quantityLabel.setBounds(250, 80, 60, 25);
        quantityField = new JTextField();
        quantityField.setBounds(310, 80, 120, 25);
        // 供应商
        JLabel supIdLabel = new JLabel("供应商:");
        supIdLabel.setBounds(460, 80, 60, 25);
        supIdCombo = new JComboBox<>();
        supIdCombo.setBounds(520, 80, 120, 25);
        loadSupplierIds(); // 加载所有供应商ID到下拉框

        // 2. 功能按钮
        JButton addBtn = new JButton("添加产品");
        addBtn.setBounds(100, 130, 100, 30);
        JButton deleteBtn = new JButton("删除产品");
        deleteBtn.setBounds(220, 130, 100, 30);
        JButton stockInBtn = new JButton("产品进货");
        stockInBtn.setBounds(340, 130, 100, 30);
        JButton stockOutBtn = new JButton("产品出货");
        stockOutBtn.setBounds(460, 130, 100, 30);
        JButton resetBtn = new JButton("重置");
        resetBtn.setBounds(580, 130, 80, 30);

        // 3. 产品列表表格
        String[] columnNames = {"产品ID", "名称", "价格", "种类", "库存", "供应商ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(30, 180, 720, 350);

        // 添加所有组件到窗口
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
        add(stockInBtn);
        add(stockOutBtn);
        add(resetBtn);
        add(scrollPane);

        // 加载产品列表到表格
        loadProductTable();

        // 4. 按钮事件绑定
        // 添加产品（需求1.a）
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 解析表单数据
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    String prodName = prodNameField.getText().trim();
                    float price = Float.parseFloat(priceField.getText().trim());
                    String type = typeField.getText().trim();
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    int supId = (Integer) supIdCombo.getSelectedItem();

                    // 合法性判断：非空、数据类型校验
                    if (prodName.isEmpty() || type.isEmpty()) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "名称和种类不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (prodName.length() > 50 || type.length() > 50) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "名称和种类长度不能超过50字符！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (price <= 0 || quantity < 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "价格需为正数，库存不能为负数！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 创建产品对象
                    Product product = new Product(prodId, prodName, price, type, quantity, supId);
                    boolean success = productService.addProduct(product);
                    if (success) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadProductTable(); // 刷新表格
                        resetFields(); // 重置表单
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "添加失败！产品已存在或供应商不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "ID、价格、库存请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 删除产品（需求1.b）
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "请选择要删除的产品！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // 获取选中产品的ID
                int prodId = (int) tableModel.getValueAt(selectedRow, 0);
                float stock = (float) tableModel.getValueAt(selectedRow, 4);
                // 异常处理：库存不为0不能删除
                if (stock != 0) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "库存不为0，无法删除！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 确认删除
                int confirm = JOptionPane.showConfirmDialog(ProductManageFrame.this, "确定删除该产品？", "确认", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = productService.deleteProduct(prodId);
                    if (success) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadProductTable();
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "删除失败！产品不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 产品进货（需求1.c）
        stockInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int prodId = Integer.parseInt(JOptionPane.showInputDialog(ProductManageFrame.this, "请输入产品ID："));
                    // 校验产品是否存在
                    Product product = productService.getProductById(prodId);
                    if (product == null) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "产品不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    float quantity = Float.parseFloat(JOptionPane.showInputDialog(ProductManageFrame.this, "当前库存：" + product.getQuantity() + "\n请输入进货量："));
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "进货量需为正数！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "进货成功！当前库存：" + (product.getQuantity() + quantity), "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadProductTable();
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "进货失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 产品出货（需求1.d）
        stockOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int prodId = Integer.parseInt(JOptionPane.showInputDialog(ProductManageFrame.this, "请输入产品ID："));
                    // 校验产品是否存在
                    Product product = productService.getProductById(prodId);
                    if (product == null) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "产品不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double currentStock = product.getQuantity();
                    float quantity = Float.parseFloat(JOptionPane.showInputDialog(ProductManageFrame.this, "当前库存：" + currentStock + "\n请输入出货量："));
                    // 合法性校验
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "出货量需为正数！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // 异常处理：库存不足
                    if (currentStock < quantity) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "库存不足！当前库存：" + currentStock, "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean success = productService.stockOut(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "出货成功！当前库存：" + (currentStock - quantity), "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadProductTable();
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "出货失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 重置表单
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }

    // 加载所有供应商ID到下拉框
    private void loadSupplierIds() {
        List<Supplier> suppliers = supplierService.findAllSupplier();
        for (Supplier supplier : suppliers) {
            supIdCombo.addItem(supplier.getExesConId());
        }
    }

    // 加载产品列表到表格
    private void loadProductTable() {
        tableModel.setRowCount(0); // 清空表格
        // 查询所有产品
        List<Product> products = productService.queryProducts(null, null, null, null, null, null);
        for (Product product : products) {
            Object[] rowData = {
                    product.getProd_id(),
                    product.getProd_name(),
                    String.format("%.2f", product.getPrice()), // 价格保留2位小数
                    product.getType(),
                    product.getQuantity(),
                    product.getSup_id()
            };
            tableModel.addRow(rowData);
        }
    }

    // 重置表单字段
    private void resetFields() {
        prodIdField.setText("");
        prodNameField.setText("");
        priceField.setText("");
        typeField.setText("");
        quantityField.setText("");
        supIdCombo.setSelectedIndex(0);
    }
}