package com.Frame;

import javax.swing.*;
import java.awt.*;

public class SystemManageFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel passwordPanel;
    private JLabel targetUserLabel; // 目标用户名标签
    private JComboBox<String> targetUserComboBox; // 目标用户名下拉框（加载所有系统用户）
    private JLabel oldPwdLabel; // 原密码标签
    private JPasswordField oldPwdField; // 原密码输入框（验证身份）
    private JLabel newPwdLabel; // 新密码标签
    private JPasswordField newPwdField; // 新密码输入框
    private JLabel confirmPwdLabel; // 确认新密码标签
    private JPasswordField confirmPwdField; // 确认新密码输入框
    private JButton submitPwdBtn; // 提交修改按钮
    private JButton resetPwdBtn; // 重置输入按钮
    // 字体设置模块
    private JPanel settingPanel;
    private JComboBox<String> fontComboBox; // 字体选择
    private JComboBox<Integer> fontSizeComboBox; // 字体大小选择
    private JButton saveSettingBtn; // 保存字体设置按钮
    // 退出系统按钮
    private JButton exitBtn;

    public SystemManageFrame() {
        // 初始化窗口基础属性
        initFrameBasic();
        // 初始化所有界面组件（密码修改+字体设置）
        initComponents();
        // 绑定组件交互事件
        bindComponentEvents();
        // 加载已保存的字体设置
        loadSavedSettings();
    }
/**
 * 初始化窗体的基本属性和布局
 * 设置窗体标题、大小、位置以及关闭操作
 * 使用BorderLayout作为布局管理器
 */
    private void initFrameBasic() {
    // 设置窗体标题为"仓库管理系统--系统管理"
        this.setTitle("仓库管理系统--系统管理");
    // 设置窗体大小为600x400像素
        this.setSize(600, 400);
    // 将窗体位置设置为屏幕中央
        this.setLocationRelativeTo(null);
    // 设置窗体关闭操作为仅关闭当前窗体
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    // 设置窗体布局为BorderLayout布局
        this.setLayout(new BorderLayout());
    }

/**
 * 初始化界面组件的方法
 * 设置主面板的布局和边距，并初始化密码面板、设置面板和退出按钮
 */
    private void initComponents() {
        mainPanel = new JPanel(new GridLayout(3,1,20,20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    // 初始化密码输入面板
        intPasswordPanel();

    // 初始化设置面板
        intSettingPanel();

    // 初始化退出按钮
        intExitBtn();
        mainPanel.add(passwordPanel);
        mainPanel.add(settingPanel);
        mainPanel.add(exitBtn);
        this.add(mainPanel, BorderLayout.CENTER);
    }

/**
 * 初始化密码修改面板的方法
 * 该方法创建一个带有标题的密码修改面板，并设置布局和约束条件
 */
    private void intPasswordPanel() {
    // 创建一个新的JPanel，使用GridBagLayout作为布局管理器
        passwordPanel = new JPanel(new GridBagLayout());
    // 为面板设置边框和标题
        passwordPanel.setBorder(BorderFactory.createTitledBorder("密码修改(请先选择用户名)"));
    // 创建GridBagConstraints对象，用于控制组件在GridBagLayout中的布局
        GridBagConstraints gbc = new GridBagConstraints();
    // 设置组件之间的间距
        gbc.insets = new Insets(10, 10, 10, 10);
    // 设置组件的填充方式为水平填充
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 第一步：目标用户名选择（必显组件）
        targetUserLabel = new JLabel("目标用户名：");
        gbc.gridx = 0; // 列索引
        gbc.gridy = 0; // 行索引
        passwordPanel.add(targetUserLabel, gbc);

        targetUserComboBox = new JComboBox<>();
        targetUserComboBox.setPreferredSize(new Dimension(220, 28)); // 下拉框宽度
        loadAllSystemUsers(); // 加载所有系统用户到下拉框（从数据库查询）
        passwordPanel.add(targetUserComboBox, gbc);

        // 第二步：原密码输入（必显组件）
/**
 * 创建密码修改面板的相关组件
 * 包括原密码、新密码、确认新密码的输入框和对应的标签
 * 以及提交修改和重置输入的功能按钮
 */
// 创建原密码标签
        oldPwdLabel = new JLabel("原密码：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        passwordPanel.add(oldPwdLabel, gbc);

// 创建原密码输入框
        oldPwdField = new JPasswordField();
        oldPwdField.setPreferredSize(new Dimension(220, 28));
        passwordPanel.add(oldPwdField, gbc);

// 创建新密码标签
        newPwdLabel = new JLabel("新密码：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        passwordPanel.add(newPwdLabel, gbc);

// 创建新密码输入框
        newPwdField = new JPasswordField();
        newPwdField.setPreferredSize(new Dimension(220, 28));
        passwordPanel.add(newPwdField, gbc);

// 创建确认新密码标签
        confirmPwdLabel = new JLabel("确认新密码：");
        gbc.gridx = 0;
        gbc.gridy = 3;
        passwordPanel.add(confirmPwdLabel, gbc);

// 创建确认新密码输入框
        confirmPwdField = new JPasswordField();
        confirmPwdField.setPreferredSize(new Dimension(220, 28));
        passwordPanel.add(confirmPwdField, gbc);

        // 第三步：功能按钮（提交/重置）
        submitPwdBtn = new JButton("提交修改");
        submitPwdBtn.setPreferredSize(new Dimension(100, 32));
        gbc.gridx = 0;
        gbc.gridy = 4;
        passwordPanel.add(submitPwdBtn, gbc);

        resetPwdBtn = new JButton("重置输入");
        resetPwdBtn.setPreferredSize(new Dimension(100, 32));
        gbc.gridx = 1;
        passwordPanel.add(resetPwdBtn, gbc);
    }

/**
 * 初始化设置面板的方法
 * 该方法用于创建和配置设置相关的界面组件
 * 通常包括各种设置选项的添加和布局
 */
    private void intSettingPanel() {
        settingPanel = new JPanel(new GridBagLayout());
        settingPanel.setBorder(BorderFactory.createTitledBorder("字体设置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 字体选择
        JLabel fontLabel = new JLabel("字体：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        settingPanel.add(fontLabel, gbc);

        fontComboBox = new JComboBox<>(new String[]{"宋体", "黑体", "微软雅黑", "楷体", "Arial"});
        fontComboBox.setPreferredSize(new Dimension(180, 26));
        settingPanel.add(fontComboBox, gbc);

        // 字体大小选择
        JLabel fontSizeLabel = new JLabel("字体大小：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        settingPanel.add(fontSizeLabel, gbc);

        fontSizeComboBox = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 22});
        fontSizeComboBox.setPreferredSize(new Dimension(180, 26));
        settingPanel.add(fontSizeComboBox, gbc);

        // 保存字体设置按钮
        saveSettingBtn = new JButton("保存字体设置");
        saveSettingBtn.setPreferredSize(new Dimension(180, 32));
        gbc.gridx = 0;
        gbc.gridy = 2;
        settingPanel.add(saveSettingBtn, gbc);
    }


/**
 * 初始化退出按钮的方法
 * 创建一个退出按钮，设置其大小和位置，并将其添加到主面板中
 */
    private void initExitBtn() {
        exitBtn = new JButton("退出系统");
        exitBtn.setPreferredSize(new Dimension(150, 38));
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.add(exitBtn);
        mainPanel.add(exitPanel);
    }

    private void bindComponentEvents() {
        // 事件1：提交密码修改（核心业务流程）
        submitPwdBtn.addActionListener(e -> submitPasswordModify());

        // 事件2：重置密码输入框（清空所有输入）
        resetPwdBtn.addActionListener(e -> resetPasswordInputs());

        // 事件3：保存字体相关设置到属性文件
        saveSettingBtn.addActionListener(e -> saveSystemSettings());

        // 事件4：退出系统（确认后关闭应用）
        exitBtn.addActionListener(e -> exitSystem());
    }
    // ======================== 核心功能函数（仅拟定框架，不实现具体逻辑） ========================
    /**
     * 功能1：加载所有系统用户到下拉框（从数据库users表查询）
     * 逻辑：查询users表的u_name字段，将所有用户名添加到下拉框
     */
    private void loadAllSystemUsers() {
        // 后续实现：
        // 1. 调用数据库DAO层接口，查询所有用户的用户名
        // 2. 遍历结果集，通过targetUserComboBox.addItem(用户名)添加选项
        // 3. 若无用户数据，添加“无系统用户”提示项并禁用提交按钮
    }

    /**
     * 功能2：提交密码修改（核心业务逻辑）
     * 逻辑：校验输入合法性→验证原密码→更新数据库密码
     */
    private void submitPasswordModify() {
        // 后续实现：
        // 1. 获取选中的目标用户名：String targetUserName = (String) targetUserComboBox.getSelectedItem()
        // 2. 获取输入的密码：原密码（oldPwdField.getPassword()）、新密码、确认密码
        // 3. 合法性校验：原密码非空、新密码非空、新密码与确认密码一致、新密码长度（如6-20位）
        // 4. 身份验证：查询数据库，比对目标用户的原密码是否正确
        // 5. 密码更新：验证通过后，将新密码加密（如MD5）后更新到users表
        // 6. 结果提示：通过JOptionPane显示“修改成功”或失败原因（如原密码错误）
    }

    /**
     * 功能3：重置密码输入框（清空所有输入内容）
     */
    private void resetPasswordInputs() {
        // 后续实现：
        // 1. 清空原密码框：oldPwdField.setText("")
        // 2. 清空新密码框：newPwdField.setText("")
        // 3. 清空确认密码框：confirmPwdField.setText("")
    }

    /**
     * 功能4：保存系统设置（仅字体、字体大小）到属性文件
     * 逻辑：获取当前选择的字体和字号，写入config.properties
     */
    private void saveSystemSettings() {
        // 后续实现：
        // 1. 获取选中的字体：String selectedFont = (String) fontComboBox.getSelectedItem()
        // 2. 获取选中的字号：Integer selectedFontSize = (Integer) fontSizeComboBox.getSelectedItem()
        // 3. 使用Properties类，将“font=选中字体”“fontSize=选中字号”存入属性对象
        // 4. 通过Properties.store()写入属性文件（如src/main/resources/config.properties）
        // 5. 提示保存结果：JOptionPane.showMessageDialog(this, "字体设置保存成功")
    }

    /**
     * 功能5：加载已保存的系统设置（从属性文件读取字体、字号）
     * 逻辑：启动时读取属性文件，自动应用之前保存的字体设置
     */
    private void loadSavedSettings() {
        // 后续实现：
        // 1. 创建Properties对象，通过Properties.load()读取属性文件
        // 2. 若文件存在且有字体配置，设置fontComboBox和fontSizeComboBox的选中项
        // 3. 若文件不存在或配置缺失，使用默认值（如“宋体”“14号”）
    }

    /**
     * 功能6：退出系统（安全退出应用）
     * 逻辑：弹出确认对话框，确认后关闭所有窗口
     */
    private void exitSystem() {
        // 后续实现：
        // 1. 弹出确认框：int result = JOptionPane.showConfirmDialog(this, "确定要退出系统吗？")
        // 2. 若result == JOptionPane.YES_OPTION，调用System.exit(0)退出应用
        // 3. 若选择“取消”，不执行任何操作
    }

    /**
     * 测试入口（可选，用于单独调试界面布局）
     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SystemManageFrame frame = new SystemManageFrame();
//            frame.setVisible(true);
//        });
//    }
}

