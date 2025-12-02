package com.Tools;

import com.Entity.Product;
import com.Entity.Supplier;
import com.Entity.User;
import lombok.extern.java.Log;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static com.Tools.DataFormat.DATA_FORMAT;

@Log
public class CSVUtil {

    public static <T>  boolean CSV_out(String output_name, List<T> data) {
        if(data==null|| data.isEmpty()){
            log.severe("data数据为空");
            return false;
        }
        Class<?> clazz = data.get(0).getClass();
        log.info("准备输出CSV，数据类型:" + clazz.getName());
        try(BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream("Output_CSV/"+output_name+".csv"));) {
            log.info("准备输出数据");
            Field[] fields = clazz.getDeclaredFields();
            switch (clazz.getSimpleName()) {
                case "Product":out.write(DATA_FORMAT.pro_format.getBytes(StandardCharsets.UTF_8));break;
                case "Supplier":out.write(DATA_FORMAT.sup_format.getBytes(StandardCharsets.UTF_8));break;
                case "User":out.write(DATA_FORMAT.user_format.getBytes(StandardCharsets.UTF_8));break;
                default:log.severe("数据类型不支持");return false;
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
            log.severe("输出流异常"+e.getMessage());
            return false;
        }
        return true;
    }

    public static List<Object> CSV_in(File file){
        log.info("接收到读入请求,路径:"+file.getPath());
        List<Object> list=new ArrayList<>();
        Object data;
        try(BufferedReader inner= new BufferedReader(new FileReader(file));){
            String format=inner.readLine();
            if(format==null||format.isEmpty()){
                log.severe("首行为空!");
                return null;
            }
            else if(format.equals(DATA_FORMAT.pro_format)){
                log.info("数据类型:产品");
                data=new Product();
            }
            else if(format.equals(DATA_FORMAT.sup_format)){
                log.info("数据类型:供应商");
                data=new Supplier();
            }
            else if(format.equals(DATA_FORMAT.user_format)){
                log.info("数据类型:用户");
                data=new User();
            }
            else{
                log.severe("格式或类型不支持");
                return null;
            }
            String line;
            while((line= inner.readLine())!=null){
                String[] fields=line.split(",");
                int i=0;
                for(Field field:data.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    field.set(data,Convert(fields[i],field.getType()));
                    i++;
                }
                list.add(data);
            }
        }
        catch (Exception e){
            log.severe("输入流异常:"+e.getMessage());
            return null;
        }
        if(list.isEmpty())return null;
        log.info("读取成功");
        return list;
    }
    private static Object Convert(String value, Class<?> clazz){
        PropertyEditor editor= PropertyEditorManager.findEditor(clazz);
        editor.setAsText(value);
        return editor.getValue();
    }
}
