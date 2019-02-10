package com.bob.freemarker.module;

import com.bob.freemarker.dto.ERDTO;
import com.bob.freemarker.dto.EntityDTO;
import com.bob.freemarker.dto.EntityFieldDTO;
import com.bob.freemarker.dto.RelationshipDTO;
import com.bob.freemarker.util.JdlParseUtil;
import com.bob.freemarker.util.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Entity模板创建
 * 方法一（推荐）：在 https://start.jhipster.tech/jdl-studio/ 网站上编辑
 * 方法二：使用Visual Studio Code的jhipster插件，新增jdl文件进行编辑
 */
public class EntityModule {

    private final Logger log = LoggerFactory.getLogger(EntityModule.class);

    /**
     * 从UML文件生成实体
     * @param umlFileName JDL文件的全路径文件名
     * @param charsetName JDL文件的编码格式
     * @param projectPath 生成的项目所在的路径
     * @param projectName 生成的项目的名称
     * @param packageName 生成的项目的包名
     * @param entityTemplatePath 实体模板文件的主路径
     * @param cfg 模板配置
     * @throws Exception
     */
    public static ERDTO generateEntities(String umlFileName, String charsetName, String projectPath, String projectName,
                                         String packageName, String entityTemplatePath, Configuration cfg) throws Exception {
        // 从JDL文件解析出entityList和relationshipList
        ERDTO erdto = JdlParseUtil.parseJdlFile(umlFileName, charsetName);
        // 获取entityList
        List<EntityDTO> entityDTOList = erdto.getEntityDTOList();
        // 获取relationshipList
        List<RelationshipDTO> relationshipList = erdto.getRelationshipDTOList();
        // 获取useDictionaryList
        List<EntityDTO> useDictionaryList = erdto.getUseDictionaryList();
        for (EntityDTO entityDTO : entityDTOList) {
            // 与本实体相关的relationship
            List<RelationshipDTO> relationshipListCurrent = new ArrayList<>();
            String entityComment = entityDTO.getEntityComment();
            String eentityName = entityDTO.getEentityName();
            for (RelationshipDTO relationshipDTO : relationshipList) {
                if (relationshipDTO.getFromToEntityType().equals(eentityName)
                        || relationshipDTO.getToFromEntityType().equals(eentityName)) {
                    relationshipListCurrent.add(relationshipDTO);
                }
            }
            entityDTO.setTableName(StringUtil.camelToUnderline(eentityName));
            List<EntityFieldDTO> fieldList = entityDTO.getFieldList();
            generateEntity(projectPath + projectName + "\\", packageName, entityComment, eentityName, fieldList,
                    relationshipListCurrent, useDictionaryList, entityTemplatePath, cfg);
        }
        return erdto;
    }

    /**
     * 生成实体对象及相关类
     * @param projectDirectory 项目主路径
     * @param packageName 项目包名
     * @param entityComment 实体注释
     * @param eentityName 首字母大写的实体名称（实体类型）
     * @param fieldList 实体的域（字段）
     * @param entityTemplatePath 实体模板文件的主路径
     * @param cfg 模板配置
     * @throws Exception
     */
    private static void generateEntity(String projectDirectory, String packageName, String entityComment,
                                       String eentityName, List<EntityFieldDTO> fieldList,
                                       List<RelationshipDTO> relationshipList, List<EntityDTO> useDictionaryList,
                                       String entityTemplatePath, Configuration cfg) throws Exception {
        Map root = new HashMap();
        // 主实体内容
        root.put("packageName", packageName);
        String entityName = eentityName.substring(0, 1).toLowerCase() + eentityName.substring(1);
        String entityUrl = StringUtil.camelToCenterline(eentityName);
        String tableName = StringUtil.camelToUnderline(eentityName);
        root.put("entityComment", entityComment);
        root.put("entityName", entityName);
        root.put("eentityName", eentityName);
        root.put("tableName", tableName);
        root.put("entityUrl", entityUrl);
        root.put("fieldList", fieldList);
        root.put("useDictionaryList", useDictionaryList);

        // from表示当前entity，to表示关联entity，fromTo表示当前entity的子属性，toFrom表示当前entity的父属性
        List<RelationshipDTO> fromToList = new ArrayList<>();
        List<RelationshipDTO> toFromList = new ArrayList<>();
        // 预处理ER关系
        for (RelationshipDTO relationship : relationshipList) {
            if (eentityName.equals(relationship.getToFromEntityType())) {
                fromToList.add(relationship);
            }
            if (eentityName.equals(relationship.getFromToEntityType())) {
                toFromList.add(relationship);
            }
        }
        root.put("fromToList", fromToList);
        root.put("toFromList", toFromList);
        // 关联查询字段的说明
        String associationNameComment = "";
        if (fromToList.size() > 0 || toFromList.size() > 0) {
            associationNameComment += "（";
            for (RelationshipDTO fromTo : fromToList) {
                associationNameComment += fromTo.getFromToEntityName() + "List：" + fromTo.getFromToComment() + "列表、";
            }
            for (RelationshipDTO toFrom : toFromList) {
                associationNameComment += toFrom.getToFromEntityName() + "：" + toFrom.getToFromComment() + "、";
            }
            associationNameComment = associationNameComment.substring(0, associationNameComment.length() - 1);
            associationNameComment += "）";
        }
        root.put("associationNameComment", associationNameComment);
        // 关联的数据字典说明
        String dictionaryNameList = "";
        for (EntityFieldDTO fieldDTO : fieldList) {
            if (fieldDTO.getDictionaryType() != null) {
                if ("".equals(dictionaryNameList)) {
                    dictionaryNameList += "（";
                }
                dictionaryNameList += fieldDTO.getCamelNameDic() + "：" + fieldDTO.getCommentDic() + "、";
            }
        }
        if (!"".equals(dictionaryNameList)) {
            dictionaryNameList = dictionaryNameList.substring(0, dictionaryNameList.length() - 1);
            dictionaryNameList += "）";
        }
        root.put("dictionaryNameList", dictionaryNameList);

        generateDomain(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateDTO(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateCriteria(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateMapper(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateMapperXml(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateService(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateServiceImpl(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
        generateController(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg);
    }

    /**
     * 生成domain类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateDomain(String projectDirectory, String packageName, String eentityName,
                                       String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName, entityTemplatePath, root, cfg,
                "domain\\", "Domain.java.ftl");
    }

    /**
     * 生成DTO类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateDTO(String projectDirectory, String packageName, String eentityName,
                                    String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "DTO", entityTemplatePath, root, cfg,
                "dto\\", "DTO.java.ftl");
    }

    /**
     * 生成Criteria类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateCriteria(String projectDirectory, String packageName, String eentityName,
                                         String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "Criteria", entityTemplatePath, root, cfg,
                "dto\\criteria\\", "Criteria.java.ftl");
    }

    /**
     * 生成Mapper类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateMapper(String projectDirectory, String packageName, String eentityName,
                                       String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "Mapper", entityTemplatePath, root, cfg,
                "mapper\\", "Mapper.java.ftl");
    }

    /**
     * 生成Mapper.xml
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateMapperXml(String projectDirectory, String packageName, String eentityName,
                                          String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateXmlFile(projectDirectory, packageName, eentityName + "Mapper", entityTemplatePath, root, cfg,
                "mapperxml\\", "Mapper.xml.ftl");
    }

    /**
     * 生成Service类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateService(String projectDirectory, String packageName, String eentityName,
                                        String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "Service", entityTemplatePath, root, cfg,
                "service\\", "Service.java.ftl");
    }

    /**
     * 生成ServiceImpl类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateServiceImpl(String projectDirectory, String packageName, String eentityName,
                                            String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "ServiceImpl", entityTemplatePath, root, cfg,
                "service\\impl\\", "ServiceImpl.java.ftl");
    }

    /**
     * 生成Controller类
     * @param projectDirectory
     * @param packageName
     * @param eentityName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @throws Exception
     */
    private static void generateController(String projectDirectory, String packageName, String eentityName,
                                           String entityTemplatePath, Map root, Configuration cfg) throws Exception {
        generateJavaFile(projectDirectory, packageName, eentityName + "Controller", entityTemplatePath, root, cfg,
                "web\\rest\\", "Controller.java.ftl");
    }

    /**
     * 根据ftl文件生成Java文件
     * @param projectDirectory
     * @param packageName
     * @param javaFileName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @param appendPath
     * @param ftlName
     * @throws Exception
     */
    private static void generateJavaFile(String projectDirectory, String packageName, String javaFileName,
                                         String entityTemplatePath, Map root, Configuration cfg,
                                         String appendPath, String ftlName) throws Exception {
        cfg.setDirectoryForTemplateLoading(new File(entityTemplatePath + appendPath));
        // 加载模板文件
        Template temp = cfg.getTemplate(ftlName, "UTF-8");
        String toPath = projectDirectory + "src\\main\\java\\" + packageName.replace(".", "\\") + "\\" + appendPath;
        String fileName = javaFileName + ".java";
        File file = new File(toPath + fileName);
        // 写文件
        temp.process(root, new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    }

    /**
     * 根据ftl文件生成Xml文件
     * @param projectDirectory
     * @param packageName
     * @param xmlFileName
     * @param entityTemplatePath
     * @param root
     * @param cfg
     * @param appendPath
     * @param ftlName
     * @throws Exception
     */
    private static void generateXmlFile(String projectDirectory, String packageName, String xmlFileName,
                                         String entityTemplatePath, Map root, Configuration cfg,
                                         String appendPath, String ftlName) throws Exception {
        cfg.setDirectoryForTemplateLoading(new File(entityTemplatePath + appendPath));
        // 加载模板文件
        Template temp = cfg.getTemplate(ftlName, "UTF-8");
        String toPath = projectDirectory + "src\\main\\resources\\mapper\\";
        String fileName = xmlFileName + ".xml";
        File file = new File(toPath + fileName);
        // 写文件
        temp.process(root, new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    }

}
