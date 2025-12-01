package com.Frame;

import jdk.jfr.Label;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Log
public class ProductView extends JFrame{
    private JTextField tfProId;
    private JTextField tfProName;
    private JTextField tfPrice;
    private JTextField tfType;
    private JTextField tfQuantity;
    private JTextField tfSupId;

    private ProductTableModel tableModel;
    private JTable productTable;

    // 供应商编号列表（放空，不实现数据库查询）
    private final List<Integer> existSupplierIds = new ArrayList<>();

/**
 * 产品视图类的构造方法
 * 用于初始化产品查询界面
 */
    public ProductView() {
        // 初始化用户界面组件
        initUI();
        // 设置窗口标题为"产品管理系统 - 产品查询"
        setTitle("产品管理系统 - 产品查询");
        // 设置窗口大小为1000x600像素
        setSize(1000, 600);
        // 设置窗口关闭时仅关闭当前窗口而不退出应用程序
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 将窗口居中显示
        setLocationRelativeTo(null);
    }

    private void initUI() {

        //查询页面
        JPanel queryPanel = new JPanel(new GridLayout(3,4,5,5));
        queryPanel.add(new JLabel("产品编号"));
        queryPanel.add(new JLabel("产品名称"));
        queryPanel.add(new JLabel("价格区间（起/止）"));
        queryPanel.add(new JLabel("类别"));
        queryPanel.add(new JLabel("库存量区间（起/止）"));
        queryPanel.add(new JLabel("供应商编号"));
        queryPanel.add(new JLabel(""));

        tfProId = new JTextField();
        tfProName = new JTextField();
        tfPrice = new JTextField();
        tfType = new JTextField();
        tfQuantity = new JTextField();
        tfSupId = new JTextField();
        JButton btnQuery = new JButton("查询");
        JButton btnReset = new JButton("重置");

        queryPanel.add(tfProId);
        queryPanel.add(tfProName);
        queryPanel.add(tfPrice);
        queryPanel.add(tfType);
        queryPanel.add(tfQuantity);
        queryPanel.add(tfSupId);
        queryPanel.add(btnQuery);
        queryPanel.add(btnReset);

        //功能按钮页面
        JPanel funcPanel = new JPanel();
        JButton btnExport = new JButton("导出");
        JButton btnImport = new JButton("导入");
        funcPanel.add(btnExport);
        funcPanel.add(btnImport);

        //表格页面
        tableModel = new ProductTableModel();
        productTable = new JTable(tableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableScroll = new JScrollPane(productTable);

        //将三个页面添加到主面板中
        this.setLayout(new BorderLayout(5, 5));
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(funcPanel, BorderLayout.CENTER);
        this.add(tableScroll, BorderLayout.SOUTH);


    }
}
