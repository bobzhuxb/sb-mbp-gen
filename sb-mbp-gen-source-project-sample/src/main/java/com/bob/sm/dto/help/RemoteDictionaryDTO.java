package com.bob.sm.dto.help;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BaseDictionary entity.
 */
public class RemoteDictionaryDTO implements Serializable {

    private Long id;

    private String dicType;

    private String dicCode;

    private String dicValue;

    private String dicDiscription;

    private Long parentId;

    private Integer currentLevel;

    private String insertTime;

    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicDiscription() {
        return dicDiscription;
    }

    public void setDicDiscription(String dicDiscription) {
        this.dicDiscription = dicDiscription;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RemoteDictionaryDTO remoteDictionaryDTO = (RemoteDictionaryDTO) o;
        if (remoteDictionaryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), remoteDictionaryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BaseDictionaryDTO{" +
            "id=" + getId() +
            ", dicType='" + getDicType() + "'" +
            ", dicCode='" + getDicCode() + "'" +
            ", dicValue='" + getDicValue() + "'" +
            ", dicDiscription='" + getDicDiscription() + "'" +
            ", parentId=" + getParentId() +
            ", currentLevel=" + getCurrentLevel() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
