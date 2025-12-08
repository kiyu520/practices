package com.Frame;

import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductManageFrame extends JFrame {
    // 表单组件
    private JTextField prodIdField, prodNameField, priceField, typeField, quantityField;
    private JTextField deleteProdIdField; // 删除产品ID输入框
    private JComboBox<Integer> supIdCombo; // 供应商下拉框
    // 功能按钮（新增：定义deleteBtn变量，解决“无法解析符号”报错）
    private JButton addBtn, deleteBtn, resetBtn;

    // 业务层对象
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();
    // 自定义图标
    private Icon customIcon;

    public ProductManageFrame() {
        // 初始化自定义图标
        initCustomIcon();
        // 统一设置JOptionPane按钮样式（增强：彻底去掉选中框）
        initOptionPaneButtonStyle();

        // 窗口基础配置
        setTitle("商品信息管理");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // ========== 1. 表单区域（添加产品） ==========
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
        loadSupplierIds(); // 加载供应商ID

        // ========== 2. 删除产品区域 ==========
        JLabel deleteProdIdLabel = new JLabel("删除产品ID:");
        deleteProdIdLabel.setBounds(30, 130, 80, 25);
        deleteProdIdField = new JTextField();
        deleteProdIdField.setBounds(120, 130, 120, 25);

        // ========== 3. 功能按钮（定义deleteBtn，解决报错） ==========
        addBtn = new JButton("添加产品");
        addBtn.setBounds(270, 130, 100, 30);
        deleteBtn = new JButton("删除产品"); // 关键：定义deleteBtn变量
        deleteBtn.setBounds(390, 130, 100, 30);
        resetBtn = new JButton("重置");
        resetBtn.setBounds(510, 130, 80, 30);

        // ========== 4. 添加所有组件到窗口 ==========
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
        add(deleteProdIdLabel);
        add(deleteProdIdField);
        add(addBtn);
        add(deleteBtn); // 关键：添加deleteBtn到窗口
        add(resetBtn);

        // ========== 5. 按钮事件绑定 ==========
        bindAddBtnEvent();
        bindDeleteBtnEvent();
        bindResetBtnEvent();
    }


    /**
     * 绑定“添加产品”按钮事件
     */
    private void bindAddBtnEvent() {
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    String prodName = prodNameField.getText().trim();
                    float price = Float.parseFloat(priceField.getText().trim());
                    String type = typeField.getText().trim();
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    int supId = (Integer) supIdCombo.getSelectedItem();

                    // 合法性校验
                    if (prodName.isEmpty() || type.isEmpty()) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "名称和种类不能为空！",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                        return;
                    }
                    if (prodName.length() > 50 || type.length() > 50) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "名称和种类长度不能超过50字符！",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                        return;
                    }
                    if (price <= 0 || quantity < 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "价格需为正数，库存不能为负数！",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                        return;
                    }

                    // 添加产品
                    Product product = new Product(prodId, prodName, price, type, quantity, supId);
                    boolean success = productService.addProduct(product);
                    if (success) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "添加成功！",
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE,
                                customIcon);
                        resetFields();
                    } else {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "添加失败！产品已存在或供应商不存在",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this,
                            "ID、价格、库存请输入合法数字！",
                            "错误",
                            JOptionPane.ERROR_MESSAGE,
                            customIcon);
                }
            }
        });
    }


    /**
     * 绑定“删除产品”按钮事件
     */
    private void bindDeleteBtnEvent() {
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deleteProdIdStr = deleteProdIdField.getText().trim();
                if (deleteProdIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this,
                            "请输入要删除的产品ID！",
                            "提示",
                            JOptionPane.WARNING_MESSAGE,
                            customIcon);
                    return;
                }

                try {
                    int prodId = Integer.parseInt(deleteProdIdStr);
                    if (prodId <= 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "产品ID必须为正整数！",
                                "提示",
                                JOptionPane.WARNING_MESSAGE,
                                customIcon);
                        return;
                    }

                    // 校验产品是否存在
                    Product product = productService.getProductById(prodId);
                    if (product == null) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "产品不存在！",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                        return;
                    }

                    // 校验库存是否为0
                    float stock = (float) product.getQuantity();
                    if (stock != 0) {
                        JOptionPane.showMessageDialog(ProductManageFrame.this,
                                "库存不为0（当前库存：" + stock + "），无法删除！",
                                "错误",
                                JOptionPane.ERROR_MESSAGE,
                                customIcon);
                        return;
                    }

                    // 确认删除弹窗（选中框已通过UIManager去掉）
                    int confirm = JOptionPane.showConfirmDialog(
                            ProductManageFrame.this,
                            "确定删除产品ID为 " + prodId + " 的产品吗？",
                            "确认",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            customIcon
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = productService.deleteProduct(prodId);
                        if (success) {
                            JOptionPane.showMessageDialog(ProductManageFrame.this,
                                    "删除成功！",
                                    "成功",
                                    JOptionPane.INFORMATION_MESSAGE,
                                    customIcon);
                            deleteProdIdField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(ProductManageFrame.this,
                                    "删除失败！",
                                    "错误",
                                    JOptionPane.ERROR_MESSAGE,
                                    customIcon);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductManageFrame.this,
                            "产品ID请输入合法数字！",
                            "错误",
                            JOptionPane.ERROR_MESSAGE,
                            customIcon);
                }
            }
        });
    }


    /**
     * 绑定“重置”按钮事件
     */
    private void bindResetBtnEvent() {
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }


    /**
     * 初始化自定义图标（优化：补充资源加载方式）
     */
    private void initCustomIcon() {
        try {
            // 替换为你的图片路径（示例：项目内资源路径）
            String imagePath = "/static/image/img13.png";
            // 优化：使用getClass().getResource确保路径正确
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            customIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("自定义图标加载失败：" + e.getMessage());
            customIcon = UIManager.getIcon("OptionPane.questionIcon");
        }
    }


    /**
     * 统一设置JOptionPane按钮样式（增强：彻底去掉选中框）
     */
    private void initOptionPaneButtonStyle() {
        try {
            // 核心：禁止绘制焦点框（去掉选中框的关键）
            UIManager.put("Button.focusPainted", Boolean.FALSE);
            // 补充：隐藏焦点边框
            UIManager.put("Button.focusBorder", BorderFactory.createEmptyBorder());
            // 补充：设置选中色为透明（避免点击后变色）
            UIManager.put("Button.select", new Color(0, 0, 0, 0));
            // 补充：禁用焦点遍历（可选，防止键盘Tab键触发焦点框）
            UIManager.put("Button.focusTraversalKeysEnabled", Boolean.FALSE);

            // 原有样式保持不变
            UIManager.put("Button.background", new Color(214, 217, 223));
            UIManager.put("Button.foreground", Color.BLACK);
        } catch (Exception e) {
            System.err.println("设置按钮样式失败：" + e.getMessage());
        }
    }


    /**
     * 加载供应商ID到下拉框
     */
    private void loadSupplierIds() {
        List<Supplier> suppliers = supplierService.findAllSupplier();
        for (Supplier supplier : suppliers) {
            supIdCombo.addItem(supplier.getExesConId());
        }
    }


    /**
     * 重置表单字段
     */
    private void resetFields() {
        prodIdField.setText("");
        prodNameField.setText("");
        priceField.setText("");
        typeField.setText("");
        quantityField.setText("");
        supIdCombo.setSelectedIndex(0);
        deleteProdIdField.setText("");
    }
}