package com.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
/**
 * 格式化日期时间方法
 * 该方法用于将Date对象格式化为包含年月日、时分秒的字符串
 *
 * @param date 需要格式化的日期时间对象
 * @return 返回格式化后的日期时间字符串，格式为"yyyy年MM月dd日 HH:mm:ss"
 */
    // 格式化日期时间（含时分秒）
    public static String formatDateTime(Date date) {
    // 创建SimpleDateFormat对象，指定日期时间的格式模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    // 使用format方法将Date对象格式化为字符串并返回
        return sdf.format(date);
    }

    // 获取当前日期时间
    public static String getCurrentDateTime() {
        return formatDateTime(new Date());
    }
}