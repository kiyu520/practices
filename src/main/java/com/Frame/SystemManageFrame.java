package com.Frame;

import javax.swing.*;
import java.awt.*;

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
        exitBtn.setPreferredSize(new Dimension(80, 26));
    }

    private void bindComponentEvents() {
        submitPwdBtn.addActionListener(e -> submitPasswordModify());
        resetPwdBtn.addActionListener(e -> resetPasswordInputs());
        saveSettingBtn.addActionListener(e -> saveSystemSettings());
        exitBtn.addActionListener(e -> exitSystem());
    }

    // 原功能函数（未修改）
    private void loadAllSystemUsers() {
        // 后续实现
    }
    private void submitPasswordModify() {
        // 后续实现
    }
    private void resetPasswordInputs() {
        // 后续实现
    }
    private void saveSystemSettings() {
        // 后续实现
    }
    private void loadSavedSettings() {
        // 后续实现
    }
    private void exitSystem() {
        // 后续实现
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SystemManageFrame frame = new SystemManageFrame();
//            frame.setVisible(true);
//        });
//    }
}