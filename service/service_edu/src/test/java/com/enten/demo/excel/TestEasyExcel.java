package com.enten.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        // writeTest();
        readTest();

    }

    /**
     * 实现读操作
     */
    private static void readTest() {
        String filename = "D:\\test.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    /**
     * 实现写操作
     */
    private static void writeTest() {

        //1 设置写入文件地址和excel文件名称
        String filename="D:\\test.xlsx";

        //2 调用EasyExcel里面的方法实现操作
        //两个参数:1 参数文件路径名称,第二个参数实体类class
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());
    }

    /**
     * 创建方法返回list集合
     */
    private static List<DemoData> getData(){
        ArrayList<DemoData> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);

        }
        return list;
    }
}
