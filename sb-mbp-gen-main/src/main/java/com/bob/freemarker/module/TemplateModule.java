package com.bob.freemarker.module;

import com.bob.freemarker.util.FileUtil;
import freemarker.template.Configuration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateModule {

    private final static Logger log = LoggerFactory.getLogger(TemplateModule.class);

    /**
     * 根据现有项目生成模板
     * @param existProjectDirectory 现有项目所在路径
     * @param templateAllDirectory 模板所在路径
     * @param existProjectName 现有项目的项目名
     * @param templateProjectName 模板项目名
     * @param skipEntityStart 过滤掉的实体名称开头
     * @throws Exception
     */
    public static void generateTemplate(String existProjectDirectory, String templateAllDirectory,
                                        String existProjectName, String templateProjectName,
                                        String skipEntityStart) throws Exception {
        File projectFile = new File(existProjectDirectory + existProjectName);
        File templateFile = new File(templateAllDirectory + templateProjectName);
        String[] skipEntityStarts = skipEntityStart.split("\\,");
        // 获取来源项目的包目录
        String existPackageName = "";
        File packageFile = new File(existProjectDirectory + existProjectName + "\\src\\main\\java");
        for (int i = 0; i < 3; i++) {
            File[] levelFiles = packageFile.listFiles();
            existPackageName += levelFiles[0].getName() + ".";
            packageFile = levelFiles[0];
        }
        existPackageName = existPackageName.substring(0, existPackageName.length() - 1);
        // 首字母小写的项目名
        String lowerProjectName = existProjectName.substring(0, 1).toLowerCase() + existProjectName.substring(1);
        // 首字母大写的项目名
        String upperProjectName = existProjectName.substring(0, 1).toUpperCase() + existProjectName.substring(1);
        // 项目目录下的文件和文件夹
        File[] level1ProjectFiles = projectFile.listFiles();
        // 判断是否已生成过模板文件
        boolean templateFileGenerated = false;
        for (File level1ProjectFile : level1ProjectFiles) {
            if (level1ProjectFile.isFile() && level1ProjectFile.getName().startsWith("build.gradle")) {
                if (level1ProjectFile.getName().equals("build.gradle.ftl")) {
                    templateFileGenerated = true;
                }
                break;
            }
        }
        if (templateFileGenerated) {
            log.info("项目目录源已经是模板");
            return;
        }
        // 清空模板目录，并将项目目录源拷贝一份到模板目录
        FileUtils.deleteDirectory(templateFile);
        FileUtils.copyDirectory(projectFile, templateFile);
        // 拷贝后模板目录下的文件和文件夹
        File[] level1TemplateFiles = templateFile.listFiles();
        // 生成模板文件
        for (File level1TemplateFile : level1TemplateFiles) {
            if (level1TemplateFile.isDirectory()) {
                if (".gradle".equals(level1TemplateFile.getName()) || ".idea".equals(level1TemplateFile.getName())
                        || "build".equals(level1TemplateFile.getName()) || "out".equals(level1TemplateFile.getName())) {
                    // 删除.gradle、.idea、build、out目录
                    FileUtils.deleteDirectory(level1TemplateFile);
                } else if ("src".equals(level1TemplateFile.getName())) {
                    // src目录所有文件追加后缀.ftl
                    FileUtil.traverseFolderAddSuffixToFile(level1TemplateFile.getAbsolutePath(), ".ftl", skipEntityStarts);
                }
            } else {
                // 其它项目文件追加后缀.ftl
                if (".gitignore".equals(level1TemplateFile.getName())) {
                    // 忽略.gitignore文件
                    continue;
                }
                String toTemplateFileName = level1TemplateFile.getAbsolutePath() + ".ftl";
                level1TemplateFile.renameTo(new File(toTemplateFileName));
            }
        }
        // 替换特殊字符等
        replaceFileCharacter(templateFile, lowerProjectName, upperProjectName, existPackageName);
    }

    /**
     * 替换文件中的特殊字符，例如${str}需要转换为${r'${str}'}
     * 用正则 替换，源：\$\{([^\$\{\}]*)\}  目标：${r'${$1}'}
     * 再进行普通替换
     * 将sdfyyhybm替换为${projectName}，将Sdfyyhybm替换为${ProjectName}，将com.ts.tm替换为${packageName}
     * 将yml配置文件中的数据库配置替换，包括${dbIp}、${dbPort}、${dbName}、${dbUsername}、${dbPassword}
     */
    public static void replaceFileCharacter(File templateFile, String lowerProjectName, String upperProjectName,
                                            String existPackageName) throws Exception {
        String regex = "\\$\\{([^\\$\\{\\}]*)\\}";
        String replacement = "${r'${$1}'}";
        File[] subTemplateFiles = templateFile.listFiles();
        for (File subTemplateFile : subTemplateFiles) {
            if (subTemplateFile.isFile()) {
                if (".gitignore".equals(subTemplateFile.getName())) {
                    continue;
                }
                List<String> lineListNew = new ArrayList<>();
                List<String> lineList = FileUtil.readFileByLines(subTemplateFile, "UTF-8");
                for (String line : lineList) {
                    // 1、用正则 替换，源：\$\{([^\$\{\}]*)\}  目标：\$\{r'\$\{$1\}'\}
                    Pattern pattern = Pattern.compile(regex);    // 匹配的模式
                    Matcher matcher = pattern.matcher(line);
                    List<String> innerStrList = new ArrayList<>();
                    int count = 0;
                    while (matcher.find()) {
                        count++;
                        String innerStr = matcher.group(1);
                        String replacementNow = replacement.replace("$1", innerStr);
                        innerStrList.add(replacementNow);
                        line = line.replaceFirst(regex, "======><><======" + count + "======><><======");
                    }
                    for (int i = 0; i < innerStrList.size(); i++) {
                        String innerStr = innerStrList.get(i);
                        line = line.replace("======><><======" + (i + 1) + "======><><======", innerStr);
                    }
                    // 2、将sdfyyhybm替换为${projectName}，将Sdfyyhybm替换为${ProjectName}，将com.ts.tm替换为${packageName}
                    line = line.replace(lowerProjectName, "${projectName}")
                            .replace(upperProjectName, "${ProjectName}")
                            .replace(existPackageName, "${packageName}");
                    // 3、将yml配置文件中的数据库配置替换，包括${dbIp}、${dbPort}、${dbName}、${dbUsername}、${dbPassword}
                    // 这个太麻烦，不做了，自己最后手动改吧
                    // 最后，把修改后的行添加到lineListNew中
                    lineListNew.add(line);
                }
                // 将lineListNew重写入文件
                FileUtils.writeLines(subTemplateFile, "UTF-8", lineListNew);
            } else {
                if (".gradle".equals(subTemplateFile.getName())) {
                    continue;
                }
                // 循环遍历目录
                replaceFileCharacter(subTemplateFile, lowerProjectName, upperProjectName, existPackageName);
            }
        }
    }

}
