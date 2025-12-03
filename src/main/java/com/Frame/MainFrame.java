package com.Frame;

import com.Entity.User;
import com.Entity.Product;
import com.Entity.Supplier;
import com.Service.ProductService;
import com.Service.SupplierService;
import com.Service.UserService;
import com.Tools.DateUtil;
import com.Model.ProductTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import com.Tools.RoundButtonUtil;

import static com.Tools.RoundButtonUtil.loadLocalIcon;


public class MainFrame extends JFrame {
    private User loginUser;
    private JLabel timeLabel;
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();
    public MainFrame(User user) {
        this.loginUser = user;
        initFrame();
        initTabbedPane();
        initTimeLabel();
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

        new Thread(() -> {
            while (true) {
                SwingUtilities.invokeLater(() -> timeLabel.setText("System Time◆◆" + DateUtil.getCurrentDateTime()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("宋体", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);
       JPanel userPanel = new JPanel();
       userPanel.setBackground(Color.WHITE);
       userPanel.setLayout(new BorderLayout(5, 5));
       userPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
       userPanel.add(ProListPanel.get());
       tabbedPane.addTab("测试", userPanel);
        tabbedPane.addTab("用户列表",new JPanel());
        // ========== 1. 基本数据选项卡 ==========
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        dataPanel.setBackground(Color.WHITE);

        // 供应商信息管理按钮：调用独立的SupplierManageFrame
        JButton supplierBtn = RoundButtonUtil.createRoundedButton(
                "供应商信息管理",
                "supplierManagement",
                "/static/image/img1.png"
        );
        supplierBtn.addActionListener(e -> new SupplierManageFrame().setVisible(true));
        // 核心修改：商品信息管理按钮→调用独立的ProductManageFrame（你的完整功能类）
        JButton productBtn = RoundButtonUtil.createRoundedButton(
                "商品信息管理",
                "productManage",
                "/static/image/img2.png"
        );
        productBtn.addActionListener(e -> {
            // 直接实例化你提供的独立ProductManageFrame类
            new ProductManageFrame().setVisible(true);
        });

        dataPanel.add(supplierBtn);
        dataPanel.add(productBtn);
        tabbedPane.addTab("基本数据", dataPanel);

        // ========== 2. 进货出货管理选项卡 ==========
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        stockPanel.setBackground(Color.WHITE);

        JButton stockInBtn = RoundButtonUtil.createRoundedButton(
                "商品进货",
                "stockIn",
                "/static/image/img3.png"
        );
        stockInBtn.addActionListener(e -> new MainFrame.StockInFrame().setVisible(true));

        JButton stockOutBtn = RoundButtonUtil.createRoundedButton(
                "商品出货",
                "stockOut",
                "/static/image/img4.png"
        );
        stockOutBtn.addActionListener(e -> new MainFrame.StockOutFrame().setVisible(true));

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
        productQueryBtn.addActionListener(e -> new MainFrame.ProductQueryFrame().setVisible(true));

        JButton supplierQueryBtn = RoundButtonUtil.createRoundedButton(
                "供应商查询",
                "supplierQuery",
                "/static/image/img6.png"
        );
        supplierQueryBtn.addActionListener(e -> new MainFrame.SupplierQueryFrame().setVisible(true));

        queryPanel.add(productQueryBtn);
        queryPanel.add(supplierQueryBtn);
        tabbedPane.addTab("查询视图", queryPanel);

        // ========== 4. 系统管理选项卡 ==========
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        systemPanel.setBackground(Color.WHITE);

        JButton changePwdBtn = RoundButtonUtil.createRoundedButton(
                "更改密码",
                "changePwd",
                "/static/image/img7.png"
        );
        changePwdBtn.addActionListener(e -> new MainFrame.ChangePasswordFrame(loginUser).setVisible(true));

        JButton settingBtn = RoundButtonUtil.createRoundedButton(
                "系统设置",
                "systemSetting",
                "/static/image/img8.png"
        );
        settingBtn.addActionListener(e -> new MainFrame.SystemManageFrame().setVisible(true));

        JButton exitBtn = RoundButtonUtil.createRoundedButton(
                "退出系统",
                "exitSystem",
                "/static/image/img9.png"
        );
        exitBtn.addActionListener(e -> System.exit(0));

        systemPanel.add(changePwdBtn);
        systemPanel.add(settingBtn);
        systemPanel.add(exitBtn);
        tabbedPane.addTab("系统管理", systemPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    class StockInFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private JComboBox<String> supplierCombo;

        public StockInFrame() {
            setTitle("商品进货");
            setSize(450, 350);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(5, 2, 10, 20));
            setPadding(20);

            add(new JLabel("商品ID:"));
            prodIdField = new JTextField();
            add(prodIdField);

            add(new JLabel("进货数量:"));
            quantityField = new JTextField();
            add(quantityField);

            add(new JLabel("供应商:"));
            supplierCombo = new JComboBox<>();
            List<Supplier> suppliers = supplierService.findAllSupplier();
            for (Supplier s : suppliers) {
                supplierCombo.addItem(s.getSupName());
            }
            add(supplierCombo);

            JButton confirmBtn = new JButton("确认进货", loadLocalIcon("D:/icons/ok.png", 20, 20));
            JButton clearBtn = new JButton("清空", loadLocalIcon("D:/icons/clear.png", 20, 20));
            confirmBtn.setIconTextGap(8);
            clearBtn.setIconTextGap(8);
            add(confirmBtn);
            add(clearBtn);

            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    String supplierName = (String) supplierCombo.getSelectedItem();

                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "进货失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            clearBtn.addActionListener(e -> clearFields());
        }

        private void clearFields() {
            prodIdField.setText("");
            quantityField.setText("");
        }

        private void setPadding(int padding) {
            ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        }
    }

    public class StockOutFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private JTextField customerField;

        public StockOutFrame() {
            setTitle("商品出货");
            setSize(450, 350);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(4, 2, 10, 20));
            setPadding(20);

            add(new JLabel("商品ID:"));
            prodIdField = new JTextField();
            add(prodIdField);

            add(new JLabel("出货数量:"));
            quantityField = new JTextField();
            add(quantityField);

            add(new JLabel("客户信息:"));
            customerField = new JTextField();
            add(customerField);

            JButton confirmBtn = new JButton("确认出货", loadLocalIcon("D:/icons/ok.png", 20, 20));
            JButton clearBtn = new JButton("清空", loadLocalIcon("D:/icons/clear.png", 20, 20));
            confirmBtn.setIconTextGap(8);
            clearBtn.setIconTextGap(8);
            add(confirmBtn);
            add(clearBtn);

            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    String customer = customerField.getText().trim();

                    boolean success = productService.stockOut(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "出货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "出货失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            clearBtn.addActionListener(e -> clearFields());
        }

        private void clearFields() {
            prodIdField.setText("");
            quantityField.setText("");
            customerField.setText("");
        }

        private void setPadding(int padding) {
            ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        }
    }

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

    class ChangePasswordFrame extends JFrame {
        private User loginUser;
        private JPasswordField oldPwdField;
        private JPasswordField newPwdField;
        private JPasswordField confirmPwdField;

        public ChangePasswordFrame(User user) {
            this.loginUser = user;
            setTitle("更改密码");
            setSize(400, 300);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(4, 2, 10, 20));
            setPadding(20);

            add(new JLabel("原密码:"));
            oldPwdField = new JPasswordField();
            add(oldPwdField);

            add(new JLabel("新密码:"));
            newPwdField = new JPasswordField();
            add(newPwdField);

            add(new JLabel("确认新密码:"));
            confirmPwdField = new JPasswordField();
            add(confirmPwdField);

            JButton confirmBtn = new JButton("确认修改", loadLocalIcon("D:/icons/ok.png", 20, 20));
            JButton cancelBtn = new JButton("取消", loadLocalIcon("D:/icons/cancel.png", 20, 20));
            confirmBtn.setIconTextGap(8);
            cancelBtn.setIconTextGap(8);
            add(confirmBtn);
            add(cancelBtn);

            confirmBtn.addActionListener(e -> {
                String oldPwd = new String(oldPwdField.getPassword()).trim();
                String newPwd = new String(newPwdField.getPassword()).trim();
                String confirmPwd = new String(confirmPwdField.getPassword()).trim();

                if (!oldPwd.equals(loginUser.getPassword())) {
                    JOptionPane.showMessageDialog(this, "原密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (newPwd.length() < 6) {
                    JOptionPane.showMessageDialog(this, "新密码至少6位！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!newPwd.equals(confirmPwd)) {
                    JOptionPane.showMessageDialog(this, "两次密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = new UserService().changePassword(loginUser.getUsername(), newPwd);
                if (success) {
                    loginUser.setPassword(newPwd);
                    JOptionPane.showMessageDialog(this, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "密码修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dispose());
        }

        private void setPadding(int padding) {
            ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        }
    }

    // 系统设置子窗口（保持不变）
    class SystemManageFrame extends JFrame {
        public SystemManageFrame() {
            setTitle("系统设置");
            setSize(400, 300);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            add(new JLabel("系统设置功能区域", SwingConstants.CENTER));
        }
    }

}