package ${packageName}.dto.help;

import java.util.List;

/**
 * 前端接口适配器条件查询DTO
 */
public class ApiAdapterCriteriaDTO {

    private String fromParam;               // 来源参数名
    private List<String> toCriteriaList;    // 转换后的条件参数

    public String getFromParam() {
        return fromParam;
    }

    public void setFromParam(String fromParam) {
        this.fromParam = fromParam;
    }

    public List<String> getToCriteriaList() {
        return toCriteriaList;
    }

    public void setToCriteriaList(List<String> toCriteriaList) {
        this.toCriteriaList = toCriteriaList;
    }
}
