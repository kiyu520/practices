package com.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(date);
    }

    public static String getCurrentDateTime() {
        return formatDateTime(new Date());
    }
}