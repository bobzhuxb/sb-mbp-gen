package ${packageName}.dto.help;

import java.util.List;

/**
 * 前端接口适配器参数DTO
 * @author Bob
 */
public class ApiAdapterParamDTO {

    private List<ApiAdapterCriteriaDTO> criteriaList;       // 条件参数配置（GET）
    private List<String> associationNameList;               // 级联查询的参数名列表（GET）
    private List<String> dictionaryNameList;                // 数据字典查询列表（GET）
    private List<String> sqlColumnList;                     // SQL查询的字段（GET）
    private String orderBy;                                 // 排序的字符串（GET）

    private Object jsonBody;                                // json格式的body实体内容

    public List<ApiAdapterCriteriaDTO> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<ApiAdapterCriteriaDTO> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public List<String> getAssociationNameList() {
        return associationNameList;
    }

    public void setAssociationNameList(List<String> associationNameList) {
        this.associationNameList = associationNameList;
    }

    public List<String> getDictionaryNameList() {
        return dictionaryNameList;
    }

    public void setDictionaryNameList(List<String> dictionaryNameList) {
        this.dictionaryNameList = dictionaryNameList;
    }

    public List<String> getSqlColumnList() {
        return sqlColumnList;
    }

    public void setSqlColumnList(List<String> sqlColumnList) {
        this.sqlColumnList = sqlColumnList;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Object getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(Object jsonBody) {
        this.jsonBody = jsonBody;
    }
}
