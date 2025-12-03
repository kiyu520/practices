package com.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 供应商管理界面（添加、修改、删除）
 */
public class SupplierManageFrame extends JFrame {
    // 组件定义
    private JLabel lblId, lblName, lblAddress, lblPostcode, lblTel, lblFax, lblRelationer, lblEmail;
    private JTextField txtId, txtName, txtAddress, txtPostcode, txtTel, txtFax, txtRelationer, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnReset;

    public SupplierManageFrame() {
        // 界面初始化
        initFrame();
        // 绑定事件
        bindEvents();
    }

    /**
     * 初始化界面布局
     */
    private void initFrame() {
        // 窗口设置
        this.setTitle("供应商信息管理");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null); // 居中显示
        this.setLayout(new GridLayout(9, 2, 10, 10)); // 网格布局
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 组件初始化
        lblId = new JLabel("供应商ID：");
        txtId = new JTextField();
        lblName = new JLabel("供应商名称：");
        txtName = new JTextField();
        lblAddress = new JLabel("地址：");
        txtAddress = new JTextField();
        lblPostcode = new JLabel("邮编：");
        txtPostcode = new JTextField();
        lblTel = new JLabel("电话：");
        txtTel = new JTextField();
        lblFax = new JLabel("传真：");
        txtFax = new JTextField();
        lblRelationer = new JLabel("联系人：");
        txtRelationer = new JTextField();
        lblEmail = new JLabel("邮箱：");
        txtEmail = new JTextField();

        btnAdd = new JButton("添加");
        btnUpdate = new JButton("修改");
        btnDelete = new JButton("删除");
        btnReset = new JButton("重置");

        // 添加组件到窗口
        this.add(lblId);
        this.add(txtId);
        this.add(lblName);
        this.add(txtName);
        this.add(lblAddress);
        this.add(txtAddress);
        this.add(lblPostcode);
        this.add(txtPostcode);
        this.add(lblTel);
        this.add(txtTel);
        this.add(lblFax);
        this.add(txtFax);
        this.add(lblRelationer);
        this.add(txtRelationer);
        this.add(lblEmail);
        this.add(txtEmail);

        // 按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);
        this.add(btnPanel);

        // 显示窗口
        this.setVisible(true);
    }

    /**
     * 绑定按钮事件
     */
    private void bindEvents() {
//        // 添加供应商
//        btnAdd.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addSupplier();
//            }
//        });
//
//        // 修改供应商
//        btnUpdate.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                updateSupplier();
//            }
//        });
//
//        // 删除供应商
//        btnDelete.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteSupplier();
//            }
//        });
//
//        // 重置输入框
//        btnReset.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                resetInput();
//            }
//        });
    }

    /**
     * 添加供应商
     */
    private void addSupplier() {
//        // 1. 获取输入数据
//        String idStr = txtId.getText().trim();
//        String name = txtName.getText().trim();
//        String address = txtAddress.getText().trim();
//        String postcode = txtPostcode.getText().trim();
//        String tel = txtTel.getText().trim();
//        String fax = txtFax.getText().trim();
//        String relationer = txtRelationer.getText().trim();
//        String email = txtEmail.getText().trim();
//
//        // 2. 合法性校验
//        if (idStr.isEmpty() || name.isEmpty() || address.isEmpty() || postcode.isEmpty()
//                || tel.isEmpty() || fax.isEmpty() || relationer.isEmpty() || email.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "所有字段不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        int id;
//        try {
//            id = Integer.parseInt(idStr);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "供应商ID必须为数字！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 3. 检查供应商是否已存在
//        if (isSupplierExist(id)) {
//            JOptionPane.showMessageDialog(this, "该供应商ID已存在！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 4. 插入数据库
//        Connection conn = DBUtil.getConnection();
//        String sql = "INSERT INTO suppliers(exesConId, sup_name, sup_address, postcode, sup_telephone, sup_fax, sup_relationer, sup_email) VALUES (?,?,?,?,?,?,?,?)";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//            pstmt.setString(2, name);
//            pstmt.setString(3, address);
//            pstmt.setString(4, postcode);
//            pstmt.setString(5, tel);
//            pstmt.setString(6, fax);
//            pstmt.setString(7, relationer);
//            pstmt.setString(8, email);
//
//            int rows = pstmt.executeUpdate();
//            if (rows > 0) {
//                JOptionPane.showMessageDialog(this, "供应商添加成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
//                resetInput(); // 重置输入框
//            } else {
//                JOptionPane.showMessageDialog(this, "供应商添加失败！", "提示", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.closeConnection(conn);
//        }
    }

    /**
     * 修改供应商
     */
    private void updateSupplier() {
//        // 1. 获取输入数据
//        String idStr = txtId.getText().trim();
//        String name = txtName.getText().trim();
//        String address = txtAddress.getText().trim();
//        String postcode = txtPostcode.getText().trim();
//        String tel = txtTel.getText().trim();
//        String fax = txtFax.getText().trim();
//        String relationer = txtRelationer.getText().trim();
//        String email = txtEmail.getText().trim();
//
//        // 2. 合法性校验
//        if (idStr.isEmpty() || name.isEmpty() || address.isEmpty() || postcode.isEmpty()
//                || tel.isEmpty() || fax.isEmpty() || relationer.isEmpty() || email.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "所有字段不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        int id;
//        try {
//            id = Integer.parseInt(idStr);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "供应商ID必须为数字！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 3. 检查供应商是否存在
//        if (!isSupplierExist(id)) {
//            JOptionPane.showMessageDialog(this, "该供应商不存在！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 4. 更新数据库
//        Connection conn = DBUtil.getConnection();
//        String sql = "UPDATE suppliers SET sup_name=?, sup_address=?, postcode=?, sup_telephone=?, sup_fax=?, sup_relationer=?, sup_email=? WHERE exesConId=?";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, name);
//            pstmt.setString(2, address);
//            pstmt.setString(3, postcode);
//            pstmt.setString(4, tel);
//            pstmt.setString(5, fax);
//            pstmt.setString(6, relationer);
//            pstmt.setString(7, email);
//            pstmt.setInt(8, id);
//
//            int rows = pstmt.executeUpdate();
//            if (rows > 0) {
//                JOptionPane.showMessageDialog(this, "供应商修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
//                resetInput();
//            } else {
//                JOptionPane.showMessageDialog(this, "供应商修改失败！", "提示", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.closeConnection(conn);
//        }
    }

    /**
     * 删除供应商
     */
    private void deleteSupplier() {
//        // 1. 获取供应商ID
//        String idStr = txtId.getText().trim();
//        if (idStr.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "请输入供应商ID！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        int id;
//        try {
//            id = Integer.parseInt(idStr);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "供应商ID必须为数字！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 2. 检查供应商是否存在
//        if (!isSupplierExist(id)) {
//            JOptionPane.showMessageDialog(this, "该供应商不存在！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 3. 检查该供应商是否关联产品（核心约束）
//        if (isSupplierRelatedProduct(id)) {
//            JOptionPane.showMessageDialog(this, "该供应商下存在产品，无法删除！", "提示", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // 4. 确认删除
//        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该供应商吗？", "确认", JOptionPane.YES_NO_OPTION);
//        if (confirm != JOptionPane.YES_OPTION) {
//            return;
//        }
//
//        // 5. 删除数据库记录
//        Connection conn = DBUtil.getConnection();
//        String sql = "DELETE FROM suppliers WHERE exesConId=?";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//
//            int rows = pstmt.executeUpdate();
//            if (rows > 0) {
//                JOptionPane.showMessageDialog(this, "供应商删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
//                resetInput();
//            } else {
//                JOptionPane.showMessageDialog(this, "供应商删除失败！", "提示", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.closeConnection(conn);
//        }
    }

    /**
     * 重置输入框
     */
    private void resetInput() {
//        txtId.setText("");
//        txtName.setText("");
//        txtAddress.setText("");
//        txtPostcode.setText("");
//        txtTel.setText("");
//        txtFax.setText("");
//        txtRelationer.setText("");
//        txtEmail.setText("");
    }

    /**
     * 检查供应商是否存在
     * @param id 供应商ID
     * @return 存在返回true，否则false
     */
    private boolean isSupplierExist(int id) {
//        Connection conn = DBUtil.getConnection();
//        String sql = "SELECT * FROM suppliers WHERE exesConId=?";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//            ResultSet rs = pstmt.executeQuery();
//            return rs.next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            DBUtil.closeConnection(conn);
//        }
        return false;
    }

    /**
     * 检查供应商是否关联产品
     * @param id 供应商ID
     * @return 关联返回true，否则false
     */
    private boolean isSupplierRelatedProduct(int id) {
//        Connection conn = DBUtil.getConnection();
//        String sql = "SELECT * FROM products WHERE sup_id=?";
//        try {
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//            ResultSet rs = pstmt.executeQuery();
//            return rs.next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            DBUtil.closeConnection(conn);
//        }
        return false;
    }

    // 测试入口
    public static void main(String[] args) {
        // Swing组件需在事件调度线程中运行
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SupplierManageFrame();
            }
        });
    }
}