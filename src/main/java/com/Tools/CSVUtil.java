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

    public static <T> boolean CSV_out(File targetFile, List<T> data) {
        if (data == null || data.isEmpty()) {
            log.severe("data数据为空");
            return false;
        }
        Class<?> clazz = data.get(0).getClass();
        log.info("准备输出CSV，数据类型:" + clazz.getName());

        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                log.severe("无法创建父目录: " + parentDir.getAbsolutePath());
                return false;
            }
        }
        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8))) {
            log.info("准备输出数据到: " + targetFile.getAbsolutePath());
            // 提前获取表头，避免switch重复写
            String header = null;
            switch (clazz.getSimpleName()) {
                case "Product":
                    header = DATA_FORMAT.pro_format;
                    break;
                case "Supplier":
                    header = DATA_FORMAT.sup_format;
                    break;
                case "User":
                    header = DATA_FORMAT.user_format;
                    break;
                default:
                    log.severe("数据类型不支持");
                    return false;
            }
            // 写入表头，去除隐形字符
            out.write(header.trim());
            out.write("\r\n");

            Field[] fields = clazz.getDeclaredFields();
            for (T t : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(t);
                    // 处理null值，避免空指针
                    String fieldValue = value == null ? "" : value.toString().trim();
                    if (i == fields.length - 1) {
                        line.append(fieldValue);
                    } else {
                        line.append(fieldValue).append(",");
                    }
                }
                out.write(line.toString());
                out.write("\r\n");
            }
            out.flush();
            log.info("CSV输出成功,文件路径:" + targetFile.getAbsolutePath());
        } catch (Exception e) {
            log.severe("输出流异常" + e.getMessage());
            return false;
        }
        return true;
    }

    public static List<Object> CSV_in(File file) {
        log.info("接收到读入请求,路径:" + file.getPath());
        List<Object> list = new ArrayList<>();
        String dataType = null;

        try (BufferedReader inner = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String format = inner.readLine();
            if (format == null || format.trim().isEmpty()) {
                log.severe("首行为空!");
                return null;
            }
            format = format.trim().replaceAll("\\s", "");
            System.out.println("读取到纯净表头: " + format);

            if (format.equals(DATA_FORMAT.pro_format.trim().replaceAll("\\s", ""))) {
                log.info("数据类型:产品");
                dataType = "Product";
            } else if (format.equals(DATA_FORMAT.sup_format.trim().replaceAll("\\s", ""))) {
                log.info("数据类型:供应商");
                dataType = "Supplier";
            } else if (format.equals(DATA_FORMAT.user_format.trim().replaceAll("\\s", ""))) {
                log.info("数据类型:用户");
                dataType = "User";
            } else {
                log.severe("格式或类型不支持，当前表头：" + format + "，预期表头：" + DATA_FORMAT.pro_format);
                return null;
            }

            String line;
            while ((line = inner.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] fields = line.split(",");
                Object data = switch (dataType) {
                    case "Product" -> new Product();
                    case "Supplier" -> new Supplier();
                    case "User" -> new User();
                    default -> null;
                };
                if (data == null) continue;

                int i = 0;
                Field[] declaredFields = data.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    // 防止数组越界 + 处理空字段
                    if (i < fields.length && !fields[i].trim().isEmpty()) {
                        try {
                            field.set(data, Convert(fields[i].trim(), field.getType()));
                        } catch (Exception e) {
                            log.warning("字段赋值失败：" + field.getName() + "，值：" + fields[i] + "，原因：" + e.getMessage());
                        }
                    }
                    i++;
                }
                list.add(data);
            }
        } catch (Exception e) {
            log.severe("输入流异常:" + e.getMessage());
            return null;
        }

        if (list.isEmpty()) {
            log.severe("读取到空数据");
            return null;
        }
        log.info("读取成功，共" + list.size() + "条数据");
        return list;
    }

    private static Object Convert(String value, Class<?> clazz) {
        if (value == null || value.isEmpty()) {
            // 基本类型返回默认值
            if (clazz == int.class || clazz == Integer.class) return 0;
            if (clazz == double.class || clazz == Double.class) return 0.0;
            if (clazz == String.class) return "";
            return null;
        }
        PropertyEditor editor = PropertyEditorManager.findEditor(clazz);
        editor.setAsText(value);
        return editor.getValue();
    }
}