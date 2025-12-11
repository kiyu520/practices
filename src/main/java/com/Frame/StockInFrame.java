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
            // 修正：调用实例的findAllSupplier方法（原代码调用静态方法易出错）
            List<Supplier> suppliers = supplierService.findAllSupplier();
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

        // 查看库存按钮逻辑（保留原有增强校验）
        if (checkStockBtn != null) {
            checkStockBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();

                    // 校验1：空值校验
                    if (prodIdStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "请先输入商品ID！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int prodId;
                    // 校验2：是否为整数 + 是否为正整数
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

                    // 校验3：商品ID是否存在
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

        // 确认进货按钮事件（核心新增：产品-供应商匹配校验）
        if (confirmBtn != null) {
            confirmBtn.addActionListener(e -> {
                try {
                    String prodIdStr = prodIdField.getText().trim();
                    String quantityStr = quantityField.getText().trim();

                    // 1. 非空校验
                    if (prodIdStr.isEmpty() || quantityStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "商品ID和数量不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 2. 数值合法性校验
                    int prodId;
                    float quantity;
                    int supplierId = (Integer) supplierCombo.getSelectedItem();
                    try {
                        prodId = Integer.parseInt(prodIdStr);
                        quantity = Float.parseFloat(quantityStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "请输入合法数字：商品ID为整数，数量为正数！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

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

                    // 3. 核心新增：校验商品是否存在
                    boolean isProdExist = productService.isProductExist(prodId);
                    if (!isProdExist) {
                        JOptionPane.showMessageDialog(this, "商品ID不存在，无法进货！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 4. 核心新增：产品-供应商匹配校验（一个产品只能对应一个供应商）
                    Integer prodBindSupplierId = getSupplierIdByProdId(prodId);
                    if (prodBindSupplierId == null) {
                        JOptionPane.showMessageDialog(this, "该商品未绑定供应商，无法进货！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!prodBindSupplierId.equals(supplierId)) {
                        JOptionPane.showMessageDialog(this,
                                String.format("供应商选择错误！商品ID %d 绑定的供应商ID为 %d，当前选择的是 %d",
                                        prodId, prodBindSupplierId, supplierId),
                                "错误",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 5. 执行进货操作
                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "进货失败（库存异常）", "错误", JOptionPane.ERROR_MESSAGE);
                    }
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

    /**
     * 核心新增方法：根据产品ID查询其绑定的供应商ID
     * 体现“一个产品只能对应一个供应商”的核心规则
     */
    private Integer getSupplierIdByProdId(int prodId) {
        try {
            // 调用ProductService的方法查询产品绑定的供应商ID
            // 需确保ProductService中实现该方法：根据prodId查询产品信息，返回其supId
            return productService.getSupplierIdByProdId(prodId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询商品绑定的供应商失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return null;
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