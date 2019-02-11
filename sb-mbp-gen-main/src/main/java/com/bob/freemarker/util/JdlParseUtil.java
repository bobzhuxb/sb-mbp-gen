package com.bob.freemarker.util;

import com.bob.freemarker.dto.ERDTO;
import com.bob.freemarker.dto.EntityDTO;
import com.bob.freemarker.dto.EntityFieldDTO;
import com.bob.freemarker.dto.RelationshipDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdlParseUtil {

    /**
     * 解析JDL文件
     * @param fullFileName
     * @param charsetName
     * @return
     */
    public static ERDTO parseJdlFile(String fullFileName, String charsetName) throws Exception {
        // 实体关系模型
        ERDTO erdto = new ERDTO();
        // entity列表
        List<EntityDTO> entityDTOList = new ArrayList<>();
        // relationship列表
        List<RelationshipDTO> relationshipDTOList = new ArrayList<>();
        // useDictionary列表
        List<EntityDTO> useDictionaryList = new ArrayList<>();
        // 设置entity、relationship和useDictionary
        erdto.setEntityDTOList(entityDTOList);
        erdto.setRelationshipDTOList(relationshipDTOList);
        erdto.setUseDictionaryList(useDictionaryList);
        // JDL文件所有行
        List<String> jdlLineList = FileUtil.readFileByLines(fullFileName, charsetName);
        // 当前注释内容
        String currentComment = null;
        // 当前数据字典类型
        String currentDictionaryType = null;
        // 当前注解列表
        List<String> currentAnnotationList = null;
        // 当前删除模式（允许值：DELETE、NULL、FORBIDDEN。默认：DELETE）
        String currentDeleteType = null;
        // 当前entity名称
        String currentEntityName = null;
        // 当前entity
        EntityDTO entityDTO = null;
        // 当前relationship
        RelationshipDTO relationshipDTO = null;
        // 当前relationship类型
        String currentRelationType = null;
        // 遍历文件每一行
        for (String jdlLine : jdlLineList) {
            // 去掉首尾空白符
            jdlLine = jdlLine.trim();
            if ("".equals(jdlLine)) {
                // 排除空行
                continue;
            }
            if (jdlLine.startsWith("//")) {
                // 注释开始行，获取注释行内容
                if (jdlLine.startsWith("//#DIC_TYPE:")) {
                    // 关联的数据字典类型行
                    currentDictionaryType = jdlLine.substring(12).trim();
                } else if (jdlLine.startsWith("//@")) {
                    // 注解行
                    if (currentAnnotationList == null) {
                        currentAnnotationList = new ArrayList<>();
                    }
                    currentAnnotationList.add(jdlLine.substring(2).trim());
                } else if (jdlLine.startsWith("//!CASCADE_DELETE:")) {
                    // 级联删除模式
                    currentDeleteType = jdlLine.substring(18).trim();
                } else {
                    // 普通注释行（包含两个斜杠和空格）
                    currentComment = jdlLine;
                }
            } else if (jdlLine.contains("{")) {
                // 包含{的行
                if (jdlLine.startsWith("entity ")) {
                    // entity开始行
                    Pattern pattern = Pattern.compile("entity\\s+(\\w+)\\s+\\{");   // 匹配的模式
                    Matcher matcher = pattern.matcher(jdlLine);
                    if (matcher.find()) {
                        // entity行匹配验证成功，创建entity
                        currentEntityName = matcher.group(1);
                        entityDTO = new EntityDTO();
                        entityDTO.setEentityName(currentEntityName);
                        entityDTO.setFieldList(new ArrayList<>());
                        entityDTO.setEntityComment(currentComment.substring(2).trim());
                        entityDTO.setAnnotationList(currentAnnotationList);
                        // 使用过一次comment就置空
                        currentComment = null;
                        // 使用过一次annotationList就置空
                        currentAnnotationList = null;
                    }
                } else if (jdlLine.startsWith("relationship ")) {
                    // relationship开始行
                    Pattern pattern = Pattern.compile("relationship\\s+(\\w+)\\s+\\{"); // 匹配的模式
                    Matcher matcher = pattern.matcher(jdlLine);
                    if (matcher.find()) {
                        // 找到一个relationship
                        currentRelationType = matcher.group(1);
                    }
                } else {
                    // relationship内容行
                    relationshipDTO = new RelationshipDTO();
                    relationshipDTO.setRelationType(currentRelationType);
                    Pattern pattern = Pattern.compile("(\\w+)\\{(\\w+)\\}\\s+to\\s+(\\w+)\\{(\\w+)\\}");    // 匹配的模式
                    Matcher matcher = pattern.matcher(jdlLine);
                    if (matcher.find()) {
                        // relationship行匹配验证成功
                        String toFromEntityType = matcher.group(1);
                        String fromToEntityName = matcher.group(2);
                        String fromToEntityType = matcher.group(3);
                        String toFromEntityName = matcher.group(4);
                        relationshipDTO.setToFromEntityType(toFromEntityType);
                        relationshipDTO.setToFromEntityName(toFromEntityName);
                        relationshipDTO.setToFromEntityUName(StringUtil.camelToUpperFirst(toFromEntityName));
                        relationshipDTO.setToFromEntityTable(StringUtil.camelToUnderline(toFromEntityType));
                        relationshipDTO.setToFromEntityUrl(StringUtil.camelToCenterline(toFromEntityType));
                        relationshipDTO.setFromToEntityType(fromToEntityType);
                        relationshipDTO.setFromToEntityName(fromToEntityName);
                        relationshipDTO.setFromToEntityUName(StringUtil.camelToUpperFirst(fromToEntityName));
                        relationshipDTO.setFromToEntityTable(StringUtil.camelToUnderline(fromToEntityType));
                        relationshipDTO.setFromToEntityUrl(StringUtil.camelToCenterline(fromToEntityType));
                        relationshipDTO.setFromColumnName(StringUtil.camelToUnderline(toFromEntityName) + "_id");
                    }
                    // 找到内容行上面的注释
                    Pattern patternComment = Pattern.compile("//\\s+([^\\{\\}]+)\\{([^\\{\\}]+)\\}");   // 匹配的模式
                    Matcher matcherComment = patternComment.matcher(currentComment);
                    if (matcherComment.find()) {
                        // relationship的注释行匹配验证成功
                        String toFromComment = matcherComment.group(1);
                        String fromToComment = matcherComment.group(2);
                        relationshipDTO.setToFromComment(toFromComment);
                        relationshipDTO.setFromToComment(fromToComment);
                    }
                    relationshipDTO.setAnnotationList(currentAnnotationList);
                    if (currentDeleteType == null) {
                        // 默认级联删除
                        relationshipDTO.setFromToDeleteType("DELETE");
                    } else {
                        // 如果有值，则设置为指定值
                        relationshipDTO.setFromToDeleteType(currentDeleteType);
                    }
                    // 使用过一次comment就置空
                    currentComment = null;
                    // 使用过一次annotationList就置空
                    currentAnnotationList = null;
                    // 使用过一次currentDeleteType就置空
                    currentDeleteType = null;
                    // 将关系添加到关系列表中
                    relationshipDTOList.add(relationshipDTO);
                }
            } else if ("}".equals(jdlLine)) {
                // entity或relationship的结束行
                if (entityDTO != null) {
                    // entity结束行
                    // 最后，默认加上insertTime和updateTime和insertUserId和operateUserId字段
                    entityDTO.getFieldList().add(new EntityFieldDTO("insertUserId", "Long", "创建者用户ID"));
                    entityDTO.getFieldList().add(new EntityFieldDTO("operateUserId", "Long", "操作者用户ID"));
                    entityDTO.getFieldList().add(new EntityFieldDTO("insertTime", "String", "插入时间"));
                    entityDTO.getFieldList().add(new EntityFieldDTO("updateTime", "String", "更新时间"));
                    entityDTOList.add(entityDTO);
                    entityDTO = null;
                } else if (relationshipDTO != null) {
                    // 同一类型的relationship结束行
                    currentRelationType = null;
                }
            } else {
                // 剩余的就是entity的fieldName行
                if (entityDTO != null) {
                    String camelName = null;
                    String javaType = null;
                    Pattern pattern = Pattern.compile("(\\w+)\\s+(\\w+)(\\,)?");  // 匹配的模式
                    Matcher matcher = pattern.matcher(jdlLine);
                    if (matcher.find()) {
                        // relationship的注释行匹配验证成功
                        camelName = matcher.group(1);
                        javaType = matcher.group(2);
                    }
                    // 找到filedName行上面的注释
                    String fieldComment = currentComment.substring(2).trim();
                    // 使用过一次comment就置空
                    currentComment = null;
                    // 找到filedName行上面的注释中的dictionaryType
                    String dictionaryType = currentDictionaryType;
                    // 使用过一次dictionaryType就置空
                    currentDictionaryType = null;
                    // insertTime和updateTime和insertUserId和operateUserId字段由生成器帮助生成，自动忽略
                    if (!"insertTime".equals(camelName) && !"updateTime".equals(camelName)
                            && !"insertUserId".equals(camelName) && !"operateUserId".equals(camelName)) {
                        // 生成entity的field
                        EntityFieldDTO entityFieldDTO = null;
                        if (dictionaryType == null) {
                            entityFieldDTO = new EntityFieldDTO(camelName, javaType, fieldComment);
                        } else {
                            String camelNameDic = null;
                            if (camelName.endsWith("Code")) {
                                camelNameDic = camelName.substring(0, camelName.length() - 4) + "Value";
                            } else {
                                camelNameDic = camelName + "Value";
                            }
                            String commentDic = null;
                            if (fieldComment.endsWith("代码")) {
                                commentDic = fieldComment.substring(0, fieldComment.length() - 2) + "值";
                            } else {
                                commentDic = fieldComment + "值";
                            }
                            entityFieldDTO = new EntityFieldDTO(camelName, javaType, fieldComment, camelNameDic,
                                    dictionaryType, commentDic);
                            // 设置使用数据字典的实体及字段
                            EntityDTO nowUseDictionary = null;
                            List<EntityFieldDTO> nowUseDictionaryFieldList = null;
                            for (EntityDTO useDictionary : useDictionaryList) {
                                if (useDictionary.getEentityName().equals(entityDTO.getEentityName())) {
                                    nowUseDictionary = useDictionary;
                                    break;
                                }
                            }
                            if (nowUseDictionary == null) {
                                nowUseDictionary = new EntityDTO();
                                String eentityName = entityDTO.getEentityName();
                                nowUseDictionary.setEentityName(eentityName);
                                nowUseDictionary.setEntityName(eentityName.substring(0, 1).toLowerCase() + eentityName.substring(1));
                                nowUseDictionary.setEntityComment(entityDTO.getEntityComment());
                                nowUseDictionaryFieldList = new ArrayList<>();
                                nowUseDictionary.setFieldList(nowUseDictionaryFieldList);
                                useDictionaryList.add(nowUseDictionary);
                            } else {
                                nowUseDictionaryFieldList = nowUseDictionary.getFieldList();
                            }
                            EntityFieldDTO nowUseDictionaryField = new EntityFieldDTO();
                            nowUseDictionaryField.setCamelName(camelName);
                            nowUseDictionaryField.setCamelNameUnderline(StringUtil.camelToUnderline(camelName));
                            nowUseDictionaryField.setDictionaryType(dictionaryType);
                            nowUseDictionaryFieldList.add(nowUseDictionaryField);
                        }
                        entityFieldDTO.setAnnotationList(currentAnnotationList);
                        // 使用过一次annotationList就置空
                        currentAnnotationList = null;
                        // 将field添加进entity
                        entityDTO.getFieldList().add(entityFieldDTO);
                    }
                }
            }
        }

        return erdto;
    }

}
