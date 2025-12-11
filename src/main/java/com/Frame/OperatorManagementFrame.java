package com.Frame;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OperatorManagementFrame extends JFrame {
    // 标签页容器
    private JTabbedPane tabbedPane;
    // 添加操作员面板组件
    private JPanel addPanel;
    private JTextField addUsernameField;
    private JPasswordField addPwdField;
    private JPasswordField addConfirmPwdField;
    private JButton addSubmitBtn;
    private JButton addResetBtn;
    // 删除操作员面板组件
    private JPanel deletePanel;
    private JComboBox<String> deleteUserComboBox;
    private JButton deleteSubmitBtn;
    private JButton deleteResetBtn;

    int userrole = LoginFrame.userole;

    public OperatorManagementFrame() {
        if (userrole == 0){
            initFrameBasic();
            initComponents();
            bindEvents();
            loadAllOperators();
            this.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "权限不足，无法访问该功能", "提示", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }
    }

    private void initFrameBasic() {
        this.setTitle("仓库管理系统--操作员管理");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("宋体", Font.PLAIN, 14));

        initAddPanel();

        initDeletePanel();

        tabbedPane.addTab("添加操作员", addPanel);
        tabbedPane.addTab("删除操作员", deletePanel);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private void initAddPanel() {
        addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        addPanel.add(new JLabel("用户名："), gbc);
        addUsernameField = new JTextField(15);
        gbc.gridx = 1;
        addPanel.add(addUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addPanel.add(new JLabel("密码："), gbc);
        addPwdField = new JPasswordField(15);
        gbc.gridx = 1;
        addPanel.add(addPwdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("再密码："), gbc);
        addConfirmPwdField = new JPasswordField(15);
        gbc.gridx = 1;
        addPanel.add(addConfirmPwdField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        addSubmitBtn = new JButton("添加");
        addResetBtn = new JButton("重置");
        btnPanel.add(addSubmitBtn);
        btnPanel.add(addResetBtn);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        addPanel.add(btnPanel, gbc);
    }

    private void initDeletePanel() {
        deletePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // 操作员下拉选择
        gbc.gridx = 0;
        gbc.gridy = 0;
        deletePanel.add(new JLabel("选择操作员："), gbc);
        deleteUserComboBox = new JComboBox<>();
        deleteUserComboBox.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        deletePanel.add(deleteUserComboBox, gbc);

        // 按钮区域
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        deleteSubmitBtn = new JButton("删除");
        deleteResetBtn = new JButton("重置");
        btnPanel.add(deleteSubmitBtn);
        btnPanel.add(deleteResetBtn);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        deletePanel.add(btnPanel, gbc);
    }

    private void bindEvents() {
        // 添加操作员-提交
        addSubmitBtn.addActionListener(e -> submitAddOperator());
        // 添加操作员-重置
        addResetBtn.addActionListener(e -> resetAddInputs());
        // 删除操作员-提交
        deleteSubmitBtn.addActionListener(e -> submitDeleteOperator());
        // 删除操作员-重置
        deleteResetBtn.addActionListener(e -> deleteUserComboBox.setSelectedIndex(-1));
    }

    private void loadAllOperators() {
        deleteUserComboBox.removeAllItems();
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            user_mapper mapper = sqlSession.getMapper(user_mapper.class);
            List<User> userList = mapper.select_user_all();
            for (User user : userList) {
                deleteUserComboBox.addItem(user.getUsername());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载操作员列表失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitAddOperator() {
        String username = addUsernameField.getText().trim();
        String pwd = new String(addPwdField.getPassword()).trim();
        String confirmPwd = new String(addConfirmPwdField.getPassword()).trim();

        if (username.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "信息不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "两次密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pwd.length() < 6) {
            JOptionPane.showMessageDialog(this, "密码长度不能少于6位！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (SqlSession sqlSession = SqlUtil.getSession()) {
            user_mapper mapper = sqlSession.getMapper(user_mapper.class);
            if (mapper.select_user_by_name(username) != null) {
                JOptionPane.showMessageDialog(this, "用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int rows = mapper.add_user_args(username, pwd, 1); // 假设2是普通操作员角色值
            sqlSession.commit();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "添加操作员成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                resetAddInputs();
                loadAllOperators();
            } else {
                JOptionPane.showMessageDialog(this, "添加操作员失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "添加异常：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitDeleteOperator() {
        String username = (String) deleteUserComboBox.getSelectedItem();
        if (username == null) {
            JOptionPane.showMessageDialog(this, "请选择要删除的操作员！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除操作员【" + username + "】吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (SqlSession sqlSession = SqlUtil.getSession()) {
            user_mapper mapper = sqlSession.getMapper(user_mapper.class);
            int rows = mapper.delete_user_by_name(username);
            sqlSession.commit();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "删除操作员成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadAllOperators(); // 刷新下拉框
            } else {
                JOptionPane.showMessageDialog(this, "删除操作员失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "删除异常：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetAddInputs() {
        addUsernameField.setText("");
        addPwdField.setText("");
        addConfirmPwdField.setText("");
    }
}