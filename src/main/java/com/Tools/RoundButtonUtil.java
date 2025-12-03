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

        button.setIconTextGap(12);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("宋体", Font.BOLD, 12));
        button.setBackground(new Color(230, 240, 250));
        button.setPreferredSize(new Dimension(150, 100));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
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
