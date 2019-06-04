package ${packageName}.dto.help;

import java.util.List;

/**
 * 前端接口适配器条件查询DTO
 */
public class ApiAdapterCriteriaDTO {

    private String fromParam;               // 来源参数名
    private List<String> toCriteriaList;    // 转换后的条件参数
    private String descr;                   // 参数描述
    private Object fixedValue;              // 固定值

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        this.fixedValue = fixedValue;
    }
}
