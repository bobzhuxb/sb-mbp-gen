package com.bob.freemarker.dto;

import java.util.List;

/**
 * 通用ER关系模型
 */
public class ERDTO {

    private List<EntityDTO> entityDTOList;      // 实体List
    private List<RelationshipDTO> relationshipDTOList;      // 关系List
    private List<EntityDTO> useDictionaryList;      // 使用数据字典的实体及字段List

    public List<EntityDTO> getEntityDTOList() {
        return entityDTOList;
    }

    public void setEntityDTOList(List<EntityDTO> entityDTOList) {
        this.entityDTOList = entityDTOList;
    }

    public List<RelationshipDTO> getRelationshipDTOList() {
        return relationshipDTOList;
    }

    public void setRelationshipDTOList(List<RelationshipDTO> relationshipDTOList) {
        this.relationshipDTOList = relationshipDTOList;
    }

    public List<EntityDTO> getUseDictionaryList() {
        return useDictionaryList;
    }

    public void setUseDictionaryList(List<EntityDTO> useDictionaryList) {
        this.useDictionaryList = useDictionaryList;
    }
}
