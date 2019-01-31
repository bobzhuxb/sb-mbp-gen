package com.bob.freemarker.module;

import com.bob.freemarker.util.FileUtil;
import freemarker.template.Configuration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class ProjectModule {

    private final Logger log = LoggerFactory.getLogger(ProjectModule.class);

    /**
     * 生成主项目（以现有项目的FreeMarker模板为参照）
     * @param templatePath 模板所在路径
     * @param projectPath 最终生成的项目所在的路径
     * @param cfg 模板配置
     * @param root 要填充到模板参数的内容
     * @param toPaths 最终生成的项目包名拆分
     * @param templateName 模板名称
     * @param TemplateName 首字母大写的模板名称
     * @param projectName 最终生成的项目的名称
     * @param ProjectName 最终生成的项目首字母大写的项目名称
     * @throws Exception
     */
    public static void doInitProject(String templatePath, String projectPath, Configuration cfg,
                                     Map<String, String> root, String[] toPaths,
                                     String templateName, String TemplateName, String projectName, String ProjectName) throws Exception {
        // 模板主路径和项目主路径
        String templateAllDirectory = templatePath + templateName + "\\";
        String projectDirectory = projectPath + projectName + "\\";
        // 获取包对应的路径名
        String toPackagePath = projectDirectory + "src\\main\\java\\";
        // 第一层，模板文件夹和项目文件夹（创建）
        File projectFile_t = new File(templateAllDirectory);
        File projectFile_o = new File(projectDirectory);
        // 清空并重新创建项目文件夹
        FileUtil.generateDirectory(projectFile_o);
        FileUtils.cleanDirectory(projectFile_o);
        // 第一层，生成模板文件之后
        File[] level1Files_t = projectFile_t.listFiles();
        // 遍历第一层的文件和目录
        for (File level1File_t : level1Files_t) {
            if (level1File_t.isDirectory()) {
                if ("gradle".equals(level1File_t.getName())) {
                    // gradle目录全拷贝
                    FileUtil.copyFolder(level1File_t, projectFile_o);
                } else if ("src".equals(level1File_t.getName())) {
                    // src目录操作（主要工作）
                    FileUtil.generateSrcFiles(templateAllDirectory, projectDirectory, "src\\", "src\\", toPaths, cfg, root);
                }
            } else {
                // 其它项目文件操作
                String ftlFullName = level1File_t.getAbsolutePath();
                String fileRealName = ftlFullName.substring(ftlFullName.lastIndexOf("\\") + 1, ftlFullName.lastIndexOf("."));
                File objFile = new File(projectDirectory + fileRealName);
                File templateDirectory = new File(templateAllDirectory);
                String templateFileName = level1File_t.getName();   // 纯文件名
                if (".gitignore".equals(templateFileName)) {
                    // 忽略.gitignore文件，该文件直接原样拷贝
                    FileUtil.copyFile(level1File_t, new File(projectDirectory + "\\.gitignore"));
                } else {
                    FileUtil.generateFileWithTemplate(objFile, templateDirectory, templateFileName, cfg, root);
                }
            }
        }

        // 修改Spring Boot启动文件的文件名
        String bootPath = toPackagePath;
        for (int i = 0; i < toPaths.length; i++) {
            bootPath += toPaths + "\\";
        }
        File templateBootFile = new File(bootPath + TemplateName + "Application.java");
        File toBootFile = new File(bootPath + ProjectName + "Application.java");
        templateBootFile.renameTo(toBootFile);
    }

}
