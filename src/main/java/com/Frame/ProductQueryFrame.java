package com.Frame;

import com.Entity.Product;
import com.Model.ProductTableModel;
import com.Service.ProductService;
import com.Service.SupplierService;
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
    private List<Integer> existSupplierIds = SupplierService.findAllSupplierId();
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
        // 查询页面：2行6列，标签和输入框一一对应
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
            // 1. 处理查询参数：空输入则设为null，不参与查询
            Integer proId = parseInteger(tfProId.getText().trim());
            String proName = tfProName.getText().trim().isEmpty() ? null : tfProName.getText().trim();
            Double price = parseDouble(tfPrice.getText().trim());
            String type = tfType.getText().trim().isEmpty() ? null : tfType.getText().trim();
            Integer quantity = parseInteger(tfQuantity.getText().trim());
            Integer supId = parseInteger(tfSupId.getText().trim()); // 供应商编号改为Integer（原String可能不匹配Service）

            // 2. 调用Service查询：所有null参数会被Service忽略
            List<Product> resultList;
            // 优化：如果所有条件都为空，查询所有数据（和重置功能一致）
            if (proId == null && proName == null && price == null && type == null && quantity == null && supId == null) {
                resultList = ProductService.findAllproducts();
            } else {
                // 修复：传参改为（proId, 名称, 价格, 类别, 库存量, 供应商ID），不再重复传price
                resultList = productService.queryProducts(proId, proName, price, type, quantity, supId);
            }

            // 3. 更新表格并提示结果
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

    /**
     * 辅助方法：字符串转Integer，空串或非数字返回null（不抛异常）
     */
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

    /**
     * 辅助方法：字符串转Double，空串或非数字返回null（不抛异常）
     */
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
        fileChooser.setSelectedFile(new File("产品数据_" + System.currentTimeMillis() + ".csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
                bw.write("产品编号,产品名称,价格,类别,库存量,供应商编号");
                bw.newLine();

                List<Product> productList = tableModel.getProducts();
                for (Product product : productList) {
                    int prodId = product.getProd_id() == 0 ? 0 : product.getProd_id();
                    String prodName = product.getProd_name() == null ? "" : product.getProd_name();
                    double price = product.getPrice() < 0 ? 0.0 : product.getPrice();
                    String type = product.getType() == null ? "" : product.getType();
                    double quantity = product.getQuantity() < 0 ? 0.0 : product.getQuantity();
                    int supId = product.getSup_id() == 0 ? 0 : product.getSup_id();

                    bw.write(String.format("%d,%s,%.2f,%s,%.0f,%d",
                            prodId, prodName, price, type, quantity, supId));
                    bw.newLine();
                }
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

    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导入产品数据");
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
            int successCount = 0;
            int failCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(importFile))) {
                String line;
                br.readLine(); // 跳过表头

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        failCount++;
                        continue;
                    }

                    String[] fields = line.split(",");
                    if (fields.length != 6) {
                        log.warning("导入失败：行数据列数错误 → " + line);
                        failCount++;
                        continue;
                    }

                    try {
                        Product product = new Product();
                        int prodId = Integer.parseInt(fields[0].trim());
                        if (prodId <= 0) {
                            failCount++;
                            continue;
                        }
                        product.setProd_id(prodId);

                        String prodName = fields[1].trim();
                        if (prodName.isEmpty()) {
                            failCount++;
                            continue;
                        }
                        product.setProd_name(prodName);

                        double price = Double.parseDouble(fields[2].trim());
                        if (price < 0) {
                            failCount++;
                            continue;
                        }
                        product.setPrice(price);

                        String type = fields[3].trim();
                        if (type.isEmpty()) {
                            failCount++;
                            continue;
                        }
                        product.setType(type);

                        double quantity = Double.parseDouble(fields[4].trim());
                        if (quantity < 0) {
                            failCount++;
                            continue;
                        }
                        product.setQuantity(quantity);

                        int supId = Integer.parseInt(fields[5].trim());
                        if (supId <= 0) {
                            failCount++;
                            continue;
                        }
                        product.setSup_id(supId);

                        if (productService.addProduct(product)) {
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