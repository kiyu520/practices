package com.Frame;

import com.Entity.User;
import com.Service.ProductService;
import com.Service.SupplierService;
import com.Tools.DateUtil;

import javax.swing.*;
import java.awt.*;

import com.Tools.RoundButtonUtil;

public class MainFrame extends JFrame {
    private User loginUser;
    private JLabel timeLabel;
    // 修复：确保服务实例非空（显式初始化）
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    public MainFrame(User user) {
        this.loginUser = user;
        initFrame();  // 初始化窗口
        initTabbedPane();  // 初始化选项卡面板
        initTimeLabel();  // 初始化时间标签
    }

    private void initFrame() {
        String title = "仓库管理系统欢迎你--" + loginUser.getUsername();
        setTitle(title);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // 修复：用Swing Timer替代while(true)，避免阻塞EDT
    private void initTimeLabel() {
        timeLabel = new JLabel("System Time◆◆" + DateUtil.getCurrentDateTime());
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
        add(timeLabel, BorderLayout.SOUTH);

        // 安全的秒级更新（Swing Timer）
        new Timer(1000, e -> timeLabel.setText("System Time◆◆" + DateUtil.getCurrentDateTime())).start();
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("宋体", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.addTab("产品列表", new JScrollPane(ProTablePanel.get()));
        tabbedPane.addTab("供应商列表", new JScrollPane(SupTablePanel.get()));

        // 1. 基本数据选项卡
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        dataPanel.setBackground(Color.WHITE);
        JButton supplierBtn = RoundButtonUtil.createRoundedButton(
                "供应商信息管理",
                "supplierManagement",
                "/static/image/img1.png"
        );
        supplierBtn.addActionListener(e -> new SupplierManageFrame().setVisible(true));
        JButton productBtn = RoundButtonUtil.createRoundedButton(
                "商品信息管理",
                "productManage",
                "/static/image/img2.png"
        );
        productBtn.addActionListener(e -> new ProductManageFrame().setVisible(true));
        dataPanel.add(supplierBtn);
        dataPanel.add(productBtn);
        tabbedPane.addTab("基本数据", dataPanel);

        // 2. 进货出货管理选项卡（核心修复：添加异常捕获+明确实例化）
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        stockPanel.setBackground(Color.WHITE);

        // 进货按钮（添加异常捕获，确保报错能显示）
        JButton stockInBtn = RoundButtonUtil.createRoundedButton(
                "商品进货",
                "stockIn",
                "/static/image/img3.png"
        );
        stockInBtn.addActionListener(e -> {
            try {
                // 修复：传递非null的主窗口实例
                StockInFrame stockInFrame = new StockInFrame(MainFrame.this, productService, supplierService);
                stockInFrame.setVisible(true);
            } catch (Exception ex) {
                // 关键：捕获并显示所有异常，定位问题
                JOptionPane.showMessageDialog(MainFrame.this,
                        "打开进货窗口失败：" + ex.getMessage() + "\n" +
                                "异常类型：" + ex.getClass().getName(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // 控制台输出完整堆栈
            }
        });

        // 出货按钮（同理添加异常捕获）
        JButton stockOutBtn = RoundButtonUtil.createRoundedButton(
                "商品出货",
                "stockOut",
                "/static/image/img4.png"
        );
        stockOutBtn.addActionListener(e -> {
            try {
                StockOutFrame stockOutFrame = new StockOutFrame(MainFrame.this, productService);
                stockOutFrame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "打开出货窗口失败：" + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        stockPanel.add(stockInBtn);
        stockPanel.add(stockOutBtn);
        tabbedPane.addTab("进货出货管理", stockPanel);

        // 3. 查询视图选项卡
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        queryPanel.setBackground(Color.WHITE);
        JButton productQueryBtn = RoundButtonUtil.createRoundedButton(
                "产品查询",
                "productQuery",
                "/static/image/img5.png"
        );
        productQueryBtn.addActionListener(e -> new ProductQueryFrame().setVisible(true));
        JButton supplierQueryBtn = RoundButtonUtil.createRoundedButton(
                "供应商查询",
                "supplierQuery",
                "/static/image/img6.png"
        );
        supplierQueryBtn.addActionListener(e -> new SupplierQueryFrame().setVisible(true));
        queryPanel.add(productQueryBtn);
        queryPanel.add(supplierQueryBtn);
        tabbedPane.addTab("查询视图", queryPanel);

        // 4. 系统管理选项卡
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        systemPanel.setBackground(Color.WHITE);

        JButton settingBtn = RoundButtonUtil.createRoundedButton(
                "更改密码",
                "systemSetting",
                "/static/image/img14.png"
        );
        settingBtn.addActionListener(e -> new PasswordManagementFrame());

        JButton operatorBtn = RoundButtonUtil.createRoundedButton(
                "操作员管理",
                "operatormanage",
                "/static/image/img15.png"
        );
        operatorBtn.addActionListener(e -> new OperatorManagementFrame());

        JButton exitBtn = RoundButtonUtil.createRoundedButton(
                "退出系统",
                "exitSystem",
                "/static/image/img9.png"
        );
        exitBtn.addActionListener(e -> System.exit(0));
        systemPanel.add(settingBtn);
        systemPanel.add(operatorBtn);
        systemPanel.add(exitBtn);

        tabbedPane.addTab("系统管理", systemPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
