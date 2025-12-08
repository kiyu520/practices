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

    public StockOutFrame(JFrame parentFrame, ProductService productService) {
        // 空校验
        if (productService == null) {
            JOptionPane.showMessageDialog(null, "ProductService 不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        this.productService = productService;

        setTitle("商品出货");
        setSize(450, 350);
        setLocationRelativeTo(parentFrame == null ? null : parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 20));
        setPadding(20);

        initComponents();
        bindEvents();
    }

    private void initComponents() {
        // 商品ID
        add(new JLabel("商品ID:"));
        prodIdField = new JTextField();
        add(prodIdField);

        // 出货数量
        add(new JLabel("出货数量:"));
        quantityField = new JTextField();
        add(quantityField);

        // 功能按钮
        JButton confirmBtn = new JButton("确认出货", loadLocalIcon("/static/image/img10.png", 20, 20));
        confirmBtn.setIconTextGap(8);
        JButton clearBtn = new JButton("清空", loadLocalIcon("/static/image/img11.png", 20, 20));
        clearBtn.setIconTextGap(8);

        add(confirmBtn);
        add(clearBtn);
    }

    private void bindEvents() {
        JButton confirmBtn = null;
        JButton clearBtn = null;

        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("确认出货")) {
                    confirmBtn = btn;
                } else if (btn.getText().equals("清空")) {
                    clearBtn = btn;
                }
            }
        }

        // 确认出货按钮
        if (confirmBtn != null) {
            confirmBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();
                    String quantityStr = quantityField.getText().trim();
                    String customer = customerField.getText().trim();

                    if (prodIdStr.isEmpty() || quantityStr.isEmpty() || customer.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "商品ID、数量、客户信息不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int prodId = Integer.parseInt(prodIdStr);
                    float quantity = Float.parseFloat(quantityStr);

                    if (prodId <= 0) {
                        JOptionPane.showMessageDialog(this, "商品ID必须为正整数！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(this, "出货数量必须大于0！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    boolean success = productService.stockOut(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "出货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "出货失败（库存不足/商品不存在）", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "出货操作异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        }

        // 清空按钮
        if (clearBtn != null) {
            clearBtn.addActionListener(e -> clearFields());
        }
    }

    private void clearFields() {
        prodIdField.setText("");
        quantityField.setText("");
        customerField.setText("");
    }

    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}