package com.Frame;

import com.Entity.Product;
import com.Model.ProTableModel;
import com.Service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

public class ProTablePanel extends JPanel {
    static List<Product> proList;
    public static JTable get(){
        proList= ProductService.findAllproducts();
        ProTableModel proTableModel = new ProTableModel(proList);
        JTable table = new JTable(proTableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        return table;
    }
}
