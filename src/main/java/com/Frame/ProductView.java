package com.Frame;

import com.Entity.Product;
import com.Model.ProductTableModel;
import com.Service.ProductService;
import com.Service.SupplierService;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.io.*;

@Log
public class ProductView extends JFrame {
    private JTextField tfProId;
    private JTextField tfProName;
    private JTextField tfPrice;
    private JTextField tfType;
    private JTextField tfQuantity;
    private JTextField tfSupId;

    // 私有成员变量，用于存储产品表格模型对象
    private ProductTableModel tableModel;
    private JTable productTable;

    // 供应商id列表
    private List existSupplierIds = (List) SupplierService.findAllSupplierId();
    private ProductService productService;

    /**
     * 产品视图类的构造方法
     * 用于初始化产品查询界面
     */
    public ProductView() {
        this.productService = new ProductService();
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
        JPanel queryPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        queryPanel.add(new JLabel("产品编号"));
        queryPanel.add(new JLabel("产品名称"));
        queryPanel.add(new JLabel("价格"));
        queryPanel.add(new JLabel("类别"));
        queryPanel.add(new JLabel("库存量"));
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
        tableModel = new ProductTableModel(); // 实例化“数据管家”
        productTable = new JTable(tableModel); // 表格绑定数据模型（关键！表格从此有了数据来源）
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // 列宽自适应窗口
        JScrollPane tableScroll = new JScrollPane(productTable); // 给表格加滚动条，适配大量数据
        //将三个页面添加到主面板中
        this.setLayout(new BorderLayout(5, 5)); // 设置窗口布局，组件间距5px
        this.add(queryPanel, BorderLayout.NORTH); // 查询面板放顶部
        this.add(funcPanel, BorderLayout.CENTER); // 功能按钮放中间
        this.add(tableScroll, BorderLayout.SOUTH); // 表格放底部

        btnQuery.addActionListener(e -> doQuery());
        btnReset.addActionListener(e -> resetQuery());//重置查询的函数
        btnExport.addActionListener(e -> exportData());//执行导出数据的函数
        btnImport.addActionListener(e -> importData());//执行导入数据的函数
    }

    /*
     * 多条件查询*/
    private void doQuery() {
        try {
//          1.获取输入框参数
            Integer proId = Integer.parseInt(tfProId.getText());
            String name = tfProName.getText();
            Double price = Double.parseDouble(tfPrice.getText());
            String type = tfType.getText();
            Integer quantity = Integer.parseInt(tfQuantity.getText());
            String supId = tfSupId.getText();
//          2.调用Service查询
            java.util.List<Product> resultList;
            if (proId == null && name == null && price == null && type == null && supId == null) {
                resultList = ProductService.findAllproducts();
            } else {
                resultList = productService.queryProducts(proId, name, price, price, type, supId);
            }
//          3.更新表格数据
            tableModel.setProducts(resultList);
            tableModel.fireTableDataChanged();
//          4.提示查询结果
            JOptionPane.showMessageDialog(this, "查询到" + resultList.size() + "条数据", "查询成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            log.severe("查询参数格式错误：" + e.getMessage());
            JOptionPane.showMessageDialog(this, "产品编号/价格/供应商编号必须为数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            log.severe("查询商品失败：" + e.getMessage());
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * 加载所有数据到表格*/
    private void loadAllProducts() {
        try {
            java.util.List<Product> productList = ProductService.findAllproducts();
            tableModel.setProducts(productList);
            tableModel.fireTableDataChanged();
        } catch (Exception e) {
            log.severe("加载所有产品失败" + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetQuery() {
        //清空所有输入框
        tfProId.setText("");
        tfProName.setText("");
        tfPrice.setText("");
        tfType.setText("");
        tfQuantity.setText("");
        tfSupId.setText("");
//        重置后重新加载所有数据
        loadAllProducts();
        JOptionPane.showMessageDialog(this, "查询条件已重置", "重置密码", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出产品数据");
        fileChooser.setSelectedFile(new File("产品数据_" + System.currentTimeMillis() + ".csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
                // 1. 写入CSV表头（和表格列名一致）
                bw.write("产品编号,产品名称,价格,类别,库存量,供应商编号");
                bw.newLine();

                // 2. 获取表格模型中的产品数据（适配你的getProducts()方法）
                java.util.List<Product> productList = tableModel.getProducts();
                // 3. 逐行写入产品数据
                for (Product product : productList) {
                    // 处理空值（避免NullPointerException）
                    int prodId = product.getProd_id() == 0 ? 0 : product.getProd_id();
                    String prodName = product.getProd_name() == null ? "" : product.getProd_name();
                    double price = product.getPrice() < 0 ? 0.0 : product.getPrice();
                    String type = product.getType() == null ? "" : product.getType();
                    double quantity = product.getQuantity() < 0 ? 0.0 : product.getQuantity();
                    int supId = product.getSup_id() == 0 ? 0 : product.getSup_id();

                    // 格式化写入（价格保留2位小数，库存量取整）
                    bw.write(String.format("%d,%s,%.2f,%s,%.0f,%d",
                            prodId, prodName, price, type, quantity, supId));
                    bw.newLine();
                }
                // 导出成功提示
                JOptionPane.showMessageDialog(this,
                        "数据导出成功：\n" + saveFile.getAbsolutePath(),
                        "导出成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                log.severe("导出数据失败：" + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "导出失败：" + e.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void importData(){
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("导入产品数据");
        // 过滤仅显示CSV文件
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
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            // 统计导入结果
            int successCount = 0;
            int failCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(importFile))) {
                String line;
                br.readLine(); // 跳过CSV表头行

                // 逐行读取并解析数据
                while ((line = br.readLine()) != null) {
                    // 跳过空行
                    if (line.trim().isEmpty()) {
                        failCount++;
                        continue;
                    }

                    String[] fields = line.split(",");
                    // 校验列数（必须6列，否则格式错误）
                    if (fields.length != 6) {
                        log.warning("导入失败：行数据列数错误 → " + line);
                        failCount++;
                        continue;
                    }

                    try {
                        // 解析CSV字段为Product对象
                        Product product = new Product();
                        // 产品编号（必填，非负）
                        int prodId = Integer.parseInt(fields[0].trim());
                        if (prodId <= 0) {
                            failCount++;
                            continue;
                        }
                        product.setProd_id(prodId);

                        // 产品名称（必填，非空）
                        String prodName = fields[1].trim();
                        if (prodName.isEmpty()) {
                            failCount++;
                            continue;
                        }
                        product.setProd_name(prodName);

                        // 价格（非负）
                        double price = Double.parseDouble(fields[2].trim());
                        if (price < 0) {
                            failCount++;
                            continue;
                        }
                        product.setPrice(price);

                        // 类别（非空）
                        String type = fields[3].trim();
                        if (type.isEmpty()) {
                            failCount++;
                            continue;
                        }
                        product.setType(type);

                        // 库存量（非负）
                        double quantity = Double.parseDouble(fields[4].trim());
                        if (quantity < 0) {
                            failCount++;
                            continue;
                        }
                        product.setQuantity(quantity);

                        // 供应商编号（非负）
                        int supId = Integer.parseInt(fields[5].trim());
                        if (supId <= 0) {
                            failCount++;
                            continue;
                        }
                        product.setSup_id(supId);

                        // 调用Service添加产品（校验唯一性+合法性）
                        if (productService.addProduct(product)) {
                            // 添加成功：更新表格模型
                            tableModel.addProduct(product);
                            successCount++;
                        } else {
                            log.warning("导入失败：产品编号重复/参数不合法 → " + prodId);
                            failCount++;
                        }
                    } catch (NumberFormatException e) {
                        log.warning("导入失败：数据格式错误 → " + line + "，原因：" + e.getMessage());
                        failCount++;
                    } catch (Exception e) {
                        log.warning("导入失败：行数据异常 → " + line + "，原因：" + e.getMessage());
                        failCount++;
                    }
                }

                // 导入完成提示
                JOptionPane.showMessageDialog(this,
                        String.format("导入完成！\n成功：%d 条\n失败：%d 条", successCount, failCount),
                        "导入结果", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                log.severe("导入数据失败：文件读取异常 → " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "导入失败：无法读取文件\n" + e.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
