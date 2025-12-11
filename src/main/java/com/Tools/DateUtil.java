package com.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类，提供日期格式化和获取当前日期时间的方法
 */
public class DateUtil {
    /**
     * 将日期格式化为指定格式的字符串
     * @param date 需要格式化的日期对象
     * @return 格式化后的日期字符串，格式为"yyyy年MM月dd日 HH:mm:ss"
     */
    public static String formatDateTime(Date date) {
        // 创建SimpleDateFormat对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // 格式化日期并返回结果
        return sdf.format(date);
    }

    /**
     * 获取当前日期时间的格式化字符串
     * @return 当前日期时间的字符串表示，格式为"yyyy年MM月dd日 HH:mm:ss"
     */
    public static String getCurrentDateTime() {
        // 调用formatDateTime方法，传入当前日期对象，返回格式化后的字符串
        return formatDateTime(new Date());
    }
}