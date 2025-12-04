package com.Tools;

import lombok.extern.java.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.InputStream;

@Log
public class RoundButtonUtil {
   public static JButton createRoundedButton(String mainText, String suffixText, String iconPath) {
        ImageIcon icon = loadLocalIcon(iconPath, 60, 60);
        String buttonText = String.format(
                "<html><body style='padding:0 5px; line-height:18px;'>%s<br/><small style='color:#666;'>%s</small></body></html>",
                mainText, suffixText
        );
        JButton button = new JButton(buttonText, icon) {
            private final int CORNER_RADIUS = 15;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 200, 200));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS));
                g2.dispose();
            }
        };

        // 设置图标与文本之间的间隔为12像素
        button.setIconTextGap(12);
        // 设置文本垂直位置在底部
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        // 设置文本水平位置在中间
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        // 设置按钮字体为宋体、粗体、大小为12
        button.setFont(new Font("宋体", Font.BOLD, 12));
        // 设置按钮背景颜色为浅蓝色
        button.setBackground(new Color(230, 240, 250));
        // 设置按钮的首选大小为150x100像素
        button.setPreferredSize(new Dimension(150, 100));
        // 设置按钮内容区域不填充背景
        button.setContentAreaFilled(false);
        // 设置按钮不绘制焦点状态
        button.setFocusPainted(false);
        // 设置按钮不绘制边框
        button.setBorderPainted(false);

        return button;
    }

    /**
     * 本地图片加载工具方法
     */
    public static ImageIcon loadLocalIcon(String Path, int iconWidth, int iconHeight) {
        try (InputStream is = RoundButtonUtil.class.getResourceAsStream(Path)) {
            if (is == null) {
                log.severe("⚠️  图标不存在（路径错误）：" + Path);
                return new ImageIcon();
            }
            Image originalImage = ImageIO.read(is);
            Image scaledImage = originalImage.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            log.severe("⚠️  图标加载失败：" + Path);
            return new ImageIcon();
        }
    }
}
