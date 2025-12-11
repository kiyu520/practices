package com.Frame;

import com.Entity.Product;
import com.Model.ProductTableModel;
import com.Service.ProductService;
import com.Service.SupplierService;
import com.Tools.CSVUtil;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

@Log
public class ProductQueryFrame extends JFrame {
    private JTextField tfProId;
    private JTextField tfProName;
    private JTextField tfPrice;
    private JTextField tfType;
    private JTextField tfQuantity;
    private JTextField tfSupId;

    private ProductTableModel tableModel;
    private JTable productTable;
    private ProductService productService;

    public ProductQueryFrame() {
        this.productService = new ProductService();
        initUI();
        setTitle("产品管理系统 - 产品查询");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel queryPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        queryPanel.add(new JLabel("产品编号"));
        queryPanel.add(new JLabel("产品名称"));
        queryPanel.add(new JLabel("价格"));
        queryPanel.add(new JLabel("类别"));
        queryPanel.add(new JLabel("库存量"));
        queryPanel.add(new JLabel("供应商编号"));

        tfProId = new JTextField();
        tfProName = new JTextField();
        tfPrice = new JTextField();
        tfType = new JTextField();
        tfQuantity = new JTextField();
        tfSupId = new JTextField();

        queryPanel.add(tfProId);
        queryPanel.add(tfProName);
        queryPanel.add(tfPrice);
        queryPanel.add(tfType);
        queryPanel.add(tfQuantity);
        queryPanel.add(tfSupId);

        // 功能按钮面板
        JPanel funcPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnQuery = new JButton("查询");
        JButton btnReset = new JButton("重置");
        JButton btnExport = new JButton("导出");
        JButton btnImport = new JButton("导入");
        funcPanel.add(btnQuery);
        funcPanel.add(btnReset);
        funcPanel.add(btnExport);
        funcPanel.add(btnImport);

        // 表格初始化
        tableModel = new ProductTableModel();
        productTable = new JTable(tableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableScroll = new JScrollPane(productTable);

        // 主布局
        this.setLayout(new BorderLayout(5, 10));
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(funcPanel, BorderLayout.CENTER);
        this.add(tableScroll, BorderLayout.SOUTH);

        // 事件绑定
        btnQuery.addActionListener(e -> doQuery());
        btnReset.addActionListener(e -> resetQuery());
        btnExport.addActionListener(e -> exportData());
        btnImport.addActionListener(e -> importData());

        // 初始化加载所有数据
        loadAllProducts();
    }

    /**
     * 修复后的多条件查询：支持任意条件组合，空输入忽略该条件
     */
    private void doQuery() {
        try {
            Integer proId = parseInteger(tfProId.getText().trim());
            String proName = tfProName.getText().trim().isEmpty() ? null : tfProName.getText().trim();
            Double price = parseDouble(tfPrice.getText().trim());
            String type = tfType.getText().trim().isEmpty() ? null : tfType.getText().trim();
            Integer quantity = parseInteger(tfQuantity.getText().trim());
            Integer supId = parseInteger(tfSupId.getText().trim()); // 供应商编号改为Integer（原String可能不匹配Service）

            // 2. 调用Service查询
            List<Product> resultList;
            if (proId == null && proName == null && price == null && type == null && quantity == null && supId == null) {
                resultList = ProductService.findAllproducts();
            } else {
                resultList = productService.queryProducts(proId, proName, price, type, quantity, supId);
            }

            tableModel.setProducts(resultList);
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this,
                    "查询到" + resultList.size() + "条匹配数据",
                    "查询成功", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            log.severe("查询参数格式错误：" + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "产品编号/价格/库存量/供应商编号必须为数字（空则忽略）！",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            log.severe("查询商品失败：" + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "查询失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer parseInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("'" + text + "' 不是有效的整数");
        }
    }

    private Double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("'" + text + "' 不是有效的数字");
        }
    }

    private void loadAllProducts() {
        try {
            List<Product> productList = ProductService.findAllproducts();
            tableModel.setProducts(productList);
            tableModel.fireTableDataChanged();
        } catch (Exception e) {
            log.severe("加载所有产品失败" + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetQuery() {
        tfProId.setText("");
        tfProName.setText("");
        tfPrice.setText("");
        tfType.setText("");
        tfQuantity.setText("");
        tfSupId.setText("");
        loadAllProducts();
        JOptionPane.showMessageDialog(this, "查询条件已重置", "重置成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出产品数据");
        // 设置默认文件名
        String defaultFileName = "产品数据_" + System.currentTimeMillis() + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            // 截取文件名（去掉后缀，因为CSVUtil会自动添加.csv）
            String fileName = saveFile.getName().replace(".csv", "");
            // 调用CSVUtil导出
            boolean isSuccess = CSVUtil.CSV_out(saveFile, tableModel.getProducts());
            if (isSuccess) {
                JOptionPane.showMessageDialog(this,
                        "数据导出成功！\n文件路径：Output_CSV/" + fileName + ".csv",
                        "导出成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "导出失败：数据为空或格式不支持",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导入产品数据");
        // 过滤CSV文件
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV文件 (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            // 调用CSVUtil读取数据
            List<Object> importList = CSVUtil.CSV_in(importFile);
            if (importList == null || importList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "导入失败：文件为空或格式不匹配",
                        "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 批量导入到数据库并更新表格
            int successCount = 0;
            int failCount = 0;
            for (Object obj : importList) {
                if (obj instanceof Product product) {
                    try {
                        // 调用Service添加产品（需包含重复编号校验）
                        if (productService.addProduct(product)) {
                            tableModel.addProduct(product);
                            successCount++;
                        } else {
                            failCount++;
                            log.warning("导入失败：产品编号重复 → " + product.getProd_id());
                        }
                    } catch (Exception e) {
                        failCount++;
                        log.warning("导入失败：数据异常 → " + e.getMessage());
                    }
                } else {
                    failCount++;
                    log.warning("导入失败：非产品类型数据");
                }
            }

            // 刷新表格
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this,
                    String.format("导入完成！\n成功：%d 条\n失败：%d 条", successCount, failCount),
                    "导入结果", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}