package com.Frame;

import com.Entity.User;

import javax.swing.*;

public class Runner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 创建登录窗口
            LoginFrame LoginFrame = new LoginFrame();
            LoginFrame.setVisible(true);
        });
    }
}
