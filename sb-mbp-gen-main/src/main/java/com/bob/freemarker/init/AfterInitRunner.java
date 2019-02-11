package com.bob.freemarker.init;

import com.bob.freemarker.dto.ERDTO;
import com.bob.freemarker.module.DbModule;
import com.bob.freemarker.module.EntityModule;
import com.bob.freemarker.module.ProjectModule;
import com.bob.freemarker.module.TemplateModule;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建模板文件前的预处理：
 * 1、用一个新的spring boot + mybatis plus项目，删除项目目录下的  .gradle  .idea  build三个目录
 * 删除src\main\webapp目录下的所有子目录和文件
 *
 * 2、替换特殊字符
 * 例如${str}需要转换为${r'${str}'}
 * 即在IDEA里面用Ctrl+Shift+R来 正则 替换，源：\$\{([^\$\{\}]*)\}  目标：\$\{r'\$\{$1\}'\}
 *
 * 3、在IEDA里面用Ctrl+Shift+R来 普通 替换项目名（首字母大写替换一次，小写替换一次）和包名（替换一次）
 * 例如：将sdfyyhybm替换为${projectName}，将Sdfyyhybm替换为${ProjectName}，将com.ts.tm替换为${packageName}
 *
 * 4、将yml配置文件中的数据库配置替换，包括${dbIp}、${dbPort}、${dbName}、${dbUsername}、${dbPassword}
 */
@Component
@Order(1)
public class AfterInitRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(AfterInitRunner.class);

    private Configuration cfg = null;

    private Map<String, String> root = new HashMap<>();

    @Override
    public void run (String... args) throws Exception {
        System.out.println("\n========================== Code Generator ( Spring Boot + MyBatis Plus ) =============================\n");
        // 解析传入参数
        for (String arg : args) {
            if (arg.equals("--help")) {
                System.out.println("Spring Boot + MyBatis Plus代码生成器参数说明：\n\n" +
                        "注意：1、只支持mysql数据库。2、只支持3层的主包名，例如com.bob.sm。3、jdl文件中的实体字段，不要有insertTime" +
                        "和updateTime和insertUserId和operateUserId字段，这两个字段分别表示数据行的插入和更新时间，由本代码生成器自动生成。" +
                        "4、关于jdl文件的编辑：方法一（推荐）：在 https://start.jhipster.tech/jdl-studio/ 网站上编辑；方法二：使用" +
                        "Visual Studio Code的jhipster插件，新增jdl文件进行编辑\n\n" +
                        "    --help\t\t\t帮助及提示\n" +
                        "    --generate-template\t\t是否生成模板（yes：是且同时生成全代码和数据表。no：只生成全代码和数据表。" +
                        "update-entity：只更新实体代码和数据表。only：只生成模板。默认值：no）\n" +
                        "    --from-project-path\t\t生成模板的来源项目路径（如果generate-template填yes或only，则此参数必填）\n" +
                        "    --from-project-name\t\t生成模板的来源项目名称（默认值：sbmbpfrom）\n" +
                        "    --skip-entity-start\t\t生成模板时过滤的实体名称开头（如果generate-template填yes或only，则需要此参数，" +
                        "如果有多个，以英文逗号隔开。默认值：Sm）\n" +
                        "    --template-path\t\t指定的模板路径及生成的模板路径（generate-template为only或no或update-entity，" +
                        "则此参数必填。generate-template为yes，则忽略该参数。）\n" +
                        "    --template-name\t\t模板名称（默认值：sbmbptemplate）\n" +
                        "    --to-project-path\t\t最终生成的项目路径（如果generate-template不是only，则此参数必填）\n" +
                        "    --to-project-name\t\t最终生成的项目名（默认值：sbmbp）\n" +
                        "    --to-project-package\t最终生成的项目包名（默认值：com.bob.sm）\n" +
                        "    --entity-template-path\t实体模板路径（如果generate-template不是only，则此参数必填）\n" +
                        "    --entity-jdl-file\t\t实体的jdl文件名（包含路径，如果generate-template不是only，则此参数必填）\n" +
                        "    --entity-jdl-file-encode\t实体的jdl文件编码格式（默认值：GB2312）\n" +
                        "    --db-ip\t\t\t数据库IP地址（默认值：localhost）\n" +
                        "    --db-port\t\t\t数据库端口号（默认值：3306）\n" +
                        "    --db-name\t\t\t数据库名（默认值与参数to-project-name相同）\n" +
                        "    --db-username\t\t数据库用户名（默认值：root）\n" +
                        "    --db-password\t\t数据库密码（默认值：123456）\n" +
                        "    --allow-change-table\t是否允许本工具修改数据库（默认值：no，为了防止修改表结构导致数据错乱，所以默认为否）\n" +
                        "    --delete-table-name-start\t允许删除的表名开头（JDL文件指定的表名开头，如果有多个，以英文逗号隔开，默认值：Sm）\n" +
                        "\n\n====> 示例：\n\n" +
                        "1、只生成模板：\n" +
                        "java -jar codeGenerate.jar --generate-template only --template-path 模板所在路径 --template-name " +
                        "模板名称 --from-project-path 来源项目所在路径 --from-project-name 来源项目名称 --skip-entity-start Sm\n\n" +
                        "2、只生成全代码和数据库：\n" +
                        "java -jar codeGenerate.jar --template-path 模板所在路径 --template-name 模板名称 --to-project-path " +
                        "生成的项目所在路径 --to-project-name 生成的项目名 --to-project-package 生成的项目的包名 " +
                        "--entity-template-path 实体模板所在的总路径 --entity-jdl-file 描述实体的JDL文件全路径名 " +
                        "--db-name 数据库名 --allow-change-table yes --delete-table-name-start Sm\n\n" +
                        "3、只更新实体代码和数据库：\n" +
                        "java -jar codeGenerate.jar --generate-template update-entity --to-project-path " +
                        "生成的项目所在路径 --to-project-name 生成的项目名 --to-project-package 生成的项目的包名 " +
                        "--entity-template-path 实体模板所在的总路径 --entity-jdl-file 描述实体的JDL文件全路径名 " +
                        "--db-name 数据库名 --allow-change-table yes --delete-table-name-start Sm\n\n" +
                        "4、生成模板、全代码和数据库：\n" +
                        "java -jar codeGenerate.jar --generate-template yes --template-path 模板所在路径 --template-name " +
                        "模板名称 --from-project-path 来源项目所在路径 --from-project-name 来源项目名称 --to-project-path " +
                        "生成的项目所在路径 --skip-entity-start Sm --to-project-name 生成的项目名 --to-project-package " +
                        "生成的项目的包名 --entity-template-path 实体模板所在的总路径 --entity-jdl-file 描述实体的JDL文件全路径名 " +
                        "--db-name 数据库名 --allow-change-table yes --delete-table-name-start Sm\n");
                return;
            }
        }
        // ================参数默认值 start================
        // 是否生成框架模板
        String generateTemplate = "no";
        // 生成框架模板的来源项目路径
        String existProjectDirectory = null;
        // 生成框架模板的来源项目名称
        String existProjectName = "sbmbpfrom";
        // 生成模板时过滤的实体名称开头（如果有多个，以英文逗号隔开）
        String skipEntityStart = "Sm";
        // 项目框架模板总路径
        String templatePath = "D:\\IdeaWorkspace\\CodeAutoGenerate\\";
        // 框架模板的默认名称（文件夹名）
        String templateName = "sbmbptemplate";
        // 生成的最终项目所在路径（此参数必填，无默认值）
        String projectPath = null;
        // 实体模板的默认总路径
        String entityTemplatePath = "D:\\IdeaWorkspace\\CodeAutoGenerate\\sb-mbp-gen-entitytemplate\\";
        // 实体的UML文件全路径名
        String umlFileName = "D:\\IdeaWorkspace\\CodeAutoGenerate\\sb-mbp-gen-domain-jdl\\test.jdl";
        // 实体的UML文件编码格式
        String umlFileCharsetName = "GB2312";
        // 最终生成的项目名称
        String projectName = "sbmbp";
        // 最终生成的项目包名（默认与模板包名相同）
        String packageName = "com.bob.sm";
        // 数据库IP
        String dbIp = "localhost";
        // 数据库Port
        String dbPort = "3306";
        // 数据库名
        String dbName = projectName;
        // 数据库用户名
        String dbUsername = "root";
        // 数据库密码
        String dbPassword = "123456";
        // 是否允许程序修改表结构（为了防止修改表结构导致数据错乱，默认为否）
        String allowChangeTable = "no";
        // 允许删除的表名开头（JDL文件指定的表名开头，如果有多个，以英文逗号隔开）
        String deleteTableNameStartStr = "Sm";
        // ================参数默认值 end================

        boolean fromProjectPathExist = false;
        boolean templatePathExist = false;
        boolean entityTemplatePathExist = false;
        boolean toProjectPathExist = false;
        boolean entityJdlFileExist = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                // 当前参数是参数名
                if (i == args.length - 1 || args[i + 1].startsWith("--")) {
                    System.out.println("参数错误：" + args[i]);
                    return;
                }
                if (arg.equals("--generate-template")) {
                    generateTemplate = args[i + 1];
                } else if (arg.equals("--from-project-path")) {
                    fromProjectPathExist = true;
                    existProjectDirectory = args[i + 1] + "\\";
                } else if (arg.equals("--from-project-name")) {
                    existProjectName = args[i + 1];
                } else if (arg.equals("--skip-entity-start")) {
                    skipEntityStart = args[i + 1];
                } else if (arg.equals("--template-path")) {
                    templatePathExist = true;
                    templatePath = args[i + 1] + "\\";
                } else if (arg.equals("--template-name")) {
                    templateName = args[i + 1] + "\\";
                } else if (arg.equals("--to-project-path")) {
                    toProjectPathExist = true;
                    projectPath = args[i + 1] + "\\";
                } else if (arg.equals("--to-project-name")) {
                    projectName = args[i + 1];
                } else if (arg.equals("--to-project-package")) {
                    packageName = args[i + 1];
                } else if (arg.equals("--entity-template-path")) {
                    entityTemplatePathExist = true;
                    entityTemplatePath = args[i + 1] + "\\";
                } else if (arg.equals("--entity-jdl-file")) {
                    entityJdlFileExist = true;
                    umlFileName = args[i + 1];
                } else if (arg.equals("--entity-jdl-file-encode")) {
                    umlFileCharsetName = args[i + 1];
                } else if (arg.equals("--db-ip")) {
                    dbIp = args[i + 1];
                } else if (arg.equals("--db-port")) {
                    dbPort = args[i + 1];
                } else if (arg.equals("--db-name")) {
                    dbName = args[i + 1];
                } else if (arg.equals("--db-username")) {
                    dbUsername = args[i + 1];
                } else if (arg.equals("--db-password")) {
                    dbPassword = args[i + 1];
                } else if (arg.equals("--allow-change-table")) {
                    allowChangeTable = args[i + 1];
                } else if (arg.equals("--delete-table-name-start")) {
                    deleteTableNameStartStr = args[i + 1];
                } else {
                    // 未识别的参数名
                }
                // 过掉参数值，参数值在上面已处理过
                i++;
            }
        }
        if ("yes".equals(generateTemplate) || "only".equals(generateTemplate)) {
            // yes：是且同时生成全代码和数据表。only：只生成模板。
            if (!fromProjectPathExist) {
                System.out.println("--from-project-path参数必填");
                return;
            }
        }
        if ("only".equals(generateTemplate) || "no".equals(generateTemplate)) {
            // only：只生成模板。no：只生成全代码和数据表。
            if (!templatePathExist) {
                System.out.println("--template-path参数必填");
                return;
            }
            if (!new File(templatePath).exists()) {
                System.out.println("--template-path指定的路径不存在");
                return;
            }
        }
        if ("yes".equals(generateTemplate) || "no".equals(generateTemplate) || "update-entity".equals(generateTemplate)) {
            // yes：是且同时生成全代码和数据表。no：只生成全代码和数据表。update-entity：只更新实体代码和数据表。
            if (!entityTemplatePathExist) {
                System.out.println("--entity-template-path参数必填");
                return;
            }
            if (!toProjectPathExist) {
                System.out.println("--to-project-path参数必填");
                return;
            }
            if (!entityJdlFileExist) {
                System.out.println("--entity-jdl-file参数必填");
                return;
            }
        }
        if (existProjectDirectory == null || !new File(existProjectDirectory + existProjectName).exists()) {
            if ("yes".equals(generateTemplate) || "only".equals(generateTemplate)) {
                // yes：是且同时生成全代码和数据表。only：只生成模板。
                System.out.println("--from-project-path和--from-project-name共同指定的路径不存在");
                return;
            }
        }
        if (!new File(templatePath + templateName).exists()) {
            if ("no".equals(generateTemplate) || "update-entity".equals(generateTemplate)) {
                // no：只生成全代码和数据表。update-entity：只更新实体代码和数据表。
                System.out.println("--template-path和--template-name共同指定的路径不存在");
                return;
            }
        }
        if (!new File(entityTemplatePath).exists() || !new File(umlFileName).exists()) {
            if ("yes".equals(generateTemplate) || "no".equals(generateTemplate) || "update-entity".equals(generateTemplate)) {
                // yes：是且同时生成全代码和数据表。no：只生成全代码和数据表。update-entity：只更新实体代码和数据表。
                System.out.println("--entity-template-path指定的路径或--entity-jdl-file指定的文件不存在");
                return;
            }
        }

        ///////////////////读取配置并初始化参数////////////////////////
        // 最终生成的项目包名拆分
        String[] toPaths = packageName.split("\\.");
        if (toPaths.length != 3) {
            log.info("暂时只支持packageName为3层，例如：com.abc.module");
            return;
        }
        // 首字母大写的最终生成项目名称
        String ProjectName = projectName.substring(0, 1).toUpperCase() + projectName.substring(1);
        // 首字母大写的模板项目名
        String TemplateName = templateName.substring(0, 1).toUpperCase() + templateName.substring(1);
        // 允许删除的表名开头（JDL文件指定的表名开头）
        String[] deleteTableNameStartTmps = deleteTableNameStartStr.split("\\,");
        List<String> deleteTableNameStartList = new ArrayList<>();
        for (String deleteTableNameStart : deleteTableNameStartTmps) {
            if (!"".equals(deleteTableNameStart.trim())) {
                deleteTableNameStartList.add(deleteTableNameStart.trim());
            }
        }

        // 向模板文件填充的内容
        root.put("projectName", projectName);
        root.put("ProjectName", ProjectName);
        root.put("packageName", packageName);
        root.put("dbIp", dbIp);
        root.put("dbPort", dbPort);
        root.put("dbName", dbName);
        root.put("dbUsername", dbUsername);
        root.put("dbPassword", dbPassword);

        // FreeMarker配置文件
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        // 设置Wrapper
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        // 设置编码
        cfg.setDefaultEncoding("UTF-8");

        ///////////////////生成项目////////////////////////
        // 初始化数据库参数
        log.info("=======> 初始化数据库 start");
        DbModule.initDbParam(dbIp, dbPort, dbName, dbUsername, dbPassword, allowChangeTable, deleteTableNameStartList);
        log.info("=======> 初始化数据库 end");
        if ("yes".equals(generateTemplate) || "only".equals(generateTemplate)) {
            // 生成模板
            log.info("=======> 生成模板 start");
            TemplateModule.generateTemplate(existProjectDirectory, templatePath, existProjectName, templateName, skipEntityStart);
            log.info("=======> 生成模板 end");
        }
        if (!"only".equals(generateTemplate)) {
            if (!"update-entity".equals(generateTemplate)) {
                // 只更新实体的，就不再生成项目，其他情况需要先生成项目
                // 生成项目
                log.info("=======> 生成项目 start");
                ProjectModule.doInitProject(templatePath, projectPath, cfg, root, toPaths, templateName, TemplateName,
                        projectName, ProjectName);
                log.info("=======> 生成项目 end");
            }
            // 生成实体
            log.info("=======> 生成实体 start");
            ERDTO erdto = EntityModule.generateEntities(umlFileName, umlFileCharsetName, projectPath,
                    projectName, packageName, entityTemplatePath, cfg);
            log.info("=======> 生成实体 end");
            // 操作数据库
            log.info("=======> 修改数据库 start");
            DbModule.createEntityTables(erdto);
            log.info("=======> 修改数据库 end");
        }
    }

}
