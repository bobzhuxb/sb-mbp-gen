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
        // 设置entity和relationship
        erdto.setEntityDTOList(entityDTOList);
        erdto.setRelationshipDTOList(relationshipDTOList);
        // JDL文件所有行
        List<String> jdlLineList = FileUtil.readFileByLines(fullFileName, charsetName);
        // 当前注释内容
        String currentComment = null;
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
                currentComment = jdlLine;
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
                        // 使用过一次comment就置空
                        currentComment = null;
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
                    Pattern pattern = Pattern.compile("(\\w+)\\{(\\w+)\\}\\s+to\\s+(\\w+)");    // 匹配的模式
                    Matcher matcher = pattern.matcher(jdlLine);
                    if (matcher.find()) {
                        // relationship行匹配验证成功
                        String toFromEntityType = matcher.group(1);
                        String fromToEntityType = matcher.group(3);
                        relationshipDTO.setToFromEntityType(toFromEntityType);
                        relationshipDTO.setToFromEntityTable(StringUtil.camelToUnderline(toFromEntityType));
                        relationshipDTO.setFromToEntityName(matcher.group(2));
                        relationshipDTO.setFromToEntityType(matcher.group(3));
                        relationshipDTO.setFromToEntityTable(StringUtil.camelToUnderline(fromToEntityType));
                        relationshipDTO.setToFromEntityName(
                                toFromEntityType.substring(0, 1).toLowerCase() + toFromEntityType.substring(1));
                        relationshipDTO.setFromColumnName(StringUtil.camelToUnderline(matcher.group(1)) + "_id");
                    }
                    // 找到内容行上面的注释
                    Pattern patternComment = Pattern.compile("//\\s+([^\\{\\}]+)\\{([^\\{\\}]+)\\}");   // 匹配的模式
                    Matcher matcherComment = patternComment.matcher(currentComment);
                    if (matcherComment.find()) {
                        // relationship的注释行匹配验证成功
                        relationshipDTO.setToFromComment(matcherComment.group(1));
                        relationshipDTO.setFromToComment(matcherComment.group(2));
                    }
                    relationshipDTOList.add(relationshipDTO);
                    // 使用过一次comment就置空
                    currentComment = null;
                }
            } else if ("}".equals(jdlLine)) {
                // entity或relationship的结束行
                if (entityDTO != null) {
                    // entity结束行
                    // 最后，默认加上insertTime和updateTime和operateUserId字段
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
                    // insertTime和updateTime和operateUserId字段由生成器帮助生成，自动忽略
                    if (!"insertTime".equals(camelName) && !"updateTime".equals(camelName) && !"operateUserId".equals(camelName)) {
                        // 生成entity的field
                        EntityFieldDTO entityFieldDTO = new EntityFieldDTO(camelName, javaType, fieldComment);
                        // 将field添加进entity
                        entityDTO.getFieldList().add(entityFieldDTO);
                    }
                }
            }
        }

        return erdto;
    }

}
