package com.bob.freemarker.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {

    /**
     * 创建路径
     * @param directory
     * @return
     */
    public static boolean generateDirectory(File directory) {
        return directory.mkdirs();
    }

    /**
     * 根据模板产生文件
     * @param objFile 目标文件
     * @param templateDirectory 模板路径
     * @param templateFileName 模板文件（不包含路径）
     * @param root 自动填充模板的Map
     * @throws Exception
     */
    public static void generateFileWithTemplate(File objFile, File templateDirectory, String templateFileName,
                                                Configuration cfg, Map<String, String> root) throws Exception {
        // 加载文件路径
        cfg.setDirectoryForTemplateLoading(templateDirectory);
        // 加载模板文件
        Template temp = cfg.getTemplate(templateFileName, "UTF-8");
        // 写文件
        temp.process(root, new OutputStreamWriter(new FileOutputStream(objFile), "UTF-8"));
    }

    /**
     * 拷贝文件
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        Files.copy(srcFile.toPath(), destFile.toPath());
    }

    /**
     * 拷贝文件夹
     * @param srcFolder
     * @param destFolder
     * @throws IOException
     */
    public static void copyFolder(File srcFolder, File destFolder) throws IOException {
        final Path srcPath = Paths.get(srcFolder.getAbsolutePath());
        // 这里多创建一级，就解决了没有外壳的问题
        final Path destPath = Paths.get(destFolder.getAbsolutePath(), srcPath.toFile().getName());
        // 检查源文件夹是否存在
        if (Files.notExists(srcPath)) {
            System.err.println("源文件夹不存在");
            System.exit(1);
        }
        // 如果目标目录不存在，则创建
        if (Files.notExists(destPath)) {
            Files.createDirectories(destPath);
        }
        // 这里是官方例子的开头，可能是针对大文件处理设置的参数
        // Files.walkFileTree(srcPath,      EnumSet.of(FileVisitOption.FOLLOW_LINKS),
        // Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {}
        //简化后的开头
        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            // 官方还调用了专门的文件夹处理，这里没使用
            // public FileVisitResult preVisitDirectory(Path dir,
            // BasicFileAttributes attrs) throws IOException {return null;}
            @Override
            // 文件处理，将文件夹也一并处理，简洁些
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Path dest = destPath.resolve(srcPath.relativize(file));
                // 如果说父路径不存在，则创建
                if (Files.notExists(dest.getParent())) {
                    Files.createDirectories(dest.getParent());
                }
                Files.copy(file, dest);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 遍历文件夹并加后缀
     */
    public static void traverseFolderAddSuffixToFile(String path, String suffix, String[] skipEntityStarts) {
        File file1 = new File(path);
        if (file1.exists()) {
            File[] files = file1.listFiles();
            if (null == files || files.length == 0) {
                // 文件夹是空的
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        // 文件夹是file2.getAbsolutePath()
                        traverseFolderAddSuffixToFile(file2.getAbsolutePath(), suffix, skipEntityStarts);
                    } else {
                        // 删除相关实体（项目模板中不需要包含自定义实体）
                        boolean deleted = false;
                        String fileRealName = file2.getName();
                        for (String skipEntityStartOne : skipEntityStarts) {
                            if (fileRealName.startsWith(skipEntityStartOne)) {
                                file2.delete();
                                deleted = true;
                                break;
                            }
                        }
                        if (!deleted) {
                            // 文件是file2.getAbsolutePath()
                            file2.renameTo(new File(file2.getAbsolutePath() + suffix));
                        }
                    }
                }
            }
        } else {
            // 文件不存在
        }
    }

    /**
     * 根据FreeMarker模板一层层生成代码文件（从src目录开始）
     * @param templateBasePath 模板总目录
     * @param sourceCodeBasePath 代码总目录
     * @param templateRelativePath 每次递归调用时的模板相对路径
     * @param projectRelativePath 每次递归调用时的项目相对路径
     * @param toPaths 生成的项目的包的三层路径
     * @param cfg 模板配置
     * @param root 模板填充内容
     * @param ProjectName 首字母大写的项目名称
     */
    public static void generateSrcFiles(final String templateBasePath, final String sourceCodeBasePath,
                                        String templateRelativePath, String projectRelativePath, String[] toPaths,
                                        Configuration cfg, Map<String, String> root, String ProjectName) throws Exception {
        // 当前模板的相对路径（例如src）
        File templatePath = new File(templateBasePath + templateRelativePath);
        if (templatePath.exists()) {
            // 生成代码的文件夹
            String[] templateRelativePaths = templateRelativePath.split("\\\\");
            File sourceCodeDirectory = new File(sourceCodeBasePath + "\\" + projectRelativePath);
            generateDirectory(sourceCodeDirectory);
            // 在模板目录下找子文件夹和子文件
            File[] templateSubFiles = templatePath.listFiles();
            if (null == templateSubFiles || templateSubFiles.length == 0) {
                // 文件夹是空的
                return;
            } else {
                // 文件夹下面还有文件或文件夹
                for (File templateSubFile : templateSubFiles) {
                    if (templateSubFile.isDirectory()) {
                        // 有子文件夹，子文件夹全目录是file2.getAbsolutePath()，相对目录是file2.getPath()
                        String subFullFolder = templateSubFile.getAbsolutePath();
                        String subFolderName = subFullFolder.substring(subFullFolder.lastIndexOf("\\") + 1);
                        String nextTemplateRelativePath = templateRelativePath + subFolderName + "\\";
                        String nextProjectRelativePath = "";
                        if (templateRelativePaths.length >= 3 && templateRelativePaths.length < 6
                                && nextTemplateRelativePath.startsWith("src\\main\\java\\")) {
                            nextProjectRelativePath = projectRelativePath + toPaths[templateRelativePaths.length - 3] + "\\";
                        } else {
                            nextProjectRelativePath = projectRelativePath + subFolderName + "\\";
                        }
                        generateSrcFiles(templateBasePath, sourceCodeBasePath, nextTemplateRelativePath,
                                nextProjectRelativePath, toPaths, cfg, root, ProjectName);
                    } else {
                        // 有子文件，文件目录是file2.getAbsolutePath()
                        // 根据模板文件生成代码文件
                        String templateFileName = templateSubFile.getName();      // 模板文件名（不包含路径）
                        if (!templateFileName.endsWith(".ftl")) {
                            // 固定文件（非模板）
                            copyFile(new File(templateSubFile.getPath()), new File(sourceCodeBasePath + projectRelativePath + templateFileName));
                        } else {
                            // 根据模板生成文件
                            String sourceFileName = templateFileName.substring(0, templateFileName.lastIndexOf("."));   // 代码文件名（不包含路径）
                            if (templateFileName.equals("Application.java.ftl")) {
                                sourceFileName = ProjectName + sourceFileName;
                            }
                            File objFile = new File(sourceCodeBasePath + projectRelativePath + sourceFileName);    // 代码文件
                            File templateDirectory = new File(templateBasePath + templateRelativePath);
                            generateFileWithTemplate(objFile, templateDirectory, templateFileName, cfg, root);
                        }
                    }
                }
            }
        } else {
            // 文件不存在
        }
    }

    /**
     * 读取文件
     * @param fileName
     * @param charsetName
     * @return
     */
    public static List<String> readFileByLines(String fileName, String charsetName) throws Exception {
        File file = new File(fileName);
        return readFileByLines(file, charsetName);
    }

    /**
     * 读取文件
     * @param file
     * @param charsetName
     * @return
     */
    public static List<String> readFileByLines(File file, String charsetName) throws Exception {
        List<String> fileContentList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                fileContentList.add(tempString);
            }
            reader.close();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return fileContentList;
    }

}
