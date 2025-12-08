package com.Frame;

import com.Entity.User;
import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;
import com.Service.UserService;
import com.Tools.DateUtil;
import com.Model.ProductTableModel;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Properties;
import com.Tools.RoundButtonUtil;
import org.apache.ibatis.session.SqlSession;

import static com.Tools.RoundButtonUtil.loadLocalIcon;

public class MainFrame extends JFrame {
    private User loginUser;
    private JLabel timeLabel;
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    // 配置文件相关常量和属性
    private static final String CONFIG_FILE = "system_config.properties";
    private static Properties configProps = new Properties();

    // 系统字体设置配置文件
    private static final String SETTINGS_FILE = "system_settings.properties";

    public MainFrame(User user) {
        this.loginUser = user;
        // 加载配置文件
        loadConfig();
        initFrame();
        initTabbedPane();
        initTimeLabel();
        // 应用配置
        applyConfig();
    }

    public MainFrame() {
        // 加载配置文件（无用户构造函数）
        loadConfig();
    }

    // 加载配置文件
    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            configProps.load(fis);
        } catch (FileNotFoundException e) {
            // 配置文件不存在时创建默认配置
            createDefaultConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "配置文件加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 创建默认配置
    private void createDefaultConfig() {
        configProps.setProperty("lookAndFeel", "Windows");
        configProps.setProperty("bgColor", "#FFFFFF");
        configProps.setProperty("fontName", "宋体");
        configProps.setProperty("fontSize", "14");
        configProps.setProperty("bgImage", "");
        // 保存默认配置
        saveConfig();
    }

    // 保存配置文件
    public static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            configProps.store(fos, "Warehouse Management System Configuration");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "配置保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 应用配置到界面
    private void applyConfig() {
        try {
            // 1. 应用界面风格
            String laf = configProps.getProperty("lookAndFeel");
            switch (laf) {
                case "Windows":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel(new NimbusLookAndFeel().getClass().getName());
                    break;
                case "Metal":
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
            }
            SwingUtilities.updateComponentTreeUI(this);

            // 2. 应用背景颜色
            String colorStr = configProps.getProperty("bgColor");
            Color bgColor = Color.decode(colorStr);
            getContentPane().setBackground(bgColor);

            // 3. 应用全局字体
            String fontName = configProps.getProperty("fontName");
            int fontSize = Integer.parseInt(configProps.getProperty("fontSize"));
            Font globalFont = new Font(fontName, Font.PLAIN, fontSize);

            // 应用到时间标签
            if (timeLabel != null) {
                timeLabel.setFont(globalFont);
            }
            // 遍历组件应用字体（简化版）
            Component[] components = getContentPane().getComponents();
            for (Component comp : components) {
                applyFontToComponent(comp, globalFont);
            }

            // 4. 背景图片（简化实现）
            String bgImagePath = configProps.getProperty("bgImage");
            if (!bgImagePath.isEmpty() && new File(bgImagePath).exists()) {
                JOptionPane.showMessageDialog(this, "背景图片已设置，重启程序后生效", "提示", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "配置应用失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 递归应用字体到所有子组件
    private void applyFontToComponent(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyFontToComponent(child, font);
            }
        }
    }

    private void initFrame() {
        String title = "仓库管理系统欢迎你--" + loginUser.getUsername();
        setTitle(title);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * 初始化实时时钟
     */
    private void initTimeLabel() {
        timeLabel = new JLabel("System Time◆◆" + DateUtil.getCurrentDateTime());
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
        add(timeLabel, BorderLayout.SOUTH);

        // 使用 Swing Timer 实现秒级更新，不阻塞 EDT
        new Timer(1000, e -> timeLabel.setText("System Time◆◆" + DateUtil.getCurrentDateTime())).start();
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("宋体", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.addTab("产品列表", new JScrollPane(ProTablePanel.get()));

        // ========== 1. 基本数据选项卡 ==========
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        dataPanel.setBackground(Color.WHITE);

        // 供应商信息管理按钮
        JButton supplierBtn = RoundButtonUtil.createRoundedButton(
                "供应商信息管理",
                "supplierManagement",
                "/static/image/img1.png"
        );
        supplierBtn.addActionListener(e -> new SupplierManageFrame().setVisible(true));

        // 商品信息管理按钮
        JButton productBtn = RoundButtonUtil.createRoundedButton(
                "商品信息管理",
                "productManage",
                "/static/image/img2.png"
        );
        productBtn.addActionListener(e -> new ProductManageFrame().setVisible(true));

        dataPanel.add(supplierBtn);
        dataPanel.add(productBtn);
        tabbedPane.addTab("基本数据", dataPanel);

        // ========== 2. 进货出货管理选项卡 ==========
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        stockPanel.setBackground(Color.WHITE);

        // 修改：调用独立的进货窗口类
        JButton stockInBtn = RoundButtonUtil.createRoundedButton(
                "商品进货",
                "stockIn",
                "/static/image/img3.png"
        );
        stockInBtn.addActionListener(e -> new StockInFrame(this, productService, supplierService).setVisible(true));

        // 修改：调用独立的出货窗口类
        JButton stockOutBtn = RoundButtonUtil.createRoundedButton(
                "商品出货",
                "stockOut",
                "/static/image/img4.png"
        );
        stockOutBtn.addActionListener(e -> new StockOutFrame(this, productService).setVisible(true));

        stockPanel.add(stockInBtn);
        stockPanel.add(stockOutBtn);
        tabbedPane.addTab("进货出货管理", stockPanel);

        // ========== 3. 查询视图选项卡 ==========
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        queryPanel.setBackground(Color.WHITE);

        JButton productQueryBtn = RoundButtonUtil.createRoundedButton(
                "产品查询",
                "productQuery",
                "/static/image/img5.png"
        );
        productQueryBtn.addActionListener(e -> new ProductView().setVisible(true));

        JButton supplierQueryBtn = RoundButtonUtil.createRoundedButton(
                "供应商查询",
                "supplierQuery",
                "/static/image/img6.png"
        );
        supplierQueryBtn.addActionListener(e -> new SupplierManageFrame().setVisible(true));

        queryPanel.add(productQueryBtn);
        queryPanel.add(supplierQueryBtn);
        tabbedPane.addTab("查询视图", queryPanel);

        // ========== 4. 系统管理选项卡 ==========
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        systemPanel.setBackground(Color.WHITE);

        JButton settingBtn = RoundButtonUtil.createRoundedButton(
                "系统设置",
                "systemSetting",
                "/static/image/img8.png"
        );
        settingBtn.addActionListener(e -> new SystemManageFrame().setVisible(true));

        JButton exitBtn = RoundButtonUtil.createRoundedButton(
                "退出系统",
                "exitSystem",
                "/static/image/img9.png"
        );
        exitBtn.addActionListener(e -> System.exit(0));

        systemPanel.add(settingBtn);
        systemPanel.add(exitBtn);
        tabbedPane.addTab("系统管理", systemPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    // 移除嵌套的 StockInFrame 和 StockOutFrame 类

    class ProductQueryFrame extends JFrame {
        private JTable productTable;
        private ProductTableModel tableModel;

        public ProductQueryFrame() {
            setTitle("产品查询");
            setSize(800, 500);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            List<Product> productList = productService.findAllproducts();
            tableModel = new ProductTableModel(productList);
            productTable = new JTable(tableModel);

            JTableHeader header = productTable.getTableHeader();
            header.setFont(new Font("宋体", Font.BOLD, 14));
            productTable.setFont(new Font("宋体", Font.PLAIN, 13));
            productTable.setRowHeight(25);

            add(new JScrollPane(productTable), BorderLayout.CENTER);
        }
    }

    class SupplierQueryFrame extends JFrame {
        private JTable supplierTable;

        public SupplierQueryFrame() {
            setTitle("供应商查询");
            setSize(900, 500);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            List<Supplier> supplierList = supplierService.findAllSupplier();
            String[] columns = {"供应商ID", "名称", "地址", "邮编", "电话", "传真", "联系人", "邮箱"};
            Object[][] data = new Object[supplierList.size()][8];
            for (int i = 0; i < supplierList.size(); i++) {
                Supplier s = supplierList.get(i);
                data[i][0] = s.getExesConId();
                data[i][1] = s.getSupName();
                data[i][2] = s.getSupAddress();
                data[i][3] = s.getPostcode();
                data[i][4] = s.getSupTelephone();
                data[i][5] = s.getSupFax();
                data[i][6] = s.getSupRelationer();
                data[i][7] = s.getSupEmail();
            }

            supplierTable = new JTable(data, columns);
            supplierTable.getTableHeader().setFont(new Font("宋体", Font.BOLD, 14));
            supplierTable.setFont(new Font("宋体", Font.PLAIN, 13));
            supplierTable.setRowHeight(28);
            add(new JScrollPane(supplierTable), BorderLayout.CENTER);
        }
    }

    private void setPadding(int padding) {
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }
}