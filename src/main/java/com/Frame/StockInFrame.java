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
    private JComboBox<Integer> supplierCombo; // 存储供应商ID（Integer类型）
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
        setLocationRelativeTo(parentFrame == null ? null : parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 20));
        setPadding(20);

        // 初始化组件（提前创建按钮，避免索引错误）
        initComponents();
        // 绑定事件（直接引用按钮对象，而非通过索引获取）
        bindEvents();
    }

    // 修复：显式创建按钮对象，加载正确的供应商ID
    private void initComponents() {
        // 1. 商品ID
        add(new JLabel("商品ID:"));
        prodIdField = new JTextField();
        add(prodIdField);

        // 2. 进货数量
        add(new JLabel("进货数量:"));
        quantityField = new JTextField();
        add(quantityField);

        // 3. 供应商（核心修改：加载供应商ID = exesConId）
        add(new JLabel("供应商ID:")); // 标签改为"供应商ID"，更直观
        supplierCombo = new JComboBox<>(); // 存储Integer类型的供应商ID
        // 加载供应商列表（适配SupplierService的静态方法和exesConId字段）
        try {
            // 调用SupplierService静态方法查询所有供应商
            List<Supplier> suppliers = SupplierService.findAllSupplier();
            if (suppliers == null || suppliers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "暂无供应商数据！", "提示", JOptionPane.WARNING_MESSAGE);
                supplierCombo.addItem(0); // 兜底（无效ID）
            } else {
                for (Supplier s : suppliers) {
                    // 关键：添加供应商的 exesConId（与SupplierService一致）
                    Integer supplierId = s.getExesConId();
                    if (supplierId != null && supplierId > 0) {
                        supplierCombo.addItem(supplierId);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载供应商列表失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            supplierCombo.addItem(0); // 兜底
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
                    int supplierId = (Integer) supplierCombo.getSelectedItem(); // 获取选中的供应商ID（exesConId）

                    // 合法性校验
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

                    // 执行进货（若stockIn方法不需要供应商ID，可删除supplierId参数）
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