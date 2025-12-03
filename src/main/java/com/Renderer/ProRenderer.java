package com.Renderer;

import com.Entity.Product;

import javax.swing.*;
import java.awt.Component;

public class ProRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, // 当前 JList
            Object value, // 当前列表项的数据（即你的自定义对象）
            int index, // 当前列表项的索引
            boolean isSelected, // 是否被选中
            boolean cellHasFocus // 是否有焦点
    ) {
        // 1. 调用父类方法，保留默认样式（选中色、焦点边框等）
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // 2. 强转成你的自定义对象（比如 User）
        if (value instanceof Product pro_data) {
            // 3. 自定义显示格式（拼接属性，按你的需求调整）
            String displayText = String.format("ID：%s | 名称：%s | 类型：%s | 价格: %f | 库存: %f | 供应商ID: %d",
                        pro_data.getProd_id(),pro_data.getProd_name(), pro_data.getType(),pro_data.getPrice(),pro_data.getQuantity(),pro_data.getSup_id());
            setText(displayText); // 设置显示文本

            // （可选）给列表项加图标（用 classpath 路径读取，参考你之前的图标加载）
            //setIcon(new ImageIcon(getClass().getResource("你的图标路径")));
        }

        // 4. 返回当前组件（即渲染后的列表项）
        return this;
    }
}
