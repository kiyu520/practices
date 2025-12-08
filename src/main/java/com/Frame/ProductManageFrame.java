package com.Frame;

import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductManageFrame extends JFrame {
    // 表单组件
    private JTextField prodIdField, prodNameField, priceField, typeField, quantityField;
    // 新增：删除产品专用的ID输入框
    private JTextField deleteProdIdField;
    private JComboBox<Integer> supIdCombo; // 供应商下拉选择框
    // 移除表格相关组件
    // private JTable productTable;
    // private DefaultTableModel tableModel;
    // 业务层对象
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    public ProductManageFrame() {
        // 窗口基础配置
        setTitle("商品信息管理");
        setSize(800, 400); // 缩小窗口高度（移除表格后）
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

        // 2. 删除产品区域（新增：输入ID删除）
        JLabel deleteProdIdLabel = new JLabel("删除产品ID:");
        deleteProdIdLabel.setBounds(30, 130, 80, 25);
        deleteProdIdField = new JTextField();
        deleteProdIdField.setBounds(120, 130, 120, 25);

        // 3. 功能按钮（调整位置，移除表格后重新布局）
        JButton addBtn = new JButton("添加产品");
        addBtn.setBounds(270, 130, 100, 30);
        JButton deleteBtn = new JButton("删除产品");
        deleteBtn.setBounds(390, 130, 100, 30);
        JButton resetBtn = new JButton("重置");
        resetBtn.setBounds(510, 130, 80, 30);

        // 添加所有组件到窗口（移除表格相关组件）
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
        // 新增删除ID输入框及标签
        add(deleteProdIdLabel);
        add(deleteProdIdField);
        add(addBtn);
        add(deleteBtn);
        add(resetBtn);

        // 移除加载表格的方法调用
        // loadProductTable();

        // 4. 按钮事件绑定
        // 添加产品（保留原有逻辑）
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
                        resetFields(); // 重置表单
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "添加失败！产品已存在或供应商不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "ID、价格、库存请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 删除产品（核心修改：改为输入ID删除）
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. 获取输入的产品ID
                String deleteProdIdStr = deleteProdIdField.getText().trim();
                if (deleteProdIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "请输入要删除的产品ID！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    // 2. 转换为整数并校验
                    int prodId = Integer.parseInt(deleteProdIdStr);
                    if (prodId <= 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "产品ID必须为正整数！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 3. 查询产品信息（校验是否存在+库存是否为0）
                    Product product = productService.getProductById(prodId);
                    if (product == null) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "产品不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 4. 库存校验：库存不为0不能删除
                    float stock = (float) product.getQuantity();
                    if (stock != 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this, "库存不为0（当前库存：" + stock + "），无法删除！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 5. 确认删除
                    int confirm = JOptionPane.showConfirmDialog(ProductManageFrame.this, "确定删除产品ID为 " + prodId + " 的产品吗？", "确认", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = productService.deleteProduct(prodId);
                        if (success) {
                            JOptionPane.showMessageDialog(ProductManageFrame.this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                            // 清空删除ID输入框
                            deleteProdIdField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(ProductManageFrame.this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this, "产品ID请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 重置表单（修改：包含删除ID输入框）
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }

    // 加载所有供应商ID到下拉框（保留原有逻辑）
    private void loadSupplierIds() {
        List<Supplier> suppliers = supplierService.findAllSupplier();
        for (Supplier supplier : suppliers) {
            supIdCombo.addItem(supplier.getExesConId());
        }
    }

    // 移除表格加载方法
    // private void loadProductTable() { ... }

    // 重置表单字段（修改：包含删除ID输入框）
    private void resetFields() {
        prodIdField.setText("");
        prodNameField.setText("");
        priceField.setText("");
        typeField.setText("");
        quantityField.setText("");
        supIdCombo.setSelectedIndex(0);
        // 新增：清空删除ID输入框
        deleteProdIdField.setText("");
    }
}