package com.bob.at.util;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\Bob\\Desktop\\src\\";//java文件夹路径
        String classPath = "C:\\Users\\Bob\\Desktop\\dest";//class文件存放路径
        //把文件夹下所有java文件完整路径存到files数组中，不用考虑引用关系//在ZGS.java中引用了LIST和INFO作为引用数据对象
        List<String> javaFileList = new ArrayList<>();
        getAllFiles(javaFileList, filePath);
        System.out.println("==================");
//        String[] files = {filePath+"LIST.java",filePath+"ZGS.java",filePath+"INFO.java"};
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null,null,null);
        Iterable<? extends JavaFileObject> javaFileObjects =
                manager.getJavaFileObjectsFromStrings(javaFileList);
        // options就是指定编译输入目录，与我们命令行写javac -d C://是一样的
        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(classPath);
        JavaCompiler.CompilationTask task = compiler.getTask(null,manager,null,options,null,javaFileObjects);
        task.call();
        manager.close();
    }

    public static void getAllFiles(List<String> javaFileList, String path) throws IOException {
        File file = new File(path);
        File[] subFiles = file.listFiles();
        for (File subFile : subFiles) {
            if (subFile.isFile()) {
                // 文件
                String fullName = subFile.getAbsolutePath();
                javaFileList.add(fullName);
            } else {
                // 目录
                String subPath = subFile.getAbsolutePath();
                getAllFiles(javaFileList, subPath);
            }
        }
    }

}
