package com.Frame;

import com.Entity.Supplier;
import com.Service.SupplierService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SupplierManageFrame extends JFrame {
    // 表单组件
    private JTextField supIdField, supNameField, addressField, postcodeField;
    private JTextField telephoneField, faxField, relationerField, emailField;
    private JTextField deleteSupIdField;

    // 功能按钮
    private JButton addBtn, deleteBtn, resetBtn, updateBtn;

    // 业务层对象
    private SupplierService supplierService = new SupplierService();
    private Icon customIcon;

    public SupplierManageFrame() {
        // 初始化自定义图标
        initCustomIcon();
        // 统一设置按钮样式
        initOptionPaneButtonStyle();

        // 窗口基础配置
        setTitle("供应商信息管理");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // 初始化组件
        initComponents();
        // 绑定事件
        bindEvents();
    }

    /**
     * 初始化表单组件
     */
    private void initComponents() {
        // 1. 供应商ID
        JLabel supIdLabel = new JLabel("供应商ID:");
        supIdLabel.setBounds(30, 30, 80, 25);
        supIdField = new JTextField();
        supIdField.setBounds(120, 30, 150, 25);

        // 2. 供应商名称
        JLabel supNameLabel = new JLabel("供应商名称:");
        supNameLabel.setBounds(320, 30, 90, 25);
        supNameField = new JTextField();
        supNameField.setBounds(420, 30, 150, 25);

        // 3. 地址
        JLabel addressLabel = new JLabel("地址:");
        addressLabel.setBounds(30, 80, 80, 25);
        addressField = new JTextField();
        addressField.setBounds(120, 80, 150, 25);

        // 4. 邮编
        JLabel postcodeLabel = new JLabel("邮编:");
        postcodeLabel.setBounds(320, 80, 90, 25);
        postcodeField = new JTextField();
        postcodeField.setBounds(420, 80, 150, 25);

        // 5. 电话
        JLabel telephoneLabel = new JLabel("电话:");
        telephoneLabel.setBounds(30, 130, 80, 25);
        telephoneField = new JTextField();
        telephoneField.setBounds(120, 130, 150, 25);

        // 6. 传真
        JLabel faxLabel = new JLabel("传真:");
        faxLabel.setBounds(320, 130, 90, 25);
        faxField = new JTextField();
        faxField.setBounds(420, 130, 150, 25);

        // 7. 联系人
        JLabel relationerLabel = new JLabel("联系人:");
        relationerLabel.setBounds(30, 180, 80, 25);
        relationerField = new JTextField();
        relationerField.setBounds(120, 180, 150, 25);

        // 8. 邮箱
        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setBounds(320, 180, 90, 25);
        emailField = new JTextField();
        emailField.setBounds(420, 180, 150, 25);

        // 9. 删除区域
        JLabel deleteSupIdLabel = new JLabel("删除供应商ID:");
        deleteSupIdLabel.setBounds(30, 230, 100, 25);
        deleteSupIdField = new JTextField();
        deleteSupIdField.setBounds(140, 230, 150, 25);

        // 10. 按钮
        addBtn = new JButton("添加供应商");
        addBtn.setBounds(320, 230, 120, 30);
        updateBtn = new JButton("修改供应商");
        updateBtn.setBounds(450, 230, 120, 30);
        deleteBtn = new JButton("删除供应商");
        deleteBtn.setBounds(580, 230, 120, 30);
        resetBtn = new JButton("重置");
        resetBtn.setBounds(320, 280, 100, 30);

        // 添加组件到窗口
        add(supIdLabel);
        add(supIdField);
        add(supNameLabel);
        add(supNameField);
        add(addressLabel);
        add(addressField);
        add(postcodeLabel);
        add(postcodeField);
        add(telephoneLabel);
        add(telephoneField);
        add(faxLabel);
        add(faxField);
        add(relationerLabel);
        add(relationerField);
        add(emailLabel);
        add(emailField);
        add(deleteSupIdLabel);
        add(deleteSupIdField);
        add(addBtn);
        add(updateBtn);
        add(deleteBtn);
        add(resetBtn);
    }

    /**
     * 绑定按钮事件
     */
    private void bindEvents() {
        bindAddBtnEvent();
        bindDeleteBtnEvent();
        bindUpdateBtnEvent();
        bindResetBtnEvent();
    }

    /**
     * 绑定"添加供应商"按钮事件
     */
    private void bindAddBtnEvent() {
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. 获取表单数据
                    int supId = Integer.parseInt(supIdField.getText().trim());
                    String supName = supNameField.getText().trim();
                    String address = addressField.getText().trim();
                    String postcode = postcodeField.getText().trim();
                    String telephone = telephoneField.getText().trim();
                    String fax = faxField.getText().trim();
                    String relationer = relationerField.getText().trim();
                    String email = emailField.getText().trim();

                    // 2. 客户端校验
                    if (supName.isEmpty() || address.isEmpty() || postcode.isEmpty() ||
                            telephone.isEmpty() || fax.isEmpty() || relationer.isEmpty() || email.isEmpty()) {
                        showErrorMsg("带*的字段不能为空！");
                        return;
                    }

                    // 3. 封装实体
                    Supplier supplier = new Supplier();
                    supplier.setExesConId(supId);
                    supplier.setSupName(supName);
                    supplier.setSupAddress(address);
                    supplier.setPostcode(postcode);
                    supplier.setSupTelephone(telephone);
                    supplier.setSupFax(fax);
                    supplier.setSupRelationer(relationer);
                    supplier.setSupEmail(email);

                    // 4. 调用服务层
                    boolean success = supplierService.addSupplier(supplier);
                    System.out.println(success);
                    if (success == true) {
                        showSuccessMsg("添加成功！");
                    } else {
                        showErrorMsg("添加失败！！！");
                    }
                } catch (NumberFormatException ex) {
                    showErrorMsg("供应商ID请输入合法数字！");
                }
            }
        });
    }

    /**
     * 绑定"删除供应商"按钮事件
     */
    private void bindDeleteBtnEvent() {
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String supIdStr = deleteSupIdField.getText().trim();
                if (supIdStr.isEmpty()) {
                    showWarnMsg("请输入要删除的供应商ID！");
                    return;
                }

                try {
                    int supId = Integer.parseInt(supIdStr);
                    if (supId <= 0) {
                        showWarnMsg("供应商ID必须为正整数！");
                        return;
                    }

                    // 校验供应商是否存在
                    Supplier supplier = supplierService.getSupplierById(supId);
                    if (supplier == null) {
                        showErrorMsg("供应商不存在！");
                        return;
                    }

                    // 校验是否关联产品
//                    if (supplierService.isSupplierRelatedProduct(supId)) {
//                        showErrorMsg("该供应商关联产品，无法删除！");
//                        return;
//                    }

                    // 确认删除
                    int confirm = JOptionPane.showConfirmDialog(
                            SupplierManageFrame.this,
                            "确定删除供应商ID为 " + supId + " 的记录吗？",
                            "确认",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            customIcon
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = supplierService.deleteSupplier(supId);
                        if (success) {
                            showSuccessMsg("删除成功！");
                            deleteSupIdField.setText("");
                        } else {
                            showErrorMsg("删除失败！");
                        }
                    }
                } catch (NumberFormatException ex) {
                    showErrorMsg("供应商ID请输入合法数字！");
                }
            }
        });
    }

    /**
     * 绑定"修改供应商"按钮事件
     */
    private void bindUpdateBtnEvent() {
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int supId = Integer.parseInt(supIdField.getText().trim());
                    String supName = supNameField.getText().trim();
                    String address = addressField.getText().trim();
                    String postcode = postcodeField.getText().trim();
                    String telephone = telephoneField.getText().trim();
                    String fax = faxField.getText().trim();
                    String relationer = relationerField.getText().trim();
                    String email = emailField.getText().trim();

                    // 客户端校验
                    if (supId <= 0) {
                        showErrorMsg("供应商ID必须为正整数！");
                        return;
                    }
                    if (supName.isEmpty() || address.isEmpty()) {
                        showErrorMsg("名称和地址不能为空！");
                        return;
                    }

                    // 封装实体
                    Supplier supplier = new Supplier();
                    supplier.setExesConId(supId);
                    supplier.setSupName(supName);
                    supplier.setSupAddress(address);
                    supplier.setPostcode(postcode);
                    supplier.setSupTelephone(telephone);
                    supplier.setSupFax(fax);
                    supplier.setSupRelationer(relationer);
                    supplier.setSupEmail(email);

                    // 调用服务层
                    boolean success = supplierService.updateSupplier(supplier);
                    if (success) {
                        showSuccessMsg("修改成功！");
                    } else {
                        showErrorMsg("修改失败！供应商不存在或数据无效");
                    }
                } catch (NumberFormatException ex) {
                    showErrorMsg("供应商ID请输入合法数字！");
                }
            }
        });
    }

    /**
     * 绑定"重置"按钮事件
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
     * 重置表单字段
     */
    private void resetFields() {
        supIdField.setText("");
        supNameField.setText("");
        addressField.setText("");
        postcodeField.setText("");
        telephoneField.setText("");
        faxField.setText("");
        relationerField.setText("");
        emailField.setText("");
        deleteSupIdField.setText("");
    }

    /**
     * 初始化自定义图标
     */
    private void initCustomIcon() {
        try {
            String imagePath = "/static/image/img13.png";
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            customIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("自定义图标加载失败：" + e.getMessage());
            customIcon = UIManager.getIcon("OptionPane.questionIcon");
        }
    }

    /**
     * 统一设置按钮样式
     */
    private void initOptionPaneButtonStyle() {
        try {
            UIManager.put("Button.focusPainted", Boolean.FALSE);
            UIManager.put("Button.focusBorder", BorderFactory.createEmptyBorder());
            UIManager.put("Button.select", new Color(0, 0, 0, 0));
            UIManager.put("Button.background", new Color(214, 217, 223));
            UIManager.put("Button.foreground", Color.BLACK);
        } catch (Exception e) {
            System.err.println("设置按钮样式失败：" + e.getMessage());
        }
    }

    /**
     * 显示成功消息
 * 该方法用于在界面上显示一个成功提示对话框
 * @param msg 需要显示的成功消息内容
     */
    private void showSuccessMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "成功",
                JOptionPane.INFORMATION_MESSAGE, customIcon);
    }

    /**
     * 显示错误消息
     */
    private void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "错误",
                JOptionPane.ERROR_MESSAGE, customIcon);
    }

    /**
     * 显示警告消息
     */
    private void showWarnMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "提示",
                JOptionPane.WARNING_MESSAGE, customIcon);
    }
}