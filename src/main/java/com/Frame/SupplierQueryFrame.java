package com.Frame;

import com.Entity.Supplier;
import com.Model.SupplierTableModel;
import com.Service.SupplierService;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.List;

@Log
public class SupplierQueryFrame extends JFrame {
    // ========== 1. 替换为供应商查询输入框 ==========
    private JTextField tfSupId;      // 供应商ID
    private JTextField tfSupName;    // 供应商名称
    private JTextField tfAddress;    // 地址
    private JTextField tfZipCode;    // 邮编
    private JTextField tfPhone;      // 电话
    private JTextField tfFax;        // 传真
    private JTextField tfContact;    // 联系人
    private JTextField tfEmail;      // 邮箱

    // ========== 2. 替换为供应商表格模型 ==========
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
        // ========== 3. 查询面板：改为供应商字段 ==========
        JPanel queryPanel = new JPanel(new GridLayout(2, 8, 5, 5));
        // 第一行：标签
        queryPanel.add(new JLabel("供应商ID"));
        queryPanel.add(new JLabel("供应商名称"));
        queryPanel.add(new JLabel("地址"));
        queryPanel.add(new JLabel("邮编"));
        queryPanel.add(new JLabel("电话"));
        queryPanel.add(new JLabel("传真"));
        queryPanel.add(new JLabel("联系人"));
        queryPanel.add(new JLabel("邮箱"));
        // 第二行：输入框
        tfSupId = new JTextField();
        tfSupName = new JTextField();
        tfAddress = new JTextField();
        tfZipCode = new JTextField();
        queryPanel.add(tfSupId);
        queryPanel.add(tfSupName);
        queryPanel.add(tfAddress);
        queryPanel.add(tfZipCode);
        tfPhone = new JTextField();
        tfFax = new JTextField();
        tfContact = new JTextField();
        tfEmail = new JTextField();
        queryPanel.add(tfPhone);
        queryPanel.add(tfFax);
        queryPanel.add(tfContact);
        queryPanel.add(tfEmail);

        // ========== 4. 功能按钮面板（保留原功能） ==========
        JPanel funcPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnQuery = new JButton("查询");
        JButton btnReset = new JButton("重置");
        JButton btnExport = new JButton("导出");
        JButton btnImport = new JButton("导入");
        funcPanel.add(btnQuery);
        funcPanel.add(btnReset);
        funcPanel.add(btnExport);
        funcPanel.add(btnImport);

        // ========== 5. 供应商表格初始化 ==========
        tableModel = new SupplierTableModel();
        supplierTable = new JTable(tableModel);
        supplierTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // 设置表格文字居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < supplierTable.getColumnCount(); i++) {
            supplierTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane tableScroll = new JScrollPane(supplierTable);

        // ========== 6. 主布局 ==========
        this.setLayout(new BorderLayout(5, 10));
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(funcPanel, BorderLayout.CENTER);
        this.add(tableScroll, BorderLayout.SOUTH);

        // ========== 7. 事件绑定 ==========
        btnQuery.addActionListener(e -> doQuery());
        btnReset.addActionListener(e -> resetQuery());
        btnExport.addActionListener(e -> exportData());
        btnImport.addActionListener(e -> importData());

        // 初始化加载所有供应商数据
        loadAllSuppliers();
    }

    /**
     * 核心：供应商多条件查询
     */
    private void doQuery() {
        try {
            // 1. 处理查询参数：空值设为null，不参与查询
            Integer supId = parseInteger(tfSupId.getText().trim());
            String supName = tfSupName.getText().trim().isEmpty() ? null : tfSupName.getText().trim();
            String address = tfAddress.getText().trim().isEmpty() ? null : tfAddress.getText().trim();
            String zipCode = tfZipCode.getText().trim().isEmpty() ? null : tfZipCode.getText().trim();
            String phone = tfPhone.getText().trim().isEmpty() ? null : tfPhone.getText().trim();
            String fax = tfFax.getText().trim().isEmpty() ? null : tfFax.getText().trim();
            String contact = tfContact.getText().trim().isEmpty() ? null : tfContact.getText().trim();
            String email = tfEmail.getText().trim().isEmpty() ? null : tfEmail.getText().trim();

            // 2. 调用Service查询：所有null参数会被忽略
            List<Supplier> resultList;
            if (supId == null && supName == null && address == null && zipCode == null
                    && phone == null && fax == null && contact == null && email == null) {
                // 无条件查询所有供应商
                resultList = SupplierService.findAllSupplier();
            } else {
                // 多条件查询（需在SupplierService中新增querySuppliers方法）
                resultList = supplierService.querySuppliers(supId, supName, address, zipCode, phone, fax, contact, email);
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
     * 辅助方法：字符串转Integer，空串返回null
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
        tfZipCode.setText("");
        tfPhone.setText("");
        tfFax.setText("");
        tfContact.setText("");
        tfEmail.setText("");
        loadAllSuppliers();
        JOptionPane.showMessageDialog(this, "查询条件已重置", "重置成功", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 导出供应商数据为CSV
     */
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出供应商数据");
        fileChooser.setSelectedFile(new File("供应商数据_" + System.currentTimeMillis() + ".csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
                // 表头：供应商字段
                bw.write("供应商ID,供应商名称,地址,邮编,电话,传真,联系人,邮箱");
                bw.newLine();

                List<Supplier> supplierList = tableModel.getSuppliers();
                for (Supplier supplier : supplierList) {
                    int supId = supplier.getExesConId() == null ? 0 : supplier.getExesConId();
                    String supName = supplier.getSupName() == null ? "" : supplier.getSupName();
                    String address = supplier.getSupAddress() == null ? "" : supplier.getSupAddress();
                    String zipCode = supplier.getPostcode() == null ? "" : supplier.getPostcode();
                    String phone = supplier.getSupTelephone() == null ? "" : supplier.getSupTelephone();
                    String fax = supplier.getSupFax() == null ? "" : supplier.getSupFax();
                    String contact = supplier.getSupRelationer() == null ? "" : supplier.getSupRelationer();
                    String email = supplier.getSupEmail() == null ? "" : supplier.getSupEmail();

                    bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                            supId, supName, address, zipCode, phone, fax, contact, email));
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

    /**
     * 从CSV导入供应商数据
     */
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
                    // 供应商数据需8个字段：ID、名称、地址、邮编、电话、传真、联系人、邮箱
                    if (fields.length != 8) {
                        log.warning("导入失败：行数据列数错误 → " + line);
                        failCount++;
                        continue;
                    }

                    try {
                        Supplier supplier = new Supplier();
                        // 1. 供应商ID
                        int supId = Integer.parseInt(fields[0].trim());
                        if (supId <= 0) {
                            failCount++;
                            continue;
                        }
                        supplier.setExesConId(supId);

                        // 2. 供应商名称
                        String supName = fields[1].trim();
                        if (supName.isEmpty()) {
                            failCount++;
                            continue;
                        }
                        supplier.setSupName(supName);

                        // 3. 其他字段
                        supplier.setSupAddress(fields[2].trim());
                        supplier.setPostcode(fields[3].trim());
                        supplier.setSupTelephone(fields[4].trim());
                        supplier.setSupFax(fields[5].trim());
                        supplier.setSupRelationer(fields[6].trim());
                        supplier.setSupEmail(fields[7].trim());

                        // 4. 调用Service添加供应商
                        boolean addResult = supplierService.addSupplier(supplier);
                        if (addResult == true) {
                            tableModel.addSupplier(supplier);
                            successCount++;
                        } else {
                            log.warning("导入失败：" + addResult + " → " + supId);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                log.warning("设置界面外观失败：" + e.getMessage());
            }

            SupplierQueryFrame queryFrame = new SupplierQueryFrame();
            queryFrame.setVisible(true);
            log.info("供应商信息查询系统已启动");
        });
    }
}