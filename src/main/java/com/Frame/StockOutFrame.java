package com.Frame;

import com.Service.ProductService;

import javax.swing.*;
import java.awt.*;

import static com.Tools.RoundButtonUtil.loadLocalIcon;

public class StockOutFrame extends JFrame {
    private JTextField prodIdField;
    private JTextField quantityField;
    private JTextField customerField;
    private ProductService productService;

    // 构造方法：接收主窗口、产品服务
    public StockOutFrame(JFrame parentFrame, ProductService productService) {
        this.productService = productService;

        // 窗口基础设置
        setTitle("商品出货");
        setSize(450, 350);
        setLocationRelativeTo(parentFrame); // 相对于主窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 20));
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

        // 出货数量
        add(new JLabel("出货数量:"));
        quantityField = new JTextField();
        add(quantityField);

        // 客户信息
        add(new JLabel("客户信息:"));
        customerField = new JTextField();
        add(customerField);

        // 功能按钮
        JButton confirmBtn = new JButton("确认出货", loadLocalIcon("/static/image/img10.png", 20, 20));
        JButton clearBtn = new JButton("清空", loadLocalIcon("/static/image/img11.png", 20, 20));
        confirmBtn.setIconTextGap(8);
        clearBtn.setIconTextGap(8);
        add(confirmBtn);
        add(clearBtn);
    }

    // 绑定按钮事件
    private void bindEvents() {
        // 确认出货按钮
        JButton confirmBtn = (JButton) getContentPane().getComponent(6);
        confirmBtn.addActionListener(e -> {
            try {
                int prodId = Integer.parseInt(prodIdField.getText().trim());
                float quantity = Float.parseFloat(quantityField.getText().trim());
                String customer = customerField.getText().trim();

                boolean success = productService.stockOut(prodId, quantity);
                if (success) {
                    JOptionPane.showMessageDialog(this, "出货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "出货失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 清空按钮
        JButton clearBtn = (JButton) getContentPane().getComponent(7);
        clearBtn.addActionListener(e -> clearFields());
    }

    // 清空输入框
    private void clearFields() {
        prodIdField.setText("");
        quantityField.setText("");
        customerField.setText("");
    }

    // 设置内边距
    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}