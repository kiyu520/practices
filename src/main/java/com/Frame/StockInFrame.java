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
    private JComboBox<Integer> supplierCombo;
    private ProductService productService;
    private SupplierService supplierService;

    public StockInFrame(JFrame parentFrame, ProductService productService, SupplierService supplierService) {
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

        setTitle("商品进货");
        setSize(450, 400);
        setLocationRelativeTo(parentFrame == null ? null : parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 20));
        setPadding(20);

        initComponents();
        bindEvents();
    }

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
        add(new JLabel("供应商ID:"));
        supplierCombo = new JComboBox<>();
        try {
            List<Supplier> suppliers = SupplierService.findAllSupplier();
            if (suppliers == null || suppliers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "暂无供应商数据！", "提示", JOptionPane.WARNING_MESSAGE);
                supplierCombo.addItem(0);
            } else {
                for (Supplier s : suppliers) {
                    Integer supplierId = s.getExesConId();
                    if (supplierId != null && supplierId > 0) {
                        supplierCombo.addItem(supplierId);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载供应商列表失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            supplierCombo.addItem(0);
        }
        add(supplierCombo);

        // 4. 查看库存按钮
        JButton checkStockBtn = new JButton("查看库存", loadLocalIcon("/static/image/img12.png", 20, 20));
        checkStockBtn.setIconTextGap(8);
        add(checkStockBtn);
        add(new JLabel()); // 占位

        // 5. 功能按钮
        JButton confirmBtn = new JButton("确认进货", loadLocalIcon("/static/image/img10.png", 20, 20));
        confirmBtn.setIconTextGap(8);
        JButton clearBtn = new JButton("清空", loadLocalIcon("/static/image/img11.png", 20, 20));
        clearBtn.setIconTextGap(8);

        add(confirmBtn);
        add(clearBtn);
    }

    private void bindEvents() {
        JButton confirmBtn = null;
        JButton clearBtn = null;
        JButton checkStockBtn = null;

        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("确认进货")) {
                    confirmBtn = btn;
                } else if (btn.getText().equals("清空")) {
                    clearBtn = btn;
                } else if (btn.getText().equals("查看库存")) {
                    checkStockBtn = btn;
                }
            }
        }

        if (checkStockBtn != null) {
            checkStockBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();

                    //空值校验
                    if (prodIdStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "请先输入商品ID！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int prodId;
                    //是否为整数 + 是否为正整数
                    try {
                        prodId = Integer.parseInt(prodIdStr);
                        if (prodId <= 0) {
                            JOptionPane.showMessageDialog(this, "商品ID必须为正整数！", "错误", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "商品ID请输入合法的正整数！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    //商品ID是否存在
                    boolean isExist = productService.isProductExist(prodId);
                    if (!isExist) {
                        JOptionPane.showMessageDialog(this, "输入的商品ID不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 所有校验通过后，查询库存
                    float stock = (float) productService.getStockById(prodId);
                    JOptionPane.showMessageDialog(this,
                            "商品ID：" + prodId + "\n当前剩余库存量：" + stock,
                            "库存查询结果",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "库存查询失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        }

        // 确认进货按钮事件
        if (confirmBtn != null) {
            confirmBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();
                    String quantityStr = quantityField.getText().trim();

                    if (prodIdStr.isEmpty() || quantityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "商品ID和数量不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int prodId = Integer.parseInt(prodIdStr);
                    float quantity = Float.parseFloat(quantityStr);
                    int supplierId = (Integer) supplierCombo.getSelectedItem();

                    if (prodId <= 0) {
                        JOptionPane.showMessageDialog(this, "商品ID必须为正整数！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(this, "进货数量必须大于0！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (supplierId <= 0) {
                        JOptionPane.showMessageDialog(this, "请选择有效的供应商！", "提示", JOptionPane.WARNING_MESSAGE);
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

    private void clearFields() {
        prodIdField.setText("");
        quantityField.setText("");
        supplierCombo.setSelectedIndex(0);
    }

    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}