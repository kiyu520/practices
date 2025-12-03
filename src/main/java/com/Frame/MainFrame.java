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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 仓库管理系统主窗口（适配现有项目结构）
 */
public class MainFrame extends JFrame {
    private User loginUser; // 当前登录用户
    private JLabel timeLabel; // 实时时钟标签

    // 业务层对象（对接数据库操作）
    private ProductService productService = new ProductService();
    private SupplierService supplierService = new SupplierService();

    public MainFrame(User user) {
        this.loginUser = user;
        initFrame();       // 初始化窗口
        initMenuBar();     // 初始化菜单栏
        initTimeLabel();   // 初始化实时时钟
    }

    public MainFrame() {

    }

    /**
     * 窗口基础配置
     */
    private void initFrame() {
        setTitle("仓库管理系统 - 欢迎您，" + loginUser.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中
        setLayout(new BorderLayout()); // 改用BorderLayout（更适配子组件）
    }

    /**
     * 初始化菜单栏
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 1. 基本数据菜单
        JMenu dataMenu = new JMenu("基本数据");
        JMenuItem productItem = new JMenuItem("商品信息管理");
        JMenuItem supplierItem = new JMenuItem("供应商信息管理");
        dataMenu.add(productItem);
        dataMenu.add(supplierItem);

        // 2. 进货出货管理菜单
        JMenu stockMenu = new JMenu("进货出货管理");
        JMenuItem stockInItem = new JMenuItem("商品进货");
        JMenuItem stockOutItem = new JMenuItem("商品出货");
        stockMenu.add(stockInItem);
        stockMenu.add(stockOutItem);

        // 3. 查询视图菜单
        JMenu queryMenu = new JMenu("查询视图");
        JMenuItem productQueryItem = new JMenuItem("产品查询");
        JMenuItem supplierQueryItem = new JMenuItem("供应商查询");
        queryMenu.add(productQueryItem);
        queryMenu.add(supplierQueryItem);

        // 4. 系统管理菜单
        JMenu systemMenu = new JMenu("系统管理");
        JMenuItem changePwdItem = new JMenuItem("更改密码");
        JMenuItem settingItem = new JMenuItem("系统设置");
        JMenuItem exitItem = new JMenuItem("退出系统");
        systemMenu.add(changePwdItem);
        systemMenu.add(settingItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);

        // 菜单添加到菜单栏
        menuBar.add(dataMenu);
        menuBar.add(stockMenu);
        menuBar.add(queryMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        // ========== 菜单事件绑定（对接现有子窗口） ==========
        // 商品信息管理
        productItem.addActionListener(e -> new ProductManageFrame().setVisible(true));
        // 供应商信息管理
        supplierItem.addActionListener(e -> new SupplierManageFrame().setVisible(true));
        // 商品进货
        stockInItem.addActionListener(e -> new StockInFrame().setVisible(true));
        // 商品出货
        stockOutItem.addActionListener(e -> new StockOutFrame().setVisible(true));
        // 产品查询
        productQueryItem.addActionListener(e -> new ProductQueryFrame().setVisible(true));
        // 供应商查询
        supplierQueryItem.addActionListener(e -> new SupplierQueryFrame().setVisible(true));
        // 更改密码
        changePwdItem.addActionListener(e -> new ChangePasswordFrame(loginUser).setVisible(true));
        // 系统设置
        settingItem.addActionListener(e -> new SystemManageFrame().setVisible(true));
        // 退出系统
        exitItem.addActionListener(e -> System.exit(0));
    }

    /**
     * 初始化实时时钟（右下角）
     */
    private void initTimeLabel() {
        timeLabel = new JLabel(DateUtil.getCurrentDateTime());
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        // 添加到窗口底部
        add(timeLabel, BorderLayout.SOUTH);

        // 启动时钟线程（每秒更新）
        new Thread(() -> {
            while (true) {
                SwingUtilities.invokeLater(() -> timeLabel.setText(DateUtil.getCurrentDateTime()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ========== 子窗口：商品进货 ==========
    class StockInFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private JTextField supplierIdField; // 新增：供应商ID

        public StockInFrame() {
            setTitle("商品进货");
            setSize(450, 350);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(4, 2, 10, 20)); // 网格布局更整洁
            setPadding(20); // 窗口内边距

            // 组件
            add(new JLabel("商品ID:"));
            prodIdField = new JTextField();
            add(prodIdField);

            add(new JLabel("进货数量:"));
            quantityField = new JTextField();
            add(quantityField);

            add(new JLabel("供应商ID:"));
            supplierIdField = new JTextField();
            add(supplierIdField);

            // 按钮
            JButton confirmBtn = new JButton("确认进货");
            JButton clearBtn = new JButton("清空");
            add(confirmBtn);
            add(clearBtn);

            // 进货事件
            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    int supplierId = Integer.parseInt(supplierIdField.getText().trim());

                    // 调用ProductService的进货方法（对接数据库）
                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "进货失败（商品/供应商不存在或数量非法）", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            // 清空事件
            clearBtn.addActionListener(e -> clearFields());
        }

        private void clearFields() {
            prodIdField.setText("");
            quantityField.setText("");
            supplierIdField.setText("");
        }

        // 窗口内边距设置
        private void setPadding(int padding) {
            ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        }
    }

    // ========== 子窗口：商品出货 ==========
    class StockOutFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private JTextField customerField; // 新增：客户信息

        public StockOutFrame() {
            setTitle("商品出货");
            setSize(450, 350);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(4, 2, 10, 20));
            setPadding(20);

            // 组件
            add(new JLabel("商品ID:"));
            prodIdField = new JTextField();
            add(prodIdField);

            add(new JLabel("出货数量:"));
            quantityField = new JTextField();
            add(quantityField);

            add(new JLabel("客户信息:"));
            customerField = new JTextField();
            add(customerField);

            // 按钮
            JButton confirmBtn = new JButton("确认出货");
            JButton clearBtn = new JButton("清空");
            add(confirmBtn);
            add(clearBtn);

            // 出货事件
            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());
                    String customer = customerField.getText().trim();

                    // 调用ProductService的出货方法（对接数据库）
                    boolean success = productService.stockOut(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "出货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "出货失败（商品不存在/库存不足）", "错误", JOptionPane.ERROR_MESSAGE);
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

    // ========== 子窗口：产品查询（适配ProductTableModel） ==========
    class ProductQueryFrame extends JFrame {
        private JTable productTable;
        private ProductTableModel tableModel;

        public ProductQueryFrame() {
            setTitle("产品查询");
            setSize(800, 500);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // 1. 获取商品列表（对接数据库）
            List<Product> productList = productService.findAllproducts();
            // 2. 初始化表格模型（使用现有ProductTableModel）
            tableModel = new ProductTableModel(productList);
            productTable = new JTable(tableModel);

            // 表格样式优化
            JTableHeader header = productTable.getTableHeader();
            header.setFont(new Font("宋体", Font.BOLD, 14));
            productTable.setFont(new Font("宋体", Font.PLAIN, 13));
            productTable.setRowHeight(25);

            // 3. 添加滚动面板
            JScrollPane scrollPane = new JScrollPane(productTable);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    // ========== 子窗口：供应商查询 ==========
    class SupplierQueryFrame extends JFrame {
        private JTable supplierTable;

        public SupplierQueryFrame() {
            setTitle("供应商查询");
            setSize(800, 500);
            setLocationRelativeTo(MainFrame.this);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // 获取供应商列表（对接数据库）
            List<Supplier> supplierList = supplierService.findAllSupplier();
            // 构建表格模型（可复用类似ProductTableModel的SupplierTableModel）
            String[] columns = {"供应商ID", "名称", "联系人", "电话", "地址"};
            Object[][] data = new Object[supplierList.size()][5];
            for (int i = 0; i < supplierList.size(); i++) {
                Supplier s = supplierList.get(i);
                data[i][0] = s.getExesConId();      // 供应商ID（主键）
                data[i][1] = s.getSupName();        // 供应商名称
                data[i][2] = s.getSupAddress();     // 地址
                data[i][3] = s.getPostcode();       // 邮编
                data[i][4] = s.getSupTelephone();   // 电话
                data[i][5] = s.getSupFax();         // 传真
                data[i][6] = s.getSupRelationer();  // 联系人
                data[i][7] = s.getSupEmail();       // 邮箱
            }

            supplierTable = new JTable(data, columns);
            // 表格样式
            supplierTable.getTableHeader().setFont(new Font("宋体", Font.BOLD, 14));
            supplierTable.setFont(new Font("宋体", Font.PLAIN, 13));
            supplierTable.setRowHeight(25);

            add(new JScrollPane(supplierTable), BorderLayout.CENTER);
        }
    }

    // ========== 子窗口：更改密码 ==========
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

            // 组件
            add(new JLabel("原密码:"));
            oldPwdField = new JPasswordField();
            add(oldPwdField);

            add(new JLabel("新密码:"));
            newPwdField = new JPasswordField();
            add(newPwdField);

            add(new JLabel("确认新密码:"));
            confirmPwdField = new JPasswordField();
            add(confirmPwdField);

            // 按钮
            JButton confirmBtn = new JButton("确认修改");
            JButton cancelBtn = new JButton("取消");
            add(confirmBtn);
            add(cancelBtn);

            // 修改事件
            confirmBtn.addActionListener(e -> {
                String oldPwd = new String(oldPwdField.getPassword()).trim();
                String newPwd = new String(newPwdField.getPassword()).trim();
                String confirmPwd = new String(confirmPwdField.getPassword()).trim();

                // 校验
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

                // 调用UserService修改密码（对接数据库）
                boolean success = new UserService().changePassword(loginUser.getUsername(), newPwd);
                if (success) {
                    loginUser.setPassword(newPwd); // 更新内存中用户密码
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

    // ========== 程序测试入口（实际通过LoginFrame跳转） ==========
    public static void main(String[] args) {
        // 模拟登录用户（实际从登录窗口传入）
        User testUser = new User(1, "admin", "123456", "管理员");
        SwingUtilities.invokeLater(() -> new MainFrame(testUser).setVisible(true));
    }
}