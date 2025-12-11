package com.Frame;

import com.Entity.Supplier;
import com.Model.SupplierTableModel;
import com.Service.SupplierService;
import com.Tools.CSVUtil;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.List;

@Log
public class SupplierQueryFrame extends JFrame {
    //替换为供应商查询输入框
    private JTextField tfSupId;      // 供应商ID
    private JTextField tfSupName;    // 供应商名称
    private JTextField tfAddress;    // 地址// 传真
    private JTextField tfContact;    // 联系人

    //替换为供应商表格模型
    private SupplierTableModel tableModel;
    private JTable supplierTable;
    private SupplierService supplierService;

    public SupplierQueryFrame() {
        this.supplierService = new SupplierService();
        initUI();
        setTitle("产品管理系统 - 供应商信息查询");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        //查询面板：改为供应商字段
        JPanel queryPanel = new JPanel(new GridLayout(2, 8, 5, 5));
        // 第一行：标签
        queryPanel.add(new JLabel("供应商ID"));
        queryPanel.add(new JLabel("供应商名称"));
        queryPanel.add(new JLabel("地址"));
        queryPanel.add(new JLabel("联系人"));
        // 第二行：输入框
        tfSupId = new JTextField();
        tfSupName = new JTextField();
        tfAddress = new JTextField();
        queryPanel.add(tfSupId);
        queryPanel.add(tfSupName);
        queryPanel.add(tfAddress);
        tfContact = new JTextField();
        queryPanel.add(tfContact);

        //功能按钮面板
        JPanel funcPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnQuery = new JButton("查询");
        JButton btnReset = new JButton("重置");
        JButton btnExport = new JButton("导出");
        JButton btnImport = new JButton("导入");
        funcPanel.add(btnQuery);
        funcPanel.add(btnReset);
        funcPanel.add(btnExport);
        funcPanel.add(btnImport);

        //供应商表格初始化
        tableModel = new SupplierTableModel();
        supplierTable = new JTable(tableModel);
        supplierTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < supplierTable.getColumnCount(); i++) {
            supplierTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane tableScroll = new JScrollPane(supplierTable);

        //主布局
        this.setLayout(new BorderLayout(5, 10));
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(funcPanel, BorderLayout.CENTER);
        this.add(tableScroll, BorderLayout.SOUTH);

        //事件绑定
        btnQuery.addActionListener(e -> doQuery());
        btnReset.addActionListener(e -> resetQuery());
        btnExport.addActionListener(e -> exportData());
        btnImport.addActionListener(e -> importData());

        // 初始化加载所有供应商数据
        loadAllSuppliers();
    }

    /**
     *供应商多条件查询
     */
    private void doQuery() {
        try {
            // 1. 处理查询参数：空值设为null，不参与查询
            Integer supId = parseInteger(tfSupId.getText().trim());
            String supName = tfSupName.getText().trim().isEmpty() ? null : tfSupName.getText().trim();
            String address = tfAddress.getText().trim().isEmpty() ? null : tfAddress.getText().trim();
            String contact = tfContact.getText().trim().isEmpty() ? null : tfContact.getText().trim();

            // 2. 调用Service查询：所有null参数会被忽略
            List<Supplier> resultList;
            if (supId == null && supName == null && address == null&& contact == null ){
                // 无条件查询所有供应商
                resultList = SupplierService.findAllSupplier();
            } else {
                // 多条件查询
                resultList = supplierService.querySuppliers(supId, supName, address, contact);
            }

            // 3. 更新表格并提示结果
            tableModel.setSuppliers(resultList);
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this,
                    "查询到" + resultList.size() + "条匹配数据",
                    "查询成功", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            log.severe("查询参数格式错误：" + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "供应商ID必须为整数！",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            log.severe("查询供应商失败：" + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "查询失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 将字符串解析为Integer类型
     * @param text 需要解析的字符串
     * @return 解析成功返回对应的Integer值，如果字符串为null或空字符串则返回null
     * @throws NumberFormatException 当字符串不是有效的整数格式时抛出此异常
     */
    private Integer parseInteger(String text) {
        // 检查字符串是否为null或空字符串（去除前后空格后）
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            // 尝试将去除前后空格的字符串转换为整数
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            // 转换失败时抛出带有中文提示信息的异常
            throw new NumberFormatException("'" + text + "' 不是有效的整数");
        }
    }

    /**
     * 加载所有供应商数据
     */
    private void loadAllSuppliers() {
        try {
            List<Supplier> supplierList = SupplierService.findAllSupplier();
            tableModel.setSuppliers(supplierList);
            tableModel.fireTableDataChanged();
        } catch (Exception e) {
            log.severe("加载所有供应商失败：" + e.getMessage());
            JOptionPane.showMessageDialog(this, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 重置查询条件
     */
    private void resetQuery() {
        tfSupId.setText("");
        tfSupName.setText("");
        tfAddress.setText("");
        tfContact.setText("");
        loadAllSuppliers();
        JOptionPane.showMessageDialog(this, "查询条件已重置", "重置成功", JOptionPane.INFORMATION_MESSAGE);
    }
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出供应商数据");
        String defaultFileName = "供应商数据_" + System.currentTimeMillis() + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            boolean isSuccess = CSVUtil.CSV_out(saveFile, tableModel.getSuppliers());
            if (isSuccess) {
                JOptionPane.showMessageDialog(this,
                        "数据导出成功！\n文件路径：" + saveFile.getAbsolutePath(),
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
        fileChooser.setDialogTitle("导入供应商数据");
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
            List<Object> importList = CSVUtil.CSV_in(importFile);
            if (importList == null || importList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "导入失败：文件为空或格式不匹配",
                        "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int successCount = 0;
            int failCount = 0;
            for (Object obj : importList) {
                if (obj instanceof Supplier supplier) {
                    try {
                        if (supplierService.addSupplier(supplier)) {
                            tableModel.addSupplier(supplier);
                            successCount++;
                        } else {
                            failCount++;
                            log.warning("导入失败：供应商ID重复 → " + supplier.getExesConId());
                        }
                    } catch (Exception e) {
                        failCount++;
                        log.warning("导入失败：数据异常 → " + e.getMessage());
                    }
                } else {
                    failCount++;
                    log.warning("导入失败：非供应商类型数据");
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