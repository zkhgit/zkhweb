package zkh.tool.excel.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import zkh.tool.excel.common.ExcelField;

public class ExcelReader {

	/**
     * 读取excel文件，并把读取到的数据封装到clazz中
     * 
     * @param path
     *            文件路径
     * @param clazz
     *            实体类
     * @return 返回clazz集合
     */
    public static <T extends Object> List<T> readExcelFile(String path, Class<T> clazz) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(path));
        } catch (FileNotFoundException e1) {
            throw new RuntimeException("文件路径异常");
        }

        Workbook wookbook = zkh.tool.excel.common.Workbook.create(path);

        // 得到一个工作表
        Sheet sheet = wookbook.getSheetAt(0);
        // 获取行总数
        int rows = sheet.getLastRowNum() + 1;
        // 获取类所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 存储excel数据
        List<T> list = new ArrayList<>();
        T obj = null; Row row = null; Cell cell = null; ExcelField excelField = null;
        int coumn = 0;
        for (int i = 1; i < rows; i++) {
            // 获取excel行
            row = sheet.getRow(i);
            try {
                // 创建实体
                obj = clazz.newInstance();
                for (Field f : fields) {
                    // 设置属性可访问
                    f.setAccessible(true);
                    // 判断是否是注解
                    if (f.isAnnotationPresent(ExcelField.class)) {
                        // 获取注解
                        excelField = f.getAnnotation(ExcelField.class);
                        // 获取列索引
                        coumn = excelField.column();
                        // 获取单元格
                        cell = row.getCell(coumn);
                        // 设置属性
                        setFieldValue(obj, f, wookbook, cell);
                    }
                }
                // 添加到集合中
                list.add(obj);
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }

        }

        try {
            //释放资源
            wookbook.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 设置属性值
     * 
     * @param obj 操作对象
     * @param f 对象属性
     * @param cell excel单元格
     */
    private static void setFieldValue(Object obj, Field f, Workbook wookbook, Cell cell) {
        try {
            if (f.getType() == int.class || f.getType() == Integer.class) {
                f.setInt(obj, getInt(cell));
            } else if (f.getType() == Double.class || f.getType() == double.class) {
                f.setDouble(obj, getDouble(null, cell));
            } else {
                f.set(obj, getString(cell));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
