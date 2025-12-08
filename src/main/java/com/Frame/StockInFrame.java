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

    // 构造方法：接收主窗口、产品服务、供应商服务
    public StockInFrame(JFrame parentFrame, ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;

        // 窗口基础设置
        setTitle("商品进货");
        setSize(450, 350);
        setLocationRelativeTo(parentFrame); // 相对于主窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 20));
        setPadding(20);

        // 初始化组件
        initComponents();
        // 绑定事件
        bindEvents();
    }

    // 初始化界面组件
    private void initComponents() {
        // 商品ID
        add(new JLabel("商品ID:"));
        prodIdField = new JTextField();
        add(prodIdField);

        // 进货数量
        add(new JLabel("进货数量:"));
        quantityField = new JTextField();
        add(quantityField);

        // 供应商
        add(new JLabel("供应商:"));
        supplierCombo = new JComboBox<>();
        // 加载供应商列表
        List<Supplier> suppliers = supplierService.findAllSupplier();
        for (Supplier s : suppliers) {
            supplierCombo.addItem(s.getSupName());
        }
        add(supplierCombo);

        // 功能按钮
        JButton confirmBtn = new JButton("确认进货", loadLocalIcon("/static/image/img10.png", 20, 20));
        JButton clearBtn = new JButton("清空", loadLocalIcon("/static/image/img11.png", 20, 20));
        confirmBtn.setIconTextGap(8);
        clearBtn.setIconTextGap(8);
        add(confirmBtn);
        add(clearBtn);
    }

    // 绑定按钮事件
    private void bindEvents() {
        // 确认进货按钮
        JButton confirmBtn = (JButton) getContentPane().getComponent(8);
        confirmBtn.addActionListener(e -> {
            try {
                int prodId = Integer.parseInt(prodIdField.getText().trim());
                float quantity = Float.parseFloat(quantityField.getText().trim());
                String supplierName = (String) supplierCombo.getSelectedItem();

                boolean success = productService.stockIn(prodId, quantity);
                if (success) {
                    JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "进货失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 清空按钮
        JButton clearBtn = (JButton) getContentPane().getComponent(9);
        clearBtn.addActionListener(e -> clearFields());
    }

    // 清空输入框
    private void clearFields() {
        prodIdField.setText("");
        quantityField.setText("");
    }

    // 设置内边距
    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}