package com.Frame;

import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.Tools.RoundButtonUtil.loadLocalIcon;

public class StockInFrame extends JFrame {
    private JTextField prodIdField;
    private JTextField quantityField;
    private JComboBox<String> supplierCombo;
    private ProductService productService;
    private SupplierService supplierService;

    // 构造方法：添加空校验，避免NullPointerException
    public StockInFrame(JFrame parentFrame, ProductService productService, SupplierService supplierService) {
        // 第一步：空校验，报错直接提示
        if (productService == null) {
            JOptionPane.showMessageDialog(null, "ProductService 不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        if (supplierService == null) {
            JOptionPane.showMessageDialog(null, "SupplierService 不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        this.productService = productService;
        this.supplierService = supplierService;

        // 窗口基础设置
        setTitle("商品进货");
        setSize(450, 350);
        // 修复：parentFrame为null时，居中显示
        setLocationRelativeTo(parentFrame == null ? null : parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 20));
        setPadding(20);

        // 初始化组件（提前创建按钮，避免索引错误）
        initComponents();
        // 绑定事件（直接引用按钮对象，而非通过索引获取）
        bindEvents();
    }

    // 修复：显式创建按钮对象，避免索引错误
    private void initComponents() {
        // 1. 商品ID
        add(new JLabel("商品ID:"));
        prodIdField = new JTextField();
        add(prodIdField);

        // 2. 进货数量
        add(new JLabel("进货数量:"));
        quantityField = new JTextField();
        add(quantityField);

        // 3. 供应商
        add(new JLabel("供应商:"));
        supplierCombo = new JComboBox<>();
        // 加载供应商列表（添加异常捕获）
        try {
            List<Supplier> suppliers = supplierService.findAllSupplier();
            for (Supplier s : suppliers) {
                supplierCombo.addItem(s.getSupName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载供应商列表失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            supplierCombo.addItem("默认供应商"); // 兜底
        }
        add(supplierCombo);

        // 4. 功能按钮（显式创建，避免索引错误）
        JButton confirmBtn = new JButton("确认进货", loadLocalIcon("/static/image/img10.png", 20, 20));
        confirmBtn.setIconTextGap(8);
        JButton clearBtn = new JButton("清空", loadLocalIcon("/static/image/img11.png", 20, 20));
        clearBtn.setIconTextGap(8);

        add(confirmBtn);
        add(clearBtn);
    }

    // 修复：直接绑定按钮对象，而非通过索引获取
    private void bindEvents() {
        // 获取按钮（通过组件遍历，更可靠）
        JButton confirmBtn = null;
        JButton clearBtn = null;

        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("确认进货")) {
                    confirmBtn = btn;
                } else if (btn.getText().equals("清空")) {
                    clearBtn = btn;
                }
            }
        }

        // 确认进货按钮事件
        if (confirmBtn != null) {
            confirmBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();
                    String quantityStr = quantityField.getText().trim();

                    // 空值校验
                    if (prodIdStr.isEmpty() || quantityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "商品ID和数量不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int prodId = Integer.parseInt(prodIdStr);
                    float quantity = Float.parseFloat(quantityStr);

                    // 合法性校验
                    if (prodId <= 0) {
                        JOptionPane.showMessageDialog(this, "商品ID必须为正整数！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(this, "进货数量必须大于0！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "进货失败（商品不存在/库存异常）", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法数字：商品ID为整数，数量为正数！", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "进货操作异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        }

        // 清空按钮事件
        if (clearBtn != null) {
            clearBtn.addActionListener(e -> clearFields());
        }
    }

    // 清空输入框
    private void clearFields() {
        prodIdField.setText("");
        quantityField.setText("");
        supplierCombo.setSelectedIndex(0);
    }

    // 设置内边距
    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}