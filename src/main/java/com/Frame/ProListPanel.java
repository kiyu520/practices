package com.Frame;

import com.Entity.Product;
import com.Entity.User;
import com.Service.ProductService;
import com.Service.UserService;

import javax.swing.*;

public class ProListPanel extends JPanel {
    static DefaultListModel<Product> listModel = new DefaultListModel<>();
    public static JList<Product> get(){
        ProductService.findAllproducts().forEach(listModel::addElement);
        return new  JList<>(listModel);
    }
}
