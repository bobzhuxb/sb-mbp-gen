package com.bob.freemarker.dto;

import java.util.List;

/**
 * 通用ER关系模型
 */
public class ERDTO {

    private List<EntityDTO> entityDTOList;
    private List<RelationshipDTO> relationshipDTOList;

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
}
