package com.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局字体工具类：管理所有界面的字体/字号配置
 */
public class GlobalFontUtil {
    // 全局字体配置（默认：微软雅黑，14号）
    private static Font GLOBAL_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    // 缓存已打开的窗口，用于刷新字体
    private static Map<JFrame, Boolean> openedFrames = new HashMap<>();

    /**
     * 设置全局字体，并刷新所有已打开窗口的字体
     * @param fontName 字体名称（如：宋体、微软雅黑、Arial）
     * @param fontSize 字体大小
     */
    public static void setGlobalFont(String fontName, int fontSize) {
        // 更新全局字体
        GLOBAL_FONT = new Font(fontName, Font.PLAIN, fontSize);
        // 刷新所有已打开窗口的字体
        refreshAllFramesFont();
    }

    /**
     * 刷新所有已打开窗口的字体
     */
    public static void refreshAllFramesFont() {
        for (JFrame frame : openedFrames.keySet()) {
            if (frame.isDisplayable()) { // 窗口未关闭才刷新
                refreshComponentFont(frame.getContentPane());
                frame.validate(); // 重新验证布局
                frame.repaint(); // 重绘窗口
            }
        }
    }

    /**
     * 递归刷新单个组件及其子组件的字体
     * @param component 要刷新的组件（如JPanel、JLabel、JButton等）
     */
    public static void refreshComponentFont(Component component) {
        // 设置当前组件的字体
        if (component instanceof JLabel || component instanceof JButton ||
                component instanceof JTextField || component instanceof JPasswordField ||
                component instanceof JComboBox || component instanceof JMenu ||
                component instanceof JMenuItem) {
            component.setFont(GLOBAL_FONT);
        }

        // 递归刷新子组件
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                refreshComponentFont(child);
            }
        }
    }

    /**
     * 注册窗口（用于后续刷新字体）
     * @param frame 要注册的窗口
     */
    public static void registerFrame(JFrame frame) {
        openedFrames.put(frame, true);
    }

    /**
     * 移除已关闭的窗口（释放资源）
     * @param frame 要移除的窗口
     */
    public static void unregisterFrame(JFrame frame) {
        openedFrames.remove(frame);
    }

    /**
     * 获取当前全局字体
     */
    public static Font getGlobalFont() {
        return GLOBAL_FONT;
    }
}
