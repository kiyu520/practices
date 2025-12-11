package com.Frame;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Service.UserService;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PasswordManagementFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel passwordModifyPanel;
    private JLabel targetUserLabel;
    private JLabel originalPwdLabel;
    private JLabel newPwdLabel;
    private JLabel confirmNewPwdLabel;
    private JComboBox<String> userSelectComboBox;
    private JPasswordField originalPwdField;
    private JPasswordField newPwdField;
    private JPasswordField confirmNewPwdField;
    private JButton submitModifyBtn;
    private JButton resetInputBtn;
    private JButton closeWindowBtn;

    private final UserService userService = new UserService();

    int userrole = LoginFrame.userole;

    public PasswordManagementFrame() {
        if(userrole == 0){
            // 初始化窗口基础配置
            initFrameConfig();
            // 初始化所有组件
            initAllComponents();
            // 绑定组件事件
            bindComponentEvents();
            this.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this,"您没有权限更改密码", "提示", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }
    }

    private void initFrameConfig() {
        this.setTitle("仓库管理系统 - 密码管理");
        this.setSize(500, 350);
        this.setLocationRelativeTo(null); // 居中显示
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 关闭当前窗口（不退出程序）
        this.setLayout(new BorderLayout(10, 10));
        this.setResizable(false); // 禁止调整窗口大小
    }

    private void initAllComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        initPasswordModifyPanel();

        initBottomButtonPanel();

        mainPanel.add(passwordModifyPanel, BorderLayout.CENTER);
        mainPanel.add(initBottomButtonPanel(), BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void initPasswordModifyPanel() {
// 使用GridBagLayout布局管理器创建密码修改面板
        passwordModifyPanel = new JPanel(new GridBagLayout());
// 为面板设置带有标题的边框，提示用户需要先选择用户
        passwordModifyPanel.setBorder(BorderFactory.createTitledBorder("密码修改（请先选择用户）"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. 目标用户选择
        targetUserLabel = new JLabel("目标用户：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(targetUserLabel, gbc);

        userSelectComboBox = new JComboBox<>();
        userSelectComboBox.setPreferredSize(new Dimension(220, 30));
        loadAllSystemUsers();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        passwordModifyPanel.add(userSelectComboBox, gbc);

        // 2. 原密码输入
        originalPwdLabel = new JLabel("原密码：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(originalPwdLabel, gbc);

        originalPwdField = new JPasswordField();
        originalPwdField.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        passwordModifyPanel.add(originalPwdField, gbc);

        // 3. 新密码输入
        newPwdLabel = new JLabel("新密码：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(newPwdLabel, gbc);

        newPwdField = new JPasswordField();
        newPwdField.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        passwordModifyPanel.add(newPwdField, gbc);

        // 4. 确认新密码输入
        confirmNewPwdLabel = new JLabel("确认新密码：");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(confirmNewPwdLabel, gbc);

        confirmNewPwdField = new JPasswordField();
        confirmNewPwdField.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        passwordModifyPanel.add(confirmNewPwdField, gbc);

        // 5. 功能按钮（提交+重置）
        submitModifyBtn = new JButton("提交修改");
        submitModifyBtn.setPreferredSize(new Dimension(100, 32));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(submitModifyBtn, gbc);

        resetInputBtn = new JButton("重置输入");
        resetInputBtn.setPreferredSize(new Dimension(100, 32));
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        passwordModifyPanel.add(resetInputBtn, gbc);
    }

/**
 * 初始化底部按钮面板
 * @return 配置好的JPanel面板
 */
    private JPanel initBottomButtonPanel() {
        // 创建一个使用右对齐流式布局的面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 创建"关闭窗口"按钮，并设置按钮大小为120x30像素
        closeWindowBtn = new JButton("关闭窗口");
        closeWindowBtn.setPreferredSize(new Dimension(120, 30));

        // 将按钮添加到底部面板中
        bottomPanel.add(closeWindowBtn);
        // 返回配置完成的底部面板
        return bottomPanel;
    }

    private void bindComponentEvents() {
        // 提交密码修改
        submitModifyBtn.addActionListener(e -> submitPasswordModification());
        // 重置输入框
        resetInputBtn.addActionListener(e -> resetPasswordInputs());
        // 关闭窗口
        closeWindowBtn.addActionListener(e -> this.dispose());
    }

/**
 * 加载所有系统用户的方法
 * 从数据库中查询所有用户，并将用户名添加到下拉选择框中
 */
    private void loadAllSystemUsers() {
        // 清空下拉选择框中的所有项目
        userSelectComboBox.removeAllItems();
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            // 获取用户映射器
            user_mapper userMapper = sqlSession.getMapper(user_mapper.class);
            // 查询所有用户
            List<User> userList = userMapper.select_user_all();

            // 遍历用户列表，将每个用户的用户名添加到下拉选择框中
            for (User user : userList) {
                userSelectComboBox.addItem(user.getUsername());
            }
        } catch (Exception e) {
            // 捕获异常并显示错误对话框
            JOptionPane.showMessageDialog(this,
                    "加载用户列表失败：" + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

/**
 * 提交密码修改方法
 * 处理用户密码修改的整个流程，包括数据获取、验证、修改操作及结果处理
 */
    private void submitPasswordModification() {
        // 获取输入数据
        String targetUsername = (String) userSelectComboBox.getSelectedItem();
        String originalPwd = new String(originalPwdField.getPassword());
        String newPwd = new String(newPwdField.getPassword());
        String confirmNewPwd = new String(confirmNewPwdField.getPassword());

        if (!validateInputData(targetUsername, originalPwd, newPwd, confirmNewPwd)) {
            return;
        }


        try {
            // 尝试调用userService的changePassword方法修改用户密码
            // 参数包括目标用户名、原始密码和新密码
            boolean modifyResult = userService.changePassword(targetUsername, originalPwd, newPwd);
            // 检查密码修改是否成功
            if (modifyResult) {
                JOptionPane.showMessageDialog(this, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                resetPasswordInputs(); // 重置输入框
            } else {
                JOptionPane.showMessageDialog(this, "原密码错误或修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "修改密码异常：" + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputData(String username, String originalPwd, String newPwd, String confirmPwd) {
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择目标用户！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (originalPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有密码输入项不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "两次输入的新密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (newPwd.length() < 6) {
            JOptionPane.showMessageDialog(this, "新密码长度不能少于6位！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (newPwd.equals(originalPwd)) {
            JOptionPane.showMessageDialog(this, "新密码不能与原密码相同！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void resetPasswordInputs() {
        originalPwdField.setText("");
        newPwdField.setText("");
        confirmNewPwdField.setText("");
    }

}