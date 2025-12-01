package com.Frame;

import com.Entity.User;
import com.Tools.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private User loginUser; // 当前登录用户
    private JLabel timeLabel; // 时钟标签

/**
 * 主窗口构造函数，用于创建仓库管理系统的主界面
 * @param user 登录系统的用户对象
 */
    public MainFrame(User user) {
    // 初始化登录用户
        this.loginUser = user;

        // 窗口配置
        setTitle("仓库管理系统 - 欢迎您，" + user.getuName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 菜单条
        JMenuBar menuBar = new JMenuBar();

        // 基本数据菜单
        JMenu dataMenu = new JMenu("基本数据");
        JMenuItem productItem = new JMenuItem("商品信息管理");
        JMenuItem supplierItem = new JMenuItem("供应商信息管理");
        dataMenu.add(productItem);
        dataMenu.add(supplierItem);

        // 进货出货管理菜单
        JMenu stockMenu = new JMenu("进货出货管理");
        JMenuItem stockInItem = new JMenuItem("商品进货");
        JMenuItem stockOutItem = new JMenuItem("商品出货");
        stockMenu.add(stockInItem);
        stockMenu.add(stockOutItem);

        // 查询视图菜单
        JMenu queryMenu = new JMenu("查询视图");
        JMenuItem productQueryItem = new JMenuItem("产品查询");
        JMenuItem supplierQueryItem = new JMenuItem("供应商查询");
        queryMenu.add(productQueryItem);
        queryMenu.add(supplierQueryItem);

        // 系统管理菜单
        JMenu systemMenu = new JMenu("系统管理");
        JMenuItem changePwdItem = new JMenuItem("更改密码");
        JMenuItem settingItem = new JMenuItem("系统设置");
        JMenuItem exitItem = new JMenuItem("退出系统");
        systemMenu.add(changePwdItem);
        systemMenu.add(settingItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);

        // 添加菜单到菜单条
        menuBar.add(dataMenu);
        menuBar.add(stockMenu);
        menuBar.add(queryMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        // 时钟标签（右下角）
        timeLabel = new JLabel(DateUtil.getCurrentDateTime());
        timeLabel.setBounds(800, 620, 200, 30);
        add(timeLabel);

        // 启动时钟线程（实时更新时间）
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            timeLabel.setText(DateUtil.getCurrentDateTime());
                        }
                    });
                    try {
                        Thread.sleep(1000); // 每秒更新一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 菜单事件绑定
        productItem.addActionListener(e -> new ProductManageFrame().setVisible(true));
        supplierItem.addActionListener(e -> new SupplierManageFrame().setVisible(true));
        stockInItem.addActionListener(e -> new StockInFrame().setVisible(true));
        stockOutItem.addActionListener(e -> new StockOutFrame().setVisible(true));
        productQueryItem.addActionListener(e -> new ProductQueryFrame().setVisible(true));
        supplierQueryItem.addActionListener(e -> new SupplierQueryFrame().setVisible(true));
        changePwdItem.addActionListener(e -> new ChangePasswordFrame(loginUser).setVisible(true));
        settingItem.addActionListener(e -> new SystemSettingFrame().setVisible(true));
        exitItem.addActionListener(e -> System.exit(0));
    }

    // 进货窗口（内部类）
    class StockInFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private ProductService productService = new ProductService();

        public StockInFrame() {
            setTitle("商品进货");
            setSize(400, 300);
            setLocationRelativeTo(MainFrame.this);
            setLayout(null);

            JLabel prodIdLabel = new JLabel("商品ID:");
            prodIdLabel.setBounds(80, 80, 60, 25);
            prodIdField = new JTextField();
            prodIdField.setBounds(140, 80, 180, 25);

            JLabel quantityLabel = new JLabel("进货量:");
            quantityLabel.setBounds(80, 130, 60, 25);
            quantityField = new JTextField();
            quantityField.setBounds(140, 130, 180, 25);

            JButton confirmBtn = new JButton("进货");
            confirmBtn.setBounds(100, 180, 80, 30);
            JButton clearBtn = new JButton("清空");
            clearBtn.setBounds(220, 180, 80, 30);

            add(prodIdLabel);
            add(prodIdField);
            add(quantityLabel);
            add(quantityField);
            add(confirmBtn);
            add(clearBtn);

            // 进货按钮事件
            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());

                    boolean success = productService.stockIn(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "进货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "进货失败（产品不存在或数量不合法）！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            // 清空按钮事件
            clearBtn.addActionListener(e -> clearFields());
        }

        private void clearFields() {
            prodIdField.setText("");
            quantityField.setText("");
        }
    }

    // 出货窗口（内部类，与进货窗口类似）
    class StockOutFrame extends JFrame {
        private JTextField prodIdField;
        private JTextField quantityField;
        private ProductService productService = new ProductService();

        public StockOutFrame() {
            setTitle("商品出货");
            setSize(400, 300);
            setLocationRelativeTo(MainFrame.this);
            setLayout(null);

            JLabel prodIdLabel = new JLabel("商品ID:");
            prodIdLabel.setBounds(80, 80, 60, 25);
            prodIdField = new JTextField();
            prodIdField.setBounds(140, 80, 180, 25);

            JLabel quantityLabel = new JLabel("出货量:");
            quantityLabel.setBounds(80, 130, 60, 25);
            quantityField = new JTextField();
            quantityField.setBounds(140, 130, 180, 25);

            JButton confirmBtn = new JButton("出货");
            confirmBtn.setBounds(100, 180, 80, 30);
            JButton clearBtn = new JButton("清空");
            clearBtn.setBounds(220, 180, 80, 30);

            add(prodIdLabel);
            add(prodIdField);
            add(quantityLabel);
            add(quantityField);
            add(confirmBtn);
            add(clearBtn);

            // 出货按钮事件
            confirmBtn.addActionListener(e -> {
                try {
                    int prodId = Integer.parseInt(prodIdField.getText().trim());
                    float quantity = Float.parseFloat(quantityField.getText().trim());

                    boolean success = productService.stockOut(prodId, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "出货成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "出货失败（产品不存在、库存不足或数量不合法）！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "请输入合法的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            clearBtn.addActionListener(e -> clearFields());
        }

        private void clearFields() {
            prodIdField.setText("");
            quantityField.setText("");
        }
    }
}