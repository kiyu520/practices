package com.Tools;

import lombok.extern.java.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

@Log
public class CSVUtil {

    private static Class<?> clazz;

    public static <T>  boolean CSV_out(String output_name, List<T> data) {
        if(data==null|| data.isEmpty()){
            log.severe("data数据为空");
            return false;
        }
        clazz = data.get(0).getClass();
        log.info("准备输出CSV，数据类型:" + clazz.getName());
        try(BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream("Output_CSV/"+output_name+".csv"));) {
            log.info("准备输出数据");
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (i != fields.length - 1) out.write((fields[i].getName() + ",").getBytes());
                else out.write(fields[i].getName().getBytes());
            }
            out.write("\r\n".getBytes());
            for (T t : data) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);//暴力反射防止private
                    if(i==fields.length-1) out.write(fields[i].get(t).toString().getBytes());
                    else out.write((fields[i].get(t).toString()+",").getBytes());
                }
                out.write("\r\n".getBytes());
            }
            log.info("CSV输出成功,文件名:" + output_name + ".csv");
            out.flush();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            log.severe("输出流创建异常");
            return false;
        }
        return true;
    }
}
