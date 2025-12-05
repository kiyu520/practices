package com.Frame;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Service.UserService;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SystemManageFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel passwordPanel;
    private JLabel targetUserLabel;
    private JComboBox<String> targetUserComboBox;
    private JLabel oldPwdLabel;
    private JPasswordField oldPwdField;
    private JLabel newPwdLabel;
    private JPasswordField newPwdField;
    private JLabel confirmPwdLabel;
    private JPasswordField confirmPwdField;
    private JButton submitPwdBtn;
    private JButton resetPwdBtn;
    // 字体设置模块
    private JPanel settingPanel;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> fontSizeComboBox;
    private JButton saveSettingBtn;
    // 退出系统按钮
    private JButton exitBtn;

    private UserService userService = new UserService();

    // 示例：定义为系统配置文件的路径
    private static final String SETTINGS_FILE = "system_settings.properties";

    public SystemManageFrame() {
        initFrameBasic();
        initComponents();
        bindComponentEvents();
        loadSavedSettings();
    }

    private void initFrameBasic() {
        this.setTitle("仓库管理系统--系统管理");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }

    private void initComponents() {
        // 主面板改用 左右分栏的BorderLayout（确保密码+字体面板强制并列）
        mainPanel = new JPanel(new BorderLayout(15, 15)); // 组件间距15
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 左侧：密码面板（收缩宽度）
        intPasswordPanel();
        passwordPanel.setPreferredSize(new Dimension(300, 300)); // 固定密码面板宽度，向左收缩

        // 右侧：字体面板（强制占右侧空间）
        intSettingPanel();
        settingPanel.setPreferredSize(new Dimension(220, 300)); // 固定字体面板宽度

        // 底部：退出按钮（右下角）
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        initExitBtn();
        bottomPanel.add(exitBtn);

        // 组装主面板：左=密码，右=字体，下=退出
        mainPanel.add(passwordPanel, BorderLayout.WEST);
        mainPanel.add(settingPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void intPasswordPanel() {
        passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("密码修改(请先选择用户名)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 目标用户名（收缩下拉框宽度）
        targetUserLabel = new JLabel("目标用户名：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        passwordPanel.add(targetUserLabel, gbc);

        targetUserComboBox = new JComboBox<>();
        targetUserComboBox.setPreferredSize(new Dimension(180, 28)); // 缩小下拉框宽度
        loadAllSystemUsers();
        gbc.gridx = 1;
        passwordPanel.add(targetUserComboBox, gbc);

        // 原密码（收缩输入框宽度）
        oldPwdLabel = new JLabel("原密码：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        passwordPanel.add(oldPwdLabel, gbc);

        oldPwdField = new JPasswordField();
        oldPwdField.setPreferredSize(new Dimension(180, 28)); // 缩小输入框宽度
        gbc.gridx = 1;
        passwordPanel.add(oldPwdField, gbc);

        // 新密码
        newPwdLabel = new JLabel("新密码：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        passwordPanel.add(newPwdLabel, gbc);

        newPwdField = new JPasswordField();
        newPwdField.setPreferredSize(new Dimension(180, 28));
        gbc.gridx = 1;
        passwordPanel.add(newPwdField, gbc);

        // 确认新密码
        confirmPwdLabel = new JLabel("确认新密码：");
        gbc.gridx = 0;
        gbc.gridy = 3;
        passwordPanel.add(confirmPwdLabel, gbc);

        confirmPwdField = new JPasswordField();
        confirmPwdField.setPreferredSize(new Dimension(180, 28));
        gbc.gridx = 1;
        passwordPanel.add(confirmPwdField, gbc);

        // 功能按钮（缩小按钮尺寸）
        submitPwdBtn = new JButton("提交修改");
        submitPwdBtn.setPreferredSize(new Dimension(90, 30));
        gbc.gridx = 0;
        gbc.gridy = 4;
        passwordPanel.add(submitPwdBtn, gbc);

        resetPwdBtn = new JButton("重置输入");
        resetPwdBtn.setPreferredSize(new Dimension(90, 30));
        gbc.gridx = 1;
        passwordPanel.add(resetPwdBtn, gbc);
    }

    private void intSettingPanel() {
        settingPanel = new JPanel(new GridBagLayout());
        settingPanel.setBorder(BorderFactory.createTitledBorder("字体设置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 字体：标签+下拉框（确保显示完整）
        JLabel fontLabel = new JLabel("字体：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        settingPanel.add(fontLabel, gbc);

        fontComboBox = new JComboBox<>(new String[]{"宋体", "黑体", "微软雅黑", "楷体", "Arial"});
        fontComboBox.setPreferredSize(new Dimension(120, 26));
        gbc.gridx = 1;
        settingPanel.add(fontComboBox, gbc);

        // 字体大小：标签+下拉框
        JLabel fontSizeLabel = new JLabel("字体大小：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        settingPanel.add(fontSizeLabel, gbc);

        fontSizeComboBox = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 22});
        fontSizeComboBox.setPreferredSize(new Dimension(120, 26));
        gbc.gridx = 1;
        settingPanel.add(fontSizeComboBox, gbc);

        // 保存按钮
        saveSettingBtn = new JButton("保存字体设置");
        saveSettingBtn.setPreferredSize(new Dimension(150, 32));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        settingPanel.add(saveSettingBtn, gbc);
    }

    private void initExitBtn() {
        exitBtn = new JButton("退出系统");
        exitBtn.setPreferredSize(new Dimension(120, 26));
    }

    private void bindComponentEvents() {
        submitPwdBtn.addActionListener(e -> submitPasswordModify());
        resetPwdBtn.addActionListener(e -> resetPasswordInputs());
        saveSettingBtn.addActionListener(e -> saveSystemSettings());
        exitBtn.addActionListener(e -> exitSystem());
    }

    // 功能函数
    private void loadAllSystemUsers() {
        // 清空下拉框
        targetUserComboBox.removeAllItems();
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            user_mapper userMapper = sqlSession.getMapper(user_mapper.class);
            // 调用Mapper查询所有用户
            List<User> userList = userMapper.select_user_all();
            // 将用户名添加到下拉框
            for (User user : userList) {
                targetUserComboBox.addItem(user.getUsername());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载用户列表失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. 提交密码修改
    private void submitPasswordModify() {
        // 获取输入数据
        String targetUser = (String) targetUserComboBox.getSelectedItem();
        String oldPwd = new String(oldPwdField.getPassword());
        String newPwd = new String(newPwdField.getPassword());
        String confirmPwd = new String(confirmPwdField.getPassword());

        // 1. 基础校验
        if (targetUser == null || targetUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择目标用户！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "密码输入不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "两次输入的新密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPwd.length() < 6) {
            JOptionPane.showMessageDialog(this, "新密码长度不能少于6位！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. 调用Service修改密码
        try (SqlSession sqlSession = SqlUtil.getSession()) {
            user_mapper userMapper = sqlSession.getMapper(user_mapper.class);
            // 先校验原密码（复用Service的登录逻辑）
            User loginUser = userService.login(targetUser, oldPwd);

            if (loginUser == null) {
                JOptionPane.showMessageDialog(this, "原密码输入错误！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 构建更新用户对象
            User updateUser = new User();
            updateUser.setUsername(targetUser);
            updateUser.setPassword(newPwd);
            updateUser.setUserRole(loginUser.getUserRole()); // 保留原有角色

            // 调用Mapper更新密码
            int rows = userMapper.update_user_by_name(updateUser);
            sqlSession.commit(); // MyBatis手动提交事务

            // 3. 结果反馈
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                resetPasswordInputs();
            } else {
                JOptionPane.showMessageDialog(this, "密码修改失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "修改密码异常：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3. 重置密码输入框（纯UI操作，无数据层）
    private void resetPasswordInputs() {
        oldPwdField.setText("");
        newPwdField.setText("");
        confirmPwdField.setText("");
    }

    // ========== 字体设置（保留原有逻辑，非核心） ==========
    // 4. 保存系统字体设置
    private void saveSystemSettings() {
        String fontName = (String) fontComboBox.getSelectedItem();
        Integer fontSize = (Integer) fontSizeComboBox.getSelectedItem();

        if (fontName == null || fontSize == null) {
            JOptionPane.showMessageDialog(this, "请选择字体和字号！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 保存到配置文件
        java.util.Properties settingsProps = new java.util.Properties();
        settingsProps.setProperty("font.name", fontName);
        settingsProps.setProperty("font.size", fontSize.toString());
        saveProperties(settingsProps, SETTINGS_FILE);

        // 立即应用字体设置
        applyFontSetting(fontName, fontSize);

        JOptionPane.showMessageDialog(this, "字体设置已保存并生效！", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    // 5. 加载保存的系统设置
    private void loadSavedSettings() {
        java.util.Properties settingsProps = loadProperties(SETTINGS_FILE);
        String fontName = settingsProps.getProperty("font.name", "宋体");
        String fontSizeStr = settingsProps.getProperty("font.size", "14");
        int fontSize = Integer.parseInt(fontSizeStr);

        // 设置下拉框选中项
        fontComboBox.setSelectedItem(fontName);
        fontSizeComboBox.setSelectedItem(fontSize);

        // 应用字体设置
        applyFontSetting(fontName, fontSize);
    }

    // 6. 退出系统
    private void exitSystem() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要退出系统吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 退出程序
            System.exit(0);
        }
    }

    // ========== 工具方法（字体设置+配置文件） ==========
    // 加载Properties文件
    private java.util.Properties loadProperties(String filePath) {
        java.util.Properties props = new java.util.Properties();
        java.io.File file = new java.io.File(filePath);
        if (file.exists()) {
            try (java.io.InputStream is = new java.io.FileInputStream(file)) {
                props.load(is);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, "加载配置文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        return props;
    }

    // 保存Properties文件
    private void saveProperties(java.util.Properties props, String filePath) {
        try (java.io.OutputStream os = new java.io.FileOutputStream(filePath)) {
            props.store(os, "System Font Settings");
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "保存配置文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 应用字体设置到所有组件
    private void applyFontSetting(String fontName, int fontSize) {
        Font newFont = new Font(fontName, Font.PLAIN, fontSize);
        // 应用到密码面板所有组件
        for (Component comp : passwordPanel.getComponents()) {
            comp.setFont(newFont);
        }
        // 应用到字体设置面板所有组件
        for (Component comp : settingPanel.getComponents()) {
            comp.setFont(newFont);
        }
        // 应用到退出按钮
        exitBtn.setFont(newFont);
        // 刷新UI
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SystemManageFrame frame = new SystemManageFrame();
            frame.setVisible(true);
        });
    }
}